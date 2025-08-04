package com.luq.store.controllers.API;

import com.luq.store.dto.request.customer.CustomerRegisterDTO;
import com.luq.store.dto.request.customer.CustomerUpdateDTO;
import com.luq.store.dto.response.customer.CustomerResponseDTO;
import com.luq.store.services.CustomerService;

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
@RequestMapping("/api/customer")
public class CustomerController {
    protected final CustomerService cService;
    
    @Autowired
    public CustomerController(CustomerService cService){
        this.cService = cService;
    }

    @PostMapping
    public CustomerResponseDTO registerCustomer(@RequestBody CustomerRegisterDTO data) {
        return cService.register(data);
    }

    @GetMapping
    public List<CustomerResponseDTO> getCustomers(){
        return cService.getAll();
    }

    @GetMapping(path="/{id}")
    public CustomerResponseDTO getCustomerById(@PathVariable("id") int id){
        return cService.getById(id);
    }

    @PutMapping(path="/{id}")
    public CustomerResponseDTO updateCustomer(@PathVariable("id") int id, @RequestBody CustomerUpdateDTO data){
        return cService.update(id, data);
    }

    @DeleteMapping(path="{id}")
    public void removeCustomer(@PathVariable("id") int id){
        cService.delete(id);
    }
}
