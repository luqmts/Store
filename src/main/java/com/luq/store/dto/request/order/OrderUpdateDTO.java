package com.luq.store.dto.request.order;

import com.luq.store.domain.OrderStatus;

import java.time.LocalDate;

public record OrderUpdateDTO(
    OrderStatus status,
    Integer quantity,
    LocalDate orderDate,
    Integer product_id,
    Integer seller_id,
    Integer customer_id
){}
