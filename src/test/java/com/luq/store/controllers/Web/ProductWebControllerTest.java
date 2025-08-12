package com.luq.store.controllers.Web;

import com.luq.store.domain.Product;
import com.luq.store.domain.Supplier;
import com.luq.store.dto.request.product.ProductRegisterDTO;
import com.luq.store.dto.request.product.ProductUpdateDTO;
import com.luq.store.dto.response.product.ProductResponseDTO;
import com.luq.store.infra.security.SecurityConfig;
import com.luq.store.repositories.UserRepository;
import com.luq.store.services.ProductService;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductWebController.class)
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
public class ProductWebControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService pService;
    @MockBean
    private SupplierService sService;

    @MockBean
    private TokenService tService;

    @MockBean
    private UserRepository uRepository;

    private ProductResponseDTO fakeProduct1Response, fakeProduct2Response;
    private ProductRegisterDTO fakeProductRegister;
    private ProductUpdateDTO fakeProductUpdate;

    @BeforeEach
    public void setUp(){
        String user = "Jimmy McGill";
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

        Supplier fakeSupplier1 = new Supplier(
                1, "Sony Brasil LTDA.", new Cnpj("04.542.534/0001-09"),
                new Mail("sony@mail.com"), new Phone("11222225555"),
                user, now, user, now
        );
        Supplier fakeSupplier2 = new Supplier(
            2, "Microsoft Brasil LTDA.", new Cnpj("43.447.044/0004-10"),
            new Mail("microsoft@mail.com"), new Phone("11000001111"),
            user, now, user, now
        );


        fakeProduct1Response = new ProductResponseDTO(
            1, "Playstation 5 Controller", "PS5Cont", "Controller for Playstation 5 Console",
            BigDecimal.valueOf(250.00), fakeSupplier2, user, now, user, now
        );
        fakeProduct2Response = new ProductResponseDTO(
            1, "Xbox One Controller", "XOneCont", "Controller for Xbox One Console",
            BigDecimal.valueOf(200.00), fakeSupplier1, user, now, user, now
        );
        fakeProductRegister = new ProductRegisterDTO(
            "Playstation 5 Controller", "PS5Cont", "Controller for Playstation 5 Console",
            BigDecimal.valueOf(250.00), fakeSupplier1.getId()
        );
        fakeProductUpdate = new ProductUpdateDTO(
            "Xbox One Controller", "XOneCont", "Controller for Xbox One Console",
            BigDecimal.valueOf(200.00), fakeSupplier2.getId()
        );
    }

    @Test
    @WithMockUser
    @DisplayName("Test if all Products are being returned on getAllSorted method with no filters applied and default user")
    public void testListAllProducts() throws Exception{
        when(pService.getAllSorted("name", "asc", null, null, null))
            .thenReturn(List.of(fakeProduct1Response, fakeProduct2Response));

        mockMvc.perform(
            get("/product/list")
                .param("sortBy", "name")
                .param("direction", "asc")
        ).andExpect(status().isOk())
        .andExpect(view().name("product-list"))
        .andExpect(model().attributeExists("products"))
        .andExpect(model().attribute("products", List.of(fakeProduct1Response, fakeProduct2Response)))
        .andExpect(model().attribute("page", "product"))
        .andExpect(model().attribute("sortBy", "name"))
        .andExpect(model().attribute("direction", "asc"));
    }

    @Test
    @WithMockUser
    @DisplayName("Test if Product is being returned on getAllSorted method with filters applied and default user")
    public void testListProductsWithOneFilter() throws Exception{
        when(pService.getAllSorted("name", "asc", 1, null, null))
            .thenReturn(List.of(fakeProduct1Response));

        mockMvc.perform(
            get("/product/list")
                .param("sortBy", "name")
                .param("direction", "asc")
                .param("supplier.id", "1")
        ).andExpect(status().isOk())
        .andExpect(view().name("product-list"))
        .andExpect(model().attributeExists("products"))
        .andExpect(model().attribute("products", List.of(fakeProduct1Response)))
        .andExpect(model().attribute("page", "product"))
        .andExpect(model().attribute("sortBy", "name"))
        .andExpect(model().attribute("direction", "asc"))
        .andExpect(model().attribute("supplierId", 1));
    }

    @Test
    @WithMockUser
    @DisplayName("Test if Product is being returned on getAllSorted method with all filters applied and default user")
    public void testListProductsWithAllFilters() throws Exception{
        when(pService.getAllSorted("name", "asc", 1, "Playstation 5 Controller", "PS5Cont"))
            .thenReturn(List.of(fakeProduct1Response));
        when(pService.getAll()).thenReturn(List.of(fakeProduct1Response, fakeProduct2Response));

        mockMvc.perform(
            get("/product/list")
                .param("sortBy", "name")
                .param("direction", "asc")
                .param("supplier.id", "1")
                .param("name", "Playstation 5 Controller")
                .param("sku", "PS5Cont")
        ).andExpect(status().isOk())
        .andExpect(view().name("product-list"))
        .andExpect(model().attributeExists("products"))
        .andExpect(model().attribute("products", List.of(fakeProduct1Response)))
        .andExpect(model().attribute("page", "product"))
        .andExpect(model().attribute("sortBy", "name"))
        .andExpect(model().attribute("direction", "asc"))
        .andExpect(model().attribute("supplierId", 1))
        .andExpect(model().attribute("name", "Playstation 5 Controller"))
        .andExpect(model().attribute("sku", "PS5Cont"));
    }

    @Test
    @WithMockUser
    @DisplayName("Test if no Product is being returned on getAllSorted method correctly as a default user")
    public void testListWithNoProducts() throws Exception{
        when(pService.getAllSorted("name", "asc", 5, null, null)).thenReturn(List.of());

        mockMvc.perform(
            get("/product/list")
                .param("sortBy", "name")
                .param("direction", "asc")
                .param("supplier.id", "5")
        ).andExpect(status().isOk())
        .andExpect(view().name("product-list"))
        .andExpect(model().attributeExists("products"))
        .andExpect(model().attribute("products", List.of()))
        .andExpect(model().attribute("page", "product"))
        .andExpect(model().attribute("sortBy", "name"))
        .andExpect(model().attribute("direction", "asc"))
        .andExpect(model().attribute("supplierId", 5));
    }

    @Test
    @WithMockUser
    @DisplayName("Default user must not access Product form page (New Product)")
    public void testProductFormAsDefaultUser() throws Exception{
        mockMvc.perform(get("/product/form"))
        .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    @DisplayName("Admin user can access Product form page (New Product)")
    public void testProductFormAsAdmin() throws Exception{
        mockMvc.perform(get("/product/form"))
        .andExpect(status().isOk())
        .andExpect(view().name("product-form"))
        .andExpect(model().attributeExists("product"))
        .andExpect(model().attribute("page", "product"));
    }

    @Test
    @WithMockUser
    @DisplayName("Default user must not access Product form page (Edit Product)")
    public void testProductEditFormAsDefaultUser() throws Exception{
        mockMvc.perform(get("/product/form/1"))
        .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    @DisplayName("Admin user can access Product form page (Edit Product)")
    public void testProductEditFormAsAdmin() throws Exception{
        when(pService.getById(1)).thenReturn(fakeProduct1Response);

        mockMvc.perform(get("/product/form/1"))
        .andExpect(status().isOk())
        .andExpect(view().name("product-form"))
        .andExpect(model().attributeExists("product"))
        .andExpect(model().attribute("product", fakeProduct1Response))
        .andExpect(model().attribute("page", "product"));
    }

    @Test
    @WithMockUser
    @DisplayName("Default user must not submit new Products")
    public void testSubmitNewProductAsUser() throws Exception{
        mockMvc.perform(
            post("/product/form")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", fakeProductRegister.name())
                .param("sku", fakeProductRegister.sku())
                .param("description", fakeProductRegister.description())
                .param("price", fakeProductRegister.price().toString())
                .param("supplier.id", fakeProductRegister.supplier_id().toString())
        ).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    @DisplayName("Admin user can submit new Products")
    public void testSubmitNewProductAsAdmin() throws Exception{
        mockMvc.perform(
            post("/product/form")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", fakeProductRegister.name())
                .param("sku", fakeProductRegister.sku())
                .param("description", fakeProductRegister.description())
                .param("price", fakeProductRegister.price().toString())
                .param("supplier.id", fakeProductRegister.supplier_id().toString())
        ).andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/product/list"));

        verify(pService, times(1)).register(fakeProductRegister);
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    @DisplayName("Products with price less than 1 is not registered and a error will be returned")
    public void testSubmitNewProductAsAdminWithInvalidPrice() throws Exception{
        mockMvc.perform(
            post("/product/form")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", fakeProductRegister.name())
                .param("sku", fakeProductRegister.sku())
                .param("description", fakeProductRegister.description())
                .param("price", BigDecimal.valueOf(-1).toString())
                .param("supplier.id", fakeProductRegister.supplier_id().toString())
        ).andExpect(status().isOk())
        .andExpect(view().name("product-form"))
        .andExpect(model().attribute("priceError", "Product's price must be greater or equal than 1"));

        verify(pService, times(0)).register(fakeProductRegister);
    }

    @Test
    @WithMockUser
    @DisplayName("Default user must not edit Products")
    public void testSubmitEditProductAsUser() throws Exception{
        mockMvc.perform(
            post("/product/form")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "1")
                .param("name", fakeProductUpdate.name())
                .param("sku", fakeProductUpdate.sku())
                .param("description", fakeProductUpdate.description())
                .param("price", fakeProductUpdate.price().toString())
                .param("supplier.id", fakeProductUpdate.supplier_id().toString())
        ).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    @DisplayName("Admin user can submit a edit on Product")
    public void testSubmitEditProductAsAdmin() throws Exception{
        when(pService.getById(1)).thenReturn(fakeProduct1Response);

        mockMvc.perform(
            post("/product/form")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "1")
                .param("name", fakeProductUpdate.name())
                .param("sku", fakeProductUpdate.sku())
                .param("description", fakeProductUpdate.description())
                .param("price", fakeProductUpdate.price().toString())
                .param("supplier.id", fakeProductUpdate.supplier_id().toString())
        ).andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/product/list"));

        verify(pService, times(1)).update(eq(1), fakeProductUpdate);
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    @DisplayName("Products with price less than 1 is not edited and a error will be returned")
    public void testSubmitEditProductAsAdminWithInvalidPrice() throws Exception{
        mockMvc.perform(
            post("/product/form")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "1")
                .param("name", fakeProductRegister.name())
                .param("sku", fakeProductRegister.sku())
                .param("description", fakeProductRegister.description())
                .param("price", BigDecimal.valueOf(-1).toString())
                .param("supplier.id", fakeProductRegister.supplier_id().toString())
        ).andExpect(status().isOk())
        .andExpect(view().name("product-form"))
        .andExpect(model().attribute("priceError", "Product's price must be greater or equal than 1"));

        verify(pService, times(0)).update(1, fakeProductUpdate);
    }

    @Test
    @WithMockUser
    @DisplayName("Default user must not delete Products")
    public void testDeleteProductAsUser() throws Exception{
        mockMvc.perform(get("/product/delete/1"))
        .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    @DisplayName("Admin user can delete Products")
    public void testDeleteProductAsAdmin() throws Exception{
        mockMvc.perform(get("/product/delete/1"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/product/list"));

        verify(pService, times(1)).delete(eq(1));
    }
}
