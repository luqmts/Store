package com.luq.store.repositories;

import com.luq.store.domain.Order;

import java.util.List;

import com.luq.store.domain.OrderStatus;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Integer>{
    @Query("""
        SELECT o FROM Order o
        WHERE (:status IS NULL OR o.status = :status)
        AND (:productId IS NULL OR o.product.id = :productId)
        AND (:sellerId IS NULL OR o.seller.id = :sellerId)
        AND (:customerId IS NULL OR o.customer.id = :customerId)
    """)
    List<Order> findByStatusProductSellerCustomer(
        Sort sort,
        @Param("status") OrderStatus status,
        @Param("productId")Integer productId, 
        @Param("sellerId")Integer sellerId, 
        @Param("customerId")Integer customerId
    );
}
