package com.luq.store.controllers.Web;

import com.luq.store.domain.Supplier;
import com.luq.store.dto.request.supplier.SupplierRegisterDTO;
import com.luq.store.dto.request.supplier.SupplierUpdateDTO;
import com.luq.store.dto.response.supplier.SupplierResponseDTO;
import com.luq.store.infra.security.SecurityConfig;
import com.luq.store.repositories.UserRepository;
import com.luq.store.services.SupplierService;
import com.luq.store.services.TokenService;
import com.luq.store.valueobjects.Cnpj;
import com.luq.store.valueobjects.Mail;
import com.luq.store.valueobjects.Phone;
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

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SupplierWebController.class)
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
public class SupplierWebControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SupplierService sService;

    @MockBean
    private TokenService tService;

    @MockBean
    private UserRepository uRepository;

    private SupplierResponseDTO fakeSupplier1Response, fakeSupplier2Response;
    private SupplierRegisterDTO fakeSupplierRegister;
    private SupplierUpdateDTO fakeSupplierUpdate;

    @BeforeEach
    public void setUp(){
        String user = "Jimmy McGill";
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

        fakeSupplier1Response = new SupplierResponseDTO(
            1, "Microsoft Brasil LTDA.", new Cnpj("43.447.044/0004-10"),
            new Mail("microsoft@mail.com"), new Phone("11000001111"),
            user, now, user, now
        );
        fakeSupplier2Response = new SupplierResponseDTO(
            1, "Sony Brasil LTDA.", new Cnpj("04.542.534/0001-09"),
            new Mail("sony@mail.com"), new Phone("11222225555"),
            user, now, user, now
        );

        fakeSupplierRegister = new SupplierRegisterDTO(
            "Microsoft Brasil LTDA.", new Cnpj("43.447.044/0004-10"),
            new Mail("microsoft@mail.com"), new Phone("11000001111")
        );

        fakeSupplierUpdate = new SupplierUpdateDTO(
            "Sony Brasil LTDA.", new Cnpj("04.542.534/0001-09"),
            new Mail("sony@mail.com"), new Phone("11222225555")
        );
    }

    @Test
    @WithMockUser
    @DisplayName("Test if all Suppliers are being returned on getAllSorted method with no filters applied and default user")
    public void testListAllSuppliers() throws Exception{
        when(sService.getAllSorted("name", "asc", null, null, null, null))
            .thenReturn(List.of(fakeSupplier1Response, fakeSupplier2Response));

        mockMvc.perform(
            get("/supplier/list")
                .param("sortBy", "name")
                .param("direction", "asc")
        ).andExpect(status().isOk())
        .andExpect(view().name("supplier-list"))
        .andExpect(model().attributeExists("suppliers"))
        .andExpect(model().attribute("suppliers", List.of(fakeSupplier1Response, fakeSupplier2Response)))
        .andExpect(model().attribute("page", "supplier"))
        .andExpect(model().attribute("sortBy", "name"))
        .andExpect(model().attribute("direction", "asc"));
    }

    @Test
    @WithMockUser
    @DisplayName("Test if Supplier is being returned on getAllSorted method with filters applied and default user")
    public void testListSuppliersWithOneFilter() throws Exception{
        when(sService.getAllSorted("name", "asc", "Microsoft Brasil LTDA.",  null, null, null))
            .thenReturn(List.of(fakeSupplier1Response));

        mockMvc.perform(
            get("/supplier/list")
                .param("sortBy", "name")
                .param("direction", "asc")
                .param("name", "Microsoft Brasil LTDA.")
        ).andExpect(status().isOk())
        .andExpect(view().name("supplier-list"))
        .andExpect(model().attributeExists("suppliers"))
        .andExpect(model().attribute("suppliers", List.of(fakeSupplier1Response)))
        .andExpect(model().attribute("page", "supplier"))
        .andExpect(model().attribute("sortBy", "name"))
        .andExpect(model().attribute("direction", "asc"))
        .andExpect(model().attribute("name", "Microsoft Brasil LTDA."));
    }

    @Test
    @WithMockUser
    @DisplayName("Test if Supplier is being returned on getAllSorted method with all filters applied and default user")
    public void testListSuppliersWithAllFilters() throws Exception{
        when(sService.getAllSorted(
            "name", "asc", "Microsoft Brasil LTDA.",
            "43.447.044/0004-10","microsoft@mail.com", "11000001111"
        )).thenReturn(List.of(fakeSupplier1Response));

        mockMvc.perform(
            get("/supplier/list")
                .param("sortBy", "name")
                .param("direction", "asc")
                .param("supplier.id", "1")
                .param("name", "Microsoft Brasil LTDA.")
                .param("cnpj", "43.447.044/0004-10")
                .param("mail", "microsoft@mail.com")
                .param("phone", "11000001111")
        ).andExpect(status().isOk())
        .andExpect(view().name("supplier-list"))
        .andExpect(model().attributeExists("suppliers"))
        .andExpect(model().attribute("suppliers", List.of(fakeSupplier1Response)))
        .andExpect(model().attribute("page", "supplier"))
        .andExpect(model().attribute("sortBy", "name"))
        .andExpect(model().attribute("direction", "asc"))
        .andExpect(model().attribute("name", "Microsoft Brasil LTDA."))
        .andExpect(model().attribute("cnpj", "43.447.044/0004-10"))
        .andExpect(model().attribute("mail", "microsoft@mail.com"))
        .andExpect(model().attribute("phone", "11000001111"));
    }

    @Test
    @WithMockUser
    @DisplayName("Test if no Supplier is being returned on getAllSorted method correctly as a default user")
    public void testListWithNoSuppliers() throws Exception{
        when(sService.getAllSorted("name", "asc", null, "Nintendo", null, null))
            .thenReturn(List.of());

        mockMvc.perform(
            get("/supplier/list")
                .param("sortBy", "name")
                .param("direction", "asc")
                .param("name", "Nintendo")
        ).andExpect(status().isOk())
        .andExpect(view().name("supplier-list"))
        .andExpect(model().attributeExists("suppliers"))
        .andExpect(model().attribute("suppliers", List.of()))
        .andExpect(model().attribute("page", "supplier"))
        .andExpect(model().attribute("sortBy", "name"))
        .andExpect(model().attribute("direction", "asc"))
        .andExpect(model().attribute("name", "Nintendo"));
    }

    @Test
    @WithMockUser
    @DisplayName("Default user must not access Supplier form page (New Supplier)")
    public void testSupplierFormAsDefaultUser() throws Exception{
        mockMvc.perform(get("/supplier/form"))
        .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    @DisplayName("Admin user can access Supplier form page (New Supplier)")
    public void testSupplierFormAsAdmin() throws Exception{
        mockMvc.perform(get("/supplier/form"))
        .andExpect(status().isOk())
        .andExpect(view().name("supplier-form"))
        .andExpect(model().attributeExists("supplier"))
        .andExpect(model().attribute("page", "supplier"));
    }

    @Test
    @WithMockUser
    @DisplayName("Default user must not access Supplier form page (Edit Supplier)")
    public void testSupplierEditFormAsDefaultUser() throws Exception{
        mockMvc.perform(get("/supplier/form/1"))
        .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    @DisplayName("Admin user can access Supplier form page (Edit Supplier)")
    public void testSupplierEditFormAsAdmin() throws Exception{
        when(sService.getById(1)).thenReturn(fakeSupplier1Response);

        mockMvc.perform(get("/supplier/form/1"))
        .andExpect(status().isOk())
        .andExpect(view().name("supplier-form"))
        .andExpect(model().attributeExists("supplier"))
        .andExpect(model().attribute("supplier", fakeSupplier1Response))
        .andExpect(model().attribute("page", "supplier"));
    }

    @Test
    @WithMockUser
    @DisplayName("Default user must not submit new Suppliers")
    public void testSubmitNewSupplierAsUser() throws Exception{
        mockMvc.perform(
            post("/supplier/form")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", fakeSupplierRegister.name())
                .param("cnpj", fakeSupplierRegister.cnpj().getValue())
                .param("mail", fakeSupplierRegister.mail().getValue())
                .param("phone", fakeSupplierRegister.phone().getValue())
        ).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    @DisplayName("Admin user can submit new Suppliers")
    public void testSubmitNewSupplierAsAdmin() throws Exception{
        mockMvc.perform(
            post("/supplier/form")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", fakeSupplierRegister.name())
                .param("cnpj", fakeSupplierRegister.cnpj().getValue())
                .param("mail", fakeSupplierRegister.mail().getValue())
                .param("phone", fakeSupplierRegister.phone().getValue())
        ).andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/supplier/list"));

        verify(sService, times(1)).register(fakeSupplierRegister);
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    @DisplayName("Suppliers with invalid cnpj must not be registered and returned a error on form page")
    public void testSubmitNewSupplierAsAdminWithInvalidCnpj() throws Exception{
        mockMvc.perform(
            post("/supplier/form")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", fakeSupplierRegister.name())
                .param("cnpj", "1235435")
                .param("mail", fakeSupplierRegister.mail().getValue())
                .param("phone", fakeSupplierRegister.phone().getValue())
        ).andExpect(status().isOk())
        .andExpect(view().name("supplier-form"))
        .andExpect(model().attributeExists("supplier"))
        .andExpect(model().attribute("cnpjError", "Invalid cnpj"));

        verify(sService, times(0)).register(fakeSupplierRegister);
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    @DisplayName("Suppliers with invalid mail must not be registered and returned a error on form page")
    public void testSubmitNewSupplierAsAdminWithInvalidMail() throws Exception{
        mockMvc.perform(
            post("/supplier/form")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", fakeSupplierRegister.name())
                .param("cnpj", fakeSupplierRegister.cnpj().getValue())
                .param("mail", "invalidmail")
                .param("phone", fakeSupplierRegister.phone().getValue())
        ).andExpect(status().isOk())
        .andExpect(view().name("supplier-form"))
        .andExpect(model().attributeExists("supplier"))
        .andExpect(model().attribute("mailError", "Invalid mail"));

        verify(sService, times(0)).register(fakeSupplierRegister);
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    @DisplayName("Suppliers with invalid phone must not be registered and returned a error on form page")
    public void testSubmitNewSupplierAsAdminWithInvalidPhone() throws Exception{
        mockMvc.perform(
            post("/supplier/form")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", fakeSupplierRegister.name())
                .param("cnpj", fakeSupplierRegister.cnpj().getValue())
                .param("mail", fakeSupplierRegister.mail().getValue())
                .param("phone", "123")
        ).andExpect(status().isOk())
        .andExpect(view().name("supplier-form"))
        .andExpect(model().attributeExists("supplier"))
        .andExpect(model().attribute("phoneError", "Invalid phone"));

        verify(sService, times(0)).register(fakeSupplierRegister);
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    @DisplayName("Suppliers with invalid cnpj, mail and phone must not be registered and returned a error on form page")
    public void testSubmitNewSupplierAsAdminWithInvalidCnpjMailAndPhone() throws Exception{
        mockMvc.perform(
            post("/supplier/form")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", fakeSupplierRegister.name())
                .param("cnpj", "1235435")
                .param("mail", "invalidmail")
                .param("phone", "123")
        ).andExpect(status().isOk())
        .andExpect(view().name("supplier-form"))
        .andExpect(model().attributeExists("supplier"))
        .andExpect(model().attribute("cnpjError", "Invalid cnpj"))
        .andExpect(model().attribute("mailError", "Invalid mail"))
        .andExpect(model().attribute("phoneError", "Invalid phone"));

        verify(sService, times(0)).register(fakeSupplierRegister);
    }

    @Test
    @WithMockUser
    @DisplayName("Default user must not edit Suppliers")
    public void testSubmitEditSupplierAsUser() throws Exception{
        mockMvc.perform(
            post("/supplier/form")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "1")
                .param("name", fakeSupplierUpdate.name())
                .param("cnpj", fakeSupplierUpdate.cnpj().getValue())
                .param("mail", fakeSupplierUpdate.mail().getValue())
                .param("phone", fakeSupplierUpdate.phone().getValue())
        ).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    @DisplayName("Admin user can submit a edit on Supplier")
    public void testSubmitEditSupplierAsAdmin() throws Exception{
        when(sService.getById(1)).thenReturn(fakeSupplier1Response);

        mockMvc.perform(
            post("/supplier/form")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "1")
                .param("name", fakeSupplierUpdate.name())
                .param("cnpj", fakeSupplierUpdate.cnpj().getValue())
                .param("mail", fakeSupplierUpdate.mail().getValue())
                .param("phone", fakeSupplierUpdate.phone().getValue())
        ).andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/supplier/list"));

        verify(sService, times(1)).update(eq(1), fakeSupplierUpdate);
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    @DisplayName("Suppliers with invalid cnpj must not be edited and returned a error on form page")
    public void testSubmitEditSupplierAsAdminWithInvalidCnpj() throws Exception{
        when(sService.getById(1)).thenReturn(fakeSupplier1Response);

        mockMvc.perform(
            post("/supplier/form")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "1")
                .param("name", fakeSupplierUpdate.name())
                .param("cnpj", "1235435")
                .param("mail", fakeSupplierUpdate.mail().getValue())
                .param("phone", fakeSupplierUpdate.phone().getValue())
        ).andExpect(status().isOk())
        .andExpect(view().name("supplier-form"))
        .andExpect(model().attributeExists("supplier"))
        .andExpect(model().attribute("cnpjError", "Invalid cnpj"));

        verify(sService, times(0)).update(eq(1), fakeSupplierUpdate);
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    @DisplayName("Suppliers with invalid mail must not be edited and returned a error on form page")
    public void testSubmitEditSupplierAsAdminWithInvalidMail() throws Exception{
        when(sService.getById(1)).thenReturn(fakeSupplier1Response);

        mockMvc.perform(
            post("/supplier/form")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "1")
                .param("name", fakeSupplierUpdate.name())
                .param("cnpj", fakeSupplierUpdate.cnpj().getValue())
                .param("mail", "invalidmail")
                .param("phone", fakeSupplierUpdate.phone().getValue())
        ).andExpect(status().isOk())
        .andExpect(view().name("supplier-form"))
        .andExpect(model().attributeExists("supplier"))
        .andExpect(model().attribute("cnpjError", "Invalid cnpj"));

        verify(sService, times(0)).update(eq(1), fakeSupplierUpdate);
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    @DisplayName("Suppliers with invalid phone must not be edited and returned a error on form page")
    public void testSubmitEditSupplierAsAdminWithInvalidPhone() throws Exception{
        when(sService.getById(1)).thenReturn(fakeSupplier1Response);

        mockMvc.perform(
            post("/supplier/form")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "1")
                .param("name", fakeSupplierUpdate.name())
                .param("cnpj", fakeSupplierUpdate.cnpj().getValue())
                .param("mail", fakeSupplierUpdate.mail().getValue())
                .param("phone", "123")
        ).andExpect(status().isOk())
        .andExpect(view().name("supplier-form"))
        .andExpect(model().attributeExists("supplier"))
        .andExpect(model().attribute("phoneError", "Invalid phone"));

        verify(sService, times(0)).update(eq(1), fakeSupplierUpdate);
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    @DisplayName("Suppliers with invalid cnpj, mail and phone must not be edited and returned a error on form page")
    public void testSubmitEditSupplierAsAdminWithInvalidCnpjMailAndPhone() throws Exception{
        when(sService.getById(1)).thenReturn(fakeSupplier1Response);

        mockMvc.perform(
            post("/supplier/form")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "1")
                .param("name", fakeSupplierUpdate.name())
                .param("cnpj", "1235435")
                .param("mail", "invalidmail")
                .param("phone", "123")
        ).andExpect(status().isOk())
        .andExpect(view().name("supplier-form"))
        .andExpect(model().attributeExists("supplier"))
        .andExpect(model().attribute("cnpjError", "Invalid cnpj"))
        .andExpect(model().attribute("phoneError", "Invalid phone"))
        .andExpect(model().attribute("mailError", "Invalid mail"));

        verify(sService, times(0)).update(eq(1), fakeSupplierUpdate);
    }

    @Test
    @WithMockUser
    @DisplayName("Default user must not delete Suppliers")
    public void testDeleteSupplierAsUser() throws Exception{
        mockMvc.perform(get("/supplier/delete/1"))
        .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    @DisplayName("Admin user can delete Suppliers")
    public void testDeleteSupplierAsAdmin() throws Exception{
        mockMvc.perform(get("/supplier/delete/1"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/supplier/list"));

        verify(sService, times(1)).delete(eq(1));
    }
}
