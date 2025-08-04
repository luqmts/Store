package com.luq.store.mapper;

import com.luq.store.domain.Customer;
import com.luq.store.domain.Order;
import com.luq.store.domain.Product;
import com.luq.store.domain.Seller;
import com.luq.store.dto.request.order.OrderRegisterDTO;
import com.luq.store.dto.response.order.OrderResponseDTO;
import com.luq.store.exceptions.NotFoundException;
import com.luq.store.services.CustomerService;
import com.luq.store.services.ProductService;
import com.luq.store.services.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderMapper {
    @Autowired
    private ProductMapper pMapper;
    @Autowired
    private ProductService pService;
    @Autowired
    private SellerMapper sMapper;
    @Autowired
    private SellerService sService;
    @Autowired
    private CustomerMapper cMapper;
    @Autowired
    private CustomerService cService;


    public Order toEntity(OrderRegisterDTO data) {
        Product product;
        Seller seller;
        Customer customer;

        try {
            product = pMapper.toEntity(pService.getById(data.product_id()));
        } catch (NullPointerException e) {
            throw new NotFoundException("Product not found");
        }

        try {
            seller = sMapper.toEntity(sService.getById(data.seller_id()));
        } catch (NullPointerException e) {
            throw new NotFoundException("Seller not found");
        }

        try {
            customer = cMapper.toEntity(cService.getById(data.customer_id()));
        } catch (NullPointerException e) {
            throw new NotFoundException("Customer not found");
        }

        Order order = new Order();
        order.setTotalPrice(data.totalPrice());
        order.setQuantity(data.quantity());
        order.setOrderDate(data.orderDate());
        order.setProduct(product);
        order.setSeller(seller);
        order.setCustomer(customer);

        return order;
    }

    public Order toEntity(OrderResponseDTO data) {
        return new Order(
            data.id(),
            data.totalPrice(),
            data.quantity(),
            data.orderDate(),
            data.product(),
            data.seller(),
            data.customer(),
            data.createdBy(),
            data.created(),
            data.createdBy(),
            data.modified()
        );
    }

    public OrderResponseDTO toDTO(Order order) {
        return new OrderResponseDTO(
            order.getId(),
            order.getTotalPrice(),
            order.getQuantity(),
            order.getOrderDate(),
            order.getProduct(),
            order.getSeller(),
            order.getCustomer(),
            order.getCreatedBy(),
            order.getCreated(),
            order.getModifiedBy(),
            order.getModified()
        );
    }

    public List<OrderResponseDTO> toDTOList(List<Order> oList) {
        return oList
            .stream()
            .map(this::toDTO)
            .toList();
    }
}
