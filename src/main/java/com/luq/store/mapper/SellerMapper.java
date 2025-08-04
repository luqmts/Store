package com.luq.store.mapper;

import com.luq.store.domain.Seller;
import com.luq.store.dto.request.seller.SellerRegisterDTO;
import com.luq.store.dto.response.seller.SellerResponseDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SellerMapper {
    public Seller toEntity(SellerRegisterDTO data) {
        Seller seller = new Seller();
        seller.setName(data.name());
        seller.setMail(data.mail());
        seller.setPhone(data.phone());
        seller.setDepartment(data.department());

        return seller;
    }

    public Seller toEntity(SellerResponseDTO data) {
        return new Seller(
            data.id(),
            data.name(),
            data.mail(),
            data.phone(),
            data.department(),
            data.createdBy(),
            data.created(),
            data.modifiedBy(),
            data.modified()
        );
    }

    public SellerResponseDTO toDTO(Seller seller) {
        return new SellerResponseDTO(
            seller.getId(),
            seller.getName(),
            seller.getMail(),
            seller.getPhone(),
            seller.getDepartment(),
            seller.getCreatedBy(),
            seller.getCreated(),
            seller.getModifiedBy(),
            seller.getModified()
        );
    }

    public List<SellerResponseDTO> toDTOList(List<Seller> oList) {
        return oList
            .stream()
            .map(this::toDTO)
            .toList();
    }
}
