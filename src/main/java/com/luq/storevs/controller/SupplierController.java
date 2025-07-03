package com.luq.storevs.controller;

import com.luq.storevs.service.SupplierService;
import com.luq.storevs.valueobjects.CNPJ;
import com.luq.storevs.valueobjects.Mail;
import com.luq.storevs.valueobjects.Phone;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.luq.storevs.model.Supplier;

@RestController
@RequestMapping("/suppliers")
public class SupplierController {
    protected final SupplierService sService;
    
    @Autowired
    public SupplierController(SupplierService sService){
        this.sService = sService;
    }

    @PostMapping
    public Supplier registerSupplier(
        @RequestParam("name") String name, 
        @RequestParam("cnpj") String cnpj, 
        @RequestParam("mail") String mail, 
        @RequestParam("phone") String phone
    ){
        return sService.register(new Supplier(name, new CNPJ(cnpj),new Mail(mail), new Phone(phone)));
    }

    @GetMapping
    public List<Supplier> getSuppliers(){
        return sService.getAll();
    }

    /*@GetMapping
    public Supplier getSupplierById(@RequestParam("id") int id){
        return sService.getById(id);
    }*/

    /*@PutMapping
    public Supplier updateSupplier(int sId, String name, String cnpj, String mail, String phone){
        return sService.updateSupplier(sId, name, cnpj, mail, phone);
    }*/

    @DeleteMapping
    public void removeSupplier(int sId){
        sService.delete(sId);
    }
}
