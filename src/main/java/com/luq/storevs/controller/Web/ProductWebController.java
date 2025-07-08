package com.luq.storevs.controller.Web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.luq.storevs.model.Product;
import com.luq.storevs.service.ProductService;
import com.luq.storevs.service.SupplierService;

@Controller
public class ProductWebController {
    protected final ProductService pService;
    protected final SupplierService sService;
    
    @Autowired
    public ProductWebController(ProductService pService, SupplierService sService){
        this.pService = pService;
        this.sService = sService;
    }

    @GetMapping(path="/product/list")
    public ModelAndView productList(){
        List<Product> pList = pService.getAll();

        ModelAndView mv = new ModelAndView("product-list");
        mv.addObject("products", pList);
        mv.addObject("page", "product");

        return mv;
    }

    @GetMapping(path="/product/form")
    public ModelAndView productFormCreate(){
        ModelAndView mv = new ModelAndView("product-form");
        mv.addObject("product", new Product());
        mv.addObject("page", "product");
        mv.addObject("suppliers", sService.getAll());
        return mv;
    }

    @GetMapping(path="/product/form/{id}")
    public ModelAndView productFormEdit(@PathVariable("id") int id){
        Product product = pService.getById(id);
        ModelAndView mv = new ModelAndView("product-form");
        mv.addObject("product", product);
        mv.addObject("page", "product");
        mv.addObject("suppliers", sService.getAll());
        return mv;
    }

    @PostMapping(path="/product/create")
    public String postProduct(Product product){
        if (product.getId() == null) {
            pService.register(product);
        } else {
            pService.update(product.getId(), product);
        }
        return "redirect:/product/list";
    }

    @GetMapping(path="/product/delete/{id}")
    public String delete(@PathVariable("id") int id){
        pService.delete(id);
        return "redirect:/product/list";
    }
}
