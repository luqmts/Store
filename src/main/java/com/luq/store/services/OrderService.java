package com.luq.store.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.luq.store.domain.Order;
import com.luq.store.repositories.OrderRepository;

@Service
public class OrderService {
    @Autowired
    private OrderRepository oRepository;
    
    public List<Order> getAll() {
        return oRepository.findAll();
    }

    public List<Order> getAllSorted(String sortBy, String direction, Integer productId, Integer sellerId, Integer customerId) {
        Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        if (productId != null || sellerId != null || customerId != null) 
            return oRepository.findByProductSellerCustomer(sort, productId, sellerId, customerId);
        return oRepository.findAll(sort);
    }

    public Order getById(int id) {
        return oRepository.findById(id).orElse(null);
    }

    public Order register(Order order) {
        return oRepository.save(order);
    }

    public void delete(int id) {
        oRepository.deleteById(id);
    }

    public Order update(int id, Order order) {
        Order order_to_update = oRepository.findById(id).orElse(null);
        
        if (order_to_update == null) return null;
        
        order.setId(id);
        return oRepository.save(order);
    }
}
