package com.luq.store.services;

import com.luq.store.dto.request.supplier.SupplierRegisterDTO;
import com.luq.store.dto.request.supplier.SupplierUpdateDTO;
import com.luq.store.dto.response.supplier.SupplierResponseDTO;
import com.luq.store.exceptions.*;
import com.luq.store.mapper.SupplierMapper;
import com.luq.store.repositories.SupplierRepository;
import com.luq.store.domain.Supplier;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.luq.store.valueobjects.Cnpj;
import com.luq.store.valueobjects.Mail;
import com.luq.store.valueobjects.Phone;
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
        List<IllegalArgumentException> errors = new ArrayList<>();

        try{new Cnpj(data.cnpj());}
        catch (InvalidCnpjException e) { errors.add(new InvalidCnpjException("Invalid cnpj")); }

        try{new Mail(data.mail());}
        catch (InvalidMailException e) { errors.add(new InvalidMailException("Invalid mail")); }

        try{new Phone(data.phone());}
        catch (InvalidPhoneException e) { errors.add(new InvalidPhoneException("Invalid phone")); }

        if (!errors.isEmpty()) throw new MultipleValidationException(errors);

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
        Supplier supplier = sRepository.findById(id).orElse(null);
        if (supplier == null) throw new NotFoundException("Supplier not found");

        List<IllegalArgumentException> errors = new ArrayList<>();

        try{supplier.setCnpj(new Cnpj(data.cnpj()));}
        catch (InvalidCnpjException e) { errors.add(new InvalidCnpjException("Invalid cnpj")); }

        try{supplier.setMail(new Mail(data.mail()));}
        catch (InvalidMailException e) { errors.add(new InvalidMailException("Invalid mail")); }

        try{supplier.setPhone(new Phone(data.phone()));}
        catch (InvalidPhoneException e) { errors.add(new InvalidPhoneException("Invalid phone")); }


        if (!errors.isEmpty()) throw new MultipleValidationException(errors);

        Cnpj cnpj = new Cnpj(data.cnpj());
        Mail mail = new Mail(data.mail());
        Phone phone = new Phone(data.phone());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        supplier.setName(data.name());
        supplier.setCnpj(cnpj);
        supplier.setMail(mail);
        supplier.setPhone(phone);

        supplier.setModifiedBy(authentication.getName());
        supplier.setModified(LocalDateTime.now());

        return sMapper.toDTO(sRepository.save(supplier));
    }

    public void delete(int id) {
        sRepository.deleteById(id);
    }
}
