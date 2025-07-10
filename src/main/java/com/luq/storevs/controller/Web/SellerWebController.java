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

import com.luq.storevs.model.Seller;
import com.luq.storevs.service.SellerService;

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
        @RequestParam(name="department", required=false) String department
    ){
        List<Seller> sList = sService.getAllSorted(department, sortBy, direction);
        
        ModelAndView mv = new ModelAndView("seller-list");
        mv.addObject("page", "seller");
        mv.addObject("sellers", sList);
        mv.addObject("departments", Seller.Department.values());
        mv.addObject("direction", direction);
        mv.addObject("sortBy", sortBy);
        mv.addObject("selectedDepartment", department);
        
        return mv;
    }

    @GetMapping(path="/seller/form")
    public ModelAndView sellerFormCreate(){
        ModelAndView mv = new ModelAndView("seller-form");
        mv.addObject("seller", new Seller());
        mv.addObject("page", "seller");
        mv.addObject("departments", Seller.Department.values());
        return mv;
    }

    @GetMapping(path="/seller/form/{id}")
    public ModelAndView sellerFormEdit(@PathVariable("id") int id){
        Seller seller = sService.getById(id);
        ModelAndView mv = new ModelAndView("seller-form");
        mv.addObject("seller", seller);
        mv.addObject("page", "seller");
        mv.addObject("departments", Seller.Department.values());
        return mv;
    }

    @PostMapping(path="/seller/create")
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
            model.addAttribute("departments", Seller.Department.values());
            return "seller-form";
        }

        if (seller.getId() == null) {
            sService.register(seller);
        } else {
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
