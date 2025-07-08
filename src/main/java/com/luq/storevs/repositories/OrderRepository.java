package com.luq.storevs.repositories;

import com.luq.storevs.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer>{
    
}
