package com.luq.storevs.service;

import com.luq.storevs.repositories.SupplierRepository;
import com.luq.storevs.model.Supplier;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class SupplierService {
    @Autowired
    private SupplierRepository sRepository;

    public List<Supplier> getAll() {
        return sRepository.findAll();
    }

    public List<Supplier> getAllSorted(String sortBy, String direction, String name, String cnpj, String mail, String phone) {
        Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        if (name != null || cnpj != null || mail != null || phone != null)
            return sRepository.findByNameAndCnpjAndMailAndPhone(sort, name, cnpj, mail, phone);

        return sRepository.findAll(sort);
    }

    public Supplier getById(int id) {
        return sRepository.findById(id).orElse(null);
    }

    public Supplier register(Supplier supplier) {
        return sRepository.save(supplier);
    }
    
    public void delete(int id) {
        sRepository.deleteById(id);
    }

    public Supplier update(int id, Supplier supplier) {
        Supplier supplier_to_update = sRepository.findById(id).orElse(null);

        if (supplier_to_update == null) return null;

        supplier.setId(id);
        return sRepository.save(supplier);
    }

}
