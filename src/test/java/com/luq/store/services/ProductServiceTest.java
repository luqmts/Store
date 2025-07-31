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

import com.luq.store.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.luq.store.domain.Product;
import com.luq.store.domain.Supplier;
import com.luq.store.valueobjects.Cnpj;
import com.luq.store.valueobjects.Mail;
import com.luq.store.valueobjects.Phone;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    ProductRepository pRepository;
    @InjectMocks
    ProductService pService;

    Supplier fakeSupplier1, fakeSupplier2;
    Product fakeProduct1, fakeProduct2, result;

    @BeforeEach
    public void setUp(){
        String user = "Jimmy McGill";
        LocalDateTime now = LocalDateTime.now();

        fakeSupplier1 = new Supplier(
            1,  "Microsoft Brasil LTDA.", new Cnpj("43.447.044/0004-10"),
            new Mail("microsoft@mail.com"), new Phone("11000001111"),
            user, now, user, now
        );
        fakeSupplier2 = new Supplier(
            2,   "Sony Brasil LTDA.", new Cnpj("04.542.534/0001-09"),
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
    }
    
    @Test
    @DisplayName("Test if Product is being registered correctly")
    public void testRegisterProduct(){
        when(pRepository.save(fakeProduct1)).thenReturn(fakeProduct1);
        result = pService.register(fakeProduct1);

        assertAll(
            () -> verify(pRepository, atMostOnce()).save(fakeProduct1),
            () -> assertNotNull(result),
            () -> assertInstanceOf(Product.class, result),
            () -> assertEquals(fakeProduct1, result)
        );
    }

    @Test
    @DisplayName("Test if Product is being updated correctly")
    public void testUpdateProduct(){
        when(pRepository.save(fakeProduct1)).thenReturn(fakeProduct1);
        when(pRepository.findById(fakeProduct1.getId())).thenReturn(Optional.ofNullable(fakeProduct1));
        when(pRepository.save(fakeProduct2)).thenReturn(fakeProduct2);

        pService.register(fakeProduct1);
        result = pService.update(fakeProduct1.getId(), fakeProduct2);

        assertAll(
            () -> verify(pRepository, times(2)).save(fakeProduct1),
            () -> verify(pRepository, times(2)).save(fakeProduct2),
            () -> verify(pRepository, atMostOnce()).findById(fakeProduct1.getId()),
            () -> assertNotNull(result),
            () -> assertInstanceOf(Product.class, result),
            () -> assertEquals(fakeProduct2, result)
        );
    }

    @Test
    @DisplayName("Test if Product is being deleted correctly")
    public void testDeleteProduct(){
        when(pRepository.save(fakeProduct1)).thenReturn(fakeProduct1);
        pService.register(fakeProduct1);

        pService.delete(fakeProduct1.getId());

        verify(pRepository, atMostOnce()).deleteById(fakeProduct1.getId());
    }

    @Test
    @DisplayName("Test if all Products registered are being returned on method getALl()")
    public void testGetAllProducts() {
        when(pRepository.save(fakeProduct1)).thenReturn(fakeProduct1);
        when(pRepository.save(fakeProduct2)).thenReturn(fakeProduct2);
        when(pRepository.findAll()).thenReturn(List.of(fakeProduct1, fakeProduct2));

        pService.register(fakeProduct1);
        pService.register(fakeProduct2);
        assertEquals(2, pService.getAll().size());
        verify(pRepository, atMostOnce()).findAll();
    }

    @Test
    @DisplayName("Test if Product is being returned by id on method getById()")
    public void testGetProductById(){
        when(pRepository.save(fakeProduct1)).thenReturn(fakeProduct1);
        when(pRepository.findById(1)).thenReturn(Optional.ofNullable(fakeProduct1));

        pService.register(fakeProduct1);
        result = pService.getById(1);
        assertAll(
                () -> verify(pRepository, atMostOnce()).findById(1),
                () -> assertNotNull(result),
                () -> assertInstanceOf(Product.class, result),
                () -> assertEquals(fakeProduct1, result)
        );
    }
}