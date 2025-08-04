package com.luq.store.controllers.Web;

import java.util.List;
import java.util.Objects;

import com.luq.store.dto.request.customer.CustomerRegisterDTO;
import com.luq.store.dto.request.customer.CustomerUpdateDTO;
import com.luq.store.dto.response.customer.CustomerResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.luq.store.domain.Customer;
import com.luq.store.services.CustomerService;

@Controller
public class CustomerWebController {
    protected final CustomerService cService;
    
    @Autowired
    public CustomerWebController(CustomerService cService){
        this.cService = cService;
    }

    @GetMapping(path="/customer/list")
    public ModelAndView customerList(
        @RequestParam(name="sortBy", required=false, defaultValue="id") String sortBy,
        @RequestParam(name="direction", required=false, defaultValue="desc") String direction,
        @RequestParam(name="name", required=false) String name
    ){
        name = (Objects.equals(name, "")) ? null : name;
        List<CustomerResponseDTO> cList = cService.getAllSorted(sortBy, direction, name);

        ModelAndView mv = new ModelAndView("customer-list");
        mv.addObject("customers", cList);
        mv.addObject("page", "customer");
        mv.addObject("direction", direction);
        mv.addObject("sortBy", sortBy);
        mv.addObject("name", name);

        return mv;
    }

    @GetMapping(path="/customer/form")
    public ModelAndView customerFormCreate(){
        ModelAndView mv = new ModelAndView("customer-form");

        mv.addObject("customer", new Customer());
        mv.addObject("page", "customer");
        return mv;
    }

    @GetMapping(path="/customer/form/{id}")
    public ModelAndView customerFormEdit(@PathVariable("id") int id){
        CustomerResponseDTO customer = cService.getById(id);
        ModelAndView mv = new ModelAndView("customer-form");
        mv.addObject("customer", customer);
        mv.addObject("page", "customer");
        return mv;
    }

    @PostMapping(path="/customer/form")
    public String postCustomer(CustomerRegisterDTO data){
        cService.register(data);

        return "redirect:/customer/list";
    }

    @PutMapping(path="/customer/form/{id}")
    public String postCustomer(@PathVariable("id") int id, CustomerUpdateDTO data){
        cService.update(id, data);
        return "redirect:/customer/list";
    }

    @GetMapping(path="/customer/delete/{id}")
    public String delete(@PathVariable("id") int id){
        cService.delete(id);
        return "redirect:/customer/list";
    }
}
