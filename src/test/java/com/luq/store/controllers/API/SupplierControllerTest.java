package com.luq.store.controllers.API;

import static org.mockito.Mockito.*;

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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

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
    @DisplayName("Testing if correct get all supplier's parameters are being returned on get call")
    public void testGetAllMethod() throws Exception {
        when(sService.getAll()).thenReturn(List.of(fakeSupplier1));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/suppliers"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(fakeSupplier1.getId()))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(fakeSupplier1.getName()))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].cnpj").value(fakeSupplier1.getCnpj().getValue()))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].mail").value(fakeSupplier1.getMail().getValue()))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].phone").value(fakeSupplier1.getPhone().getValue()))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].createdBy").value(fakeSupplier1.getCreatedBy()))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].created").value(fakeSupplier1.getCreated().toString()))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].modifiedBy").value(fakeSupplier1.getModifiedBy()))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].modified").value(fakeSupplier1.getModified().toString()));

        verify(sService, times(1)).getAll();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Testing if registering a new Supplier with a admin's role user is going successfully")
    public void testRegisterMethodWithAdmin() throws Exception {
        when(sService.register(fakeSupplier1)).thenReturn(fakeSupplier1);
        String supplierJson = objectMapper.writeValueAsString(fakeSupplier1);

        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/suppliers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(supplierJson)
            ).andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(MockMvcResultMatchers.jsonPath("id").value(fakeSupplier1.getId()))
            .andExpect(MockMvcResultMatchers.jsonPath("name").value(fakeSupplier1.getName()))
            .andExpect(MockMvcResultMatchers.jsonPath("cnpj").value(fakeSupplier1.getCnpj().getValue()))
            .andExpect(MockMvcResultMatchers.jsonPath("mail").value(fakeSupplier1.getMail().getValue()))
            .andExpect(MockMvcResultMatchers.jsonPath("phone").value(fakeSupplier1.getPhone().getValue()))
            .andExpect(MockMvcResultMatchers.jsonPath("createdBy").value(fakeSupplier1.getCreatedBy()))
            .andExpect(MockMvcResultMatchers.jsonPath("created").value(fakeSupplier1.getCreated().toString()))
            .andExpect(MockMvcResultMatchers.jsonPath("modifiedBy").value(fakeSupplier1.getModifiedBy()))
            .andExpect(MockMvcResultMatchers.jsonPath("modified").value(fakeSupplier1.getModified().toString()));

        verify(sService, times(1)).register(fakeSupplier1);
    }

    @Test
    @WithMockUser()
    @DisplayName("Testing if registering a new Supplier with a user's role user is going unauthorized")
    public void testRegisterMethodWithUser() throws Exception {
        when(sService.register(fakeSupplier1)).thenReturn(fakeSupplier1);
        String supplierJson = objectMapper.writeValueAsString(fakeSupplier1);

        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/suppliers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(supplierJson)
            ).andExpect(MockMvcResultMatchers.status().isForbidden());

        verifyNoInteractions(sService);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Testing if updating a Supplier with a admin's role user is going successfully")
    public void testUpdateMethodWithAdmin() throws Exception {
        when(sService.update(1, fakeSupplier2)).thenReturn(fakeSupplier2);
        String supplierJson = objectMapper.writeValueAsString(fakeSupplier2);

        mockMvc.perform(
            MockMvcRequestBuilders.put("/api/suppliers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(supplierJson)
            ).andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(MockMvcResultMatchers.jsonPath("id").value(fakeSupplier2.getId()))
            .andExpect(MockMvcResultMatchers.jsonPath("name").value(fakeSupplier2.getName()))
            .andExpect(MockMvcResultMatchers.jsonPath("cnpj").value(fakeSupplier2.getCnpj().getValue()))
            .andExpect(MockMvcResultMatchers.jsonPath("mail").value(fakeSupplier2.getMail().getValue()))
            .andExpect(MockMvcResultMatchers.jsonPath("phone").value(fakeSupplier2.getPhone().getValue()))
            .andExpect(MockMvcResultMatchers.jsonPath("createdBy").value(fakeSupplier2.getCreatedBy()))
            .andExpect(MockMvcResultMatchers.jsonPath("created").value(fakeSupplier2.getCreated().toString()))
            .andExpect(MockMvcResultMatchers.jsonPath("modifiedBy").value(fakeSupplier2.getModifiedBy()))
            .andExpect(MockMvcResultMatchers.jsonPath("modified").value(fakeSupplier2.getModified().toString()));

        verify(sService, times(1)).update(1, fakeSupplier2);
    }

    @Test
    @WithMockUser()
    @DisplayName("Testing if updating a Supplier with a user's role user is going unauthorized")
    public void testUpdateMethodWithUser() throws Exception {
        when(sService.register(fakeSupplier2)).thenReturn(fakeSupplier2);
        String supplierJson = objectMapper.writeValueAsString(fakeSupplier2);

        mockMvc.perform(
            MockMvcRequestBuilders.put("/api/suppliers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(supplierJson)
        ).andExpect(MockMvcResultMatchers.status().isForbidden());

        verifyNoInteractions(sService);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Testing if deleting a Supplier with a admin's role user is going successfully")
    public void testDeleteMethodWithAdmin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/suppliers/1"))
             .andExpect(MockMvcResultMatchers.status().isOk());

        verify(sService, times(1)).delete(1);
    }

    @Test
    @WithMockUser()
    @DisplayName("Testing if deleting a Supplier with a user's role user is going unauthorized")
    public void testDeleteMethodWithUser() throws Exception {
        mockMvc.perform( MockMvcRequestBuilders.put("/api/suppliers/1"))
            .andExpect(MockMvcResultMatchers.status().isForbidden());

        verifyNoInteractions(sService);
    }
}