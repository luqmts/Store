package com.luq.store.controllers.Web;

import com.luq.store.domain.Customer;
import com.luq.store.infra.security.SecurityConfig;
import com.luq.store.repositories.UserRepository;
import com.luq.store.services.CustomerService;
import com.luq.store.services.TokenService;
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

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@WebMvcTest(CustomerWebController.class)
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
public class CustomerWebControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService cService;

    @MockBean
    private TokenService tService;

    @MockBean
    private UserRepository uRepository;

    private Customer fakeCustomer1, fakeCustomer2;

    @BeforeEach
    public void setUp(){
        String user = "Jimmy McGill";
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

        fakeCustomer1 = new Customer(1, "Test Customer 01", user, now, user, now);
        fakeCustomer2 = new Customer(2, "Test Customer 02", user, now, user, now);
    }

    @Test
    @WithMockUser
    @DisplayName("Test if all Customers are being returned on getAllSorted method with no filters applied and default user")
    public void testListAllCustomers() throws Exception{
        when(cService.getAllSorted("id", "asc", null)).thenReturn(List.of(fakeCustomer1, fakeCustomer2));

        mockMvc.perform(
            get("/customer/list")
                .param("sortBy", "id")
                .param("direction", "asc")
        ).andExpect(status().isOk())
        .andExpect(view().name("customer-list"))
        .andExpect(model().attributeExists("customers"))
        .andExpect(model().attribute("customers", List.of(fakeCustomer1, fakeCustomer2)))
        .andExpect(model().attribute("page", "customer"))
        .andExpect(model().attribute("sortBy", "id"))
        .andExpect(model().attribute("direction", "asc"));
    }

    @Test
    @WithMockUser
    @DisplayName("Test if Customer is being returned on getAllSorted method with filters applied and default user")
    public void testListCustomersWithOneFilter() throws Exception{
        when(cService.getAllSorted("id", "asc", "Test Customer 01")).thenReturn(List.of(fakeCustomer1));

        mockMvc.perform(
            get("/customer/list")
                .param("sortBy", "id")
                .param("direction", "asc")
                .param("name", "Test Customer 01")
        ).andExpect(status().isOk())
        .andExpect(view().name("customer-list"))
        .andExpect(model().attributeExists("customers"))
        .andExpect(model().attribute("customers", List.of(fakeCustomer1)))
        .andExpect(model().attribute("page", "customer"))
        .andExpect(model().attribute("sortBy", "id"))
        .andExpect(model().attribute("direction", "asc"))
        .andExpect(model().attribute("name", "Test Customer 01"));
    }

    @Test
    @WithMockUser
    @DisplayName("Test if no Customer is being returned on getAllSorted method correctly as a default user")
    public void testListWithNoCustomers() throws Exception{
        when(cService.getAllSorted("id", "asc", "return nothing")).thenReturn(List.of());

        mockMvc.perform(
            get("/customer/list")
                .param("sortBy", "id")
                .param("direction", "asc")
                .param("name", "return nothing")
        ).andExpect(status().isOk())
        .andExpect(view().name("customer-list"))
        .andExpect(model().attributeExists("customers"))
        .andExpect(model().attribute("customers", List.of()))
        .andExpect(model().attribute("page", "customer"))
        .andExpect(model().attribute("sortBy", "id"))
        .andExpect(model().attribute("direction", "asc"))
        .andExpect(model().attribute("name", "return nothing"));
    }

    @Test
    @WithMockUser
    @DisplayName("Default user must not access Customer form page (New Customer)")
    public void testCustomerFormAsDefaultUser() throws Exception{
        mockMvc.perform(get("/customer/form"))
        .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    @DisplayName("Admin user can access Customer form page (New Customer)")
    public void testCustomerFormAsAdmin() throws Exception{
        mockMvc.perform(get("/customer/form"))
        .andExpect(status().isOk())
        .andExpect(view().name("customer-form"))
        .andExpect(model().attributeExists("customer"))
        .andExpect(model().attribute("page", "customer"));
    }

    @Test
    @WithMockUser
    @DisplayName("Default user must not access Customer form page (Edit Customer)")
    public void testCustomerEditFormAsDefaultUser() throws Exception{
        mockMvc.perform(get("/customer/form/1"))
        .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    @DisplayName("Admin user can access Customer form page (Edit Customer)")
    public void testCustomerEditFormAsAdmin() throws Exception{
        when(cService.getById(1)).thenReturn(fakeCustomer1);

        mockMvc.perform(get("/customer/form/1"))
        .andExpect(status().isOk())
        .andExpect(view().name("customer-form"))
        .andExpect(model().attributeExists("customer"))
        .andExpect(model().attribute("customer", fakeCustomer1))
        .andExpect(model().attribute("page", "customer"));
    }

    @Test
    @WithMockUser
    @DisplayName("Default user must not submit new Customers")
    public void testSubmitNewCustomerAsUser() throws Exception{
        mockMvc.perform(
            post("/customer/form")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "Customer without id")
        ).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    @DisplayName("Admin user can submit new Customers")
    public void testSubmitNewCustomerAsAdmin() throws Exception{
        mockMvc.perform(
            post("/customer/form")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "Customer without id")
        ).andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/customer/list"));

        verify(cService, times(1)).register(any(Customer.class));
    }

    @Test
    @WithMockUser
    @DisplayName("Default user must not edit Customers")
    public void testSubmitEditCustomerAsUser() throws Exception{
        mockMvc.perform(
            post("/customer/form")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "1")
                .param("name", "Customer without id")
        ).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    @DisplayName("Admin user can submit a edit on Customer")
    public void testSubmitEditCustomerAsAdmin() throws Exception{
        when(cService.getById(1)).thenReturn(fakeCustomer1);

        mockMvc.perform(
            post("/customer/form")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "1")
                .param("name", "Customer without id")
        ).andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/customer/list"));

        verify(cService, times(1)).update(eq(1), any(Customer.class));
    }

    @Test
    @WithMockUser
    @DisplayName("Default user must not delete Customers")
    public void testDeleteCustomerAsUser() throws Exception{
        mockMvc.perform(get("/customer/delete/1"))
        .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    @DisplayName("Admin user can delete Customers")
    public void testDeleteCustomerAsAdmin() throws Exception{
        mockMvc.perform(get("/customer/delete/1"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/customer/list"));

        verify(cService, times(1)).delete(eq(1));
    }
}
