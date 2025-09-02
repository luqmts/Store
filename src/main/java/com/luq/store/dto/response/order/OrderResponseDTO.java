package com.luq.store.dto.response.order;

import com.luq.store.domain.Customer;
import com.luq.store.domain.OrderStatus;
import com.luq.store.domain.Product;
import com.luq.store.domain.Seller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record OrderResponseDTO(
    Integer id,
    String checkoutUrl,
    BigDecimal unitPrice,
    BigDecimal totalPrice,
    OrderStatus status,
    Integer quantity,
    LocalDate orderDate,
    Product product,
    Seller seller,
    Customer customer,
    String createdBy,
    LocalDateTime created,
    String modifiedBy,
    LocalDateTime modified
){}
