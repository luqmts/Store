package com.luq.store.controllers.Web;

import com.luq.store.domain.Supply;
import com.luq.store.dto.request.customer.CustomerUpdateDTO;
import com.luq.store.dto.request.supply.SupplyRegisterDTO;
import com.luq.store.dto.request.supply.SupplyUpdateDTO;
import com.luq.store.dto.response.supply.SupplyResponseDTO;
import com.luq.store.exceptions.ProductRegisteredException;
import com.luq.store.services.SupplyService;
import com.luq.store.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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
        List<SupplyResponseDTO> sList = sService.getAllSorted(sortBy, direction, productId);

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
        SupplyResponseDTO supply = sService.getById(id);
        ModelAndView mv = new ModelAndView("supply-form");
        mv.addObject("supply", supply);
        mv.addObject("page", "supply");
        mv.addObject("products", pService.getAllNotRegisteredOnSupply(supply.id()));
        
        return mv;
    }

    @PostMapping(path="/supply/form")
    public String postSupply(SupplyRegisterDTO data, Model model){
        try {
            sService.register(data);
            return "redirect:/supply/list";
        } catch (ProductRegisteredException e) {
            model.addAttribute("productError", e.getMessage());
            model.addAttribute("page", "supply");
            model.addAttribute("supply", data);
            model.addAttribute("products", pService.getAll());
            return "supply-form";
        }
    }

    @PutMapping(path="/supply/form/{id}")
    public String postSupply(@PathVariable("id") int id, SupplyUpdateDTO data){
        sService.update(id, data);
        return "redirect:/supply/list";
    }

    @GetMapping(path="/supply/delete/{id}")
    public String delete(@PathVariable("id") int id){
        sService.delete(id);
        return "redirect:/supply/list";
    }
}
