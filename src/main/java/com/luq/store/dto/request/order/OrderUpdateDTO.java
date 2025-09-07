package com.luq.store.dto.request.order;

import com.luq.store.domain.OrderStatus;

import java.time.LocalDate;

public record OrderUpdateDTO(
    OrderStatus status,
    Integer quantity,
    LocalDate orderDate,
    Integer productId,
    Integer sellerId,
    Integer customerId
){}
