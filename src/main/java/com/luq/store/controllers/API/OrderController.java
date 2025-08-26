package com.luq.store.controllers.API;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.luq.store.dto.request.order.OrderRegisterDTO;
import com.luq.store.dto.request.order.OrderUpdateDTO;
import com.luq.store.dto.response.order.OrderResponseDTO;
import com.luq.store.services.OrderService;

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

@RestController
@RequestMapping("/api/order")
public class OrderController {
    protected final OrderService oService;
    
    @Autowired
    public OrderController(OrderService oService){
        this.oService = oService;
    }

    @PostMapping
    public OrderResponseDTO registerOrder(@RequestBody OrderRegisterDTO data) throws JsonProcessingException {
        return oService.register(data);
    }

    @GetMapping
    public List<OrderResponseDTO> getOrders(){
        return oService.getAll();
    }

    @GetMapping(path="/{id}")
    public OrderResponseDTO getOrderById(@PathVariable("id") int id){
        return oService.getById(id);
    }

    @PutMapping(path="/{id}")
    public OrderResponseDTO updateOrder(@PathVariable("id") int id, @RequestBody OrderUpdateDTO data){
        return oService.update(id, data);
    }

    @DeleteMapping(path="{id}")
    public void removeOrder(@PathVariable("id") int id){
        oService.delete(id);
    }
}
