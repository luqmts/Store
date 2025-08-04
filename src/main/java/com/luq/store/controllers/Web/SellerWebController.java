package com.luq.store.controllers.Web;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import com.luq.store.dto.request.customer.CustomerUpdateDTO;
import com.luq.store.dto.request.seller.SellerRegisterDTO;
import com.luq.store.dto.request.seller.SellerUpdateDTO;
import com.luq.store.dto.response.seller.SellerResponseDTO;
import com.luq.store.exceptions.InvalidMailException;
import com.luq.store.exceptions.InvalidPhoneException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public SellerWebController(SellerService sService){
        this.sService = sService;
    }

    @GetMapping(path="/seller/list")
    public ModelAndView sellerList(
        @RequestParam(name="sortBy", required=false, defaultValue="id") String sortBy,
        @RequestParam(name="direction", required=false, defaultValue="asc") String direction,
        @RequestParam(name="department", required=false) String department,
        @RequestParam(name="name", required=false) String name,
        @RequestParam(name="mail", required=false) String mail,
        @RequestParam(name="phone", required=false) String phone
    ){
        name = (Objects.equals(name, "")) ? null : name;
        mail = (Objects.equals(mail, "")) ? null : mail;
        phone = (Objects.equals(phone, "")) ? null : phone;

        List<SellerResponseDTO> sList = sService.getAllSorted(sortBy, direction, department, name, mail, phone);
        
        ModelAndView mv = new ModelAndView("seller-list");
        mv.addObject("page", "seller");
        mv.addObject("sellers", sList);
        mv.addObject("departments", Department.values());
        mv.addObject("direction", direction);
        mv.addObject("sortBy", sortBy);
        mv.addObject("selectedDepartment", department);
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
        } catch (InvalidMailException | InvalidPhoneException e) {
            if (e.getClass().equals(InvalidMailException.class)) model.addAttribute("mailError", e.getMessage());
            if (e.getClass().equals(InvalidPhoneException.class)) model.addAttribute("phoneError", e.getMessage());
            model.addAttribute("seller", data);
            model.addAttribute("departments", Department.values());
            return "seller-form";
        }
    }

    @PutMapping(path="/seller/form/{id}")
    public String postSeller(@PathVariable("id") int id, SellerUpdateDTO data, Model model){
        try {
            sService.update(id, data);
            return "redirect:/seller/list";
        } catch (InvalidMailException | InvalidPhoneException e) {
            if (e.getClass().equals(InvalidMailException.class)) model.addAttribute("mailError", e.getMessage());
            if (e.getClass().equals(InvalidPhoneException.class)) model.addAttribute("phoneError", e.getMessage());
            model.addAttribute("seller", data);
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
