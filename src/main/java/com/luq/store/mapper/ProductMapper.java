package com.luq.store.mapper;

import com.luq.store.domain.Product;
import com.luq.store.domain.Supplier;
import com.luq.store.dto.request.product.ProductRegisterDTO;
import com.luq.store.dto.request.product.ProductUpdateDTO;
import com.luq.store.dto.response.product.ProductResponseDTO;
import com.luq.store.exceptions.NotFoundException;
import com.luq.store.services.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductMapper {
    @Autowired
    private SupplierService sService;
    @Autowired
    private SupplierMapper sMapper;

    public Product toEntity(ProductRegisterDTO data) {
        Supplier supplier;

        try {
            supplier = sMapper.toEntity(sService.getById(data.supplier_id()));
        } catch (NullPointerException e) {
            throw new NotFoundException("Supplier not found");
        }

        Product product = new Product();
        product.setName(data.name());
        product.setSku(data.sku());
        product.setDescription(data.description());
        product.setPrice(data.price());
        product.setSupplier(supplier);

        return product;
    }

    public Product toEntity(ProductUpdateDTO data) {
        Supplier supplier;

        try {
            supplier = sMapper.toEntity(sService.getById(data.supplier_id()));
        } catch (NullPointerException e) {
            throw new NotFoundException("Supplier not found");
        }

        Product product = new Product();
        product.setName(data.name());
        product.setSku(data.sku());
        product.setDescription(data.description());
        product.setPrice(data.price());
        product.setSupplier(supplier);

        return product;
    }

    public Product toEntity(ProductResponseDTO data) {
        return new Product(
            data.id(),
            data.name(),
            data.sku(),
            data.description(),
            data.price(),
            data.supplier(),
            data.createdBy(),
            data.created(),
            data.modifiedBy(),
            data.modified()
        );
    }

    public ProductResponseDTO toDTO(Product product) {
        return new ProductResponseDTO(
            product.getId(),
            product.getName(),
            product.getSku(),
            product.getDescription(),
            product.getPrice(),
            product.getSupplier(),
            product.getCreatedBy(),
            product.getCreated(),
            product.getModifiedBy(),
            product.getModified()
        );
    }

    public List<ProductResponseDTO> toDTOList(List<Product> oList) {
        return oList
            .stream()
            .map(this::toDTO)
            .toList();
    }
}
