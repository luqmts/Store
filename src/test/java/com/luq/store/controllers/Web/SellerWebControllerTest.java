package com.luq.store.controllers.Web;

import com.luq.store.domain.Department;
import com.luq.store.domain.Seller;
import com.luq.store.infra.security.SecurityConfig;
import com.luq.store.repositories.UserRepository;
import com.luq.store.services.SellerService;
import com.luq.store.services.TokenService;
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

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SellerWebController.class)
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
public class SellerWebControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SellerService sService;

    @MockBean
    private TokenService tService;

    @MockBean
    private UserRepository uRepository;

    private Seller fakeSeller1, fakeSeller2;

    @BeforeEach
    public void setUp(){
        String user = "Jimmy McGill";
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

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
    }

    @Test
    @WithMockUser
    @DisplayName("Test if all Sellers are being returned on getAllSorted method with no filters applied and default user")
    public void testListAllSellers() throws Exception{
        when(sService.getAllSorted("id", "asc", null, null, null, null)).thenReturn(List.of(fakeSeller1, fakeSeller2));

        mockMvc.perform(
            get("/seller/list")
                .param("sortBy", "id")
                .param("direction", "asc")
        ).andExpect(status().isOk())
        .andExpect(view().name("seller-list"))
        .andExpect(model().attributeExists("sellers"))
        .andExpect(model().attribute("sellers", List.of(fakeSeller1, fakeSeller2)))
        .andExpect(model().attribute("page", "seller"))
        .andExpect(model().attribute("sortBy", "id"))
        .andExpect(model().attribute("direction", "asc"))
        .andExpect(model().attribute("departments", Department.values()));
    }

    @Test
    @WithMockUser
    @DisplayName("Test if Seller is being returned on getAllSorted method with filters applied and default user")
    public void testListSellersWithOneFilter() throws Exception{
        when(sService.getAllSorted("id", "asc", null, "Walter White", null, null)).thenReturn(List.of(fakeSeller1));

        mockMvc.perform(
            get("/seller/list")
                .param("sortBy", "id")
                .param("direction", "asc")
                .param("name", "Walter White")
        ).andExpect(status().isOk())
        .andExpect(view().name("seller-list"))
        .andExpect(model().attributeExists("sellers"))
        .andExpect(model().attribute("sellers", List.of(fakeSeller1)))
        .andExpect(model().attribute("page", "seller"))
        .andExpect(model().attribute("sortBy", "id"))
        .andExpect(model().attribute("direction", "asc"))
        .andExpect(model().attribute("name", "Walter White"))
        .andExpect(model().attribute("departments", Department.values()));
    }

    @Test
    @WithMockUser
    @DisplayName("Test if Seller is being returned on getAllSorted method with all filters applied and default user")
    public void testListSellersWithAllFilters() throws Exception{
        when(sService.getAllSorted(
            "id", "asc", "FOOD","Walter White","WalterWhite@Cooking.com", "11901010101")
        ).thenReturn(List.of(fakeSeller1));

        mockMvc.perform(
            get("/seller/list")
                .param("sortBy", "id")
                .param("direction", "asc")
                .param("supplier.id", "1")
                .param("name", "Walter White")
                .param("mail", "WalterWhite@Cooking.com")
                .param("phone", "11901010101")
                .param("department", "FOOD")
        ).andExpect(status().isOk())
        .andExpect(view().name("seller-list"))
        .andExpect(model().attributeExists("sellers"))
        .andExpect(model().attribute("sellers", List.of(fakeSeller1)))
        .andExpect(model().attribute("page", "seller"))
        .andExpect(model().attribute("sortBy", "id"))
        .andExpect(model().attribute("direction", "asc"))
        .andExpect(model().attribute("name", "Walter White"))
        .andExpect(model().attribute("mail", "WalterWhite@Cooking.com"))
        .andExpect(model().attribute("phone", "11901010101"))
        .andExpect(model().attribute("selectedDepartment", "FOOD"))
        .andExpect(model().attribute("departments", Department.values()));
    }

    @Test
    @WithMockUser
    @DisplayName("Test if no Seller is being returned on getAllSorted method correctly as a default user")
    public void testListWithNoSellers() throws Exception{
        when(sService.getAllSorted("id", "asc", null, "Saul Goodman", null, null)).thenReturn(List.of());

        mockMvc.perform(
            get("/seller/list")
                .param("sortBy", "id")
                .param("direction", "asc")
                .param("name", "Saul Goodman")
        ).andExpect(status().isOk())
        .andExpect(view().name("seller-list"))
        .andExpect(model().attributeExists("sellers"))
        .andExpect(model().attribute("sellers", List.of()))
        .andExpect(model().attribute("page", "seller"))
        .andExpect(model().attribute("sortBy", "id"))
        .andExpect(model().attribute("direction", "asc"))
        .andExpect(model().attribute("name", "Saul Goodman"))
        .andExpect(model().attribute("departments", Department.values()));
    }

    @Test
    @WithMockUser
    @DisplayName("Default user must not access Seller form page (New Seller)")
    public void testSellerFormAsDefaultUser() throws Exception{
        mockMvc.perform(get("/seller/form"))
        .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    @DisplayName("Admin user can access Seller form page (New Seller)")
    public void testSellerFormAsAdmin() throws Exception{
        mockMvc.perform(get("/seller/form"))
        .andExpect(status().isOk())
        .andExpect(view().name("seller-form"))
        .andExpect(model().attributeExists("seller"))
        .andExpect(model().attribute("departments", Department.values()))
        .andExpect(model().attribute("page", "seller"));
    }

    @Test
    @WithMockUser
    @DisplayName("Default user must not access Seller form page (Edit Seller)")
    public void testSellerEditFormAsDefaultUser() throws Exception{
        mockMvc.perform(get("/seller/form/1"))
        .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    @DisplayName("Admin user can access Seller form page (Edit Seller)")
    public void testSellerEditFormAsAdmin() throws Exception{
        when(sService.getById(1)).thenReturn(fakeSeller1);

        mockMvc.perform(get("/seller/form/1"))
        .andExpect(status().isOk())
        .andExpect(view().name("seller-form"))
        .andExpect(model().attributeExists("seller"))
        .andExpect(model().attribute("seller", fakeSeller1))
        .andExpect(model().attribute("departments", Department.values()))
        .andExpect(model().attribute("page", "seller"));
    }

    @Test
    @WithMockUser
    @DisplayName("Default user must not submit new Sellers")
    public void testSubmitNewSellerAsUser() throws Exception{
        mockMvc.perform(
            post("/seller/form")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "New Seller")
                .param("mail", "newseller@mail.com")
                .param("phone", "85945454545")
                .param("department", "TECHNOLOGY")
        ).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    @DisplayName("Admin user can submit new Sellers")
    public void testSubmitNewSellerAsAdmin() throws Exception{
        mockMvc.perform(
            post("/seller/form")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "New Seller")
                .param("mail", "newseller@mail.com")
                .param("phone", "85945454545")
                .param("department", "TECHNOLOGY")
        ).andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/seller/list"));

        verify(sService, times(1)).register(any(Seller.class));
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    @DisplayName("Sellers with invalid mail must not be registered and returned a error on form page")
    public void testSubmitNewSellerAsAdminWithInvalidMail() throws Exception{
        mockMvc.perform(
            post("/seller/form")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "New Seller")
                .param("mail", "newseller")
                .param("phone", "85945454545")
                .param("department", "TECHNOLOGY")
        ).andExpect(status().isOk())
        .andExpect(view().name("seller-form"))
        .andExpect(model().attributeExists("seller"))
        .andExpect(model().attribute("departments", Department.values()))
        .andExpect(model().attribute("mailError", "Invalid mail"));

        verify(sService, times(0)).register(any(Seller.class));
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    @DisplayName("Sellers with invalid phone must not be registered and returned a error on form page")
    public void testSubmitNewSellerAsAdminWithInvalidPhone() throws Exception{
        mockMvc.perform(
            post("/seller/form")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "New Seller")
                .param("mail", "newseller@mail.com")
                .param("phone", "123")
                .param("department", "TECHNOLOGY")
        ).andExpect(status().isOk())
        .andExpect(view().name("seller-form"))
        .andExpect(model().attributeExists("seller"))
        .andExpect(model().attribute("departments", Department.values()))
        .andExpect(model().attribute("phoneError", "Invalid phone"));

        verify(sService, times(0)).register(any(Seller.class));
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    @DisplayName("Sellers with invalid mail and phone must not be registered and returned a error on form page")
    public void testSubmitNewSellerAsAdminWithInvalidMailAndPhone() throws Exception{
        mockMvc.perform(
            post("/seller/form")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "New Seller")
                .param("mail", "newseller")
                .param("phone", "123")
                .param("department", "TECHNOLOGY")
        ).andExpect(status().isOk())
        .andExpect(view().name("seller-form"))
        .andExpect(model().attributeExists("seller"))
        .andExpect(model().attribute("departments", Department.values()))
        .andExpect(model().attribute("phoneError", "Invalid phone"))
        .andExpect(model().attribute("mailError", "Invalid mail"));

        verify(sService, times(0)).register(any(Seller.class));
    }

    @Test
    @WithMockUser
    @DisplayName("Default user must not edit Sellers")
    public void testSubmitEditSellerAsUser() throws Exception{
        mockMvc.perform(
            post("/seller/form")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "1")
                .param("name", "Seller")
                .param("mail", "newseller@mail.com")
                .param("phone", "85945454545")
                .param("department", "TECHNOLOGY")
        ).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    @DisplayName("Admin user can submit a edit on Seller")
    public void testSubmitEditSellerAsAdmin() throws Exception{
        when(sService.getById(1)).thenReturn(fakeSeller1);

        mockMvc.perform(
            post("/seller/form")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "1")
                .param("name", "Seller")
                .param("mail", "newseller@mail.com")
                .param("phone", "85945454545")
                .param("department", "TECHNOLOGY")
        ).andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/seller/list"));

        verify(sService, times(1)).update(eq(1), any(Seller.class));
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    @DisplayName("Sellers with invalid mail must not be edited and returned a error on form page")
    public void testSubmitEditSellerAsAdminWithInvalidMail() throws Exception{
        when(sService.getById(1)).thenReturn(fakeSeller1);

        mockMvc.perform(
            post("/seller/form")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "1")
                .param("name", "Seller")
                .param("mail", "seller")
                .param("phone", "85945454545")
                .param("department", "TECHNOLOGY")
        ).andExpect(status().isOk())
        .andExpect(view().name("seller-form"))
        .andExpect(model().attributeExists("seller"))
        .andExpect(model().attribute("departments", Department.values()))
        .andExpect(model().attribute("mailError", "Invalid mail"));

        verify(sService, times(0)).update(eq(1), any(Seller.class));
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    @DisplayName("Sellers with invalid phone must not be edited and returned a error on form page")
    public void testSubmitEditSellerAsAdminWithInvalidPhone() throws Exception{
        when(sService.getById(1)).thenReturn(fakeSeller1);

        mockMvc.perform(
            post("/seller/form")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "1")
                .param("name", "Seller")
                .param("mail", "seller@mail.com")
                .param("phone", "12345")
                .param("department", "TECHNOLOGY")
        ).andExpect(status().isOk())
        .andExpect(view().name("seller-form"))
        .andExpect(model().attributeExists("seller"))
        .andExpect(model().attribute("departments", Department.values()))
        .andExpect(model().attribute("phoneError", "Invalid phone"));

        verify(sService, times(0)).update(eq(1), any(Seller.class));
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    @DisplayName("Sellers with invalid mail and phone must not be edited and returned a error on form page")
    public void testSubmitEditSellerAsAdminWithInvalidMailAndPhone() throws Exception{
        when(sService.getById(1)).thenReturn(fakeSeller1);

        mockMvc.perform(
            post("/seller/form")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "1")
                .param("name", "Seller")
                .param("mail", "seller")
                .param("phone", "12345")
                .param("department", "TECHNOLOGY")
        ).andExpect(status().isOk())
        .andExpect(view().name("seller-form"))
        .andExpect(model().attributeExists("seller"))
        .andExpect(model().attribute("departments", Department.values()))
        .andExpect(model().attribute("phoneError", "Invalid phone"))
        .andExpect(model().attribute("mailError", "Invalid mail"));

        verify(sService, times(0)).update(eq(1), any(Seller.class));
    }

    @Test
    @WithMockUser
    @DisplayName("Default user must not delete Sellers")
    public void testDeleteSellerAsUser() throws Exception{
        mockMvc.perform(get("/seller/delete/1"))
        .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    @DisplayName("Admin user can delete Sellers")
    public void testDeleteSellerAsAdmin() throws Exception{
        mockMvc.perform(get("/seller/delete/1"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/seller/list"));

        verify(sService, times(1)).delete(eq(1));
    }
}
