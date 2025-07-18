package com.luq.store.controllers.Web;

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

        List<Seller> sList = sService.getAllSorted(sortBy, direction, department, name, mail, phone);
        
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
        Seller seller = sService.getById(id);
        ModelAndView mv = new ModelAndView("seller-form");
        mv.addObject("seller", seller);
        mv.addObject("page", "seller");
        mv.addObject("departments", Department.values());
        return mv;
    }

    @PostMapping(path="/seller/form")
    public String postSeller(Seller seller, Model model){
        boolean hasError = false;
        
        if (seller.getMail() == null){
            model.addAttribute("mailError", "Invalid mail");
            hasError = true;
        }
        
        if (seller.getPhone() == null){
            model.addAttribute("phoneError", "Invalid phone");
            hasError = true;
        }
        
        if (hasError){
            model.addAttribute("seller", seller);
            model.addAttribute("departments", Department.values());
            return "seller-form";
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (seller.getId() == null) {
            seller.setCreatedBy(authentication.getName());
            seller.setCreated(LocalDateTime.now());
            seller.setModifiedBy(authentication.getName());
            seller.setModified(LocalDateTime.now());
            sService.register(seller);
        } else {
            seller.setModifiedBy(authentication.getName());
            seller.setModified(LocalDateTime.now());
            sService.update(seller.getId(), seller);
        }

        return "redirect:/seller/list";    
    }

    @GetMapping(path="/seller/delete/{id}")
    public String delete(@PathVariable("id") int id){
        sService.delete(id);
        return "redirect:/seller/list";
    }
}
