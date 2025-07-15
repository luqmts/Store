package com.luq.storevs.controllers.API;

import com.luq.storevs.services.OrderService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.luq.storevs.domain.Order;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    protected final OrderService oService;
    
    @Autowired
    public OrderController(OrderService oService){
        this.oService = oService;
    }

    @PostMapping
    public Order registerOrder(@RequestBody Order order){
        return oService.register(order);
    }

    @GetMapping
    public List<Order> getOrders(){
        return oService.getAll();
    }

    @GetMapping(path="/{id}")
    public Order getOrderById(@PathVariable("id") int id){
        return oService.getById(id);
    }

    @PutMapping(path="/{id}")
    public Order updateOrder(@PathVariable("id") int id, @RequestBody Order order){
        return oService.update(id, order);
    }

    @DeleteMapping(path="{id}")
    public void removeOrder(@PathVariable("id") int id){
        oService.delete(id);
    }
}
