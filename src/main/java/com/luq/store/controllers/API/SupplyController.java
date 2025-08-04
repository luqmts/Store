package com.luq.store.controllers.API;

import com.luq.store.dto.request.supply.SupplyRegisterDTO;
import com.luq.store.dto.request.supply.SupplyUpdateDTO;
import com.luq.store.dto.response.supply.SupplyResponseDTO;
import com.luq.store.services.SupplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/supply")
public class SupplyController {
    protected final SupplyService sService;

    @Autowired
    public SupplyController(SupplyService sService){
        this.sService = sService;
    }

    @PostMapping
    public SupplyResponseDTO registerSupply(@RequestBody SupplyRegisterDTO data){
        return sService.register(data);
    }

    @GetMapping
    public List<SupplyResponseDTO> getSupply(){
        return sService.getAll();
    }

    @GetMapping(path="/{id}")
    public SupplyResponseDTO getSupplyById(@PathVariable("id") int id){
        return sService.getById(id);
    }

    @PutMapping(path="/{id}")
    public SupplyResponseDTO updateSupply(@PathVariable("id") int id, @RequestBody SupplyUpdateDTO data){
        return sService.update(id, data);
    }

    @DeleteMapping(path="{id}")
    public void removeSupply(@PathVariable("id") int id){
        sService.delete(id);
    }
}
