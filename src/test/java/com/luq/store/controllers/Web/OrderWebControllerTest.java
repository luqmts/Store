package com.luq.store.controllers.Web;

import com.luq.store.domain.*;
import com.luq.store.dto.request.order.OrderRegisterDTO;
import com.luq.store.dto.request.order.OrderUpdateDTO;
import com.luq.store.dto.response.customer.CustomerResponseDTO;
import com.luq.store.dto.response.order.OrderResponseDTO;
import com.luq.store.dto.response.product.ProductResponseDTO;
import com.luq.store.dto.response.seller.SellerResponseDTO;
import com.luq.store.dto.response.supply.SupplyResponseDTO;
import com.luq.store.exceptions.InvalidQuantityException;
import com.luq.store.mapper.*;
import com.luq.store.services.CustomerService;
import com.luq.store.services.OrderService;
import com.luq.store.services.ProductService;
import com.luq.store.services.SellerService;
import com.luq.store.services.SupplyService;
import com.luq.store.services.TokenService;
import com.luq.store.infra.security.SecurityConfig;
import com.luq.store.repositories.UserRepository;
import com.luq.store.valueobjects.Cnpj;
import com.luq.store.valueobjects.Mail;
import com.luq.store.valueobjects.Phone;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderWebController.class)
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
public class OrderWebControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService cService;
    @MockBean
    private OrderService oService;
    @MockBean
    private ProductService pService;
    @MockBean
    private SellerService sellerService;
    @MockBean
    private SupplyService supplyService;

    @MockBean
    ProductMapper pMapper;
    @MockBean
    SellerMapper sellerMapper;
    @MockBean
    SupplyMapper supplyMapper;
    @MockBean
    CustomerMapper cMapper;
    @MockBean
    OrderMapper oMapper;
    @MockBean
    private TokenService tService;

    @MockBean
    private UserRepository uRepository;

    private OrderResponseDTO fakeOrder1Response, fakeOrder2Response;
    private OrderRegisterDTO fakeOrderRegister;
    private OrderUpdateDTO fakeOrderUpdate;
    private Order fakeOrder;
    private SupplyResponseDTO fakeSupplyResponse;
    private ProductResponseDTO fakeProduct1Response, fakeProduct2Response;
    private Product fakeProduct1, fakeProduct2;
    private SellerResponseDTO fakeSeller1Response, fakeSeller2Response;
    private Seller fakeSeller1, fakeSeller2;
    private CustomerResponseDTO fakeCustomer1Response, fakeCustomer2Response;
    private Customer fakeCustomer1, fakeCustomer2;

    @BeforeEach
    public void setUp(){
        String user = "Jimmy McGill";
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

        fakeCustomer1Response = new CustomerResponseDTO(1, "Test Customer 01", user, now, user, now);
        fakeCustomer2Response = new CustomerResponseDTO(2, "Test Customer 02", user, now, user, now);
        fakeCustomer1 = new Customer(1, "Test Customer 01", user, now, user, now);
        fakeCustomer2 = new Customer(2, "Test Customer 02", user, now, user, now);

        fakeSeller1Response = new SellerResponseDTO(
                1, "Walter White",
                "WalterWhite@Cooking.com", "11901010101", Department.FOOD,
                user, now, user, now
        );
        fakeSeller2Response = new SellerResponseDTO(
                2, "Jesse Pinkman",
                "Jesse Pinkman@Cooking.com", "11904040404", Department.FOOD,
                user, now, user, now
        );
        fakeSeller1 = new Seller(
            1, "Walter White",
            new Mail("WalterWhite@Cooking.com"), new Phone("11901010101"), Department.FOOD,
            user, now, user, now
        );
        fakeSeller2 = new Seller(
            2, "Jesse Pinkman",
            new Mail("Jesse Pinkman@Cooking.com"), new Phone("11904040404"), Department.FOOD,
            user, now, user, now
        );

        Supplier fakeSupplier1 = new Supplier(
            1, "Microsoft Brasil LTDA.", new Cnpj("43.447.044/0004-10"),
            new Mail("microsoft@mail.com"), new Phone("11000001111"),
            user, now, user, now
        );
        Supplier fakeSupplier2 = new Supplier(
            2, "Sony Brasil LTDA.", new Cnpj("04.542.534/0001-09"),
            new Mail("sony@mail.com"), new Phone("11222225555"),
            user, now, user, now
        );
        fakeProduct1Response = new ProductResponseDTO(
                1, "Xbox One Controller", "XOneCont", "Controller for Xbox One Console",
                BigDecimal.valueOf(200.00), fakeSupplier1, user, now, user, now
        );
        fakeProduct2Response = new ProductResponseDTO(
                2, "Playstation 5 Controller", "PS5Cont", "Controller for Playstation 5 Console",
                BigDecimal.valueOf(250.00), fakeSupplier2, user, now, user, now
        );
        fakeProduct1 = new Product(
            1, "Xbox One Controller", "XOneCont", "Controller for Xbox One Console",
            BigDecimal.valueOf(200.00), fakeSupplier1, user, now, user, now
        );
        fakeProduct2 = new Product(
            2, "Playstation 5 Controller", "PS5Cont", "Controller for Playstation 5 Console",
            BigDecimal.valueOf(250.00), fakeSupplier2, user, now, user, now
        );

        fakeSupplyResponse = new SupplyResponseDTO(1, 50, fakeProduct1, user, now, user, now);
        Supply fakeSupply = new Supply(1, 50, fakeProduct1, user, now, user, now);

        fakeOrder1Response = new OrderResponseDTO(
            1, "", BigDecimal.valueOf(400.00), BigDecimal.valueOf(200.00), OrderStatus.PENDING_PAYMENT, 2, LocalDate.now(),
            fakeProduct1, fakeSeller1, fakeCustomer1, user, now, user, now
        );
        fakeOrder2Response = new OrderResponseDTO(
            1, "", BigDecimal.valueOf(1000.00), BigDecimal.valueOf(250.00), OrderStatus.PENDING_PAYMENT, 4, LocalDate.now(),
            fakeProduct2, fakeSeller2, fakeCustomer2, user, now, user, now
        );
        fakeOrderRegister = new OrderRegisterDTO(
            2, LocalDate.now(),
            fakeProduct1.getId(), fakeSeller1.getId(), fakeCustomer1.getId()
        );
        fakeOrderUpdate = new OrderUpdateDTO(
            OrderStatus.PAID,4, LocalDate.now(),
            fakeProduct2.getId(), fakeSeller2.getId(), fakeCustomer2.getId()
        );
        fakeOrder = new Order(
            1, BigDecimal.valueOf(400.00), BigDecimal.valueOf(200.00), OrderStatus.PENDING_PAYMENT,  2, LocalDate.now(), "",
            fakeProduct1, fakeSeller1, fakeCustomer1, user, now, user, now
        );
    }

    @Test
    @WithMockUser
    @DisplayName("Test if all Orders are being returned on getAllSorted method with no filters applied and default user")
    public void testListAllOrders() throws Exception{
        when(oService.getAllSorted("name", "asc", null,null, null, null))
            .thenReturn(List.of(fakeOrder1Response, fakeOrder2Response));
        when(pService.getAll())
            .thenReturn(List.of(fakeProduct1Response, fakeProduct2Response));
        when(cService.getAll())
            .thenReturn(List.of(fakeCustomer1Response, fakeCustomer2Response));
        when(sellerService.getAll())
            .thenReturn(List.of(fakeSeller1Response, fakeSeller2Response));

        mockMvc.perform(
            get("/order/list")
                .param("sortBy", "name")
                .param("direction", "asc")
        ).andExpect(status().isOk())
        .andExpect(view().name("order-list"))
        .andExpect(model().attributeExists("orders"))
        .andExpect(model().attribute("orders", List.of(fakeOrder1Response, fakeOrder2Response)))
        .andExpect(model().attribute("page", "order"))
        .andExpect(model().attribute("sortBy", "name"))
        .andExpect(model().attribute("direction", "asc"))
        .andExpect(model().attribute("statuses", OrderStatus.values()))
        .andExpect(model().attribute("products", List.of(fakeProduct1Response, fakeProduct2Response)))
        .andExpect(model().attribute("customers", List.of(fakeCustomer1Response, fakeCustomer2Response)))
        .andExpect(model().attribute("sellers", List.of(fakeSeller1Response, fakeSeller2Response)));

        verify(oService, times(1))
            .getAllSorted("name", "asc", null, null, null, null);
        verify(pService, times(1))
            .getAll();
        verify(cService, times(1))
            .getAll();
        verify(sellerService, times(1))
            .getAll();
    }

    @Test
    @WithMockUser
    @DisplayName("Test if Order is being returned on getAllSorted method with one filter applied and default user")
    public void testListOrdersWithOneFilter() throws Exception{
        when(oService.getAllSorted("name", "asc", null,1, null, null))
            .thenReturn(List.of(fakeOrder1Response));
        when(pService.getAll())
            .thenReturn(List.of(fakeProduct1Response, fakeProduct2Response));
        when(cService.getAll())
            .thenReturn(List.of(fakeCustomer1Response, fakeCustomer2Response));
        when(sellerService.getAll())
            .thenReturn(List.of(fakeSeller1Response, fakeSeller2Response));

        mockMvc.perform(
            get("/order/list")
                .param("sortBy", "name")
                .param("direction", "asc")
                .param("productId", "1")
        ).andExpect(status().isOk())
        .andExpect(view().name("order-list"))
        .andExpect(model().attributeExists("orders"))
        .andExpect(model().attribute("orders", List.of(fakeOrder1Response)))
        .andExpect(model().attribute("page", "order"))
        .andExpect(model().attribute("sortBy", "name"))
        .andExpect(model().attribute("direction", "asc"))
        .andExpect(model().attribute("productId", 1))
        .andExpect(model().attribute("statuses", OrderStatus.values()))
        .andExpect(model().attribute("products", List.of(fakeProduct1Response, fakeProduct2Response)))
        .andExpect(model().attribute("customers", List.of(fakeCustomer1Response, fakeCustomer2Response)))
        .andExpect(model().attribute("sellers", List.of(fakeSeller1Response, fakeSeller2Response)));

        verify(oService, times(1))
            .getAllSorted("name", "asc", null, 1, null, null);
        verify(pService, times(1))
            .getAll();
        verify(cService, times(1))
            .getAll();
        verify(sellerService, times(1))
            .getAll();
    }

    @Test
    @WithMockUser
    @DisplayName("Test if Order is being returned on getAllSorted method with all filters applied and default user")
    public void testListOrdersWithAllFilters() throws Exception{
        when(oService.getAllSorted("name", "asc", "PENDING_PAYMENT",1, 1, 1))
            .thenReturn(List.of(fakeOrder1Response));
        when(pService.getAll())
            .thenReturn(List.of(fakeProduct1Response, fakeProduct2Response));
        when(cService.getAll())
            .thenReturn(List.of(fakeCustomer1Response, fakeCustomer2Response));
        when(sellerService.getAll())
            .thenReturn(List.of(fakeSeller1Response, fakeSeller2Response));

        mockMvc.perform(
            get("/order/list")
                .param("sortBy", "name")
                .param("direction", "asc")
                .param("selectedStatus", "PENDING_PAYMENT")
                .param("productId", "1")
                .param("sellerId", "1")
                .param("customerId", "1")
        ).andExpect(status().isOk())
        .andExpect(view().name("order-list"))
        .andExpect(model().attributeExists("orders"))
        .andExpect(model().attribute("orders", List.of(fakeOrder1Response)))
        .andExpect(model().attribute("page", "order"))
        .andExpect(model().attribute("sortBy", "name"))
        .andExpect(model().attribute("direction", "asc"))
        .andExpect(model().attribute("productId", 1))
        .andExpect(model().attribute("sellerId", 1))
        .andExpect(model().attribute("customerId", 1))
        .andExpect(model().attribute("statuses", OrderStatus.values()))
        .andExpect(model().attribute("products", List.of(fakeProduct1Response, fakeProduct2Response)))
        .andExpect(model().attribute("customers", List.of(fakeCustomer1Response, fakeCustomer2Response)))
        .andExpect(model().attribute("sellers", List.of(fakeSeller1Response, fakeSeller2Response)))
        .andExpect(model().attribute("selectedStatus", "PENDING_PAYMENT"));

        verify(oService, times(1))
            .getAllSorted("name", "asc", "PENDING_PAYMENT", 1, 1, 1);
        verify(pService, times(1))
            .getAll();
        verify(cService, times(1))
            .getAll();
        verify(sellerService, times(1))
            .getAll();
    }

    @Test
    @WithMockUser
    @DisplayName("Test if no Order is being returned on getAllSorted method correctly as a default user")
    public void testListWithNoOrders() throws Exception{
        when(oService.getAllSorted("name", "asc", null, 5, null, null))
            .thenReturn(List.of());
        when(pService.getAll())
            .thenReturn(List.of(fakeProduct1Response, fakeProduct2Response));
        when(cService.getAll())
            .thenReturn(List.of(fakeCustomer1Response, fakeCustomer2Response));
        when(sellerService.getAll())
            .thenReturn(List.of(fakeSeller1Response, fakeSeller2Response));

        mockMvc.perform(
            get("/order/list")
                .param("sortBy", "name")
                .param("direction", "asc")
                .param("productId", "5")
        ).andExpect(status().isOk())
        .andExpect(view().name("order-list"))
        .andExpect(model().attributeExists("orders"))
        .andExpect(model().attribute("orders", List.of()))
        .andExpect(model().attribute("page", "order"))
        .andExpect(model().attribute("sortBy", "name"))
        .andExpect(model().attribute("direction", "asc"))
        .andExpect(model().attribute("productId", 5))
        .andExpect(model().attribute("products", List.of(fakeProduct1Response, fakeProduct2Response)))
        .andExpect(model().attribute("customers", List.of(fakeCustomer1Response, fakeCustomer2Response)))
        .andExpect(model().attribute("sellers", List.of(fakeSeller1Response, fakeSeller2Response)))
        .andExpect(model().attribute("statuses", OrderStatus.values()));

        verify(oService, times(1))
            .getAllSorted("name", "asc", null, 5, null, null);
        verify(pService, times(1))
            .getAll();
        verify(cService, times(1))
            .getAll();
        verify(sellerService, times(1))
            .getAll();
    }

    @Test
    @WithMockUser
    @DisplayName("Default user can access Order form page (New Order)")
    public void testOrderFormAsDefaultUser() throws Exception{
        when(pService.getAllRegisteredOnSupply())
            .thenReturn(List.of(fakeProduct1Response, fakeProduct2Response));
        when(sellerService.getAll())
            .thenReturn(List.of(fakeSeller1Response, fakeSeller2Response));
        when(cService.getAll())
            .thenReturn(List.of(fakeCustomer1Response, fakeCustomer2Response));

        mockMvc.perform(get("/order/form"))
        .andExpect(status().isOk())
        .andExpect(view().name("order-form"))
        .andExpect(model().attributeExists("order"))
        .andExpect(model().attribute("page", "order"))
        .andExpect(model().attribute("statuses", OrderStatus.values()))
        .andExpect(model().attribute("products", List.of(fakeProduct1Response, fakeProduct2Response)))
        .andExpect(model().attribute("customers", List.of(fakeCustomer1Response, fakeCustomer2Response)))
        .andExpect(model().attribute("sellers", List.of(fakeSeller1Response, fakeSeller2Response)));

        verify(pService, times(1))
            .getAllRegisteredOnSupply();
        verify(sellerService, times(1))
            .getAll();
        verify(cService, times(1))
            .getAll();
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    @DisplayName("Admin user can access Order form page (New Order)")
    public void testOrderFormAsAdmin() throws Exception{
        when(pService.getAllRegisteredOnSupply())
            .thenReturn(List.of(fakeProduct1Response, fakeProduct2Response));
        when(sellerService.getAll())
            .thenReturn(List.of(fakeSeller1Response, fakeSeller2Response));
        when(cService.getAll())
            .thenReturn(List.of(fakeCustomer1Response, fakeCustomer2Response));

        mockMvc.perform(get("/order/form"))
        .andExpect(status().isOk())
        .andExpect(view().name("order-form"))
        .andExpect(model().attributeExists("order"))
        .andExpect(model().attribute("page", "order"))
        .andExpect(model().attribute("statuses", OrderStatus.values()))
        .andExpect(model().attribute("products", List.of(fakeProduct1Response, fakeProduct2Response)))
        .andExpect(model().attribute("customers", List.of(fakeCustomer1Response, fakeCustomer2Response)))
        .andExpect(model().attribute("sellers", List.of(fakeSeller1Response, fakeSeller2Response)));

        verify(pService, times(1))
            .getAllRegisteredOnSupply();
        verify(sellerService, times(1))
            .getAll();
        verify(cService, times(1))
            .getAll();
    }

    @Test
    @WithMockUser
    @DisplayName("Default user can access Order form page (Edit Order)")
    public void testOrderEditFormAsDefaultUser() throws Exception{
        when(oService.getById(1))
            .thenReturn(fakeOrder1Response);
        when(supplyService.getByProductId(fakeOrder1Response.product().getId()))
            .thenReturn(fakeSupplyResponse);
        when(pService.getAllRegisteredOnSupply(fakeSupplyResponse.product().getId()))
            .thenReturn(List.of(fakeProduct1Response, fakeProduct2Response));
        when(sellerService.getAll())
            .thenReturn(List.of(fakeSeller1Response, fakeSeller2Response));
        when(cService.getAll())
            .thenReturn(List.of(fakeCustomer1Response, fakeCustomer2Response));

        mockMvc.perform(get("/order/form/1"))
        .andExpect(status().isOk())
        .andExpect(view().name("order-form"))
        .andExpect(model().attributeExists("order"))
        .andExpect(model().attribute("order", fakeOrder1Response))
        .andExpect(model().attribute("page", "order"))
        .andExpect(model().attribute("statuses", OrderStatus.values()))
        .andExpect(model().attribute("products", List.of(fakeProduct1Response, fakeProduct2Response)))
        .andExpect(model().attribute("customers", List.of(fakeCustomer1Response, fakeCustomer2Response)))
        .andExpect(model().attribute("sellers", List.of(fakeSeller1Response, fakeSeller2Response)));

        verify(oService, times(1))
            .getById(1);
        verify(supplyService, times(1))
            .getByProductId(fakeOrder1Response.product().getId());
        verify(pService, times(1))
            .getAllRegisteredOnSupply(fakeOrder1Response.product().getId());
        verify(sellerService, times(1))
            .getAll();
        verify(cService, times(1))
            .getAll();
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    @DisplayName("Admin user can access Order form page (Edit Order)")
    public void testOrderEditFormAsAdmin() throws Exception{
        when(oService.getById(1))
            .thenReturn(fakeOrder1Response);
        when(supplyService.getByProductId(fakeOrder1Response.product().getId()))
            .thenReturn(fakeSupplyResponse);
        when(pService.getAllRegisteredOnSupply(fakeSupplyResponse.product().getId()))
            .thenReturn(List.of(fakeProduct1Response, fakeProduct2Response));
        when(sellerService.getAll())
            .thenReturn(List.of(fakeSeller1Response, fakeSeller2Response));
        when(cService.getAll())
            .thenReturn(List.of(fakeCustomer1Response, fakeCustomer2Response));

        mockMvc.perform(get("/order/form/1"))
        .andExpect(status().isOk())
        .andExpect(view().name("order-form"))
        .andExpect(model().attributeExists("order"))
        .andExpect(model().attribute("order", fakeOrder1Response))
        .andExpect(model().attribute("page", "order"))
        .andExpect(model().attribute("statuses", OrderStatus.values()))
        .andExpect(model().attribute("products", List.of(fakeProduct1Response, fakeProduct2Response)))
        .andExpect(model().attribute("customers", List.of(fakeCustomer1Response, fakeCustomer2Response)))
        .andExpect(model().attribute("sellers", List.of(fakeSeller1Response, fakeSeller2Response)));

        verify(oService, times(1))
            .getById(1);
        verify(supplyService, times(1))
            .getByProductId(fakeOrder1Response.product().getId());
        verify(pService, times(1))
            .getAllRegisteredOnSupply(fakeOrder1Response.product().getId());
        verify(sellerService, times(1))
            .getAll();
        verify(cService, times(1))
            .getAll();
    }

    @Test
    @WithMockUser
    @DisplayName("Default user can submit new Orders")
    public void testSubmitNewOrderAsUser() throws Exception{
        mockMvc.perform(
            post("/order/form")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("quantity", fakeOrderRegister.quantity().toString())
                .param("orderDate", fakeOrderRegister.orderDate().toString())
                .param("productId", fakeOrderRegister.productId().toString())
                .param("sellerId", fakeOrderRegister.sellerId().toString())
                .param("customerId", fakeOrderRegister.customerId().toString())
        ).andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/order/list"));

        verify(oService, times(1))
            .register(fakeOrderRegister);
        verify(oMapper, times(0))
            .toEntity(fakeOrderRegister);
        verify(pService, times(0))
            .getAllRegisteredOnSupply();
        verify(sellerService, times(0))
            .getAll();
        verify(cService, times(0))
            .getAll();
    }

    @Test
    @WithMockUser
    @DisplayName("If order's quantity is greater than on supply's quantity, error will be returned on quantityError model registering a new Order")
    public void testSubmitNewOrderAsUserWithInvalidQuantity() throws Exception{
        fakeOrderRegister = new OrderRegisterDTO(
            51, LocalDate.now(), fakeProduct1.getId(),
            fakeSeller1.getId(), fakeCustomer1.getId()
        );

        when(oMapper.toEntity(any(OrderRegisterDTO.class)))
            .thenReturn(fakeOrder);
        when(pService.getAllRegisteredOnSupply())
            .thenReturn(List.of(fakeProduct1Response, fakeProduct2Response));
        when(sellerService.getAll())
            .thenReturn(List.of(fakeSeller1Response, fakeSeller2Response));
        when(cService.getAll())
            .thenReturn(List.of(fakeCustomer1Response, fakeCustomer2Response));
        when(oService.register(fakeOrderRegister))
            .thenThrow(new InvalidQuantityException("You inserted a quantity greater that actually have in supply, please change it"));

        mockMvc.perform(
            post("/order/form")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("quantity", fakeOrderRegister.quantity().toString())
                .param("orderDate", fakeOrderRegister.orderDate().toString())
                .param("productId", fakeOrderRegister.productId().toString())
                .param("sellerId", fakeOrderRegister.sellerId().toString())
                .param("customerId", fakeOrderRegister.customerId().toString())
        ).andExpect(status().isOk())
        .andExpect(model().attribute("quantityError", "You inserted a quantity greater that actually have in supply, please change it"))
        .andExpect(view().name("order-form"))
        .andExpect(model().attribute("page", "order"))
        .andExpect(model().attribute("order", fakeOrder))
        .andExpect(model().attribute("products", List.of(fakeProduct1Response, fakeProduct2Response)))
        .andExpect(model().attribute("sellers", List.of(fakeSeller1Response, fakeSeller2Response)))
        .andExpect(model().attribute("customers", List.of(fakeCustomer1Response, fakeCustomer2Response)));

        verify(oService, times(1))
            .register(fakeOrderRegister);
        verify(oMapper, times(1))
            .toEntity(fakeOrderRegister);
        verify(pService, times(1))
            .getAllRegisteredOnSupply();
        verify(sellerService, times(1))
            .getAll();
        verify(cService, times(1))
            .getAll();
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    @DisplayName("Admin user can submit new Orders")
    public void testSubmitNewOrderAsAdmin() throws Exception{
        mockMvc.perform(
            post("/order/form")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("quantity", fakeOrderRegister.quantity().toString())
                .param("orderDate", fakeOrderRegister.orderDate().toString())
                .param("productId", fakeOrderRegister.productId().toString())
                .param("sellerId", fakeOrderRegister.sellerId().toString())
                .param("customerId", fakeOrderRegister.customerId().toString())
        ).andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/order/list"));

        verify(oService, times(1))
            .register(fakeOrderRegister);
        verify(oMapper, times(0))
            .toEntity(fakeOrderRegister);
        verify(pService, times(0))
            .getAllRegisteredOnSupply();
        verify(sellerService, times(0))
            .getAll();
        verify(cService, times(0))
            .getAll();
    }

    @Test
    @WithMockUser
    @DisplayName("Default user can submit a edit in Order")
    public void testSubmitEditOrderAsUser() throws Exception{
        mockMvc.perform(
            post("/order/form/{id}", 1)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("quantity", fakeOrderUpdate.quantity().toString())
                .param("orderDate", fakeOrderUpdate.orderDate().toString())
                .param("productId", fakeOrderUpdate.productId().toString())
                .param("sellerId", fakeOrderUpdate.sellerId().toString())
                .param("customerId", fakeOrderUpdate.customerId().toString())
                .param("status", fakeOrderUpdate.status().toString())
        ).andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/order/list"));

        verify(oService, times(1))
            .update(1, fakeOrderUpdate);
        verify(oMapper, times(0))
            .toEntity(fakeOrderUpdate);
        verify(pService, times(0))
            .getAllRegisteredOnSupply(fakeOrderUpdate.productId());
        verify(sellerService, times(0))
            .getAll();
        verify(cService, times(0))
            .getAll();
    }

    @Test
    @WithMockUser
    @DisplayName("If order's quantity is greater than on supply's quantity, error will be returned on quantityError model registering a new Order")
    public void testSubmitEditOrderAsUserWithInvalidQuantity() throws Exception{
        fakeOrderUpdate = new OrderUpdateDTO(
            OrderStatus.PAID,51, LocalDate.now(),
            fakeProduct2.getId(), fakeSeller2.getId(), fakeCustomer2.getId()
        );

        when(oMapper.toEntity(any(OrderUpdateDTO.class)))
            .thenReturn(fakeOrder);
        when(pService.getAllRegisteredOnSupply(fakeOrderUpdate.productId()))
            .thenReturn(List.of(fakeProduct1Response, fakeProduct2Response));
        when(sellerService.getAll())
            .thenReturn(List.of(fakeSeller1Response, fakeSeller2Response));
        when(cService.getAll())
            .thenReturn(List.of(fakeCustomer1Response, fakeCustomer2Response));
        when(oService.update(1, fakeOrderUpdate))
            .thenThrow(new InvalidQuantityException("You inserted a quantity greater that actually have in supply, please change it"));

        mockMvc.perform(
            post("/order/form/{id}", 1)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("quantity", fakeOrderUpdate.quantity().toString())
            .param("status", fakeOrderUpdate.status().toString())
            .param("orderDate", fakeOrderUpdate.orderDate().toString())
            .param("productId", fakeOrderUpdate.productId().toString())
            .param("sellerId", fakeOrderUpdate.sellerId().toString())
            .param("customerId", fakeOrderUpdate.customerId().toString())
        ).andExpect(status().isOk())
        .andExpect(view().name("order-form"))
        .andExpect(model().attribute("quantityError", "You inserted a quantity greater that actually have in supply, please change it"))
        .andExpect(model().attribute("page", "order"))
        .andExpect(model().attribute("products", List.of(fakeProduct1Response, fakeProduct2Response)))
        .andExpect(model().attribute("sellers", List.of(fakeSeller1Response, fakeSeller2Response)))
        .andExpect(model().attribute("customers", List.of(fakeCustomer1Response, fakeCustomer2Response)));;

        verify(oService, times(1))
            .update(1, fakeOrderUpdate);
        verify(oMapper, times(1))
            .toEntity(fakeOrderUpdate);
        verify(pService, times(1))
            .getAllRegisteredOnSupply(fakeOrderUpdate.productId());
        verify(sellerService, times(1))
            .getAll();
        verify(cService, times(1))
            .getAll();
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    @DisplayName("Admin user can submit a edit on Order")
    public void testSubmitEditOrderAsAdmin() throws Exception{
        mockMvc.perform(
            post("/order/form/{id}", 1)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("quantity", fakeOrderUpdate.quantity().toString())
                .param("status", fakeOrderUpdate.status().toString())
                .param("orderDate", fakeOrderUpdate.orderDate().toString())
                .param("productId", fakeOrderUpdate.productId().toString())
                .param("sellerId", fakeOrderUpdate.sellerId().toString())
                .param("customerId", fakeOrderUpdate.customerId().toString())
        ).andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/order/list"));

        verify(oService, times(1))
            .update(1, fakeOrderUpdate);
        verify(oMapper, times(0))
            .toEntity(fakeOrderUpdate);
        verify(pService, times(0))
            .getAllRegisteredOnSupply(fakeOrderUpdate.productId());
        verify(sellerService, times(0))
            .getAll();
        verify(cService, times(0))
            .getAll();
    }

    @Test
    @WithMockUser
    @DisplayName("Default user can delete Orders")
    public void testDeleteOrderAsUser() throws Exception{
        mockMvc.perform(get("/order/delete/1"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/order/list"));

        verify(oService, times(1))
            .delete(1);
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    @DisplayName("Admin user can delete Orders")
    public void testDeleteOrderAsAdmin() throws Exception{
        mockMvc.perform(get("/order/delete/1"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/order/list"));

        verify(oService, times(1))
            .delete(1);
    }
}
