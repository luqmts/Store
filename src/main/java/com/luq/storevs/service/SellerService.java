package com.luq.storevs.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

        order_to_update.setName(order.getName());
        order_to_update.setMail(order.getMail());
        order_to_update.setPhone(order.getPhone());
        order_to_update.setDepartment(order.getDepartment());

        return sRepository.save(order_to_update);
    }
}
