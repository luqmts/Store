package com.luq.storevs.controller.Web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/supplier/list")
    public ModelAndView supplierList(){
        List<Supplier> sList = sService.getAll();

        ModelAndView mv = new ModelAndView("supplier-list");
        mv.addObject("suppliers", sList);
        mv.addObject("page", "supplier");
        return mv;
    }

    @GetMapping("/supplier/form")
    public ModelAndView supplierFormCreate(){
        ModelAndView mv = new ModelAndView("supplier-form");
        mv.addObject("page", "supplier");
        mv.addObject("supplier", new Supplier());
        return mv;
    }

    @GetMapping("/supplier/form/{id}")
    public ModelAndView supplierFormEdit(@PathVariable("id") int id){
        Supplier supplier = sService.getById(id);
        ModelAndView mv = new ModelAndView("supplier-form");
        mv.addObject("page", "supplier");
        mv.addObject("supplier", supplier);
        return mv;
    }

    @PostMapping("/supplier/create")
    public String create(Supplier supplier, Model model){
        if (supplier.getId() == null)
            sService.register(supplier);
        else 
            sService.update(supplier.getId(), supplier);

        return "redirect:/supplier/list";
    }

    @GetMapping("/supplier/delete/{id}")
    public String delete(@PathVariable("id") int id) {
        sService.delete(id);
        return "redirect:/supplier/list";
    }
}
