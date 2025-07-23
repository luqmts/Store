package com.luq.store.controllers.Web;

import com.luq.store.domain.*;
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
import org.junit.jupiter.api.Disabled;
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
    private TokenService tService;

    @MockBean
    private UserRepository uRepository;

    private Order fakeOrder1, fakeOrder2;
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
            200.00F, fakeSupplier1, user, now, user, now
        );
        fakeProduct2 = new Product(
            2, "Playstation 5 Controller", "PS5Cont", "Controller for Playstation 5 Console",
            250.00F, fakeSupplier2, user, now, user, now
        );

        fakeSupply = new Supply(1, 50, fakeProduct1, user, now, user, now);

        fakeOrder1 = new Order(
            1, 400.00F, 2, LocalDate.now(),
            fakeProduct1, fakeSeller1, fakeCustomer1, user, now, user, now
        );
        fakeOrder2 = new Order(
            2, 1000.00F, 4, LocalDate.now(),
            fakeProduct2, fakeSeller2, fakeCustomer2, user, now, user, now
        );
    }

    @Test
    @WithMockUser
    @DisplayName("Test if all Orders are being returned on getAllSorted method with no filters applied and default user")
    public void testListAllOrders() throws Exception{
        when(oService.getAllSorted("id", "asc", null, null, null)).thenReturn(List.of(fakeOrder1, fakeOrder2));
        when(pService.getAll()).thenReturn(List.of(fakeProduct1, fakeProduct2));
        when(sellerService.getAll()).thenReturn(List.of(fakeSeller1, fakeSeller2));
        when(cService.getAll()).thenReturn(List.of(fakeCustomer1, fakeCustomer2));

        mockMvc.perform(
            get("/order/list")
                .param("sortBy", "id")
                .param("direction", "asc")
        ).andExpect(status().isOk())
        .andExpect(view().name("order-list"))
        .andExpect(model().attributeExists("orders"))
        .andExpect(model().attribute("orders", List.of(fakeOrder1, fakeOrder2)))
        .andExpect(model().attribute("page", "order"))
        .andExpect(model().attribute("sortBy", "id"))
        .andExpect(model().attribute("direction", "asc"))
        .andExpect(model().attribute("products", List.of(fakeProduct1, fakeProduct2)))
        .andExpect(model().attribute("customers", List.of(fakeCustomer1, fakeCustomer2)))
        .andExpect(model().attribute("sellers", List.of(fakeSeller1, fakeSeller2)));
    }

    @Test
    @WithMockUser
    @DisplayName("Test if Order is being returned on getAllSorted method with one filter applied and default user")
    public void testListOrdersWithOneFilter() throws Exception{
        when(oService.getAllSorted("id", "asc", 1, null, null)).thenReturn(List.of(fakeOrder1));
        when(pService.getAll()).thenReturn(List.of(fakeProduct1, fakeProduct2));
        when(sellerService.getAll()).thenReturn(List.of(fakeSeller1, fakeSeller2));
        when(cService.getAll()).thenReturn(List.of(fakeCustomer1, fakeCustomer2));

        mockMvc.perform(
            get("/order/list")
                .param("sortBy", "id")
                .param("direction", "asc")
                .param("product.id", "1")
        ).andExpect(status().isOk())
        .andExpect(view().name("order-list"))
        .andExpect(model().attributeExists("orders"))
        .andExpect(model().attribute("orders", List.of(fakeOrder1)))
        .andExpect(model().attribute("page", "order"))
        .andExpect(model().attribute("sortBy", "id"))
        .andExpect(model().attribute("direction", "asc"))
        .andExpect(model().attribute("productId", 1))
        .andExpect(model().attribute("products", List.of(fakeProduct1, fakeProduct2)))
        .andExpect(model().attribute("customers", List.of(fakeCustomer1, fakeCustomer2)))
        .andExpect(model().attribute("sellers", List.of(fakeSeller1, fakeSeller2)));
    }

    @Test
    @WithMockUser
    @DisplayName("Test if Order is being returned on getAllSorted method with all filters applied and default user")
    public void testListOrdersWithAllFilters() throws Exception{
        when(oService.getAllSorted("id", "asc", 1, 1, 1)).thenReturn(List.of(fakeOrder1));
        when(pService.getAll()).thenReturn(List.of(fakeProduct1, fakeProduct2));
        when(sellerService.getAll()).thenReturn(List.of(fakeSeller1, fakeSeller2));
        when(cService.getAll()).thenReturn(List.of(fakeCustomer1, fakeCustomer2));

        mockMvc.perform(
            get("/order/list")
                .param("sortBy", "id")
                .param("direction", "asc")
                .param("product.id", "1")
                .param("seller.id", "1")
                .param("customer.id", "1")
        ).andExpect(status().isOk())
        .andExpect(view().name("order-list"))
        .andExpect(model().attributeExists("orders"))
        .andExpect(model().attribute("orders", List.of(fakeOrder1)))
        .andExpect(model().attribute("page", "order"))
        .andExpect(model().attribute("sortBy", "id"))
        .andExpect(model().attribute("direction", "asc"))
        .andExpect(model().attribute("productId", 1))
        .andExpect(model().attribute("sellerId", 1))
        .andExpect(model().attribute("customerId", 1))
        .andExpect(model().attribute("products", List.of(fakeProduct1, fakeProduct2)))
        .andExpect(model().attribute("customers", List.of(fakeCustomer1, fakeCustomer2)))
        .andExpect(model().attribute("sellers", List.of(fakeSeller1, fakeSeller2)));
    }

    @Test
    @WithMockUser
    @DisplayName("Test if no Order is being returned on getAllSorted method correctly as a default user")
    public void testListWithNoOrders() throws Exception{
        when(oService.getAllSorted("id", "asc", 5, null, null)).thenReturn(List.of());
        when(pService.getAll()).thenReturn(List.of(fakeProduct1, fakeProduct2));
        when(sellerService.getAll()).thenReturn(List.of(fakeSeller1, fakeSeller2));
        when(cService.getAll()).thenReturn(List.of(fakeCustomer1, fakeCustomer2));

        mockMvc.perform(
            get("/order/list")
                .param("sortBy", "id")
                .param("direction", "asc")
                .param("product.id", "5")
        ).andExpect(status().isOk())
        .andExpect(view().name("order-list"))
        .andExpect(model().attributeExists("orders"))
        .andExpect(model().attribute("orders", List.of()))
        .andExpect(model().attribute("page", "order"))
        .andExpect(model().attribute("sortBy", "id"))
        .andExpect(model().attribute("direction", "asc"))
        .andExpect(model().attribute("productId", 5))
        .andExpect(model().attribute("products", List.of(fakeProduct1, fakeProduct2)))
        .andExpect(model().attribute("customers", List.of(fakeCustomer1, fakeCustomer2)))
        .andExpect(model().attribute("sellers", List.of(fakeSeller1, fakeSeller2)));
    }

    @Test
    @WithMockUser
    @DisplayName("Default user can access Order form page (New Order)")
    public void testOrderFormAsDefaultUser() throws Exception{
        when(pService.getAllRegisteredOnSupply()).thenReturn(List.of(fakeProduct1, fakeProduct2));
        when(sellerService.getAll()).thenReturn(List.of(fakeSeller1, fakeSeller2));
        when(cService.getAll()).thenReturn(List.of(fakeCustomer1, fakeCustomer2));

        mockMvc.perform(get("/order/form"))
            .andExpect(status().isOk())
            .andExpect(view().name("order-form"))
            .andExpect(model().attributeExists("order"))
            .andExpect(model().attribute("page", "order"))
            .andExpect(model().attribute("products", List.of(fakeProduct1, fakeProduct2)))
            .andExpect(model().attribute("customers", List.of(fakeCustomer1, fakeCustomer2)))
            .andExpect(model().attribute("sellers", List.of(fakeSeller1, fakeSeller2)));
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    @DisplayName("Admin user can access Order form page (New Order)")
    public void testOrderFormAsAdmin() throws Exception{
        when(pService.getAllRegisteredOnSupply()).thenReturn(List.of(fakeProduct1, fakeProduct2));
        when(sellerService.getAll()).thenReturn(List.of(fakeSeller1, fakeSeller2));
        when(cService.getAll()).thenReturn(List.of(fakeCustomer1, fakeCustomer2));

        mockMvc.perform(get("/order/form"))
        .andExpect(status().isOk())
        .andExpect(view().name("order-form"))
        .andExpect(model().attributeExists("order"))
        .andExpect(model().attribute("page", "order"))
        .andExpect(model().attribute("products", List.of(fakeProduct1, fakeProduct2)))
        .andExpect(model().attribute("customers", List.of(fakeCustomer1, fakeCustomer2)))
        .andExpect(model().attribute("sellers", List.of(fakeSeller1, fakeSeller2)));
    }

    @Test
    @WithMockUser
    @DisplayName("Default user can access Order form page (Edit Order)")
    public void testOrderEditFormAsDefaultUser() throws Exception{
        when(oService.getById(1)).thenReturn(fakeOrder1);
        when(supplyService.getByProduct(fakeOrder1.getProduct())).thenReturn(fakeSupply);
        when(pService.getAllRegisteredOnSupply(1)).thenReturn(List.of(fakeProduct1, fakeProduct2));
        when(sellerService.getAll()).thenReturn(List.of(fakeSeller1, fakeSeller2));
        when(cService.getAll()).thenReturn(List.of(fakeCustomer1, fakeCustomer2));

        mockMvc.perform(get("/order/form/1"))
            .andExpect(status().isOk())
            .andExpect(view().name("order-form"))
            .andExpect(model().attributeExists("order"))
            .andExpect(model().attribute("order", fakeOrder1))
            .andExpect(model().attribute("page", "order"))
            .andExpect(model().attribute("products", List.of(fakeProduct1, fakeProduct2)))
            .andExpect(model().attribute("customers", List.of(fakeCustomer1, fakeCustomer2)))
            .andExpect(model().attribute("sellers", List.of(fakeSeller1, fakeSeller2)));
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    @DisplayName("Admin user can access Order form page (Edit Order)")
    public void testOrderEditFormAsAdmin() throws Exception{
        when(oService.getById(1)).thenReturn(fakeOrder1);
        when(supplyService.getByProduct(fakeOrder1.getProduct())).thenReturn(fakeSupply);
        when(pService.getAllRegisteredOnSupply(1)).thenReturn(List.of(fakeProduct1, fakeProduct2));
        when(sellerService.getAll()).thenReturn(List.of(fakeSeller1, fakeSeller2));
        when(cService.getAll()).thenReturn(List.of(fakeCustomer1, fakeCustomer2));

        mockMvc.perform(get("/order/form/1"))
        .andExpect(status().isOk())
        .andExpect(view().name("order-form"))
        .andExpect(model().attributeExists("order"))
        .andExpect(model().attribute("order", fakeOrder1))
        .andExpect(model().attribute("page", "order"))
        .andExpect(model().attribute("products", List.of(fakeProduct1, fakeProduct2)))
        .andExpect(model().attribute("customers", List.of(fakeCustomer1, fakeCustomer2)))
        .andExpect(model().attribute("sellers", List.of(fakeSeller1, fakeSeller2)));
    }

    @Test
    @WithMockUser
    @DisplayName("Default user can submit new Orders")
    public void testSubmitNewOrderAsUser() throws Exception{
        when(supplyService.getById(1)).thenReturn(fakeSupply);
        when(supplyService.getByProduct(fakeProduct1)).thenReturn(fakeSupply);

        mockMvc.perform(
            post("/order/form")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("totalPrice", "500.00F")
                .param("quantity", "2")
                .param("orderDate", LocalDate.now().toString())
                .param("product.id", "1")
                .param("seller.id", "1")
                .param("customer.id", "1")
        ).andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/order/list"));

        verify(oService, times(1)).register(any(Order.class));
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    @DisplayName("Admin user can submit new Orders")
    public void testSubmitNewOrderAsAdmin() throws Exception{
        when(supplyService.getById(1)).thenReturn(fakeSupply);
        when(supplyService.getByProduct(fakeProduct1)).thenReturn(fakeSupply);

        mockMvc.perform(
            post("/order/form")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("totalPrice", "500.00F")
                .param("quantity", "2")
                .param("orderDate", LocalDate.now().toString())
                .param("product.id", "1")
                .param("seller.id", "1")
                .param("customer.id", "1")
        ).andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/order/list"));

        verify(oService, times(1)).register(any(Order.class));
    }

    @Test
    @WithMockUser
    @DisplayName("Default user can submit a edit in Order")
    public void testSubmitEditOrderAsUser() throws Exception{
        when(oService.getById(1)).thenReturn(fakeOrder1);
        when(supplyService.getByProduct(fakeProduct1)).thenReturn(fakeSupply);

        mockMvc.perform(
            post("/order/form")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "1")
                .param("totalPrice", "500.00F")
                .param("quantity", "2")
                .param("orderDate", LocalDate.now().toString())
                .param("product.id", "1")
                .param("seller.id", "1")
                .param("customer.id", "1")
        ).andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/order/list"));

        verify(oService, times(1)).update(eq(1), any(Order.class));
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    @DisplayName("Admin user can submit a edit on Order")
    public void testSubmitEditOrderAsAdmin() throws Exception{
        when(oService.getById(1)).thenReturn(fakeOrder1);
        when(supplyService.getByProduct(fakeProduct1)).thenReturn(fakeSupply);

        mockMvc.perform(
            post("/order/form")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "1")
                .param("totalPrice", "500.00F")
                .param("quantity", "2")
                .param("orderDate", LocalDate.now().toString())
                .param("product.id", "1")
                .param("seller.id", "1")
                .param("customer.id", "1")
        ).andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/order/list"));

        verify(oService, times(1)).update(eq(1), any(Order.class));
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
