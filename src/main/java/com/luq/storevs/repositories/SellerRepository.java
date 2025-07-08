package com.luq.storevs.repositories;

import com.luq.storevs.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerRepository extends JpaRepository<Seller, Integer>{
    
}
