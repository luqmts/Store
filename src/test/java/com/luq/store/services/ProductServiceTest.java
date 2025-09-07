package com.luq.store.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.luq.store.domain.Product;
import com.luq.store.dto.request.product.ProductRegisterDTO;
import com.luq.store.dto.request.product.ProductUpdateDTO;
import com.luq.store.dto.response.product.ProductResponseDTO;
import com.luq.store.dto.response.supplier.SupplierResponseDTO;
import com.luq.store.mapper.ProductMapper;
import com.luq.store.mapper.SupplierMapper;
import com.luq.store.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.luq.store.domain.Supplier;
import com.luq.store.valueobjects.Cnpj;
import com.luq.store.valueobjects.Mail;
import com.luq.store.valueobjects.Phone;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private ProductRepository pRepository;
    @InjectMocks
    private ProductService pService;
    @Mock
    private ProductMapper pMapper;

    @Mock
    private SupplierService sService;
    @Mock
    private SupplierMapper sMapper;

    private ProductResponseDTO fakeProduct1Response, fakeProduct2Response, result;
    private ProductRegisterDTO fakeProductRegister;
    private ProductUpdateDTO fakeProductUpdate;
    private Product fakeProduct1, fakeProduct2;
    private SupplierResponseDTO fakeSupplierResponse;
    private Supplier fakeSupplier2;

    Authentication authentication;
    SecurityContext securityContext;

    String user;
    LocalDateTime now;

    @BeforeEach
    public void setUp(){
        user = "Jimmy McGill";
        now = LocalDateTime.now();

        fakeSupplierResponse = new SupplierResponseDTO(
            2, "Sony Brasil LTDA.", "04.542.534/0001-09",
            "sony@mail.com", "11222225555",
            user, now, user, now
        );
        Supplier fakeSupplier1 = new Supplier(
                1, "Microsoft Brasil LTDA.", new Cnpj("43.447.044/0004-10"),
                new Mail("microsoft@mail.com"), new Phone("11000001111"),
                user, now, user, now
        );
        fakeSupplier2 = new Supplier(
                2, "Sony Brasil LTDA.", new Cnpj("04.542.534/0001-09"),
                new Mail("sony@mail.com"), new Phone("11222225555"),
                user, now, user, now
        );

        fakeProduct1Response = new ProductResponseDTO(
                1, "Xbox One Controller", "XOneCont", "Controller for Xbox One Console",
            BigDecimal.valueOf(200.00), fakeSupplier1, user, now, user, now
        );
        fakeProduct2Response = new ProductResponseDTO(
            1, "PS5 Controller", "PS5Cont", "Controller for PlayStation 5 Console",
            BigDecimal.valueOf(250.00), fakeSupplier2, user, now, user, now
        );
        fakeProductRegister = new ProductRegisterDTO(
            "Xbox One Controller", "XOneCont", "Controller for Xbox One Console",
            BigDecimal.valueOf(200.00), fakeSupplier1.getId()
        );
        fakeProductUpdate = new ProductUpdateDTO(
            "PS5 Controller", "PS5Cont", "Controller for PlayStation 5 Console",
            BigDecimal.valueOf(250.00), fakeSupplier2.getId()
        );
        fakeProduct1 = new Product(
            1, "XOneCont", "Xbox One Controller", "Controller for Xbox One Console",
            BigDecimal.valueOf(200.00), fakeSupplier1, user, now, user, now
        );
        fakeProduct2 = new Product(
            1, "PS5Cont", "PS5 Controller", "Controller for Playstation 5 Console",
            BigDecimal.valueOf(250.00), fakeSupplier2 , user, now, user, now
        );

        authentication = mock(Authentication.class);
        securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
    }
    
    @Test
    @DisplayName("Test if Product is being registered correctly")
    public void testRegisterProduct(){
        when(authentication.getName()).thenReturn(user);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(pRepository.save(fakeProduct1)).thenReturn(fakeProduct1);
        when(pMapper.toEntity(fakeProductRegister)).thenReturn(fakeProduct1);
        when(pMapper.toDTO(fakeProduct1)).thenReturn(fakeProduct1Response);

        result = pService.register(fakeProductRegister);

        assertAll(
            () -> assertNotNull(result),
            () -> assertInstanceOf(ProductResponseDTO.class, result),
            () -> assertEquals(fakeProduct1Response, result)
        );
    }

    @Test
    @DisplayName("Test if Product is being updated correctly")
    public void testUpdateProduct(){
        when(authentication.getName()).thenReturn(user);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        when(pRepository.findById(fakeProduct1Response.id())).thenReturn(Optional.ofNullable(fakeProduct1));
        when(pRepository.save(fakeProduct2)).thenReturn(fakeProduct2);
        when(pMapper.toDTO(fakeProduct2)).thenReturn(fakeProduct2Response);

        when(sService.getById(fakeProductUpdate.supplierId())).thenReturn(fakeSupplierResponse);
        when(sMapper.toEntity(fakeSupplierResponse)).thenReturn(fakeSupplier2);

        result = pService.update(fakeProduct1Response.id(), fakeProductUpdate);

        assertAll(
            () -> assertNotNull(result),
            () -> assertInstanceOf(ProductResponseDTO.class, result),
            () -> assertEquals(fakeProduct2Response, result)
        );
    }

    @Test
    @DisplayName("Test if Product is being deleted correctly")
    public void testDeleteProduct(){
        pService.delete(fakeProduct1Response.id());

        verify(pRepository, atMostOnce()).deleteById(fakeProduct1Response.id());
    }

    @Test
    @DisplayName("Test if all Products registered are being returned on method getALl()")
    public void testGetAllProducts() {
        when(pRepository.findAll()).thenReturn(List.of(fakeProduct1, fakeProduct2));
        when(pMapper.toDTOList(List.of(fakeProduct1, fakeProduct2))).thenReturn(List.of(fakeProduct1Response, fakeProduct2Response));
        List<ProductResponseDTO> result = pService.getAll();

        assertEquals(2, result.size());
        verify(pRepository, atMostOnce()).findAll();
    }

    @Test
    @DisplayName("Test if Product is being returned by id on method getById()")
    public void testGetProductById(){
        when(pRepository.findById(1)).thenReturn(Optional.ofNullable(fakeProduct1));
        when(pMapper.toDTO(fakeProduct1)).thenReturn(fakeProduct1Response);

        result = pService.getById(1);
        assertAll(
                () -> verify(pRepository, atMostOnce()).findById(1),
                () -> assertNotNull(result),
                () -> assertInstanceOf(ProductResponseDTO.class, result),
                () -> assertEquals(fakeProduct1Response, result)
        );
    }
}