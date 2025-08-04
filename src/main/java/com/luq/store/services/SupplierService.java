package com.luq.store.services;

import com.luq.store.dto.request.supplier.SupplierRegisterDTO;
import com.luq.store.dto.request.supplier.SupplierUpdateDTO;
import com.luq.store.dto.response.supplier.SupplierResponseDTO;
import com.luq.store.exceptions.InvalidCnpjException;
import com.luq.store.exceptions.InvalidMailException;
import com.luq.store.exceptions.InvalidPhoneException;
import com.luq.store.exceptions.NotFoundException;
import com.luq.store.mapper.SupplierMapper;
import com.luq.store.repositories.SupplierRepository;
import com.luq.store.domain.Supplier;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SupplierService {
    @Autowired
    private SupplierRepository sRepository;
    @Autowired
    private SupplierMapper sMapper;

    public List<SupplierResponseDTO> getAll() {
        return sMapper.toDTOList(sRepository.findAll());
    }

    public List<SupplierResponseDTO> getAllSorted(String sortBy, String direction, String name, String cnpj, String mail, String phone) {
        Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        if (name != null || cnpj != null || mail != null || phone != null)
            return sMapper.toDTOList(sRepository.findByNameAndCnpjAndMailAndPhone(sort, name, cnpj, mail, phone));

        return sMapper.toDTOList(sRepository.findAll());
    }

    public SupplierResponseDTO getById(int id) {
        Supplier supplier = sRepository.findById(id).orElse(null);
        assert supplier != null;
        return sMapper.toDTO(supplier);
    }

    public SupplierResponseDTO register(SupplierRegisterDTO data) {
        if (data.mail().toString().isBlank()) throw new InvalidMailException("Invalid mail");
        if (data.phone().toString().isBlank()) throw new InvalidPhoneException("Invalid phone");
        if (data.cnpj().toString().isBlank()) throw new InvalidCnpjException("Invalid cnpj");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Supplier supplier = sMapper.toEntity(data);


        supplier.setModifiedBy(authentication.getName());
        supplier.setModified(LocalDateTime.now());
        supplier.setCreatedBy(authentication.getName());
        supplier.setCreated(LocalDateTime.now());

        supplier = sRepository.save(supplier);
        return sMapper.toDTO(supplier);
    }

    public SupplierResponseDTO update(int id, SupplierUpdateDTO data) {
        if (data.mail().toString().isBlank()) throw new InvalidMailException("Invalid mail");
        if (data.phone().toString().isBlank()) throw new InvalidPhoneException("Invalid phone");
        if (data.cnpj().toString().isBlank()) throw new InvalidCnpjException("Invalid cnpj");

        Supplier supplier = sRepository.findById(id).orElse(null);

        if (supplier == null) throw new NotFoundException("Supplier not found");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        supplier.setName(data.name());
        supplier.setCnpj(data.cnpj());
        supplier.setMail(data.mail());
        supplier.setPhone(data.phone());

        supplier.setModifiedBy(authentication.getName());
        supplier.setModified(LocalDateTime.now());

        return sMapper.toDTO(sRepository.save(supplier));
    }

    public void delete(int id) {
        sRepository.deleteById(id);
    }
}
