package com.luq.store.dto.response.supplier;

import com.luq.store.valueobjects.Cnpj;
import com.luq.store.valueobjects.Mail;
import com.luq.store.valueobjects.Phone;

import java.time.LocalDateTime;

public record SupplierResponseDTO(
    Integer id,
    String name,
    Cnpj cnpj,
    Mail mail,
    Phone phone,
    String createdBy,
    LocalDateTime created,
    String modifiedBy,
    LocalDateTime modified
){}
