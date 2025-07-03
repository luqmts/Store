package com.luq.storevs.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.luq.storevs.model.Product;
import com.luq.storevs.service.ProductService;

@RestController
@RequestMapping("/products")
public class ProductController {   
    @Autowired
    private final ProductService pService;
    
    @Autowired
    public ProductController(ProductService pService) {
        this.pService = pService;
    }

    @PostMapping
    public Product registerProduct(
        @RequestParam("sku") String sku, 
        @RequestParam("name") String name, 
        @RequestParam("description") String description, 
        @RequestParam("supplier_id") int supplier_id
    ){
        return pService.register(new Product(sku, name, description, supplier_id));
    }

    @GetMapping
    public List<Product> getProducts(){
       return pService.getAll();
    }

    /*@GetMapping
    public Product getProductById(@RequestParam("id") int id){
        return pService.getById(id);
    }*/

    /*@PutMapping
    public Product updateProduct(int pId, String sku, String name, String description, int id){
        return pService.updateProduct(pId, sku, name, description, id);
    }*/

    @DeleteMapping
    public void removeProduct(@RequestParam("id") int id){
        pService.delete(id);
    }
}