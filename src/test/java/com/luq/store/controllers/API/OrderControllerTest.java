package com.luq.store.controllers.API;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luq.store.domain.*;
import com.luq.store.services.OrderService;
import com.luq.store.valueobjects.Cnpj;
import com.luq.store.valueobjects.Mail;
import com.luq.store.valueobjects.Phone;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private OrderService oService;

    private Order fakeOrder1, fakeOrder2;

    @BeforeEach
    public void setUp() {
        String user = "Jimmy McGill";
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

        Customer fakeCustomer1 = new Customer(1, "Test Customer 01", user, now, user, now);
        Customer fakeCustomer2 = new Customer(2, "Test Customer 02", user, now, user, now);

        Seller fakeSeller1 = new Seller(
            1, "Walter White",
            new Mail("WalterWhite@Cooking.com"), new Phone("11901010101"), Department.FOOD,
            user, now, user, now
        );
        Seller fakeSeller2 = new Seller(
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

        Product fakeProduct1 = new Product(
            1, "Xbox One Controller", "XOneCont", "Controller for Xbox One Console",
            200.00F, fakeSupplier1, user, now, user, now
        );
        Product fakeProduct2 = new Product(
            2, "Playstation 5 Controller", "PS5Cont", "Controller for Playstation 5 Console",
            250.00F, fakeSupplier2, user, now, user, now
        );

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
    @DisplayName("Testing if correct order's parameters are being returned on get all method")
    public void testGetAllMethod() throws Exception {
        when(oService.getAll()).thenReturn(List.of(fakeOrder1));
        String orderJson = objectMapper.writeValueAsString(fakeOrder1);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/order"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
            .andExpect(content().json("[" + orderJson + "]"));


        verify(oService, times(1)).getAll();
    }

    @Test
    @WithMockUser
    @DisplayName("Testing if correct order's parameters are being returned on get by id")
    public void testGetByIdMethod() throws Exception {
        when(oService.getById(1)).thenReturn(fakeOrder1);
        String orderJson = objectMapper.writeValueAsString(fakeOrder1);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/order/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(orderJson));

        verify(oService, times(1)).getById(1);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Testing if registering a new Customer with a admin's role user is going successfully")
    public void testRegisterMethodWithAdmin() throws Exception {
        when(oService.register(fakeOrder1)).thenReturn(fakeOrder1);
        String orderJson = objectMapper.writeValueAsString(fakeOrder1);

        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(orderJson)
            ).andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().json(orderJson));

        verify(oService, times(1)).register(fakeOrder1);
    }

    @Test
    @WithMockUser()
    @DisplayName("Testing if registering a new Customer with a user's role user is going unauthorized")
    public void testRegisterMethodWithUser() throws Exception {
        when(oService.register(fakeOrder1)).thenReturn(fakeOrder1);
        String orderJson = objectMapper.writeValueAsString(fakeOrder2);

        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(orderJson)
        ).andExpect(MockMvcResultMatchers.status().isForbidden());

        verifyNoInteractions(oService);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Testing if updating a Customer with a admin's role user is going successfully")
    public void testUpdateMethodWithAdmin() throws Exception {
        when(oService.update(1, fakeOrder2)).thenReturn(fakeOrder2);
        String orderJson = objectMapper.writeValueAsString(fakeOrder2);

        mockMvc.perform(
            MockMvcRequestBuilders.put("/api/order/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(orderJson)
            ).andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().json(orderJson));


        verify(oService, times(1)).update(1, fakeOrder2);
    }

    @Test
    @WithMockUser()
    @DisplayName("Testing if updating a Customer with a user's role user is going unauthorized")
    public void testUpdateMethodWithUser() throws Exception {
        when(oService.register(fakeOrder2)).thenReturn(fakeOrder2);
        String orderJson = objectMapper.writeValueAsString(fakeOrder2);

        mockMvc.perform(
            MockMvcRequestBuilders.put("/api/order/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(orderJson)
        ).andExpect(MockMvcResultMatchers.status().isForbidden());

        verifyNoInteractions(oService);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Testing if deleting a Customer with a admin's role user is going successfully")
    public void testDeleteMethodWithAdmin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/order/1"))
            .andExpect(MockMvcResultMatchers.status().isOk());

        verify(oService, times(1)).delete(1);
    }

    @Test
    @WithMockUser()
    @DisplayName("Testing if deleting a Customer with a user's role user is going unauthorized")
    public void testDeleteMethodWithUser() throws Exception {
        mockMvc.perform( MockMvcRequestBuilders.put("/api/order/1"))
            .andExpect(MockMvcResultMatchers.status().isForbidden());

        verifyNoInteractions(oService);
    }
}