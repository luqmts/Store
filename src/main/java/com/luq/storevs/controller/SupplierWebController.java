package com.luq.storevs.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.luq.storevs.model.Supplier;
import com.luq.storevs.service.SupplierService;

@Controller
public class SupplierWebController {
    protected final SupplierService sService;
    
    @Autowired
    public SupplierWebController(SupplierService sService){
        this.sService = sService;
    }

    @GetMapping("/supplier-list")
    public ModelAndView supplierList(){
        List<Supplier> sList = sService.getAll();

        ModelAndView mv = new ModelAndView("supplier-list");
        mv.addObject("suppliers", sList);
        return mv;
    }

    @GetMapping("/supplier-form")
    public String supplierForm(){
        return "supplier-form";
    }

    @PostMapping("create-supplier")
    public String create(Supplier supplier){
        sService.register(supplier);
        return "redirect:/supplier-list";
    }
}
