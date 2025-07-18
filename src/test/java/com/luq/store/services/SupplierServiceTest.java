package com.luq.store.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.atMostOnce;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.luq.store.repositories.SupplierRepository;
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

@ExtendWith(MockitoExtension.class)
public class SupplierServiceTest {
    @Mock
    SupplierRepository sRepository;
    @InjectMocks
    SupplierService sService;

    Supplier fakeSupplier1, fakeSupplier2, result;

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
    }
    
    @Test
    @DisplayName("Test if Supplier is being registered correctly")
    public void testRegisterSupplier(){
        when(sRepository.save(fakeSupplier1)).thenReturn(fakeSupplier1);
        result = sService.register(fakeSupplier1);

        assertAll(
            () -> verify(sRepository, atMostOnce()).save(fakeSupplier1),
            () -> assertNotNull(result),
            () -> assertInstanceOf(Supplier.class, result),
            () -> assertEquals(fakeSupplier1, result)
        );
    }

    @Test
    @DisplayName("Test if Supplier is being updated correctly")
    public void testUpdateSupplier(){
        when(sRepository.save(fakeSupplier1)).thenReturn(fakeSupplier1);
        when(sRepository.findById(fakeSupplier1.getId())).thenReturn(Optional.ofNullable(fakeSupplier1));
        when(sRepository.save(fakeSupplier2)).thenReturn(fakeSupplier2);

        sService.register(fakeSupplier1);
        result = sService.update(fakeSupplier1.getId(), fakeSupplier2);

        assertAll(
            () -> verify(sRepository, times(2)).save(fakeSupplier1),
            () -> verify(sRepository, times(2)).save(fakeSupplier2),
            () -> verify(sRepository, atMostOnce()).findById(fakeSupplier1.getId()),
            () -> assertNotNull(result),
            () -> assertInstanceOf(Supplier.class, result),
            () -> assertEquals(fakeSupplier2, result)
        );
    }

    @Test
    @DisplayName("Test if Supplier is being deleted correctly")
    public void testDeleteSupplier(){
        when(sRepository.save(fakeSupplier1)).thenReturn(fakeSupplier1);
        sService.register(fakeSupplier1);

        sService.delete(fakeSupplier1.getId());

        verify(sRepository, atMostOnce()).deleteById(fakeSupplier1.getId());
    }

    @Test
    @DisplayName("Test if all Suppliers registered are being returned on method getALl()")
    public void testGetAllSuppliers(){
        when(sRepository.save(fakeSupplier1)).thenReturn(fakeSupplier1);
        when(sRepository.save(fakeSupplier2)).thenReturn(fakeSupplier2);
        when(sRepository.findAll()).thenReturn(List.of(fakeSupplier1, fakeSupplier2));

        sService.register(fakeSupplier1);
        sService.register(fakeSupplier2);
        assertEquals(2, sService.getAll().size());
        verify(sRepository, atMostOnce()).findAll();
    }
}