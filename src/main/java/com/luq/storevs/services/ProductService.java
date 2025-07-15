package com.luq.storevs.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.luq.storevs.domain.Product;
import com.luq.storevs.repositories.ProductRepository;

@Service
public class ProductService {
    @Autowired
    private ProductRepository pRepository;
    
    public List<Product> getAll() {
        return pRepository.findAll();
    }
    
    public List<Product> getAllSorted(String sortBy, String direction, Integer supplierId, String name, String sku) {
        Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        
        if (supplierId != null || name != null || sku != null) {
            return pRepository.findBySupplierIdAndNameAndSku(sort, supplierId, name, sku);
        }

        return pRepository.findAll(sort);
    }

    public List<Product> getAllNotRegisteredOnSupply(){
        return pRepository.findAllNotRegisteredOnSupply();
    }

    public List<Product> getAllRegisteredOnSupply(){
        return pRepository.findAllRegisteredOnSupply();
    }

    public List<Product> getAllRegisteredOnSupply(Integer id){
        return pRepository.findAllRegisteredOnSupply(id);
    }
    public Product getById(int id) {
        return pRepository.findById(id).orElse(null);
    }

    public Product register(Product product) {
        return pRepository.save(product);
    }

    public void delete(int id) {
        pRepository.deleteById(id);
    }

    public Product update(int id, Product product) {
        Product product_to_update = pRepository.findById(id).orElse(null);
        
        if (product_to_update == null) return null;
        
        product.setId(id);
        return pRepository.save(product);
    }
}
