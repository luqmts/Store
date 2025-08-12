package com.luq.store.dto.request.order;

import java.math.BigDecimal;
import java.time.LocalDate;

public record OrderRegisterDTO(
    BigDecimal totalPrice,
    Integer quantity,
    LocalDate orderDate,
    Integer product_id,
    Integer seller_id,
    Integer customer_id
){}