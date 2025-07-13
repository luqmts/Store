package com.luq.storevs.controller.Web;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.luq.storevs.model.Customer;
import com.luq.storevs.service.CustomerService;

@Controller
public class CustomerWebController {
    protected final CustomerService cService;
    
    @Autowired
    public CustomerWebController(CustomerService cService){
        this.cService = cService;
    }

    @GetMapping(path="/customer/list")
    public ModelAndView productList(
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
    public ModelAndView productFormCreate(){
        ModelAndView mv = new ModelAndView("customer-form");
        mv.addObject("customer", new Customer());
        mv.addObject("page", "customer");
        return mv;
    }

    @GetMapping(path="/customer/form/{id}")
    public ModelAndView productFormEdit(@PathVariable("id") int id){
        Customer customer = cService.getById(id);
        ModelAndView mv = new ModelAndView("customer-form");
        mv.addObject("customer", customer);
        mv.addObject("page", "customer");
        return mv;
    }

    @PostMapping(path="/customer/create")
    public String postProduct(Customer customer){
        if (customer.getId() == null) {
            cService.register(customer);
        } else {
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
