package com.luq.storevs.service;

import com.luq.storevs.repositories.SupplierRepository;
import com.luq.storevs.model.Supplier;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SupplierService {
    @Autowired
    private SupplierRepository sRepository;

    public List<Supplier> getAll() {
        return sRepository.findAll();
    }

    public Supplier getById(int id) {
        return sRepository.findById(id).orElse(null);
    }

    public Supplier register(Supplier supplier) {
        return sRepository.save(supplier);
    }

    public Supplier update(int id, Supplier supplier) {
        Supplier supplier_to_update = sRepository.findById(id).orElse(null);

        if (supplier_to_update == null) return null;

        supplier_to_update.setName(supplier.getName());
        supplier_to_update.setCnpj(supplier.getCnpj());
        supplier_to_update.setMail(supplier.getMail());
        supplier_to_update.setPhone(supplier.getPhone());

        return sRepository.save(supplier_to_update);
    }

    public void delete(int id) {
        sRepository.deleteById(id);
    }
}
