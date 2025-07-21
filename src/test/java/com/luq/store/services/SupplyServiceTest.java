package com.luq.store.services;

import com.luq.store.domain.Product;
import com.luq.store.domain.Supplier;
import com.luq.store.domain.Supply;
import com.luq.store.repositories.SupplyRepository;
import com.luq.store.valueobjects.Cnpj;
import com.luq.store.valueobjects.Mail;
import com.luq.store.valueobjects.Phone;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SupplyServiceTest {
    @Mock
    SupplyRepository sRepository;
    @InjectMocks
    SupplyService sService;

    Supply fakeSupply1, fakeSupply2, result;

    @BeforeEach
    public void setUp(){
        String user = "Jimmy McGill";
        LocalDateTime now = LocalDateTime.now();

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

        Product fakeProduct1 = new Product(
                1, "Xbox One Controller", "XOneCont", "Controller for Xbox One Console",
                200.00F, fakeSupplier1, user, now, user, now
        );
        Product fakeProduct2 = new Product(
                2, "Playstation 5 Controller", "PS5Cont", "Controller for Playstation 5 Console",
                250.00F, fakeSupplier2, user, now, user, now
        );

        fakeSupply1 = new Supply(1, 100, fakeProduct1, user, now, user, now);
        fakeSupply2 = new Supply(2, 300, fakeProduct2, user, now, user, now);
    }
    
    @Test
    @DisplayName("Test if Supply is being registered correctly")
    public void testRegisterSupply(){
        when(sRepository.save(fakeSupply1)).thenReturn(fakeSupply1);
        result = sService.register(fakeSupply1);

        assertAll(
            () -> verify(sRepository, atMostOnce()).save(fakeSupply1),
            () -> assertNotNull(result),
            () -> assertInstanceOf(Supply.class, result),
            () -> assertEquals(fakeSupply1, result)
        );
    }

    @Test
    @DisplayName("Test if Supply is being updated correctly")
    public void testUpdateSupply(){
        when(sRepository.save(fakeSupply1)).thenReturn(fakeSupply1);
        when(sRepository.findById(fakeSupply1.getId())).thenReturn(Optional.ofNullable(fakeSupply1));
        when(sRepository.save(fakeSupply2)).thenReturn(fakeSupply2);

        sService.register(fakeSupply1);
        result = sService.update(fakeSupply1.getId(), fakeSupply2);

        assertAll(
            () -> verify(sRepository, times(2)).save(fakeSupply1),
            () -> verify(sRepository, times(2)).save(fakeSupply2),
            () -> verify(sRepository, atMostOnce()).findById(fakeSupply1.getId()),
            () -> assertNotNull(result),
            () -> assertInstanceOf(Supply.class, result),
            () -> assertEquals(fakeSupply2, result)
        );
    }

    @Test
    @DisplayName("Test if Supply is being deleted correctly")
    public void testDeleteSupply(){
        when(sRepository.save(fakeSupply1)).thenReturn(fakeSupply1);
        sService.register(fakeSupply1);

        sService.delete(fakeSupply1.getId());

        verify(sRepository, atMostOnce()).deleteById(fakeSupply1.getId());
    }

    @Test
    @DisplayName("Test if all Supply registered are being returned on method getALl()")
    public void testGetAllSupply() {
        when(sRepository.save(fakeSupply1)).thenReturn(fakeSupply1);
        when(sRepository.save(fakeSupply2)).thenReturn(fakeSupply2);
        when(sRepository.findAll()).thenReturn(List.of(fakeSupply1, fakeSupply2));

        sService.register(fakeSupply1);
        sService.register(fakeSupply2);
        assertEquals(2, sService.getAll().size());
        verify(sRepository, atMostOnce()).findAll();
    }

    @Test
    @DisplayName("Test if Seller is being returned by id on method getById()")
    public void testGetSellerById(){
        when(sRepository.save(fakeSupply1)).thenReturn(fakeSupply1);
        when(sRepository.findById(1)).thenReturn(Optional.ofNullable(fakeSupply1));

        sService.register(fakeSupply1);
        result = sService.getById(1);
        assertAll(
                () -> verify(sRepository, atMostOnce()).findById(1),
                () -> assertNotNull(result),
                () -> assertInstanceOf(Supply.class, result),
                () -> assertEquals(fakeSupply1, result)
        );
    }
}