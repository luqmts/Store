package com.luq.store.repositories;

import com.luq.store.domain.Supply;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SupplyRepository extends JpaRepository<Supply, Integer>{
    List<Supply> getAllByProductId(
        Sort sort,
        Integer productId
    );

    @Query("""
        SELECT s from Supply s
        WHERE (:productId IS NULL OR s.product.id = :productId)
    """)
    Supply getByProductId(
        @Param("productId") Integer productId
    );
}
