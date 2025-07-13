package com.luq.storevs.repositories;

import com.luq.storevs.model.Product;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Integer>{
    @Query("""
        SELECT p FROM Product p
        WHERE (:supplierId IS NULL OR p.supplier.id = :supplierId)
        AND (:name IS NULL OR p.name LIKE :name || '%')
        AND (:sku IS NULL OR p.sku LIKE :sku || '%')
    """)
    List<Product> findBySupplierIdAndNameAndSku(
        Sort sort,
        @Param("supplierId") Integer supplierId,
        @Param("name") String name,
        @Param("sku") String sku
    );

    @Query("""
        SELECT p FROM Product p
        WHERE (p NOT IN (SELECT s.product FROM Supply s))
    """)
    List<Product> findAllNotRegisteredOnSupply();

    @Query("""
        SELECT p FROM Product p
        WHERE (p IN (
            SELECT s.product FROM Supply s
            WHERE s.quantity > 0
        ))
    """)
    List<Product> findAllRegisteredOnSupply();
}