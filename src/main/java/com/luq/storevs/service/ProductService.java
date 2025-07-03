package com.luq.storevs.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.luq.storevs.model.Product;
import com.luq.storevs.repositories.ProductRepository;

@Service
public class ProductService {
    @Autowired
    private ProductRepository pRepository;

    public List<Product> getAll() {
        return pRepository.findAll();
    }

    public Product getById(int id) {
        return pRepository.findById(id).orElse(null);
    }

    public Product register(Product product) {
        return pRepository.save(product);
    }

    public void delete(int id) {
        pRepository.deleteById(id);;
    }

    public Product update(int id, Product product) {
        Product product_to_update = pRepository.findById(id).orElse(null);
        
        if (product_to_update == null) return null;

        product_to_update.setName(product.getName());
        product_to_update.setDescription(product.getDescription());
        product_to_update.setSku(product.getSku());
        product_to_update.setSupplier_id(product.getSupplier_id());

        return pRepository.save(product_to_update);
    }
}
