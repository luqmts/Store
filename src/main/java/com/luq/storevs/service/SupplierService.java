package com.luq.storevs.service;

import com.luq.storevs.repositories.SupplierRepository;
import com.luq.storevs.model.Supplier;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SupplierService {
    @Autowired
    private SupplierRepository pRepository;

    public List<Supplier> getAll() {
        return pRepository.findAll();
    }

    public Supplier getById(int id) {
        return pRepository.findById(id).orElse(null);
    }

    public Supplier register(Supplier supplier) {
        return pRepository.save(supplier);
    }

    public Supplier update(int id, Supplier supplier) {
        Supplier supplier_to_update = pRepository.findById(id).orElse(null);

        if (supplier_to_update == null) return null;

        supplier_to_update.setName(supplier.getName());
        supplier_to_update.setCNPJ(supplier.getCNPJ());
        supplier_to_update.setMail(supplier.getMail());
        supplier_to_update.setPhone(supplier.getPhone());

        return pRepository.save(supplier_to_update);
    }

    public void delete(int id) {
        pRepository.deleteById(id);
    }
}
