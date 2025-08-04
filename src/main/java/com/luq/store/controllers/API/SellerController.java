package com.luq.store.controllers.API;

import com.luq.store.dto.request.seller.SellerRegisterDTO;
import com.luq.store.dto.request.seller.SellerUpdateDTO;
import com.luq.store.dto.response.seller.SellerResponseDTO;
import com.luq.store.services.SellerService;

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
@RequestMapping("/api/seller")
public class SellerController {
    protected final SellerService sService;
    
    @Autowired
    public SellerController(SellerService sService){
        this.sService = sService;
    }

    @PostMapping
    public SellerResponseDTO registerSeller(@RequestBody SellerRegisterDTO data){
        return sService.register(data);
    }

    @GetMapping
    public List<SellerResponseDTO> getSellers(){
        return sService.getAll();
    }

    @GetMapping(path="/{id}")
    public SellerResponseDTO getSellerById(@PathVariable("id") int id){
        return sService.getById(id);
    }

    @PutMapping(path="/{id}")
    public SellerResponseDTO updateSeller(@PathVariable("id") int id, @RequestBody SellerUpdateDTO data){
        return sService.update(id, data);
    }

    @DeleteMapping(path="{id}")
    public void removeSeller(@PathVariable("id") int id){
        sService.delete(id);
    }
}
