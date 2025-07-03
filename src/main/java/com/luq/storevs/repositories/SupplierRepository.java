package com.luq.storevs.repositories;

import com.luq.storevs.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier, Integer>{
    
}
