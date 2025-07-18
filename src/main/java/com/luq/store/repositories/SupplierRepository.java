package com.luq.store.repositories;

import com.luq.store.domain.Supplier;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SupplierRepository extends JpaRepository<Supplier, Integer>{
    @Query("""
        SELECT s FROM Supplier s
        WHERE (:name IS NULL OR s.name LIKE :name || '%')
        AND (:cnpj IS NULL OR s.cnpj LIKE :cnpj || '%')
        AND (:mail IS NULL OR s.mail LIKE :mail || '%')
        AND (:phone IS NULL OR s.phone LIKE :phone || '%')
    """)
    List<Supplier> findByNameAndCnpjAndMailAndPhone(
        Sort sort,
        @Param("name") String name,
        @Param("cnpj") String cnpj,
        @Param("mail") String mail,
        @Param("phone") String phone
    );
}
