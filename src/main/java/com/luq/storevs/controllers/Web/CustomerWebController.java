package com.luq.storevs.controllers.Web;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.luq.storevs.domain.Customer;
import com.luq.storevs.services.CustomerService;

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
        List<Customer> cList = cService.getAllSorted(sortBy, direction, name);

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
        Customer customer = cService.getById(id);
        ModelAndView mv = new ModelAndView("customer-form");
        mv.addObject("customer", customer);
        mv.addObject("page", "customer");
        return mv;
    }

    @PostMapping(path="/customer/form")
    public String postCustomer(Customer customer){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (customer.getId() == null) {
            customer.setCreatedBy(authentication.getName());
            customer.setCreated(LocalDateTime.now());
            customer.setModifiedBy(authentication.getName());
            customer.setModified(LocalDateTime.now());

            cService.register(customer);
        } else {
            customer.setModifiedBy(authentication.getName());
            customer.setModified(LocalDateTime.now());
            cService.update(customer.getId(), customer);
        }
        return "redirect:/customer/list";
    }

    @GetMapping(path="/customer/delete/{id}")
    public String delete(@PathVariable("id") int id){
        cService.delete(id);
        return "redirect:/customer/list";
    }
}
