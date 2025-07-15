package com.luq.storevs.controllers.Web;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.luq.storevs.domain.Supply;
import com.luq.storevs.services.*;
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

import com.luq.storevs.domain.Order;

@Controller
public class OrderWebController {
    protected final OrderService oService;
    protected final ProductService pService;
    protected final SellerService sellerService;
    protected final CustomerService cService;
    protected final SupplyService supplyService;

    @Autowired
    public OrderWebController(
            OrderService oService,
            ProductService pService,
            SellerService sellerService,
            CustomerService cService,
            SupplyService supplyService
    ){
        this.oService = oService;
        this.pService = pService;
        this.sellerService = sellerService;
        this.cService = cService;
        this.supplyService = supplyService;
    }

    @GetMapping(path="/order/list")
    public ModelAndView orderList(
        @RequestParam(name="sortBy", required=false, defaultValue="orderDate") String sortBy,
        @RequestParam(name="direction", required=false, defaultValue="desc") String direction,
        @RequestParam(name="product.id", required=false) Integer productId,
        @RequestParam(name="seller.id", required=false) Integer sellerId,
        @RequestParam(name="customer.id", required=false) Integer customerId
    ){
        List<Order> oList = oService.getAllSorted(sortBy, direction, productId, sellerId, customerId);

        ModelAndView mv = new ModelAndView("order-list");
        mv.addObject("orders", oList);
        mv.addObject("page", "order");
        mv.addObject("products", pService.getAll());
        mv.addObject("sellers", sellerService.getAll());
        mv.addObject("customers", cService.getAll());
        mv.addObject("productId", productId);
        mv.addObject("sellerId", sellerId);
        mv.addObject("customerId", customerId);
        mv.addObject("direction", direction);
        mv.addObject("sortBy", sortBy);

        return mv;
    }

    @GetMapping(path="/order/form")
    public ModelAndView orderFormCreate(){
        ModelAndView mv = new ModelAndView("order-form");
        Order order = new Order();
        order.setOrderDate(LocalDate.now());

        mv.addObject("order", order);
        mv.addObject("page", "order");
        mv.addObject("products", pService.getAllRegisteredOnSupply());
        mv.addObject("sellers", sellerService.getAll());
        mv.addObject("customers", cService.getAll());

        return mv;
    }

    @GetMapping(path="/order/form/{id}")
    public ModelAndView orderFormEdit(@PathVariable("id") int id){
        Order order = oService.getById(id);
        ModelAndView mv = new ModelAndView("order-form");
        Supply supply = supplyService.getByProductId(order.getProduct());
        mv.addObject("order", order);
        mv.addObject("page", "order");
        mv.addObject("products", pService.getAllRegisteredOnSupply(supply.getId()));
        mv.addObject("sellers", sellerService.getAll());
        mv.addObject("customers", cService.getAll());
        
        return mv;
    }

    @PostMapping(path="/order/create")
    public String postOrder(Order order, Model model){
        Supply supply = supplyService.getById(order.getProduct().getId());
        Integer orderQuantity = (order.getId() != null) ? oService.getById(order.getId()).getQuantity() : 0;

        if (
            (order.getId() == null && order.getQuantity() > supply.getQuantity())
            || (order.getId() != null && (order.getQuantity() - orderQuantity) > supply.getQuantity())
        ) {
            model.addAttribute("order", order);
            model.addAttribute("products", (order.getId() == null) ? pService.getAllRegisteredOnSupply() : pService.getAllRegisteredOnSupply(supply.getId()));
            model.addAttribute("page", "order");
            model.addAttribute("sellers", sellerService.getAll());
            model.addAttribute("customers", cService.getAll());
            model.addAttribute("quantityError", "You  inserted a quantity greater that actually have in supply, please change it");
            return "order-form";
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (order.getId() == null) {
            order.setCreatedBy(authentication.getName());
            order.setCreated(LocalDateTime.now());
            order.setModifiedBy(authentication.getName());
            order.setModified(LocalDateTime.now());
            oService.register(order);
            supply.setQuantity(supply.getQuantity() - order.getQuantity());
        }

        if (order.getId() != null) {
            order.setModifiedBy(authentication.getName());
            order.setModified(LocalDateTime.now());
            oService.update(order.getId(), order);
            supply.setQuantity(supply.getQuantity() - (order.getQuantity() - orderQuantity));
        }

        supplyService.update(supply.getId(), supply);

        return "redirect:/order/list";
    }

    @GetMapping(path="/order/delete/{id}")
    public String delete(@PathVariable("id") int id){
        oService.delete(id);
        return "redirect:/order/list";
    }
}
