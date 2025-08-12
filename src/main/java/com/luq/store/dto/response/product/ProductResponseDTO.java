package com.luq.store.dto.response.product;

import com.luq.store.domain.Supplier;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductResponseDTO(
    Integer id,
    String name,
    String sku,
    String description,
    BigDecimal price,
    Supplier supplier,
    String createdBy,
    LocalDateTime created,
    String modifiedBy,
    LocalDateTime modified
){}
