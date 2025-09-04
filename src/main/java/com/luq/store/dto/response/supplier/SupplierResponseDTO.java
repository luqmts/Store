package com.luq.store.dto.response.supplier;

import com.luq.store.valueobjects.Cnpj;
import com.luq.store.valueobjects.Mail;
import com.luq.store.valueobjects.Phone;
import com.luq.store.valueobjects.converters.MailConverter;
import jakarta.persistence.Convert;

import java.time.LocalDateTime;

public record SupplierResponseDTO(
    Integer id,
    String name,
    String cnpj,
    String mail,
    String phone,
    String createdBy,
    LocalDateTime created,
    String modifiedBy,
    LocalDateTime modified
){}
