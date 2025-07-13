package com.luq.storevs.repositories;

import com.luq.storevs.model.Order;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Integer>{
    @Query("""
        SELECT o FROM Order o
        WHERE (:productId IS NULL OR o.product.id = :productId)
        AND (:sellerId IS NULL OR o.seller.id = :sellerId)
        AND (:customerId IS NULL OR o.customer.id = :customerId)
    """)
    List<Order> findByProductSellerCustomer(
        Sort sort,
        @Param("productId")Integer productId, 
        @Param("sellerId")Integer sellerId, 
        @Param("customerId")Integer customerId
    );
}
