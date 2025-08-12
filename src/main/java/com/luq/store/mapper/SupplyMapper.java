package com.luq.store.mapper;

import com.luq.store.domain.Product;
import com.luq.store.domain.Supply;
import com.luq.store.dto.request.supply.SupplyRegisterDTO;
import com.luq.store.dto.request.supply.SupplyUpdateDTO;
import com.luq.store.dto.response.supply.SupplyResponseDTO;
import com.luq.store.exceptions.NotFoundException;
import com.luq.store.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SupplyMapper {
    @Autowired
    private ProductService pService;
    @Autowired
    private ProductMapper pMapper;

    public Supply toEntity(SupplyRegisterDTO data) {
        Product product;

        try {
            product = pMapper.toEntity(pService.getById(data.product_id()));
        } catch (NullPointerException e) {
            throw new NotFoundException("Product not found");
        }

        Supply supply = new Supply();
        supply.setQuantity(data.quantity());
        supply.setProduct(product);

        return supply;
    }

    public Supply toEntity(SupplyUpdateDTO data) {
        Product product;

        try {
            product = pMapper.toEntity(pService.getById(data.product_id()));
        } catch (NullPointerException e) {
            throw new NotFoundException("Product not found");
        }

        Supply supply = new Supply();
        supply.setQuantity(data.quantity());
        supply.setProduct(product);

        return supply;
    }

    public Supply toEntity(SupplyResponseDTO data) {
        return new Supply(
            data.id(),
            data.quantity(),
            data.product(),
            data.createdBy(),
            data.created(),
            data.modifiedBy(),
            data.modified()
        );
    }

    public SupplyResponseDTO toDTO(Supply supply) {
        return new SupplyResponseDTO(
            supply.getId(),
            supply.getQuantity(),
            supply.getProduct(),
            supply.getCreatedBy(),
            supply.getCreated(),
            supply.getModifiedBy(),
            supply.getModified()
        );
    }

    public List<SupplyResponseDTO> toDTOList(List<Supply> oList) {
        return oList
            .stream()
            .map(this::toDTO)
            .toList();
    }
}
