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

        order_to_update.setProduct(order.getProduct());
        order_to_update.setQuantity(order.getQuantity());
        order_to_update.setSeller(order.getSeller());
        order_to_update.setTotalPrice(order.getTotalPrice());

        return oRepository.save(order_to_update);
    }
}
