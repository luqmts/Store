package com.luq.storevs.repositories;

import com.luq.storevs.model.Seller;
import com.luq.storevs.model.Seller.Department;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerRepository extends JpaRepository<Seller, Integer>{
    List<Seller> findByDepartment(Department department, Sort sort);
}
