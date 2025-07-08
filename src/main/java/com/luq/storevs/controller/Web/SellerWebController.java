package com.luq.storevs.controller.Web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
    public ModelAndView productList(){
        List<Seller> sList = sService.getAll();
        
        ModelAndView mv = new ModelAndView("seller-list");
        mv.addObject("page", "seller");
        mv.addObject("sellers", sList);
        
        return mv;
    }

    @GetMapping(path="/seller/form")
    public ModelAndView productFormCreate(){
        ModelAndView mv = new ModelAndView("seller-form");
        mv.addObject("seller", new Seller());
        mv.addObject("page", "seller");
        mv.addObject("departments", Seller.Department.values());
        return mv;
    }

    @GetMapping(path="/seller/form/{id}")
    public ModelAndView productFormEdit(@PathVariable("id") int id){
        Seller seller = sService.getById(id);
        ModelAndView mv = new ModelAndView("seller-form");
        mv.addObject("seller", seller);
        mv.addObject("page", "seller");
        mv.addObject("departments", Seller.Department.values());
        return mv;
    }

    @PostMapping(path="/seller/create")
    public Object postProduct(Seller seller, Model model){
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
            ModelAndView mv = new ModelAndView("seller-form");
            mv.addObject("departments", Seller.Department.values());
            return mv;
        }

        if (seller.getId() == null) {
            sService.register(seller);
        } else {
            sService.update(seller.getId(), seller);
        }

        return "redirect:/seller/list";    }

    @GetMapping(path="/seller/delete/{id}")
    public String delete(@PathVariable("id") int id){
        sService.delete(id);
        return "redirect:/seller/list";
    }
}
