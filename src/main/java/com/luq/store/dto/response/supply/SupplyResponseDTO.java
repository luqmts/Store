package com.luq.store.dto.response.supply;

import com.luq.store.domain.Product;

import java.time.LocalDateTime;

public record SupplyResponseDTO(
    Integer id,
    Integer quantity,
    Product product,
    String createdBy,
    LocalDateTime created,
    String modifiedBy,
    LocalDateTime modified
){}
