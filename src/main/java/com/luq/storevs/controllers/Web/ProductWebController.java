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

import com.luq.storevs.domain.Product;
import com.luq.storevs.services.ProductService;
import com.luq.storevs.services.SupplierService;

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
    public ModelAndView productList(
        @RequestParam(name="sortBy", required=false, defaultValue="id") String sortBy,
        @RequestParam(name="direction", required=false, defaultValue="asc") String direction,
        @RequestParam(name="supplier.id", required=false) Integer supplierId,
        @RequestParam(name="name", required=false) String name,
        @RequestParam(name="sku", required=false) String sku
    ){  
        name = (Objects.equals(name, "")) ? null : name;
        sku = (Objects.equals(sku, "")) ? null : sku;
        
        List<Product> pList = pService.getAllSorted(sortBy, direction, supplierId, name, sku);

        ModelAndView mv = new ModelAndView("product-list");
        mv.addObject("products", pList);
        mv.addObject("suppliers", sService.getAll());
        mv.addObject("page", "product");
        mv.addObject("direction", direction);
        mv.addObject("sortBy", sortBy);
        mv.addObject("supplierId", supplierId);
        mv.addObject("name", name);
        mv.addObject("sku", sku);

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
    public String postProduct(Product product, Model model){
        boolean hasError = false;
        if (product.getPrice() < 1){
            model.addAttribute("priceError", "Product's price must be greater or equal than 1");
            hasError = true;
        }

        if (hasError){
            model.addAttribute("product", product);
            model.addAttribute("suppliers", sService.getAll());
            return "product-form";   
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (product.getId() == null) {
            product.setCreatedBy(authentication.getName());
            product.setCreated(LocalDateTime.now());
            product.setModifiedBy(authentication.getName());
            product.setModified(LocalDateTime.now());
            pService.register(product);
        } else {
            product.setModifiedBy(authentication.getName());
            product.setModified(LocalDateTime.now());
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
