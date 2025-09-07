package com.luq.store.services;

import com.luq.store.domain.Supply;
import com.luq.store.dto.request.supply.SupplyRegisterDTO;
import com.luq.store.dto.request.supply.SupplyUpdateDTO;
import com.luq.store.dto.response.supply.SupplyResponseDTO;
import com.luq.store.exceptions.InvalidQuantityException;
import com.luq.store.exceptions.NotFoundException;
import com.luq.store.exceptions.ProductRegisteredException;
import com.luq.store.mapper.ProductMapper;
import com.luq.store.mapper.SupplyMapper;
import com.luq.store.repositories.SupplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SupplyService {
    @Autowired
    private SupplyRepository sRepository;
    @Autowired
    private SupplyMapper sMapper;

    @Autowired
    private ProductService pService;
    @Autowired
    private ProductMapper pMapper;

    public List<SupplyResponseDTO> getAll() {
        return sMapper.toDTOList(sRepository.findAll());
    }

    public List<SupplyResponseDTO> getAllSorted(String sortBy, String direction, Integer productId) {
        Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        if (productId != null)
            return sMapper.toDTOList(sRepository.getAllByProductId(sort, productId));
        return sMapper.toDTOList(sRepository.findAll(sort));
    }

    public SupplyResponseDTO getById(int id) {
        Supply supply = sRepository.findById(id).orElse(null);
        assert supply != null;
        return sMapper.toDTO(supply);
    }

    public SupplyResponseDTO getByProductId(Integer product_id) {
        return sMapper.toDTO(sRepository.getByProductId(product_id));
    }

    public SupplyResponseDTO register(SupplyRegisterDTO data) {
        if (data.quantity().compareTo(0) < 0)
            throw new InvalidQuantityException("Quantity must be greater or equal to 0");

        if (sRepository.getByProductId(data.productId()) != null)
            throw new ProductRegisteredException("This product is already registered on supply, please update it instead");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Supply supply = sMapper.toEntity(data);

        supply.setModifiedBy(authentication.getName());
        supply.setModified(LocalDateTime.now());
        supply.setCreatedBy(authentication.getName());
        supply.setCreated(LocalDateTime.now());

        supply = sRepository.save(supply);
        return sMapper.toDTO(supply);
    }

    public SupplyResponseDTO update(int id, SupplyUpdateDTO data) {
        Supply supply = sRepository.findById(id).orElse(null);
        if (supply == null) throw new NotFoundException("Supply not found");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        supply.setProduct(pMapper.toEntity(pService.getById(data.productId())));
        supply.setQuantity(data.quantity());

        supply.setModifiedBy(authentication.getName());
        supply.setModified(LocalDateTime.now());

        return sMapper.toDTO(sRepository.save(supply));
    }

    public void delete(int id) {
        sRepository.deleteById(id);
    }
}
