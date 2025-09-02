package com.luq.store.controllers.Web;

import java.util.List;
import java.util.Objects;

import com.luq.store.dto.request.product.ProductRegisterDTO;
import com.luq.store.dto.request.product.ProductUpdateDTO;
import com.luq.store.dto.response.product.ProductResponseDTO;
import com.luq.store.exceptions.InvalidProductPriceException;
import com.luq.store.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.luq.store.domain.Product;
import com.luq.store.services.ProductService;
import com.luq.store.services.SupplierService;

@Controller
public class ProductWebController {
    protected final ProductService pService;
    protected final SupplierService sService;

    @Autowired
    private ProductMapper pMapper;
    
    @Autowired
    public ProductWebController(ProductService pService, SupplierService sService){
        this.pService = pService;
        this.sService = sService;
    }

    @GetMapping(path="/product/list")
    public ModelAndView productList(
        @RequestParam(name="sortBy", required=false, defaultValue="id") String sortBy,
        @RequestParam(name="direction", required=false, defaultValue="asc") String direction,
        @RequestParam(name="supplierId", required=false) Integer supplierId,
        @RequestParam(name="name", required=false) String name,
        @RequestParam(name="sku", required=false) String sku
    ){  
        name = (Objects.equals(name, "")) ? null : name;
        sku = (Objects.equals(sku, "")) ? null : sku;
        
        List<ProductResponseDTO> pList = pService.getAllSorted(sortBy, direction, supplierId, name, sku);

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
        ProductResponseDTO product = pService.getById(id);
        ModelAndView mv = new ModelAndView("product-form");
        mv.addObject("product", product);
        mv.addObject("page", "product");
        mv.addObject("suppliers", sService.getAll());
        return mv;
    }

    @PostMapping(path="/product/form")
    public String postProduct(ProductRegisterDTO data, Model model){
        try {
            pService.register(data);
            return "redirect:/product/list";
        } catch (InvalidProductPriceException e) {
            model.addAttribute("priceError", e.getMessage());
            model.addAttribute("product", pMapper.toEntity(data));
            model.addAttribute("suppliers", sService.getAll());

            return "product-form";
        }
    }

    @PutMapping(path="/product/form/{id}")
    public String postProduct(@PathVariable("id") int id, ProductUpdateDTO data, Model model){
        try {
            pService.update(id, data);
            return "redirect:/product/list";
        } catch (InvalidProductPriceException e) {
            model.addAttribute("priceError", e.getMessage());
            model.addAttribute("product", pMapper.toEntity(data));
            model.addAttribute("suppliers", sService.getAll());

            return "product-form";
        }
    }

    @GetMapping(path="/product/delete/{id}")
    public String delete(@PathVariable("id") int id){
        pService.delete(id);
        return "redirect:/product/list";
    }
}
