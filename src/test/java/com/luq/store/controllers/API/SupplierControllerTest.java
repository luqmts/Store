package com.luq.store.controllers.API;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luq.store.dto.request.supplier.SupplierRegisterDTO;
import com.luq.store.dto.request.supplier.SupplierUpdateDTO;
import com.luq.store.dto.response.supplier.SupplierResponseDTO;
import com.luq.store.valueobjects.Cnpj;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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

    private SupplierResponseDTO fakeSupplier1Response, fakeSupplier2Response;
    private SupplierRegisterDTO fakeSupplierRegister;
    private SupplierUpdateDTO fakeSupplierUpdate;

    @BeforeEach
    public void setUp() {
        String user = "Jimmy McGill";
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

        fakeSupplier1Response = new SupplierResponseDTO(
            1, "Microsoft Brasil LTDA.", "43.447.044/0004-10",
            "microsoft@mail.com", "11000001111",
            user, now, user, now
        );
        fakeSupplier2Response = new SupplierResponseDTO(
            1, "Sony Brasil LTDA.", "04.542.534/0001-09",
            "sony@mail.com", "11222225555",
            user, now, user, now
        );

        fakeSupplierRegister = new SupplierRegisterDTO(
            "Microsoft Brasil LTDA.", "43.447.044/0004-10",
            "microsoft@mail.com", "11000001111"
        );

        fakeSupplierUpdate = new SupplierUpdateDTO(
            "Sony Brasil LTDA.", "04.542.534/0001-09",
            "sony@mail.com", "11222225555"
        );
    }

    @Test
    @WithMockUser
    @DisplayName("Testing if correct supplier's parameters are being returned on get all method")
    public void testGetAllMethod() throws Exception {
        when(sService.getAll()).thenReturn(List.of(fakeSupplier1Response));
        String responseJson = objectMapper.writeValueAsString(fakeSupplier1Response);

        mockMvc.perform(get("/api/supplier"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(content().json("[" + responseJson + "]"));

        verify(sService, times(1)).getAll();
    }

    @Test
    @WithMockUser
    @DisplayName("Testing if correct supplier's parameters are being returned on get by id")
    public void testGetByIdMethod() throws Exception {
        when(sService.getById(1)).thenReturn(fakeSupplier1Response);
        String responseJson = objectMapper.writeValueAsString(fakeSupplier1Response);

        mockMvc.perform(get("/api/supplier/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(responseJson));

        verify(sService, times(1)).getById(1);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Testing if registering a new Supplier with a admin's role user is going successfully")
    public void testRegisterMethodWithAdmin() throws Exception {
        when(sService.register(fakeSupplierRegister)).thenReturn(fakeSupplier1Response);
        String registerJson = objectMapper.writeValueAsString(fakeSupplierRegister);
        String responseJson = objectMapper.writeValueAsString(fakeSupplier1Response);

        mockMvc.perform(
            post("/api/supplier")
                .contentType(MediaType.APPLICATION_JSON)
                .content(registerJson)
            ).andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().json(responseJson));

        verify(sService, times(1)).register(fakeSupplierRegister);
    }

    @Test
    @WithMockUser()
    @DisplayName("Testing if registering a new Supplier with a user's role user is going unauthorized")
    public void testRegisterMethodWithUser() throws Exception {
        when(sService.register(fakeSupplierRegister)).thenReturn(fakeSupplier1Response);
        String registerJson = objectMapper.writeValueAsString(fakeSupplierRegister);

        mockMvc.perform(
            post("/api/supplier")
                .contentType(MediaType.APPLICATION_JSON)
                .content(registerJson)
            ).andExpect(status().isForbidden());

        verifyNoInteractions(sService);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Testing if updating a Supplier with a admin's role user is going successfully")
    public void testUpdateMethodWithAdmin() throws Exception {
        when(sService.update(1, fakeSupplierUpdate)).thenReturn(fakeSupplier2Response);
        String updateJson = objectMapper.writeValueAsString(fakeSupplierUpdate);
        String responseJson = objectMapper.writeValueAsString(fakeSupplier2Response);

        mockMvc.perform(
            put("/api/supplier/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJson)
            ).andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().json(responseJson));

        verify(sService, times(1)).update(1, fakeSupplierUpdate);
    }

    @Test
    @WithMockUser()
    @DisplayName("Testing if updating a Supplier with a user's role user is going unauthorized")
    public void testUpdateMethodWithUser() throws Exception {
        when(sService.update(1, fakeSupplierUpdate)).thenReturn(fakeSupplier2Response);
        String updateJson = objectMapper.writeValueAsString(fakeSupplierUpdate);

        mockMvc.perform(
            put("/api/supplier/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJson)
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