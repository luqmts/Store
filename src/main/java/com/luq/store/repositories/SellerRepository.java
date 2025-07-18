package com.luq.store.repositories;

import com.luq.store.domain.Seller;
import com.luq.store.domain.Department;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SellerRepository extends JpaRepository<Seller, Integer>{
    
    @Query("""
        SELECT s FROM Seller s
        WHERE (:department IS NULL OR s.department = :department)
        AND (:name IS NULL OR s.name LIKE :name || '%')
        AND (:mail IS NULL OR s.mail LIKE :mail || '%')
        AND (:phone IS NULL OR s.phone LIKE :phone || '%')
    """)
       List<Seller> findByDepartmentAndNameAndMailAndPhone(
        Sort sort,
        @Param("department") Department department,
        @Param("name") String name,
        @Param("mail") String mail,
        @Param("phone") String phone
    );
}
