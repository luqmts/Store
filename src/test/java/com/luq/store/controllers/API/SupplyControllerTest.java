package com.luq.store.controllers.API;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luq.store.domain.Product;
import com.luq.store.domain.Supplier;
import com.luq.store.domain.Supply;
import com.luq.store.services.SupplyService;
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
public class SupplyControllerTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private SupplyService sService;

    private Supply fakeSupply1, fakeSupply2;

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

        Product fakeProduct1 = new Product(
            1, "Xbox One Controller", "XOneCont", "Controller for Xbox One Console",
            BigDecimal.valueOf(200.00), fakeSupplier1, user, now, user, now
        );
        Product fakeProduct2 = new Product(
            2, "Playstation 5 Controller", "PS5Cont", "Controller for Playstation 5 Console",
            BigDecimal.valueOf(250.00), fakeSupplier2, user, now, user, now
        );

        fakeSupply1 = new Supply(1, 100, fakeProduct1, user, now, user, now);
        fakeSupply2 = new Supply(2, 300, fakeProduct2, user, now, user, now);
    }

    @Test
    @WithMockUser
    @DisplayName("Testing if correct supply's parameters are being returned on get all method")
    public void testGetAllMethod() throws Exception {
        when(sService.getAll()).thenReturn(List.of(fakeSupply1));
        String supplyJson = objectMapper.writeValueAsString(fakeSupply1);

        mockMvc.perform(get("/api/supply"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(content().json("[" + supplyJson + "]"));

        verify(sService, times(1)).getAll();
    }

    @Test
    @WithMockUser
    @DisplayName("Testing if correct supply's parameters are being returned on get by id")
    public void testGetByIdMethod() throws Exception {
        when(sService.getById(1)).thenReturn(fakeSupply1);
        String supplyJson = objectMapper.writeValueAsString(fakeSupply1);

        mockMvc.perform(get("/api/supply/1"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().json(supplyJson));

        verify(sService, times(1)).getById(1);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Testing if registering a new Supply with a admin's role user is going successfully")
    public void testRegisterMethodWithAdmin() throws Exception {
        when(sService.register(fakeSupply1)).thenReturn(fakeSupply1);
        String supplyJson = objectMapper.writeValueAsString(fakeSupply1);

        mockMvc.perform(
            post("/api/supply")
                .contentType(MediaType.APPLICATION_JSON)
                .content(supplyJson)
            ).andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().json(supplyJson));

        verify(sService, times(1)).register(fakeSupply1);
    }

    @Test
    @WithMockUser()
    @DisplayName("Testing if registering a new Supply with a user's role user is going unauthorized")
    public void testRegisterMethodWithUser() throws Exception {
        when(sService.register(fakeSupply1)).thenReturn(fakeSupply1);
        String supplyJson = objectMapper.writeValueAsString(fakeSupply1);

        mockMvc.perform(
            post("/api/supply")
                .contentType(MediaType.APPLICATION_JSON)
                .content(supplyJson)
            ).andExpect(status().isForbidden());

        verifyNoInteractions(sService);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Testing if updating a Supply with a admin's role user is going successfully")
    public void testUpdateMethodWithAdmin() throws Exception {
        when(sService.update(1, fakeSupply2)).thenReturn(fakeSupply2);
        String supplyJson = objectMapper.writeValueAsString(fakeSupply2);

        mockMvc.perform(
            put("/api/supply/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(supplyJson)
            ).andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().json(supplyJson));

        verify(sService, times(1)).update(1, fakeSupply2);
    }

    @Test
    @WithMockUser()
    @DisplayName("Testing if updating a Supply with a user's role user is going unauthorized")
    public void testUpdateMethodWithUser() throws Exception {
        when(sService.register(fakeSupply2)).thenReturn(fakeSupply2);
        String supplyJson = objectMapper.writeValueAsString(fakeSupply2);

        mockMvc.perform(
            put("/api/supply/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(supplyJson)
        ).andExpect(status().isForbidden());

        verifyNoInteractions(sService);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Testing if deleting a Supply with a admin's role user is going successfully")
    public void testDeleteMethodWithAdmin() throws Exception {
        mockMvc.perform(delete("/api/supply/1"))
             .andExpect(status().isOk());

        verify(sService, times(1)).delete(1);
    }

    @Test
    @WithMockUser()
    @DisplayName("Testing if deleting a Supply with a user's role user is going unauthorized")
    public void testDeleteMethodWithUser() throws Exception {
        mockMvc.perform(put("/api/supply/1"))
            .andExpect(status().isForbidden());

        verifyNoInteractions(sService);
    }
}