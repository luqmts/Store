package com.luq.store.controllers.Web;

import java.util.List;
import java.util.Objects;

import com.luq.store.dto.request.supplier.SupplierRegisterDTO;
import com.luq.store.dto.request.supplier.SupplierUpdateDTO;
import com.luq.store.dto.response.supplier.SupplierResponseDTO;
import com.luq.store.exceptions.InvalidCnpjException;
import com.luq.store.exceptions.InvalidMailException;
import com.luq.store.exceptions.InvalidPhoneException;
import com.luq.store.exceptions.MultipleValidationException;
import com.luq.store.mapper.SupplierMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.luq.store.domain.Supplier;
import com.luq.store.services.SupplierService;

@Controller
public class SupplierWebController {
    protected final SupplierService sService;

    @Autowired
    private SupplierMapper sMapper;

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
        List<SupplierResponseDTO> sList = sService.getAllSorted(sortBy, direction, name, cnpj, mail, phone);

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
        SupplierResponseDTO supplier = sService.getById(id);
        ModelAndView mv = new ModelAndView("supplier-form");
        mv.addObject("page", "supplier");
        mv.addObject("supplier", supplier);
        return mv;
    }

    @PostMapping("/supplier/form")
    public String postSupplier(SupplierRegisterDTO data, Model model){
        try {
            sService.register(data);
            return "redirect:/supplier/list";
        } catch (MultipleValidationException e) {
            e.getExceptions()
                .stream()
                .filter(ex -> ex instanceof  InvalidCnpjException)
                .findFirst()
                .ifPresent(ex -> model.addAttribute("cnpjError", ex.getMessage()));
            e.getExceptions()
                .stream()
                .filter(ex -> ex instanceof  InvalidMailException)
                .findFirst()
                .ifPresent(ex -> model.addAttribute("mailError", ex.getMessage()));
            e.getExceptions()
                .stream()
                .filter(ex -> ex instanceof  InvalidPhoneException)
                .findFirst()
                .ifPresent(ex -> model.addAttribute("phoneError", ex.getMessage()));
            model.addAttribute("supplier", sMapper.toEntity(data));
            return "supplier-form";
        }
    }

    @PostMapping(path="/supplier/form/{id}")
    public String postSupplier(@PathVariable("id") int id, SupplierUpdateDTO data, Model model){
        try {
            sService.update(id, data);
            return "redirect:/supplier/list";
        } catch (MultipleValidationException e) {
            e.getExceptions()
                .stream()
                .filter(ex -> ex instanceof  InvalidCnpjException)
                .findFirst()
                .ifPresent(ex -> model.addAttribute("cnpjError", ex.getMessage()));
            e.getExceptions()
                .stream()
                .filter(ex -> ex instanceof  InvalidMailException)
                .findFirst()
                .ifPresent(ex -> model.addAttribute("mailError", ex.getMessage()));
            e.getExceptions()
                .stream()
                .filter(ex -> ex instanceof  InvalidPhoneException)
                .findFirst()
                .ifPresent(ex -> model.addAttribute("phoneError", ex.getMessage()));
            model.addAttribute("supplier", sMapper.toEntity(data));
            return "supplier-form";
        }
    }

    @GetMapping("/supplier/delete/{id}")
    public String delete(@PathVariable("id") int id) {
        sService.delete(id);
        return "redirect:/supplier/list";
    }
}
