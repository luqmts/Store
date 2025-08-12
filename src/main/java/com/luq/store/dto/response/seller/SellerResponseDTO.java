package com.luq.store.dto.response.seller;

import com.luq.store.domain.Department;
import com.luq.store.valueobjects.Mail;
import com.luq.store.valueobjects.Phone;

import java.time.LocalDateTime;

public record SellerResponseDTO(
    Integer id,
    String name,
    Mail mail,
    Phone phone,
    Department department,
    String createdBy,
    LocalDateTime created,
    String modifiedBy,
    LocalDateTime modified
){}
