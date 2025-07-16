package com.luq.storevs.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.luq.storevs.domain.Department;
import com.luq.storevs.domain.Seller;
import com.luq.storevs.repositories.SellerRepository;

@Service
public class SellerService {
    @Autowired
    private SellerRepository sRepository;
    
    public List<Seller> getAll() {
        return sRepository.findAll();
    }

    public List<Seller> getAllSorted(String sortBy, String direction, String department, String name, String mail, String phone) {
        Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        
        if (department != null || name != null || mail != null || phone != null){
            return sRepository.findByDepartmentAndNameAndMailAndPhone(sort, Department.getDepartment(department), name, mail, phone);
        }
        return sRepository.findAll(sort);
    }

    public Seller getById(int id) {
        return sRepository.findById(id).orElse(null);
    }

    public Seller register(Seller seller) {
        return sRepository.save(seller);
    }

    public void delete(int id) {
        sRepository.deleteById(id);
    }

    public Seller update(int id, Seller seller) {
        Seller seller_to_update = sRepository.findById(id).orElse(null);
        
        if (seller_to_update == null) return null;

        seller.setId(id);
        return sRepository.save(seller);
    }
}
