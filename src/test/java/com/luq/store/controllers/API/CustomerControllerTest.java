package com.luq.store.controllers.API;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luq.store.domain.Customer;
import com.luq.store.services.CustomerService;
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

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

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

@SpringBootTest
@AutoConfigureMockMvc
public class CustomerControllerTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CustomerService cService;

    private Customer fakeCustomer1, fakeCustomer2;

    @BeforeEach
    public void setUp() {
        String user = "Jimmy McGill";
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

        fakeCustomer1 = new Customer(1, "Test Customer 01", user, now, user, now);
        fakeCustomer2 = new Customer(2, "Test Customer 02", user, now, user, now);
    }

    @Test
    @WithMockUser
    @DisplayName("Testing if correct customer's parameters are being returned on get all method")
    public void testGetAllMethod() throws Exception {
        when(cService.getAll()).thenReturn(List.of(fakeCustomer1));
        String customerJson = objectMapper.writeValueAsString(fakeCustomer1);

        mockMvc.perform(get("/api/customer"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(content().json("[" + customerJson + "]"));

        verify(cService, times(1)).getAll();
    }

    @Test
    @WithMockUser
    @DisplayName("Testing if correct customer's parameters are being returned on get by id")
    public void testGetByIdMethod() throws Exception {
        when(cService.getById(1)).thenReturn(fakeCustomer1);
        String customerJson = objectMapper.writeValueAsString(fakeCustomer1);

        mockMvc.perform(get("/api/customer/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(customerJson));

        verify(cService, times(1)).getById(1);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Testing if registering a new Customer with a admin's role user is going successfully")
    public void testRegisterMethodWithAdmin() throws Exception {
        when(cService.register(fakeCustomer1)).thenReturn(fakeCustomer1);
        String customerJson = objectMapper.writeValueAsString(fakeCustomer1);

        mockMvc.perform(
            post("/api/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(customerJson)
            ).andExpect(status().isOk())
            .andExpect(content().json(customerJson));

        verify(cService, times(1)).register(fakeCustomer1);
    }

    @Test
    @WithMockUser()
    @DisplayName("Testing if registering a new Customer with a user's role user is going unauthorized")
    public void testRegisterMethodWithUser() throws Exception {
        when(cService.register(fakeCustomer1)).thenReturn(fakeCustomer1);
        String customerJson = objectMapper.writeValueAsString(fakeCustomer1);

        mockMvc.perform(
            post("/api/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(customerJson)
        ).andExpect(status().isForbidden());

        verifyNoInteractions(cService);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Testing if updating a Customer with a admin's role user is going successfully")
    public void testUpdateMethodWithAdmin() throws Exception {
        when(cService.update(1, fakeCustomer2)).thenReturn(fakeCustomer2);
        String customerJson = objectMapper.writeValueAsString(fakeCustomer2);

        mockMvc.perform(
            put("/api/customer/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(customerJson)
            ).andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().json(customerJson));

        verify(cService, times(1)).update(1, fakeCustomer2);
    }

    @Test
    @WithMockUser()
    @DisplayName("Testing if updating a Customer with a user's role user is going unauthorized")
    public void testUpdateMethodWithUser() throws Exception {
        when(cService.register(fakeCustomer2)).thenReturn(fakeCustomer2);
        String customerJson = objectMapper.writeValueAsString(fakeCustomer2);

        mockMvc.perform(
            put("/api/customer/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(customerJson)
        ).andExpect(status().isForbidden());

        verifyNoInteractions(cService);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Testing if deleting a Customer with a admin's role user is going successfully")
    public void testDeleteMethodWithAdmin() throws Exception {
        mockMvc.perform(delete("/api/customer/1"))
            .andExpect(status().isOk());

        verify(cService, times(1)).delete(1);
    }

    @Test
    @WithMockUser()
    @DisplayName("Testing if deleting a Customer with a user's role user is going unauthorized")
    public void testDeleteMethodWithUser() throws Exception {
        mockMvc.perform(put("/api/customer/1"))
            .andExpect(status().isForbidden());

        verifyNoInteractions(cService);
    }
}