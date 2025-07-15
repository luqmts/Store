package com.luq.storevs.controllers.Web;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.luq.storevs.domain.Supplier;
import com.luq.storevs.services.SupplierService;

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
        name = (Objects.equals(name, "")) ? null : name;
        cnpj = (Objects.equals(cnpj, "")) ? null : cnpj;
        mail = (Objects.equals(mail, "")) ? null : mail;
        phone = (Objects.equals(phone, "")) ? null : phone;
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
    public String postSupplier(Supplier supplier, Model model){
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

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (supplier.getId() == null) {
            supplier.setCreatedBy(authentication.getName());
            supplier.setCreated(LocalDateTime.now());
            supplier.setModifiedBy(authentication.getName());
            supplier.setModified(LocalDateTime.now());
            sService.register(supplier);
        }
        else{
            supplier.setModifiedBy(authentication.getName());
            supplier.setModified(LocalDateTime.now());
            sService.update(supplier.getId(), supplier);
        }

        return "redirect:/supplier/list";
    }

    @GetMapping("/supplier/delete/{id}")
    public String delete(@PathVariable("id") int id) {
        sService.delete(id);
        return "redirect:/supplier/list";
    }
}
