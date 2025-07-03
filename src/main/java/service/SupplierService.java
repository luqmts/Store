package service;

import repositories.SupplierRepository;
import model.Supplier;

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

    public void delete(int id) {
        pRepository.deleteById(id);
    }
}
