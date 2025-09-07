package com.luq.store.services;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.luq.store.domain.*;
import com.luq.store.dto.request.order.OrderRegisterDTO;
import com.luq.store.dto.request.order.OrderUpdateDTO;
import com.luq.store.dto.request.payment.PaymentRequestDTO;
import com.luq.store.dto.response.order.OrderResponseDTO;
import com.luq.store.dto.response.payment.PaymentStatusDTO;
import com.luq.store.exceptions.InvalidQuantityException;
import com.luq.store.exceptions.NotFoundException;
import com.luq.store.mapper.*;
import com.luq.store.repositories.ProductRepository;
import com.luq.store.repositories.SupplyRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.luq.store.repositories.OrderRepository;

@Service
public class OrderService {
    @Autowired
    private OrderRepository oRepository;
    @Autowired
    private SupplyRepository sRepository;

    @Autowired
    private OrderMapper oMapper;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ProductMapper pMapper;
    @Autowired
    private CustomerMapper cMapper;
    @Autowired
    private SupplyMapper supplyMapper;
    @Autowired
    private SellerMapper sellerMapper;

    @Autowired
    private CustomerService cService;
    @Autowired
    private ProductService pService;
    @Autowired
    private SupplyService supplyService;
    @Autowired
    private SellerService sellerService;
    @Autowired
    private ProductRepository pRepository;

    public List<OrderResponseDTO> getAll() {
        return oMapper.toDTOList(oRepository.findAll());
    }

    public List<OrderResponseDTO> getAllSorted(String sortBy, String direction, String status, Integer productId, Integer sellerId, Integer customerId) {
        Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        if (productId != null || sellerId != null || customerId != null || status != null)
            return oMapper.toDTOList(oRepository.findByStatusProductSellerCustomer(sort, OrderStatus.getOrderStatus(status), productId, sellerId, customerId));

        return oMapper.toDTOList(oRepository.findAll(sort));
    }

    public OrderResponseDTO getById(int id) {
        Order order = oRepository.findById(id).orElse(null);
        assert order != null;
        return oMapper.toDTO(order);
    }

    public OrderResponseDTO register(OrderRegisterDTO data) throws JsonProcessingException, URISyntaxException {
        Supply supply = supplyMapper.toEntity(supplyService.getByProductId(data.productId()));
        if (supply.getId() == null) throw new NotFoundException("Supply not found for this product");

        Product product = pRepository.findById(data.productId()).orElse(null);
        if (product == null) throw new NotFoundException("Product not found");

        if (data.quantity().compareTo(supply.getQuantity()) > 0)
            throw new InvalidQuantityException("A quantity greater that actually have in supply was inserted, please change it");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Order order = oMapper.toEntity(data);

        order.setStatus(OrderStatus.PENDING_PAYMENT);
        order.setModifiedBy(authentication.getName());
        order.setModified(LocalDateTime.now());
        order.setCreatedBy(authentication.getName());
        order.setCreated(LocalDateTime.now());

        supply.setQuantity(supply.getQuantity() - order.getQuantity());

        supply.setModified(LocalDateTime.now());
        supply.setModifiedBy(authentication.getName());

        order = oRepository.save(order);
        sRepository.save(supply);
        setCheckoutUrl(order);

        return oMapper.toDTO(order);
    }

    public void setCheckoutUrl(Order order) throws URISyntaxException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        PaymentRequestDTO paymentDTO = new PaymentRequestDTO(
            order.getId(),
            order.getProduct().getPrice(),
            order.getQuantity(),
            order.getProduct().getName()
        );
        String payment_info = mapper.writeValueAsString(paymentDTO);

        try{
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8081/api/payments/create-checkout"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(payment_info))
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject responseJson = new JSONObject(response.body());

            String checkoutUrl = responseJson.getString("checkoutUrl");

            if (checkoutUrl != null) {
                order.setCheckoutUrl(checkoutUrl);
                oRepository.save(order);
            }
        } catch (URISyntaxException | IOException | InterruptedException ex) {
            System.out.println(ex);
        }
    }

    public OrderResponseDTO update(int id, OrderUpdateDTO data) {
        Order order = oRepository.findById(id).orElse(null);
        if (order == null) throw new NotFoundException("Order not found");

        Product product = pRepository.findById(data.productId()).orElse(null);
        if (product == null) throw new NotFoundException("Product not found");

        Supply supply = sRepository.getByProductId(order.getProduct().getId());

        if (order.getQuantity().compareTo(supply.getQuantity()) > 0)
            throw new InvalidQuantityException("A quantity greater that actually have in supply was inserted, please change it");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        order.setUnitPrice(product.getPrice());
        order.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(data.quantity())));
        order.setQuantity(data.quantity());
        order.setOrderDate(data.orderDate());
        order.setProduct(pMapper.toEntity(pService.getById(data.productId())));
        order.setSeller(sellerMapper.toEntity(sellerService.getById(data.sellerId())));
        order.setCustomer(cMapper.toEntity(cService.getById(data.customerId())));

        order.setModifiedBy(authentication.getName());
        order.setModified(LocalDateTime.now());

        supply.setQuantity(supply.getQuantity() - order.getQuantity());

        supply.setModified(LocalDateTime.now());
        supply.setModifiedBy(authentication.getName());

        order = oRepository.save(order);
        sRepository.save(supply);
        return oMapper.toDTO(order);
    }

    public void delete(int id) {
        oRepository.deleteById(id);
    }

    public void updateStatus(PaymentStatusDTO data) {
        Order order = oRepository.findById(data.orderId()).orElse(null);

        if (order == null) throw new NotFoundException("Order not found");

        order.setStatus(data.status());
        order.setModifiedBy("STRIPE_WEBHOOK");
        order.setModified(LocalDateTime.now());

        System.out.println("order " + order.getId() + " updated. new status: " + order.getStatus());
        oRepository.save(order);
    }
}
