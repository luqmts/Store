package com.luq.storevs.controllers.Web;

import com.luq.storevs.domain.Supply;
import com.luq.storevs.services.SupplyService;
import com.luq.storevs.services.ProductService;
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

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class SupplyWebController {
    protected final SupplyService sService;
    protected final ProductService pService;

    @Autowired
    public SupplyWebController(SupplyService sService, ProductService pService){
        this.sService = sService;
        this.pService = pService;
    }

    @GetMapping(path="/supply/list")
    public ModelAndView supplyList(
        @RequestParam(name="sortBy", required=false, defaultValue="id") String sortBy,
        @RequestParam(name="direction", required=false, defaultValue="desc") String direction,
        @RequestParam(name="product.id", required=false) Integer productId
    ){
        List<Supply> sList = sService.getAllSorted(sortBy, direction, productId);

        ModelAndView mv = new ModelAndView("supply-list");
        mv.addObject("supply", sList);
        mv.addObject("page", "supply");
        mv.addObject("products", pService.getAll());
        mv.addObject("productId", productId);
        mv.addObject("direction", direction);
        mv.addObject("sortBy", sortBy);

        return mv;
    }

    @GetMapping(path="/supply/form")
    public ModelAndView supplyFormCreate(){
        ModelAndView mv = new ModelAndView("supply-form");
        Supply supply = new Supply();

        mv.addObject("supply", supply);
        mv.addObject("page", "supply");
        mv.addObject("products", pService.getAllNotRegisteredOnSupply());

        return mv;
    }

    @GetMapping(path="/supply/form/{id}")
    public ModelAndView supplyFormEdit(@PathVariable("id") int id){
        Supply supply = sService.getById(id);
        ModelAndView mv = new ModelAndView("supply-form");
        mv.addObject("supply", supply);
        mv.addObject("page", "supply");
        mv.addObject("products", pService.getAll());
        
        return mv;
    }

    @PostMapping(path="/supply/create")
    public String postSupply(Supply supply, Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (supply.getId() == null) {
            if (sService.getByProductId(supply.getProduct()) == null) {
                supply.setCreatedBy(authentication.getName());
                supply.setCreated(LocalDateTime.now());
                supply.setModifiedBy(authentication.getName());
                supply.setModified(LocalDateTime.now());
                sService.register(supply);
            }
            else {
                model.addAttribute("productError", "This product is already registered on supply, please update it instead");
                model.addAttribute("page", "supply");
                model.addAttribute("products", pService.getAll());
                return "supply-form";
            }
        } else {
            supply.setModifiedBy(authentication.getName());
            supply.setModified(LocalDateTime.now());
            sService.update(supply.getId(), supply);
        }
        return "redirect:/supply/list";
    }

    @GetMapping(path="/supply/delete/{id}")
    public String delete(@PathVariable("id") int id){
        sService.delete(id);
        return "redirect:/supply/list";
    }
}
