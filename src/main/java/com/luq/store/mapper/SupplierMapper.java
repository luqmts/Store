package com.luq.store.mapper;

import com.luq.store.domain.Supplier;
import com.luq.store.dto.request.supplier.SupplierRegisterDTO;
import com.luq.store.dto.response.supplier.SupplierResponseDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SupplierMapper {
    public Supplier toEntity(SupplierRegisterDTO data) {
        Supplier supplier = new Supplier();
        supplier.setName(data.name());
        supplier.setCnpj(data.cnpj());
        supplier.setMail(data.mail());
        supplier.setPhone(data.phone());

        return supplier;
    }

    public Supplier toEntity(SupplierResponseDTO data) {
        return new Supplier(
            data.id(),
            data.name(),
            data.cnpj(),
            data.mail(),
            data.phone(),
            data.createdBy(),
            data.created(),
            data.modifiedBy(),
            data.modified()
        );
    }

    public SupplierResponseDTO toDTO(Supplier supplier) {
        return new SupplierResponseDTO(
            supplier.getId(),
            supplier.getName(),
            supplier.getCnpj(),
            supplier.getMail(),
            supplier.getPhone(),
            supplier.getCreatedBy(),
            supplier.getCreated(),
            supplier.getModifiedBy(),
            supplier.getModified()
        );
    }

    public List<SupplierResponseDTO> toDTOList(List<Supplier> oList) {
        return oList
            .stream()
            .map(this::toDTO)
            .toList();
    }
}
