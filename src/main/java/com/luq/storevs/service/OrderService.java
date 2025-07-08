package com.luq.storevs.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.luq.storevs.model.Order;
import com.luq.storevs.repositories.OrderRepository;

@Service
public class OrderService {
    @Autowired
    private OrderRepository oRepository;
    
    public List<Order> getAll() {
        return oRepository.findAll();
    }

    public Order getById(int id) {
        return oRepository.findById(id).orElse(null);
    }

    public Order register(Order order) {
        return oRepository.save(order);
    }

    public void delete(int id) {
        oRepository.deleteById(id);;
    }

    public Order update(int id, Order order) {
        Order order_to_update = oRepository.findById(id).orElse(null);
        
        if (order_to_update == null) return null;
        
        order.setId(id);
        return oRepository.save(order);
    }
}
