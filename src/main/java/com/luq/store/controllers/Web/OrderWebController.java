package com.luq.store.controllers.Web;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.luq.store.domain.Supply;
import com.luq.store.dto.request.customer.CustomerUpdateDTO;
import com.luq.store.dto.request.order.OrderRegisterDTO;
import com.luq.store.dto.request.order.OrderUpdateDTO;
import com.luq.store.dto.response.order.OrderResponseDTO;
import com.luq.store.dto.response.supply.SupplyResponseDTO;
import com.luq.store.exceptions.InvalidQuantityException;
import com.luq.store.exceptions.NotFoundException;
import com.luq.store.mapper.OrderMapper;
import com.luq.store.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.luq.store.domain.Order;

@Controller
public class OrderWebController {
    protected final OrderService oService;
    protected final ProductService pService;
    protected final SellerService sellerService;
    protected final CustomerService cService;
    protected final SupplyService supplyService;

    @Autowired
    private OrderMapper oMapper;

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
        List<OrderResponseDTO> oList = oService.getAllSorted(sortBy, direction, productId, sellerId, customerId);

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
        OrderResponseDTO order = oService.getById(id);
        ModelAndView mv = new ModelAndView("order-form");
        SupplyResponseDTO supply = supplyService.getByProductId(order.product().getId());

        mv.addObject("order", order);
        mv.addObject("page", "order");
        mv.addObject("id", order.id());
        mv.addObject("products", pService.getAllRegisteredOnSupply(supply.id()));
        mv.addObject("sellers", sellerService.getAll());
        mv.addObject("customers", cService.getAll());
        
        return mv;
    }

    @PostMapping(path="/order/form")
    public String postOrder(OrderRegisterDTO data, Model model){
        try {
            oService.register(data);
            return "redirect:/order/list";
        } catch (InvalidQuantityException | NotFoundException e) {
            model.addAttribute("order", oMapper.toEntity(data));
            model.addAttribute("products", pService.getAllRegisteredOnSupply());
            model.addAttribute("page", "order");
            model.addAttribute("sellers", sellerService.getAll());
            model.addAttribute("customers", cService.getAll());
            model.addAttribute("quantityError", e.getMessage());
            model.addAttribute("supplyError", e.getMessage());

            return "order-form";
        }
    }

    @PutMapping(path="/order/form/{id}")
    public String postOrder(@PathVariable("id") int id, OrderUpdateDTO data, Model model){
        try {
            oService.update(id, data);
            return "redirect:/order/list";
        } catch (InvalidQuantityException e) {
            model.addAttribute("order", oMapper.toEntity(data));
            model.addAttribute("products", pService.getAllRegisteredOnSupply(data.product_id()));
            model.addAttribute("page", "order");
            model.addAttribute("page", data.id());
            model.addAttribute("sellers", sellerService.getAll());
            model.addAttribute("customers", cService.getAll());
            model.addAttribute("quantityError", e.getMessage());

            return "order-form";
        }
    }

    @GetMapping(path="/order/delete/{id}")
    public String delete(@PathVariable("id") int id){
        oService.delete(id);
        return "redirect:/order/list";
    }
}
