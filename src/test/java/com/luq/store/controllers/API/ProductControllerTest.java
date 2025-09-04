package com.luq.store.controllers.API;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luq.store.dto.request.product.ProductRegisterDTO;
import com.luq.store.dto.request.product.ProductUpdateDTO;
import com.luq.store.dto.response.product.ProductResponseDTO;
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

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.math.BigDecimal;
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

    private ProductResponseDTO fakeProduct1Response, fakeProduct2Response;
    private ProductRegisterDTO fakeProductRegister;
    private ProductUpdateDTO fakeProductUpdate;

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

        fakeProduct1Response = new ProductResponseDTO(
            1, "Xbox One Controller", "XOneCont", "Controller for Xbox One Console",
            BigDecimal.valueOf(200.00), fakeSupplier1, user, now, user, now
        );

        fakeProduct2Response = new ProductResponseDTO(
            1, "PS5 Controller", "PS5Cont", "Controller for PlayStation 5 Console",
            BigDecimal.valueOf(250.00), fakeSupplier2, user, now, user, now
        );

        fakeProductRegister = new ProductRegisterDTO(
            "Xbox One Controller", "XOneCont", "Controller for Xbox One Console",
            BigDecimal.valueOf(200.00), fakeSupplier1.getId()
        );

        fakeProductUpdate = new ProductUpdateDTO(
            "PS5 Controller", "PS5Cont", "Controller for PlayStation 5 Console",
            BigDecimal.valueOf(250.00), fakeSupplier2.getId()
        );
    }

    @Test
    @WithMockUser
    @DisplayName("Testing if correct product's parameters are being returned on get all method")
    public void testGetAllMethod() throws Exception {
        when(pService.getAll()).thenReturn(List.of(fakeProduct1Response));
        String responseJson = objectMapper.writeValueAsString(fakeProduct1Response);

        mockMvc.perform(get("/api/product"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(content().json("[" + responseJson + "]"));

        verify(pService, times(1)).getAll();
    }

    @Test
    @WithMockUser
    @DisplayName("Testing if correct product's parameters are being returned on get by id")
    public void testGetByIdMethod() throws Exception {
        when(pService.getById(1)).thenReturn(fakeProduct1Response);
        String responseJson = objectMapper.writeValueAsString(fakeProduct1Response);

        mockMvc.perform(get("/api/product/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(responseJson));

        verify(pService, times(1)).getById(1);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Testing if registering a new Product with a admin's role user is going successfully")
    public void testRegisterMethodWithAdmin() throws Exception {
        when(pService.register(fakeProductRegister)).thenReturn(fakeProduct1Response);
        String registerJson = objectMapper.writeValueAsString(fakeProductRegister);
        String responseJson = objectMapper.writeValueAsString(fakeProduct1Response);

        mockMvc.perform(
            post("/api/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(registerJson)
            ).andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().json(responseJson));


        verify(pService, times(1)).register(fakeProductRegister);
    }

    @Test
    @WithMockUser()
    @DisplayName("Testing if registering a new Product with a user's role user is going unauthorized")
    public void testRegisterMethodWithUser() throws Exception {
        when(pService.register(fakeProductRegister)).thenReturn(fakeProduct1Response);
        String registerJson = objectMapper.writeValueAsString(fakeProductRegister);

        mockMvc.perform(
            post("/api/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(registerJson)
        ).andExpect(status().isForbidden());

        verifyNoInteractions(pService);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Testing if updating a Product with a admin's role user is going successfully")
    public void testUpdateMethodWithAdmin() throws Exception {
        when(pService.update(1, fakeProductUpdate)).thenReturn(fakeProduct2Response);
        String updateJson = objectMapper.writeValueAsString(fakeProductUpdate);
        String responseJson = objectMapper.writeValueAsString(fakeProduct2Response);

        mockMvc.perform(
            put("/api/product/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJson)
            ).andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().json(responseJson));

        verify(pService, times(1)).update(1, fakeProductUpdate);
    }

    @Test
    @WithMockUser()
    @DisplayName("Testing if updating a Product with a user's role user is going unauthorized")
    public void testUpdateMethodWithUser() throws Exception {
        when(pService.update(1, fakeProductUpdate)).thenReturn(fakeProduct2Response);
        String updateJson = objectMapper.writeValueAsString(fakeProductUpdate);

        mockMvc.perform(
            put("/api/product/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJson)
        ).andExpect(status().isForbidden());

        verifyNoInteractions(pService);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Testing if deleting a Product with a admin's role user is going successfully")
    public void testDeleteMethodWithAdmin() throws Exception {
        mockMvc.perform(delete("/api/product/1"))
            .andExpect(status().isOk());

        verify(pService, times(1)).delete(1);
    }

    @Test
    @WithMockUser()
    @DisplayName("Testing if deleting a Product with a user's role user is going unauthorized")
    public void testDeleteMethodWithUser() throws Exception {
        mockMvc.perform(put("/api/product/1"))
            .andExpect(status().isForbidden());

        verifyNoInteractions(pService);
    }
}