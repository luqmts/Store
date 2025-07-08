package com.luq.storevs.controller.API;

import com.luq.storevs.service.CustomerService;
import com.luq.storevs.service.OrderService;

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

import com.luq.storevs.model.Customer;

@RestController
@RequestMapping("/customers")
public class CustomerController {
    protected final CustomerService cService;
    
    @Autowired
    public CustomerController(CustomerService cService){
        this.cService = cService;
    }

    @PostMapping
    public Customer registerCustomer(@RequestBody Customer customer){
        return cService.register(customer);
    }

    @GetMapping
    public List<Customer> getCustomers(){
        return cService.getAll();
    }

    @GetMapping(path="/{id}")
    public Customer getCustomerById(@PathVariable("id") int id){
        return cService.getById(id);
    }

    @PutMapping(path="/{id}")
    public Customer updateCustomer(@PathVariable("id") int id, @RequestBody Customer customer){
        return cService.update(id, customer);
    }

    @DeleteMapping(path="{id}")
    public void removeCustomer(@PathVariable("id") int id){
        cService.delete(id);
    }
}
