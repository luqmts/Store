package com.luq.store.controllers.API;

import com.luq.store.dto.request.supplier.SupplierRegisterDTO;
import com.luq.store.dto.request.supplier.SupplierUpdateDTO;
import com.luq.store.dto.response.supplier.SupplierResponseDTO;
import com.luq.store.services.SupplierService;

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

@RestController
@RequestMapping("/api/supplier")
public class SupplierController {
    protected final SupplierService sService;
    
    @Autowired
    public SupplierController(SupplierService sService){
        this.sService = sService;
    }

    @PostMapping
    public SupplierResponseDTO registerSupplier(@RequestBody SupplierRegisterDTO data){
        return sService.register(data);
    }

    @GetMapping
    public List<SupplierResponseDTO> getSuppliers(){
        return sService.getAll();
    }

    @GetMapping(path="/{id}")
    public SupplierResponseDTO getSupplierById(@PathVariable("id") int id){
        return sService.getById(id);
    }

    @PutMapping(path="/{id}")
    public SupplierResponseDTO updateSupplier(@PathVariable("id") int id, @RequestBody SupplierUpdateDTO supplier){
        return sService.update(id, supplier);
    }

    @DeleteMapping(path="{id}")
    public void removeSupplier(@PathVariable("id") int id){
        sService.delete(id);
    }
}
