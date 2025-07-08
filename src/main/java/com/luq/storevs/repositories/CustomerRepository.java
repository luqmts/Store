package com.luq.storevs.repositories;

import com.luq.storevs.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Integer>{
    
}
