package com.luq.store.controllers.API;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luq.store.domain.Product;
import com.luq.store.services.ProductService;
import com.luq.store.valueobjects.Cnpj;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.luq.store.domain.Supplier;
import com.luq.store.valueobjects.Mail;
import com.luq.store.valueobjects.Phone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ProductService pService;

    private Product fakeProduct1, fakeProduct2;

    @BeforeEach
    public void setUp() {
        String user = "Jimmy McGill";
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

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
    }

    @Test
    @WithMockUser
    @DisplayName("Testing if correct product's parameters are being returned on get all method")
    public void testGetAllMethod() throws Exception {
        when(pService.getAll()).thenReturn(List.of(fakeProduct1));
        String productJson = objectMapper.writeValueAsString(fakeProduct1);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/product"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
            .andExpect(content().json("[" + productJson + "]"));

        verify(pService, times(1)).getAll();
    }

    @Test
    @WithMockUser
    @DisplayName("Testing if correct product's parameters are being returned on get by id")
    public void testGetByIdMethod() throws Exception {
        when(pService.getById(1)).thenReturn(fakeProduct1);
        String productJson = objectMapper.writeValueAsString(fakeProduct1);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/product/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(productJson));

        verify(pService, times(1)).getById(1);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Testing if registering a new Product with a admin's role user is going successfully")
    public void testRegisterMethodWithAdmin() throws Exception {
        when(pService.register(fakeProduct1)).thenReturn(fakeProduct1);
        String productJson = objectMapper.writeValueAsString(fakeProduct1);

        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(productJson)
            ).andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().json(productJson));


        verify(pService, times(1)).register(fakeProduct1);
    }

    @Test
    @WithMockUser()
    @DisplayName("Testing if registering a new Product with a user's role user is going unauthorized")
    public void testRegisterMethodWithUser() throws Exception {
        when(pService.register(fakeProduct1)).thenReturn(fakeProduct1);
        String productJson = objectMapper.writeValueAsString(fakeProduct1);

        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(productJson)
        ).andExpect(MockMvcResultMatchers.status().isForbidden());

        verifyNoInteractions(pService);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Testing if updating a Product with a admin's role user is going successfully")
    public void testUpdateMethodWithAdmin() throws Exception {
        when(pService.update(1, fakeProduct2)).thenReturn(fakeProduct2);
        String productJson = objectMapper.writeValueAsString(fakeProduct2);

        mockMvc.perform(
            MockMvcRequestBuilders.put("/api/product/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(productJson)
            ).andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().json(productJson));

        verify(pService, times(1)).update(1, fakeProduct2);
    }

    @Test
    @WithMockUser()
    @DisplayName("Testing if updating a Product with a user's role user is going unauthorized")
    public void testUpdateMethodWithUser() throws Exception {
        when(pService.register(fakeProduct2)).thenReturn(fakeProduct2);
        String productJson = objectMapper.writeValueAsString(fakeProduct2);

        mockMvc.perform(
            MockMvcRequestBuilders.put("/api/product/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(productJson)
        ).andExpect(MockMvcResultMatchers.status().isForbidden());

        verifyNoInteractions(pService);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Testing if deleting a Product with a admin's role user is going successfully")
    public void testDeleteMethodWithAdmin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/product/1"))
            .andExpect(MockMvcResultMatchers.status().isOk());

        verify(pService, times(1)).delete(1);
    }

    @Test
    @WithMockUser()
    @DisplayName("Testing if deleting a Product with a user's role user is going unauthorized")
    public void testDeleteMethodWithUser() throws Exception {
        mockMvc.perform( MockMvcRequestBuilders.put("/api/product/1"))
            .andExpect(MockMvcResultMatchers.status().isForbidden());

        verifyNoInteractions(pService);
    }
}