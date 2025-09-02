package com.luq.store.dto.request.order;

import java.time.LocalDate;

public record OrderRegisterDTO(
    Integer quantity,
    LocalDate orderDate,
    Integer product_id,
    Integer seller_id,
    Integer customer_id
){}