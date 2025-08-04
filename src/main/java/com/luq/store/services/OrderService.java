package com.luq.store.services;

import java.time.LocalDateTime;
import java.util.List;

import com.luq.store.domain.*;
import com.luq.store.dto.request.order.OrderRegisterDTO;
import com.luq.store.dto.request.order.OrderUpdateDTO;
import com.luq.store.dto.response.order.OrderResponseDTO;
import com.luq.store.exceptions.InvalidQuantityException;
import com.luq.store.exceptions.NotFoundException;
import com.luq.store.mapper.*;
import com.luq.store.repositories.SupplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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

    public List<OrderResponseDTO> getAll() {
        return oMapper.toDTOList(oRepository.findAll());
    }

    public List<OrderResponseDTO> getAllSorted(String sortBy, String direction, Integer productId, Integer sellerId, Integer customerId) {
        Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        if (productId != null || sellerId != null || customerId != null)
            return oMapper.toDTOList(oRepository.findByProductSellerCustomer(sort, productId, sellerId, customerId));

        return oMapper.toDTOList(oRepository.findAll(sort));
    }

    public OrderResponseDTO getById(int id) {
        Order order = oRepository.findById(id).orElse(null);
        assert order != null;
        return oMapper.toDTO(order);
    }

    public OrderResponseDTO register(OrderRegisterDTO data) {
        Order order = oMapper.toEntity(data);
        Supply supply = sRepository.getByProductId(data.product_id());

        if (order.getQuantity().compareTo(supply.getQuantity()) > 0)
            throw new InvalidQuantityException("A quantity greater that actually have in supply was inserted, please change it");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        order.setModifiedBy(authentication.getName());
        order.setModified(LocalDateTime.now());
        order.setCreatedBy(authentication.getName());
        order.setCreated(LocalDateTime.now());

        supply.setQuantity(supply.getQuantity() - order.getQuantity());

        supply.setModified(LocalDateTime.now());
        supply.setModifiedBy(authentication.getName());

        order = oRepository.save(order);
        sRepository.save(supply);
        return oMapper.toDTO(order);
    }

    public OrderResponseDTO update(int id, OrderUpdateDTO data) {
        Order order = oRepository.findById(id).orElse(null);

        if (order == null) throw new NotFoundException("Order not found");

        Supply supply = sRepository.getByProductId(order.getProduct().getId());

        if (order.getQuantity().compareTo(supply.getQuantity()) > 0)
            throw new InvalidQuantityException("A quantity greater that actually have in supply was inserted, please change it");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        order.setTotalPrice(data.totalPrice());
        order.setQuantity(data.quantity());
        order.setOrderDate(data.orderDate());
        order.setProduct(pMapper.toEntity(pService.getById(data.product_id())));
        order.setSeller(sMapper.toEntity(sService.getById(data.seller_id())));
        order.setCustomer(cMapper.toEntity(cService.getById(data.customer_id())));

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
}
