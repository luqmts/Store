package com.luq.storevs.repositories;

import com.luq.storevs.model.Product;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer>{
    List<Product> findBySupplierId(Integer supplierId, Sort sort);
}