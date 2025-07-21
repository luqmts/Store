package com.luq.store.services;

import com.luq.store.domain.Department;
import com.luq.store.domain.Seller;
import com.luq.store.repositories.SellerRepository;
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
public class SellerServiceTest {
    @Mock
    SellerRepository sRepository;
    @InjectMocks
    SellerService sService;

    Seller fakeSeller1, fakeSeller2, result;

    @BeforeEach
    public void setUp(){
        String user = "Jimmy McGill";
        LocalDateTime now = LocalDateTime.now();

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
    @DisplayName("Test if Seller is being registered correctly")
    public void testRegisterSeller(){
        when(sRepository.save(fakeSeller1)).thenReturn(fakeSeller1);
        result = sService.register(fakeSeller1);

        assertAll(
            () -> verify(sRepository, atMostOnce()).save(fakeSeller1),
            () -> assertNotNull(result),
            () -> assertInstanceOf(Seller.class, result),
            () -> assertEquals(fakeSeller1, result)
        );
    }

    @Test
    @DisplayName("Test if Seller is being updated correctly")
    public void testUpdateSeller(){
        when(sRepository.save(fakeSeller1)).thenReturn(fakeSeller1);
        when(sRepository.findById(fakeSeller1.getId())).thenReturn(Optional.ofNullable(fakeSeller1));
        when(sRepository.save(fakeSeller2)).thenReturn(fakeSeller2);

        sService.register(fakeSeller1);
        result = sService.update(fakeSeller1.getId(), fakeSeller2);

        assertAll(
            () -> verify(sRepository, times(2)).save(fakeSeller1),
            () -> verify(sRepository, times(2)).save(fakeSeller2),
            () -> verify(sRepository, atMostOnce()).findById(fakeSeller1.getId()),
            () -> assertNotNull(result),
            () -> assertInstanceOf(Seller.class, result),
            () -> assertEquals(fakeSeller2, result)
        );
    }

    @Test
    @DisplayName("Test if Seller is being deleted correctly")
    public void testDeleteSeller(){
        when(sRepository.save(fakeSeller1)).thenReturn(fakeSeller1);
        sService.register(fakeSeller1);

        sService.delete(fakeSeller1.getId());

        verify(sRepository, atMostOnce()).deleteById(fakeSeller1.getId());
    }

    @Test
    @DisplayName("Test if all Sellers registered are being returned on method getAll()")
    public void testGetAllSellers(){
        when(sRepository.save(fakeSeller1)).thenReturn(fakeSeller1);
        when(sRepository.save(fakeSeller2)).thenReturn(fakeSeller2);
        when(sRepository.findAll()).thenReturn(List.of(fakeSeller1, fakeSeller2));

        sService.register(fakeSeller1);
        sService.register(fakeSeller2);
        assertEquals(2, sService.getAll().size());
        verify(sRepository, atMostOnce()).findAll();
    }

    @Test
    @DisplayName("Test if Seller is being returned by id on method getById()")
    public void testGetSellerById(){
        when(sRepository.save(fakeSeller1)).thenReturn(fakeSeller1);
        when(sRepository.findById(1)).thenReturn(Optional.ofNullable(fakeSeller1));

        sService.register(fakeSeller1);
        result = sService.getById(1);
        assertAll(
            () -> verify(sRepository, atMostOnce()).findById(1),
            () -> assertNotNull(result),
            () -> assertInstanceOf(Seller.class, result),
            () -> assertEquals(fakeSeller1, result)
        );
    }
}