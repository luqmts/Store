package controller;

import service.SupplierService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import model.Supplier;

@RestController
@RequestMapping("/supplier")
public class SupplierController {
    protected final SupplierService sService;
    
    @Autowired
    public SupplierController(SupplierService sService){
        this.sService = sService;
    }

    @PostMapping
    public Supplier registerSupplier(String sName, String sCNPJ, String sMail, String sPhone){
        return sService.registerSupplier(sName, sCNPJ, sMail, sPhone);
    }

    @GetMapping
    public String showAllSuppliers(){
        return sService.showAllSuppliers();
    }

    @PutMapping
    public Supplier updateSupplier(int sId, String sName, String sCNPJ, String sMail, String sPhone){
        return sService.updateSupplier(sId, sName, sCNPJ, sMail, sPhone);
    }

    @DeleteMapping
    public int removeSupplier(int sId){
        return sService.deleteSupplier(sId);
    }
}
