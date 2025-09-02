package com.luq.store.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.luq.store.dto.request.product.ProductRegisterDTO;
import com.luq.store.dto.request.product.ProductUpdateDTO;
import com.luq.store.dto.response.product.ProductResponseDTO;
import com.luq.store.exceptions.InvalidProductPriceException;
import com.luq.store.exceptions.NotFoundException;
import com.luq.store.mapper.ProductMapper;
import com.luq.store.mapper.SupplierMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.luq.store.domain.Product;
import com.luq.store.repositories.ProductRepository;

@Service
public class ProductService {
    @Autowired
    private ProductRepository pRepository;
    @Autowired
    private ProductMapper pMapper;

    @Autowired
    private SupplierService sService;
    @Autowired
    private SupplierMapper sMapper;
    
    public List<ProductResponseDTO> getAll() {
        return pMapper.toDTOList(pRepository.findAll());
    }
    
    public List<ProductResponseDTO> getAllSorted(String sortBy, String direction, Integer supplierId, String name, String sku) {
        Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        
        if (supplierId != null || name != null || sku != null) {
            return pMapper.toDTOList(pRepository.findBySupplierIdAndNameAndSku(sort, supplierId, name, sku));
        }

        return pMapper.toDTOList(pRepository.findAll(sort));
    }

    public List<ProductResponseDTO> getAllNotRegisteredOnSupply(){
        return pMapper.toDTOList(pRepository.findAllNotRegisteredOnSupply());
    }

    public List<ProductResponseDTO> getAllNotRegisteredOnSupply(Integer supplyId){
        return pMapper.toDTOList(pRepository.findAllNotRegisteredOnSupply(supplyId));
    }

    public List<ProductResponseDTO> getAllRegisteredOnSupply(){
        return pMapper.toDTOList(pRepository.findAllRegisteredOnSupply());
    }

    public List<ProductResponseDTO> getAllRegisteredOnSupply(Integer id){
        return pMapper.toDTOList(pRepository.findAllRegisteredOnSupply(id));
    }

    public ProductResponseDTO getById(int id) {
        Product product = pRepository.findById(id).orElse(null);
        assert product != null;
        return pMapper.toDTO(product);
    }

    public ProductResponseDTO register(ProductRegisterDTO data) {
        if (data.price().compareTo(BigDecimal.valueOf(1)) < 0)
            throw new InvalidProductPriceException("Product's price must be greater or equal than 1");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Product product = pMapper.toEntity(data);

        product.setModifiedBy(authentication.getName());
        product.setModified(LocalDateTime.now());
        product.setCreatedBy(authentication.getName());
        product.setCreated(LocalDateTime.now());

        product = pRepository.save(product);
        return pMapper.toDTO(product);
    }

    public ProductResponseDTO update(int id, ProductUpdateDTO data) {
        Product product = pRepository.findById(id).orElse(null);
        if (product == null) throw new NotFoundException("Product not found");

        if (data.price().compareTo(BigDecimal.valueOf(1)) < 0) {
            throw new InvalidProductPriceException("Product's price must be greater or equal than 1");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        product.setName(data.name());
        product.setSku(data.sku());
        product.setDescription(data.description());
        product.setPrice(data.price());
        product.setSupplier(sMapper.toEntity(sService.getById(data.supplier_id())));

        product.setModifiedBy(authentication.getName());
        product.setModified(LocalDateTime.now());

        return pMapper.toDTO(pRepository.save(product));
    }

    public void delete(int id) {
        pRepository.deleteById(id);
    }
}
