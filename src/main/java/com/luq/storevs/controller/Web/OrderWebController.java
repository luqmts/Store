package com.luq.storevs.controller.Web;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.luq.storevs.model.Order;
import com.luq.storevs.service.CustomerService;
import com.luq.storevs.service.OrderService;
import com.luq.storevs.service.ProductService;
import com.luq.storevs.service.SellerService;

@Controller
public class OrderWebController {
    protected final OrderService oService;
    protected final ProductService pService;
    protected final SellerService sService;
    protected final CustomerService cService;
    
    @Autowired
    public OrderWebController(OrderService oService, ProductService pService, SellerService sServcice, CustomerService cService){
        this.oService = oService;
        this.pService = pService;
        this.sService = sServcice;
        this.cService = cService;
    }

    @GetMapping(path="/order/list")
    public ModelAndView productList(){
        List<Order> oList = oService.getAll();

        ModelAndView mv = new ModelAndView("order-list");
        mv.addObject("orders", oList);
        mv.addObject("page", "order");

        return mv;
    }

    @GetMapping(path="/order/form")
    public ModelAndView productFormCreate(){
        ModelAndView mv = new ModelAndView("order-form");
        Order order = new Order();
        order.setOrderDate(LocalDate.now());

        mv.addObject("order", order);
        mv.addObject("page", "order");
        mv.addObject("products", pService.getAll());
        mv.addObject("sellers", sService.getAll());
        mv.addObject("customers", cService.getAll());

        return mv;
    }

    @GetMapping(path="/order/form/{id}")
    public ModelAndView productFormEdit(@PathVariable("id") int id){
        Order order = oService.getById(id);
        ModelAndView mv = new ModelAndView("order-form");
        mv.addObject("order", order);
        mv.addObject("page", "order");
        mv.addObject("products", pService.getAll());
        mv.addObject("sellers", sService.getAll());
        mv.addObject("customers", cService.getAll());
        
        return mv;
    }

    @PostMapping(path="/order/create")
    public String postProduct(Order order){
        if (order.getId() == null) {
            oService.register(order);
        } else {
            oService.update(order.getId(), order);
        }
        return "redirect:/order/list";
    }

    @GetMapping(path="/order/delete/{id}")
    public String delete(@PathVariable("id") int id){
        oService.delete(id);
        return "redirect:/order/list";
    }
}
