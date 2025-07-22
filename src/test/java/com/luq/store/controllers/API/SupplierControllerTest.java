package com.luq.store.controllers.API;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luq.store.valueobjects.Cnpj;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.luq.store.domain.Supplier;
import com.luq.store.services.SupplierService;
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

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class SupplierControllerTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private SupplierService sService;

    private Supplier fakeSupplier1, fakeSupplier2;

    @BeforeEach
    public void setUp() {
        String user = "Jimmy McGill";
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

        fakeSupplier1 = new Supplier(
            1, "Microsoft Brasil LTDA.", new Cnpj("43.447.044/0004-10"),
            new Mail("microsoft@mail.com"), new Phone("11000001111"),
            user, now, user, now
        );

        fakeSupplier2 = new Supplier(
            2, "Sony Brasil LTDA.", new Cnpj("04.542.534/0001-09"),
            new Mail("sony@mail.com"), new Phone("11222225555"),
            user, now, user, now
        );
    }

    @Test
    @WithMockUser
    @DisplayName("Testing if correct supplier's parameters are being returned on get all method")
    public void testGetAllMethod() throws Exception {
        when(sService.getAll()).thenReturn(List.of(fakeSupplier1));
        String supplierJson = objectMapper.writeValueAsString(fakeSupplier1);

        mockMvc.perform(get("/api/supplier"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(content().json("[" + supplierJson + "]"));

        verify(sService, times(1)).getAll();
    }

    @Test
    @WithMockUser
    @DisplayName("Testing if correct supplier's parameters are being returned on get by id")
    public void testGetByIdMethod() throws Exception {
        when(sService.getById(1)).thenReturn(fakeSupplier1);
        String supplierJson = objectMapper.writeValueAsString(fakeSupplier1);

        mockMvc.perform(get("/api/supplier/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(supplierJson));

        verify(sService, times(1)).getById(1);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Testing if registering a new Supplier with a admin's role user is going successfully")
    public void testRegisterMethodWithAdmin() throws Exception {
        when(sService.register(fakeSupplier1)).thenReturn(fakeSupplier1);
        String supplierJson = objectMapper.writeValueAsString(fakeSupplier1);

        mockMvc.perform(
            post("/api/supplier")
                .contentType(MediaType.APPLICATION_JSON)
                .content(supplierJson)
            ).andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().json(supplierJson));

        verify(sService, times(1)).register(fakeSupplier1);
    }

    @Test
    @WithMockUser()
    @DisplayName("Testing if registering a new Supplier with a user's role user is going unauthorized")
    public void testRegisterMethodWithUser() throws Exception {
        when(sService.register(fakeSupplier1)).thenReturn(fakeSupplier1);
        String supplierJson = objectMapper.writeValueAsString(fakeSupplier1);

        mockMvc.perform(
            post("/api/supplier")
                .contentType(MediaType.APPLICATION_JSON)
                .content(supplierJson)
            ).andExpect(status().isForbidden());

        verifyNoInteractions(sService);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Testing if updating a Supplier with a admin's role user is going successfully")
    public void testUpdateMethodWithAdmin() throws Exception {
        when(sService.update(1, fakeSupplier2)).thenReturn(fakeSupplier2);
        String supplierJson = objectMapper.writeValueAsString(fakeSupplier2);

        mockMvc.perform(
            put("/api/supplier/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(supplierJson)
            ).andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().json(supplierJson));

        verify(sService, times(1)).update(1, fakeSupplier2);
    }

    @Test
    @WithMockUser()
    @DisplayName("Testing if updating a Supplier with a user's role user is going unauthorized")
    public void testUpdateMethodWithUser() throws Exception {
        when(sService.register(fakeSupplier2)).thenReturn(fakeSupplier2);
        String supplierJson = objectMapper.writeValueAsString(fakeSupplier2);

        mockMvc.perform(
            put("/api/supplier/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(supplierJson)
        ).andExpect(status().isForbidden());

        verifyNoInteractions(sService);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Testing if deleting a Supplier with a admin's role user is going successfully")
    public void testDeleteMethodWithAdmin() throws Exception {
        mockMvc.perform(delete("/api/supplier/1"))
             .andExpect(status().isOk());

        verify(sService, times(1)).delete(1);
    }

    @Test
    @WithMockUser()
    @DisplayName("Testing if deleting a Supplier with a user's role user is going unauthorized")
    public void testDeleteMethodWithUser() throws Exception {
        mockMvc.perform(put("/api/supplier/1"))
            .andExpect(status().isForbidden());

        verifyNoInteractions(sService);
    }
}