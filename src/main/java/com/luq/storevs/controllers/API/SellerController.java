package com.luq.storevs.controllers.API;

import com.luq.storevs.services.SellerService;

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

import com.luq.storevs.domain.Seller;

@RestController
@RequestMapping("/api/sellers")
public class SellerController {
    protected final SellerService sService;
    
    @Autowired
    public SellerController(SellerService sService){
        this.sService = sService;
    }

    @PostMapping
    public Seller registerSeller(@RequestBody Seller seller){
        return sService.register(seller);
    }

    @GetMapping
    public List<Seller> getSellers(){
        return sService.getAll();
    }

    @GetMapping(path="/{id}")
    public Seller getSellerById(@PathVariable("id") int id){
        return sService.getById(id);
    }

    @PutMapping(path="/{id}")
    public Seller updateSeller(@PathVariable("id") int id, @RequestBody Seller seller){
        return sService.update(id, seller);
    }

    @DeleteMapping(path="{id}")
    public void removeSeller(@PathVariable("id") int id){
        sService.delete(id);
    }
}
