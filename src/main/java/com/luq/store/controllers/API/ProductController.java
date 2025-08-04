package com.luq.store.controllers.API;

import com.luq.store.dto.request.product.ProductRegisterDTO;
import com.luq.store.dto.request.product.ProductUpdateDTO;
import com.luq.store.dto.response.product.ProductResponseDTO;
import com.luq.store.exceptions.InvalidProductPriceException;
import com.luq.store.services.ProductService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/product")
public class ProductController {
    protected final ProductService pService;
    
    @Autowired
    public ProductController(ProductService pService){
        this.pService = pService;
    }

    @PostMapping
    public ProductResponseDTO registerProduct(@RequestBody ProductRegisterDTO data){
        return pService.register(data);
    }

    @GetMapping
    public List<ProductResponseDTO> getProducts(){
        return pService.getAll();
    }

    @GetMapping(path="/{id}")
    public ProductResponseDTO getProductById(@PathVariable("id") int id){
        return pService.getById(id);
    }

    @PutMapping(path="/{id}")
    public ProductResponseDTO updateProduct(@PathVariable("id") int id, @RequestBody ProductUpdateDTO data){
        return pService.update(id, data);
    }

    @DeleteMapping(path="{id}")
    public void removeProduct(@PathVariable("id") int id){
        pService.delete(id);
    }
}
