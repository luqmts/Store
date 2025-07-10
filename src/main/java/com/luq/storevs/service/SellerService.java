package com.luq.storevs.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.luq.storevs.model.Seller;
import com.luq.storevs.repositories.SellerRepository;

@Service
public class SellerService {
    @Autowired
    private SellerRepository sRepository;
    
    public List<Seller> getAll() {
        return sRepository.findAll();
    }

    public List<Seller> getAllSorted(String department, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        if (department != null){
            Seller.Department depEnum = Seller.Department.valueOf(department);
            return sRepository.findByDepartment(depEnum, sort);
        }
        return sRepository.findAll(sort);
    }

    public Seller getById(int id) {
        return sRepository.findById(id).orElse(null);
    }

    public Seller register(Seller order) {
        return sRepository.save(order);
    }

    public void delete(int id) {
        sRepository.deleteById(id);;
    }

    public Seller update(int id, Seller order) {
        Seller order_to_update = sRepository.findById(id).orElse(null);
        
        if (order_to_update == null) return null;

        order.setId(id);
        return sRepository.save(order);
    }
}
