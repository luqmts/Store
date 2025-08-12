package com.luq.store.controllers.Web;

import com.luq.store.domain.*;
import com.luq.store.dto.request.supply.SupplyRegisterDTO;
import com.luq.store.dto.request.supply.SupplyUpdateDTO;
import com.luq.store.dto.response.supply.SupplyResponseDTO;
import com.luq.store.infra.security.SecurityConfig;
import com.luq.store.mapper.ProductMapper;
import com.luq.store.repositories.UserRepository;
import com.luq.store.services.*;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SupplyWebController.class)
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
public class SupplyWebControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SupplyService sService;
    @MockBean
    private ProductService pService;
    @MockBean
    private TokenService tService;

    @MockBean
    private UserRepository uRepository;

    @Autowired
    private ProductMapper pMapper;

    private SupplyResponseDTO fakeSupply1Response, fakeSupply2Response;
    private SupplyRegisterDTO fakeSupplyRegister;
    private SupplyUpdateDTO fakeSupplyUpdate;
    private Product fakeProduct1, fakeProduct2;

    @BeforeEach
    public void setUp(){
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
            BigDecimal.valueOf(200.00), fakeSupplier1, user, now, user, now
        );
        fakeProduct2 = new Product(
            2, "Playstation 5 Controller", "PS5Cont", "Controller for Playstation 5 Console",
            BigDecimal.valueOf(250.00), fakeSupplier2, user, now, user, now
        );

        fakeSupply1Response = new SupplyResponseDTO(1, 50, fakeProduct1, user, now, user, now );
        fakeSupply2Response = new SupplyResponseDTO(1, 30, fakeProduct2, user, now, user, now );

        fakeSupplyRegister = new SupplyRegisterDTO(50, fakeProduct1.getId());

        fakeSupplyUpdate = new SupplyUpdateDTO(30, fakeProduct2.getId());
    }

    @Test
    @WithMockUser
    @DisplayName("Test if all Supply are being returned on getAllSorted method with no filters applied and default user")
    public void testListAllSupply() throws Exception{
        when(sService.getAllSorted("productId", "asc", null))
            .thenReturn(List.of(fakeSupply1Response, fakeSupply2Response));
        when(pService.getAll()).thenReturn(List.of(pMapper.toDTO(fakeProduct1), pMapper.toDTO(fakeProduct2)));

        mockMvc.perform(
            get("/supply/list")
                .param("sortBy", "productId")
                .param("direction", "asc")
        ).andExpect(status().isOk())
        .andExpect(view().name("supply-list"))
        .andExpect(model().attributeExists("supply"))
        .andExpect(model().attribute("supply", List.of(fakeSupply1Response, fakeSupply2Response)))
        .andExpect(model().attribute("page", "supply"))
        .andExpect(model().attribute("sortBy", "productId"))
        .andExpect(model().attribute("direction", "asc"))
        .andExpect(model().attribute("products", List.of(fakeProduct1, fakeProduct2)));
    }

    @Test
    @WithMockUser
    @DisplayName("Test if Supply is being returned on getAllSorted method with one filter applied and default user")
    public void testListSupplyWithOneFilter() throws Exception{
        when(sService.getAllSorted("productId", "asc", 1)).thenReturn(List.of(fakeSupply1Response));
        when(pService.getAll()).thenReturn(List.of(pMapper.toDTO(fakeProduct1), pMapper.toDTO(fakeProduct2)));

        mockMvc.perform(
            get("/supply/list")
                .param("sortBy", "productId")
                .param("direction", "asc")
                .param("product.id", "1")
        ).andExpect(status().isOk())
        .andExpect(view().name("supply-list"))
        .andExpect(model().attributeExists("supply"))
        .andExpect(model().attribute("supply", List.of(fakeSupply1Response)))
        .andExpect(model().attribute("page", "supply"))
        .andExpect(model().attribute("sortBy", "productId"))
        .andExpect(model().attribute("direction", "asc"))
        .andExpect(model().attribute("productId", 1))
        .andExpect(model().attribute("products", List.of(fakeProduct1, fakeProduct2)));
    }

    @Test
    @WithMockUser
    @DisplayName("Test if no Supply is being returned on getAllSorted method correctly as a default user")
    public void testListWithNoSupply() throws Exception{
        when(sService.getAllSorted("productId", "asc", 5)).thenReturn(List.of());
        when(pService.getAll()).thenReturn(List.of(pMapper.toDTO(fakeProduct1), pMapper.toDTO(fakeProduct2)));

        mockMvc.perform(
            get("/supply/list")
                .param("sortBy", "productId")
                .param("direction", "asc")
                .param("product.id", "5")
        ).andExpect(status().isOk())
        .andExpect(view().name("supply-list"))
        .andExpect(model().attributeExists("supply"))
        .andExpect(model().attribute("supply", List.of()))
        .andExpect(model().attribute("page", "supply"))
        .andExpect(model().attribute("sortBy", "productId"))
        .andExpect(model().attribute("direction", "asc"))
        .andExpect(model().attribute("productId", 5))
        .andExpect(model().attribute("products", List.of(pMapper.toDTO(fakeProduct1), pMapper.toDTO(fakeProduct2))));
    }

    @Test
    @WithMockUser
    @DisplayName("Default user must not access Supply form page (New Supply)")
    public void testSupplyFormAsDefaultUser() throws Exception{
        when(pService.getAllRegisteredOnSupply()).thenReturn(List.of(pMapper.toDTO(fakeProduct1), pMapper.toDTO(fakeProduct2)));

        mockMvc.perform(get("/supply/form"))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    @DisplayName("Admin user can access Supply form page (New Supply)")
    public void testSupplyFormAsAdmin() throws Exception{
        when(pService.getAllNotRegisteredOnSupply()).thenReturn(List.of(pMapper.toDTO(fakeProduct1), pMapper.toDTO(fakeProduct2)));

        mockMvc.perform(get("/supply/form"))
        .andExpect(status().isOk())
        .andExpect(view().name("supply-form"))
        .andExpect(model().attributeExists("supply"))
        .andExpect(model().attribute("page", "supply"))
        .andExpect(model().attribute("products", List.of(fakeProduct1, fakeProduct2)));
    }

    @Test
    @WithMockUser
    @DisplayName("Default user must not access Supply form page (Edit Supply)")
    public void testSupplyEditFormAsDefaultUser() throws Exception{
        when(sService.getById(1)).thenReturn(fakeSupply1Response);
        when(pService.getAllRegisteredOnSupply(1)).thenReturn(List.of(pMapper.toDTO(fakeProduct1)));

        mockMvc.perform(get("/supply/form/1"))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    @DisplayName("Admin user can access Supply form page (Edit Supply)")
    public void testSupplyEditFormAsAdmin() throws Exception{
        when(sService.getById(1)).thenReturn(fakeSupply1Response);
        when(pService.getAllNotRegisteredOnSupply(fakeSupply1Response.id())).thenReturn(List.of(pMapper.toDTO(fakeProduct1), pMapper.toDTO(fakeProduct2)));

        mockMvc.perform(get("/supply/form/1"))
        .andExpect(status().isOk())
        .andExpect(view().name("supply-form"))
        .andExpect(model().attributeExists("supply"))
        .andExpect(model().attribute("supply", fakeSupply1Response))
        .andExpect(model().attribute("page", "supply"))
        .andExpect(model().attribute("products", List.of(fakeProduct1, fakeProduct2)));
    }

    @Test
    @WithMockUser
    @DisplayName("Default user must not submit new Supply")
    public void testSubmitNewSupplyAsUser() throws Exception{
        mockMvc.perform(
            post("/supply/form")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("quantity", fakeSupplyRegister.quantity().toString())
                .param("product.id", fakeSupplyRegister.product_id().toString())
        ).andExpect(status().isForbidden());


        verify(sService, times(0)).register(fakeSupplyRegister);
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    @DisplayName("Admin user can submit new Supply")
    public void testSubmitNewSupplyAsAdmin() throws Exception{
        mockMvc.perform(
            post("/supply/form")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("quantity", fakeSupplyRegister.quantity().toString())
                .param("product.id", fakeSupplyRegister.product_id().toString())
        ).andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/supply/list"));

        verify(sService, times(1)).register(fakeSupplyRegister);
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    @DisplayName("If a product that is already registered is tried to be submitted, a error should be returned")
    public void testSubmitNewSupplyAsAdminWithProductAlreadyRegistered() throws Exception{
        when(sService.getByProductId(fakeProduct1.getId())).thenReturn(fakeSupply1Response);
        when(pService.getAll()).thenReturn(List.of(pMapper.toDTO(fakeProduct1), pMapper.toDTO(fakeProduct2)));

        mockMvc.perform(
            post("/supply/form")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("quantity", fakeSupplyRegister.quantity().toString())
                .param("product.id", "1")
        ).andExpect(status().isOk())
        .andExpect(model().attributeExists("supply"))
        .andExpect(model().attribute("page", "supply"))
        .andExpect(model().attribute("products", List.of(fakeProduct1, fakeProduct2)))
        .andExpect(model().attribute("productError", "This product is already registered on supply, please update it instead"))
        .andExpect(view().name("supply-form"));

        verify(sService, times(0)).register(fakeSupplyRegister);
    }

    @Test
    @WithMockUser
    @DisplayName("Default must not submit a edit in Supply")
    public void testSubmitEditSupplyAsUser() throws Exception{
        when(sService.getById(1)).thenReturn(fakeSupply1Response);

        mockMvc.perform(
            post("/supply/form")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "1")
                .param("quantity", fakeSupplyUpdate.quantity().toString())
                .param("product.id", fakeSupplyUpdate.product_id().toString())
        ).andExpect(status().isForbidden());

        verify(sService, times(0)).update(eq(1), fakeSupplyUpdate);
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    @DisplayName("Admin user can submit a edit on Supply")
    public void testSubmitEditSupplyAsAdmin() throws Exception{
        when(sService.getById(1)).thenReturn(fakeSupply1Response);

        mockMvc.perform(
            post("/supply/form")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "1")
                .param("quantity", fakeSupplyUpdate.quantity().toString())
                .param("product.id", fakeSupplyUpdate.product_id().toString())
        ).andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/supply/list"));

        verify(sService, times(1)).update(eq(1), fakeSupplyUpdate);
    }

    @Test
    @WithMockUser
    @DisplayName("Default user must not delete Supply")
    public void testDeleteSupplyAsUser() throws Exception{
        mockMvc.perform(get("/supply/delete/1"))
        .andExpect(status().isForbidden());

        verify(sService, times(0)).delete(eq(1));
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    @DisplayName("Admin user can delete Supply")
    public void testDeleteSupplyAsAdmin() throws Exception{
        mockMvc.perform(get("/supply/delete/1"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/supply/list"));

        verify(sService, times(1)).delete(eq(1));
    }
}
