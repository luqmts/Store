package com.luq.storevs.service;

import com.luq.storevs.domain.Supply;
import com.luq.storevs.domain.Product;
import com.luq.storevs.repositories.SupplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplyService {
    @Autowired
    private SupplyRepository sRepository;
    
    public List<Supply> getAll() {
        return sRepository.findAll();
    }

    public List<Supply> getAllSorted(String sortBy, String direction, Integer productId) {
        Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        if (productId != null)
            return sRepository.findByProductId(sort, productId);
        return sRepository.findAll(sort);
    }

    public Supply getById(int id) {
        return sRepository.findById(id).orElse(null);
    }

    public Supply getByProductId(Product product) {
        return sRepository.getByProductId(product);
    }

    public Supply register(Supply supply) {
        return sRepository.save(supply);
    }

    public void delete(int id) {
        sRepository.deleteById(id);
    }

    public Supply update(int id, Supply supply) {
        Supply supply_to_update = sRepository.findById(id).orElse(null);
        
        if (supply_to_update == null) return null;
        
        supply.setId(id);
        return sRepository.save(supply);
    }
}
