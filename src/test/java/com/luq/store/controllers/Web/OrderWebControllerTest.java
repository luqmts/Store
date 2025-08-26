package com.luq.store.controllers.Web;

import com.luq.store.domain.*;
import com.luq.store.dto.request.order.OrderRegisterDTO;
import com.luq.store.dto.request.order.OrderUpdateDTO;
import com.luq.store.dto.response.order.OrderResponseDTO;
import com.luq.store.mapper.CustomerMapper;
import com.luq.store.mapper.ProductMapper;
import com.luq.store.mapper.SellerMapper;
import com.luq.store.mapper.SupplyMapper;
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

    @Autowired
    ProductMapper pMapper;
    @Autowired
    SellerMapper sellerMapper;
    @Autowired
    SupplyMapper supplyMapper;
    @Autowired
    CustomerMapper cMapper;

    @MockBean
    private TokenService tService;

    @MockBean
    private UserRepository uRepository;

    private OrderResponseDTO fakeOrder1Response, fakeOrder2Response;
    private OrderRegisterDTO fakeOrderRegister;
    private OrderUpdateDTO fakeOrderUpdate;
    private Supply fakeSupply;
    private Product fakeProduct1, fakeProduct2;
    private Seller fakeSeller1, fakeSeller2;
    private Customer fakeCustomer1, fakeCustomer2;

    @BeforeEach
    public void setUp(){
        String user = "Jimmy McGill";
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

        fakeCustomer1 = new Customer(1, "Test Customer 01", user, now, user, now);
        fakeCustomer2 = new Customer(2, "Test Customer 02", user, now, user, now);

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

        fakeProduct1 = new Product(
            1, "Xbox One Controller", "XOneCont", "Controller for Xbox One Console",
            BigDecimal.valueOf(200.00), fakeSupplier1, user, now, user, now
        );
        fakeProduct2 = new Product(
            2, "Playstation 5 Controller", "PS5Cont", "Controller for Playstation 5 Console",
            BigDecimal.valueOf(250.00), fakeSupplier2, user, now, user, now
        );

        fakeSupply = new Supply(1, 50, fakeProduct1, user, now, user, now);

        fakeOrder1Response = new OrderResponseDTO(
            1, BigDecimal.valueOf(400.00), BigDecimal.valueOf(200.00), 2, LocalDate.now(),
            fakeProduct1, fakeSeller1, fakeCustomer1, user, now, user, now
        );
        fakeOrder2Response = new OrderResponseDTO(
            1, BigDecimal.valueOf(1000.00), BigDecimal.valueOf(250.00), 4, LocalDate.now(),
            fakeProduct2, fakeSeller2, fakeCustomer2, user, now, user, now
        );

        fakeOrderRegister = new OrderRegisterDTO(
            BigDecimal.valueOf(400.00), BigDecimal.valueOf(200.00), 2, LocalDate.now(),
            fakeProduct1.getId(), fakeSeller1.getId(), fakeCustomer1.getId()
        );
        fakeOrderUpdate = new OrderUpdateDTO(
            1, BigDecimal.valueOf(1000.00), BigDecimal.valueOf(250.00), 4, LocalDate.now(),
            fakeProduct2.getId(), fakeSeller2.getId(), fakeCustomer2.getId()
        );
    }

    @Test
    @WithMockUser
    @DisplayName("Test if all Orders are being returned on getAllSorted method with no filters applied and default user")
    public void testListAllOrders() throws Exception{
        when(oService.getAllSorted("name", "asc", null, null, null))
            .thenReturn(List.of(fakeOrder1Response, fakeOrder2Response));
        when(pService.getAll()).thenReturn(List.of(pMapper.toDTO(fakeProduct1), pMapper.toDTO(fakeProduct2)));
        when(sellerService.getAll()).thenReturn(List.of(sellerMapper.toDTO(fakeSeller1), sellerMapper.toDTO(fakeSeller2)));
        when(cService.getAll()).thenReturn(List.of(cMapper.toDTO(fakeCustomer1), cMapper.toDTO(fakeCustomer2)));

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
        .andExpect(model().attribute("products", List.of(pMapper.toDTO(fakeProduct1), pMapper.toDTO(fakeProduct2))))
        .andExpect(model().attribute("customers", List.of(sellerMapper.toDTO(fakeSeller1), sellerMapper.toDTO(fakeSeller2))))
        .andExpect(model().attribute("sellers", List.of(cMapper.toDTO(fakeCustomer1), cMapper.toDTO(fakeCustomer2))));
    }

    @Test
    @WithMockUser
    @DisplayName("Test if Order is being returned on getAllSorted method with one filter applied and default user")
    public void testListOrdersWithOneFilter() throws Exception{
        when(oService.getAllSorted("name", "asc", 1, null, null))
            .thenReturn(List.of(fakeOrder1Response));
        when(pService.getAll()).thenReturn(List.of(pMapper.toDTO(fakeProduct1), pMapper.toDTO(fakeProduct2)));
        when(sellerService.getAll()).thenReturn(List.of(sellerMapper.toDTO(fakeSeller1), sellerMapper.toDTO(fakeSeller2)));
        when(cService.getAll()).thenReturn(List.of(cMapper.toDTO(fakeCustomer1), cMapper.toDTO(fakeCustomer2)));

        mockMvc.perform(
            get("/order/list")
                .param("sortBy", "name")
                .param("direction", "asc")
                .param("product.id", "1")
        ).andExpect(status().isOk())
        .andExpect(view().name("order-list"))
        .andExpect(model().attributeExists("orders"))
        .andExpect(model().attribute("orders", List.of(fakeOrder1Response)))
        .andExpect(model().attribute("page", "order"))
        .andExpect(model().attribute("sortBy", "name"))
        .andExpect(model().attribute("direction", "asc"))
        .andExpect(model().attribute("productId", 1))
        .andExpect(model().attribute("products", List.of(pMapper.toDTO(fakeProduct1), pMapper.toDTO(fakeProduct2))))
        .andExpect(model().attribute("customers", List.of(sellerMapper.toDTO(fakeSeller1), sellerMapper.toDTO(fakeSeller2))))
        .andExpect(model().attribute("sellers", List.of(cMapper.toDTO(fakeCustomer1), cMapper.toDTO(fakeCustomer2))));
    }

    @Test
    @WithMockUser
    @DisplayName("Test if Order is being returned on getAllSorted method with all filters applied and default user")
    public void testListOrdersWithAllFilters() throws Exception{
        when(oService.getAllSorted("name", "asc", 1, 1, 1))
            .thenReturn(List.of(fakeOrder1Response));
        when(pService.getAll()).thenReturn(List.of(pMapper.toDTO(fakeProduct1), pMapper.toDTO(fakeProduct2)));
        when(sellerService.getAll()).thenReturn(List.of(sellerMapper.toDTO(fakeSeller1), sellerMapper.toDTO(fakeSeller2)));
        when(cService.getAll()).thenReturn(List.of(cMapper.toDTO(fakeCustomer1), cMapper.toDTO(fakeCustomer2)));

        mockMvc.perform(
            get("/order/list")
                .param("sortBy", "name")
                .param("direction", "asc")
                .param("product.id", "1")
                .param("seller.id", "1")
                .param("customer.id", "1")
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
        .andExpect(model().attribute("products", List.of(pMapper.toDTO(fakeProduct1), pMapper.toDTO(fakeProduct2))))
        .andExpect(model().attribute("customers", List.of(sellerMapper.toDTO(fakeSeller1), sellerMapper.toDTO(fakeSeller2))))
        .andExpect(model().attribute("sellers", List.of(cMapper.toDTO(fakeCustomer1), cMapper.toDTO(fakeCustomer2))));
    }

    @Test
    @WithMockUser
    @DisplayName("Test if no Order is being returned on getAllSorted method correctly as a default user")
    public void testListWithNoOrders() throws Exception{
        when(oService.getAllSorted("name", "asc", 5, null, null)).thenReturn(List.of());
        when(pService.getAll()).thenReturn(List.of(pMapper.toDTO(fakeProduct1), pMapper.toDTO(fakeProduct2)));
        when(sellerService.getAll()).thenReturn(List.of(sellerMapper.toDTO(fakeSeller1), sellerMapper.toDTO(fakeSeller2)));
        when(cService.getAll()).thenReturn(List.of(cMapper.toDTO(fakeCustomer1), cMapper.toDTO(fakeCustomer2)));

        mockMvc.perform(
            get("/order/list")
                .param("sortBy", "name")
                .param("direction", "asc")
                .param("product.id", "5")
        ).andExpect(status().isOk())
        .andExpect(view().name("order-list"))
        .andExpect(model().attributeExists("orders"))
        .andExpect(model().attribute("orders", List.of()))
        .andExpect(model().attribute("page", "order"))
        .andExpect(model().attribute("sortBy", "name"))
        .andExpect(model().attribute("direction", "asc"))
        .andExpect(model().attribute("productId", 5))
        .andExpect(model().attribute("products", List.of(pMapper.toDTO(fakeProduct1), pMapper.toDTO(fakeProduct2))))
        .andExpect(model().attribute("customers", List.of(sellerMapper.toDTO(fakeSeller1), sellerMapper.toDTO(fakeSeller2))))
        .andExpect(model().attribute("sellers", List.of(cMapper.toDTO(fakeCustomer1), cMapper.toDTO(fakeCustomer2))));
    }

    @Test
    @WithMockUser
    @DisplayName("Default user can access Order form page (New Order)")
    public void testOrderFormAsDefaultUser() throws Exception{
        when(pService.getAll()).thenReturn(List.of(pMapper.toDTO(fakeProduct1), pMapper.toDTO(fakeProduct2)));
        when(sellerService.getAll()).thenReturn(List.of(sellerMapper.toDTO(fakeSeller1), sellerMapper.toDTO(fakeSeller2)));
        when(cService.getAll()).thenReturn(List.of(cMapper.toDTO(fakeCustomer1), cMapper.toDTO(fakeCustomer2)));

        mockMvc.perform(get("/order/form"))
            .andExpect(status().isOk())
            .andExpect(view().name("order-form"))
            .andExpect(model().attributeExists("order"))
            .andExpect(model().attribute("page", "order"))
            .andExpect(model().attribute("products", List.of(pMapper.toDTO(fakeProduct1), pMapper.toDTO(fakeProduct2))))
            .andExpect(model().attribute("customers", List.of(sellerMapper.toDTO(fakeSeller1), sellerMapper.toDTO(fakeSeller2))))
            .andExpect(model().attribute("sellers", List.of(cMapper.toDTO(fakeCustomer1), cMapper.toDTO(fakeCustomer2))));
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    @DisplayName("Admin user can access Order form page (New Order)")
    public void testOrderFormAsAdmin() throws Exception{
        when(pService.getAll()).thenReturn(List.of(pMapper.toDTO(fakeProduct1), pMapper.toDTO(fakeProduct2)));
        when(sellerService.getAll()).thenReturn(List.of(sellerMapper.toDTO(fakeSeller1), sellerMapper.toDTO(fakeSeller2)));
        when(cService.getAll()).thenReturn(List.of(cMapper.toDTO(fakeCustomer1), cMapper.toDTO(fakeCustomer2)));

        mockMvc.perform(get("/order/form"))
        .andExpect(status().isOk())
        .andExpect(view().name("order-form"))
        .andExpect(model().attributeExists("order"))
        .andExpect(model().attribute("page", "order"))
        .andExpect(model().attribute("products", List.of(pMapper.toDTO(fakeProduct1), pMapper.toDTO(fakeProduct2))))
        .andExpect(model().attribute("customers", List.of(sellerMapper.toDTO(fakeSeller1), sellerMapper.toDTO(fakeSeller2))))
        .andExpect(model().attribute("sellers", List.of(cMapper.toDTO(fakeCustomer1), cMapper.toDTO(fakeCustomer2))));
    }

    @Test
    @WithMockUser
    @DisplayName("Default user can access Order form page (Edit Order)")
    public void testOrderEditFormAsDefaultUser() throws Exception{
        when(oService.getById(1)).thenReturn(fakeOrder1Response);
        when(supplyService.getByProductId(fakeOrder1Response.product().getId())).thenReturn(supplyMapper.toDTO(fakeSupply));
        when(pService.getAll()).thenReturn(List.of(pMapper.toDTO(fakeProduct1), pMapper.toDTO(fakeProduct2)));
        when(sellerService.getAll()).thenReturn(List.of(sellerMapper.toDTO(fakeSeller1), sellerMapper.toDTO(fakeSeller2)));
        when(cService.getAll()).thenReturn(List.of(cMapper.toDTO(fakeCustomer1), cMapper.toDTO(fakeCustomer2)));

        mockMvc.perform(get("/order/form/1"))
            .andExpect(status().isOk())
            .andExpect(view().name("order-form"))
            .andExpect(model().attributeExists("order"))
            .andExpect(model().attribute("order", fakeOrder1Response))
            .andExpect(model().attribute("page", "order"))
            .andExpect(model().attribute("products", List.of(pMapper.toDTO(fakeProduct1), pMapper.toDTO(fakeProduct2))))
            .andExpect(model().attribute("customers", List.of(sellerMapper.toDTO(fakeSeller1), sellerMapper.toDTO(fakeSeller2))))
            .andExpect(model().attribute("sellers", List.of(cMapper.toDTO(fakeCustomer1), cMapper.toDTO(fakeCustomer2))));
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    @DisplayName("Admin user can access Order form page (Edit Order)")
    public void testOrderEditFormAsAdmin() throws Exception{
        when(oService.getById(1)).thenReturn(fakeOrder1Response);
        when(supplyService.getByProductId(fakeOrder1Response.product().getId())).thenReturn(supplyMapper.toDTO(fakeSupply));
        when(pService.getAll()).thenReturn(List.of(pMapper.toDTO(fakeProduct1), pMapper.toDTO(fakeProduct2)));
        when(sellerService.getAll()).thenReturn(List.of(sellerMapper.toDTO(fakeSeller1), sellerMapper.toDTO(fakeSeller2)));
        when(cService.getAll()).thenReturn(List.of(cMapper.toDTO(fakeCustomer1), cMapper.toDTO(fakeCustomer2)));

        mockMvc.perform(get("/order/form/1"))
        .andExpect(status().isOk())
        .andExpect(view().name("order-form"))
        .andExpect(model().attributeExists("order"))
        .andExpect(model().attribute("order", fakeOrder1Response))
        .andExpect(model().attribute("page", "order"))
        .andExpect(model().attribute("products", List.of(pMapper.toDTO(fakeProduct1), pMapper.toDTO(fakeProduct2))))
        .andExpect(model().attribute("customers", List.of(sellerMapper.toDTO(fakeSeller1), sellerMapper.toDTO(fakeSeller2))))
        .andExpect(model().attribute("sellers", List.of(cMapper.toDTO(fakeCustomer1), cMapper.toDTO(fakeCustomer2))));
    }

    @Test
    @WithMockUser
    @DisplayName("Default user can submit new Orders")
    public void testSubmitNewOrderAsUser() throws Exception{
        when(supplyService.getById(1)).thenReturn(supplyMapper.toDTO(fakeSupply));
        when(supplyService.getByProductId(fakeProduct1.getId())).thenReturn(supplyMapper.toDTO(fakeSupply));

        mockMvc.perform(
            post("/order/form")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("totalPrice", fakeOrderRegister.totalPrice().toString())
                .param("quantity", fakeOrderRegister.quantity().toString())
                .param("orderDate", fakeOrderRegister.orderDate().toString())
                .param("product.id", fakeOrderRegister.product_id().toString())
                .param("seller.id", fakeOrderRegister.seller_id().toString())
                .param("customer.id", fakeOrderRegister.customer_id().toString())
        ).andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/order/list"));

        verify(oService, times(1)).register(fakeOrderRegister);
    }

    @Test
    @WithMockUser
    @DisplayName("If order's quantity is greater than on supply's quantity, error will be returned on quantityError model registering a new Order")
    public void testSubmitNewOrderAsUserWithInvalidQuantity() throws Exception{
        when(supplyService.getById(1)).thenReturn(supplyMapper.toDTO(fakeSupply));
        when(supplyService.getByProductId(fakeProduct1.getId())).thenReturn(supplyMapper.toDTO(fakeSupply));

        mockMvc.perform(
            post("/order/form")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("totalPrice", fakeOrderRegister.totalPrice().toString())
                .param("quantity", "51")
                .param("orderDate", fakeOrderRegister.orderDate().toString())
                .param("product.id", fakeOrderRegister.product_id().toString())
                .param("seller.id", fakeOrderRegister.seller_id().toString())
                .param("customer.id", fakeOrderRegister.customer_id().toString())
        ).andExpect(status().isOk())
        .andExpect(model().attribute("quantityError", "You  inserted a quantity greater that actually have in supply, please change it"))
        .andExpect(view().name("order-form"));

        verify(oService, times(0)).register(fakeOrderRegister);
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    @DisplayName("Admin user can submit new Orders")
    public void testSubmitNewOrderAsAdmin() throws Exception{
        when(supplyService.getById(1)).thenReturn(supplyMapper.toDTO(fakeSupply));
        when(supplyService.getByProductId(fakeProduct1.getId())).thenReturn(supplyMapper.toDTO(fakeSupply));

        mockMvc.perform(
            post("/order/form")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("totalPrice", fakeOrderRegister.totalPrice().toString())
                .param("quantity", fakeOrderRegister.quantity().toString())
                .param("orderDate", fakeOrderRegister.orderDate().toString())
                .param("product.id", fakeOrderRegister.product_id().toString())
                .param("seller.id", fakeOrderRegister.seller_id().toString())
                .param("customer.id", fakeOrderRegister.customer_id().toString())
        ).andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/order/list"));

        verify(oService, times(1)).register(fakeOrderRegister);
    }

    @Test
    @WithMockUser
    @DisplayName("Default user can submit a edit in Order")
    public void testSubmitEditOrderAsUser() throws Exception{
        when(oService.getById(1)).thenReturn(fakeOrder1Response);
        when(supplyService.getByProductId(fakeProduct1.getId())).thenReturn(supplyMapper.toDTO(fakeSupply));

        mockMvc.perform(
            post("/order/form")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "1")
                .param("totalPrice", fakeOrderUpdate.totalPrice().toString())
                .param("quantity", fakeOrderUpdate.quantity().toString())
                .param("orderDate", fakeOrderUpdate.orderDate().toString())
                .param("product.id", fakeOrderUpdate.product_id().toString())
                .param("seller.id", fakeOrderUpdate.seller_id().toString())
                .param("customer.id", fakeOrderUpdate.customer_id().toString())
        ).andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/order/list"));

        verify(oService, times(1)).update(eq(1), fakeOrderUpdate);
    }

    @Test
    @WithMockUser
    @DisplayName("If order's quantity is greater than on supply's quantity, error will be returned on quantityError model registering a new Order")
    public void testSubmitEditOrderAsUserWithInvalidQuantity() throws Exception{
        when(oService.getById(1)).thenReturn(fakeOrder1Response);
        when(supplyService.getByProductId(fakeProduct1.getId())).thenReturn(supplyMapper.toDTO(fakeSupply));

        mockMvc.perform(
            post("/order/form")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("totalPrice", fakeOrderRegister.totalPrice().toString())
                .param("quantity", "51")
                .param("orderDate", fakeOrderRegister.orderDate().toString())
                .param("product.id", fakeOrderRegister.product_id().toString())
                .param("seller.id", fakeOrderRegister.seller_id().toString())
                .param("customer.id", fakeOrderRegister.customer_id().toString())
        ).andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/order/list"));

        verify(oService, times(1)).update(eq(1), fakeOrderUpdate);
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    @DisplayName("Admin user can submit a edit on Order")
    public void testSubmitEditOrderAsAdmin() throws Exception{
        when(oService.getById(1)).thenReturn(fakeOrder1Response);
        when(supplyService.getByProductId(fakeProduct1.getId())).thenReturn(supplyMapper.toDTO(fakeSupply));

        mockMvc.perform(
            post("/order/form")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "1")
                .param("totalPrice", fakeOrderUpdate.totalPrice().toString())
                .param("quantity", fakeOrderUpdate.quantity().toString())
                .param("orderDate", fakeOrderUpdate.orderDate().toString())
                .param("product.id", fakeOrderUpdate.product_id().toString())
                .param("seller.id", fakeOrderUpdate.seller_id().toString())
                .param("customer.id", fakeOrderUpdate.customer_id().toString())
        ).andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/order/list"));

        verify(oService, times(1)).update(eq(1), fakeOrderUpdate);
    }

    @Test
    @WithMockUser
    @DisplayName("Default user can delete Orders")
    public void testDeleteOrderAsUser() throws Exception{
        mockMvc.perform(get("/order/delete/1"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/order/list"));

        verify(oService, times(1)).delete(eq(1));
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    @DisplayName("Admin user can delete Orders")
    public void testDeleteOrderAsAdmin() throws Exception{
        mockMvc.perform(get("/order/delete/1"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/order/list"));

        verify(oService, times(1)).delete(eq(1));
    }
}
