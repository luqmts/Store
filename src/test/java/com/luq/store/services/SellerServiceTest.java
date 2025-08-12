package com.luq.store.services;

import com.luq.store.domain.Department;
import com.luq.store.domain.Seller;
import com.luq.store.dto.request.seller.SellerRegisterDTO;
import com.luq.store.dto.request.seller.SellerUpdateDTO;
import com.luq.store.dto.response.seller.SellerResponseDTO;
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

    private SellerResponseDTO fakeSeller1Response, fakeSeller2Response, result;
    private SellerRegisterDTO fakeSellerRegister;
    private SellerUpdateDTO fakeSellerUpdate;

    @BeforeEach
    public void setUp(){
        String user = "Jimmy McGill";
        LocalDateTime now = LocalDateTime.now();

        fakeSeller1Response = new SellerResponseDTO(
            1, "Walter White",
            new Mail("WalterWhite@Cooking.com"), new Phone("11901010101"), Department.FOOD,
            user, now, user, now
        );
        fakeSeller2Response = new SellerResponseDTO(
            1, "Jesse Pinkman",
            new Mail("Jesse Pinkman@Cooking.com"), new Phone("11904040404"), Department.TECHNOLOGY,
            user, now, user, now
        );

        fakeSellerRegister = new SellerRegisterDTO(
            "Jesse Pinkman", new Mail("Jesse Pinkman@Cooking.com"), new Phone("11904040404"), Department.FOOD
        );

        fakeSellerUpdate = new SellerUpdateDTO(
            "Walter White", new Mail("WalterWhite@Cooking.com"), new Phone("11901010101"), Department.TECHNOLOGY
        );
    }
    
    @Test
    @DisplayName("Test if Seller is being registered correctly")
    public void testRegisterSeller(){
        result = sService.register(fakeSellerRegister);

        assertAll(
            () -> assertNotNull(result),
            () -> assertInstanceOf(SellerResponseDTO.class, result),
            () -> assertEquals(fakeSeller1Response, result)
        );
    }

    @Test
    @DisplayName("Test if Seller is being updated correctly")
    public void testUpdateSeller(){
        sService.register(fakeSellerRegister);
        result = sService.update(fakeSeller1Response.id(), fakeSellerUpdate);

        assertAll(
            () -> assertNotNull(result),
            () -> assertInstanceOf(SellerResponseDTO.class, result),
            () -> assertEquals(fakeSeller2Response, result)
        );
    }

    @Test
    @DisplayName("Test if Seller is being deleted correctly")
    public void testDeleteSeller(){
        sService.register(fakeSellerRegister);

        sService.delete(fakeSeller1Response.id());

        verify(sRepository, atMostOnce()).deleteById(fakeSeller1Response.id());
    }

    @Test
    @DisplayName("Test if all Sellers registered are being returned on method getAll()")
    public void testGetAllSellers(){
        sService.register(fakeSellerRegister);
        sService.register(fakeSellerRegister);
        assertEquals(2, sService.getAll().size());
        verify(sRepository, atMostOnce()).findAll();
    }

    @Test
    @DisplayName("Test if Seller is being returned by id on method getById()")
    public void testGetSellerById(){
        sService.register(fakeSellerRegister);
        result = sService.getById(1);
        assertAll(
            () -> assertNotNull(result),
            () -> assertInstanceOf(SellerResponseDTO.class, result),
            () -> assertEquals(fakeSeller1Response, result)
        );
    }
}