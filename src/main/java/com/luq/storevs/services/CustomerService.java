package com.luq.storevs.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.luq.storevs.domain.Customer;
import com.luq.storevs.repositories.CustomerRepository;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository cRepository;
    
    public List<Customer> getAll() {
        return cRepository.findAll();
    }

    public List<Customer> getAllSorted(String sortBy, String direction, String name) {
        Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        if (name != null)
            return cRepository.findByNameIgnoreCase(sort, name);
        return cRepository.findAll(sort);
    }

    public Customer getById(int id) {
        return cRepository.findById(id).orElse(null);
    }

    public Customer register(Customer customer) {
        return cRepository.save(customer);
    }

    public void delete(int id) {
        cRepository.deleteById(id);
    }

    public Customer update(int id, Customer customer) {
        Customer product_to_update = cRepository.findById(id).orElse(null);
        
        if (product_to_update == null) return null;
        
        customer.setId(id);
        return cRepository.save(customer);
    }
}
