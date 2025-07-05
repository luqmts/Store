package com.luq.storevs.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.luq.storevs.model.Product;
import com.luq.storevs.service.ProductService;

@Controller
public class ProductWebController {
        protected final ProductService pService;
    
    @Autowired
    public ProductWebController(ProductService pService){
        this.pService = pService;
    }

    @GetMapping("/product-list")
    public ModelAndView productList(){
        List<Product> pList = pService.getAll();

        ModelAndView mv = new ModelAndView("product-list");
        mv.addObject("products", pList);
        return mv;
    }

    @GetMapping("/product-form")
    public String productForm(){
        return "product-form";
    }

    @PostMapping("/create-product")
    public String create(Product product){
        pService.register(product);
        return "redirect:/product-list";
    }
}
