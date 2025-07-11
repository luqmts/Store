package com.luq.storevs.repositories;

import com.luq.storevs.model.Customer;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Integer>{
    List<Customer> findByNameIgnoreCase(
        Sort sort,  
        String name
    );
}
