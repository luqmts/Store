package com.luq.storevs.controller.Web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public ModelAndView supplierList(
        @RequestParam(name="sortBy", required=false, defaultValue="id") String sortBy,
        @RequestParam(name="direction", required=false, defaultValue="asc") String direction,
        @RequestParam(name="name", required=false) String name,
        @RequestParam(name="cnpj", required=false) String cnpj, 
        @RequestParam(name="mail", required=false) String mail,
        @RequestParam(name="phone", required=false) String phone
    ){
        name = (name == "") ? null : name;
        cnpj = (cnpj == "") ? null : cnpj;
        mail = (mail == "") ? null : mail;
        phone = (phone == "") ? null : phone;
        List<Supplier> sList = sService.getAllSorted(sortBy, direction, name, cnpj, mail, phone);

        ModelAndView mv = new ModelAndView("supplier-list");
        mv.addObject("suppliers", sList);
        mv.addObject("page", "supplier");
        mv.addObject("direction", direction);
        mv.addObject("sortBy", sortBy);
        mv.addObject("name", name);
        mv.addObject("cnpj", cnpj);
        mv.addObject("mail", mail);
        mv.addObject("phone", phone);

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
        boolean hasError = false;
        if (supplier.getMail() == null){
            model.addAttribute("mailError", "Invalid mail");
            hasError = true;
        }
        
        if (supplier.getCnpj() == null){
            model.addAttribute("cnpjError", "Invalid cnpj");
            hasError = true;
        }
        
        if (supplier.getPhone() == null){
            model.addAttribute("phoneError", "Invalid phone");
            hasError = true;
        }
        
        if (hasError){
            model.addAttribute("supplier", supplier);
            return "supplier-form";   
        }
        
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
