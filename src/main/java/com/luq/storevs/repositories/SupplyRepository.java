package com.luq.storevs.repositories;

import com.luq.storevs.domain.Product;
import com.luq.storevs.domain.Supply;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SupplyRepository extends JpaRepository<Supply, Integer>{
    List<Supply> findByProductId(
        Sort sort,
        int productId
    );

    @Query("""
        SELECT s from Supply s
        WHERE (:product IS NULL OR s.product = :product)
    """)
    Supply getByProductId(
        @Param("product") Product product
    );
}
