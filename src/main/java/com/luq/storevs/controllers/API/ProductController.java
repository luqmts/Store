package com.luq.storevs.controllers.API;

import com.luq.storevs.services.ProductService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.luq.storevs.domain.Product;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    protected final ProductService pService;
    
    @Autowired
    public ProductController(ProductService pService){
        this.pService = pService;
    }

    @PostMapping
    public Product registerProduct(@RequestBody Product product){
        return pService.register(product);
    }

    @GetMapping
    public List<Product> getProducts(){
        return pService.getAll();
    }

    @GetMapping(path="/{id}")
    public Product getProductById(@PathVariable("id") int id){
        return pService.getById(id);
    }

    @PutMapping(path="/{id}")
    public Product updateProduct(@PathVariable("id") int id, @RequestBody Product product){
        return pService.update(id, product);
    }

    @DeleteMapping(path="{id}")
    public void removeProduct(@PathVariable("id") int id){
        pService.delete(id);
    }
}
