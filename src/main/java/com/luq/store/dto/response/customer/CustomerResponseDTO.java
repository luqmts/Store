package com.luq.store.dto.response.customer;

import java.time.LocalDateTime;

public record CustomerResponseDTO (
    Integer id,
    String name,
    String createdBy,
    LocalDateTime created,
    String modifiedBy,
    LocalDateTime modified
){}
