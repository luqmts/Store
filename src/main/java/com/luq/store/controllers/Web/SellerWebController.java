package com.luq.store.controllers.Web;

import java.util.List;
import java.util.Objects;

import com.luq.store.dto.request.seller.SellerRegisterDTO;
import com.luq.store.dto.request.seller.SellerUpdateDTO;
import com.luq.store.dto.response.seller.SellerResponseDTO;
import com.luq.store.exceptions.InvalidMailException;
import com.luq.store.exceptions.InvalidPhoneException;
import com.luq.store.exceptions.MultipleValidationException;
import com.luq.store.mapper.SellerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.luq.store.domain.Department;
import com.luq.store.domain.Seller;
import com.luq.store.services.SellerService;

@Controller
public class SellerWebController {
    protected final SellerService sService;

    @Autowired
    private SellerMapper sMapper;
    
    @Autowired
    public SellerWebController(SellerService sService){
        this.sService = sService;
    }

    @GetMapping(path="/seller/list")
    public ModelAndView sellerList(
        @RequestParam(name="sortBy", required=false, defaultValue="id") String sortBy,
        @RequestParam(name="direction", required=false, defaultValue="asc") String direction,
        @RequestParam(name="selectedDepartment", required=false) String selectedDepartment,
        @RequestParam(name="name", required=false) String name,
        @RequestParam(name="mail", required=false) String mail,
        @RequestParam(name="phone", required=false) String phone
    ){
        name = (Objects.equals(name, "")) ? null : name;
        mail = (Objects.equals(mail, "")) ? null : mail;
        phone = (Objects.equals(phone, "")) ? null : phone;

        List<SellerResponseDTO> sList = sService.getAllSorted(sortBy, direction, selectedDepartment, name, mail, phone);
        
        ModelAndView mv = new ModelAndView("seller-list");
        mv.addObject("page", "seller");
        mv.addObject("sellers", sList);
        mv.addObject("departments", Department.values());
        mv.addObject("direction", direction);
        mv.addObject("sortBy", sortBy);
        mv.addObject("selectedDepartment", selectedDepartment);
        mv.addObject("name", name);
        mv.addObject("mail", mail);
        mv.addObject("phone", phone);
        
        return mv;
    }

    @GetMapping(path="/seller/form")
    public ModelAndView sellerFormCreate(){
        ModelAndView mv = new ModelAndView("seller-form");
        mv.addObject("seller", new Seller());
        mv.addObject("page", "seller");
        mv.addObject("departments", Department.values());
        return mv;
    }

    @GetMapping(path="/seller/form/{id}")
    public ModelAndView sellerFormEdit(@PathVariable("id") int id){
        SellerResponseDTO seller = sService.getById(id);
        ModelAndView mv = new ModelAndView("seller-form");
        mv.addObject("seller", seller);
        mv.addObject("page", "seller");
        mv.addObject("departments", Department.values());
        return mv;
    }

    @PostMapping(path="/seller/form")
    public String postSeller(SellerRegisterDTO data, Model model){
        try {
            sService.register(data);
            return "redirect:/seller/list";
        } catch (MultipleValidationException e) {
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
            model.addAttribute("seller", sMapper.toEntity(data));
            model.addAttribute("page", "seller");
            model.addAttribute("departments", Department.values());
            return "seller-form";
        }
    }

    @PostMapping(path="/seller/form/{id}")
    public String postSeller(@PathVariable("id") int id, SellerUpdateDTO data, Model model){
        try {
            sService.update(id, data);
            return "redirect:/seller/list";
        } catch (MultipleValidationException e) {
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
            model.addAttribute("seller", sMapper.toEntity(data));
            model.addAttribute("page", "seller");
            model.addAttribute("departments", Department.values());
            return "seller-form";
        }
    }

    @GetMapping(path="/seller/delete/{id}")
    public String delete(@PathVariable("id") int id){
        sService.delete(id);
        return "redirect:/seller/list";
    }
}
