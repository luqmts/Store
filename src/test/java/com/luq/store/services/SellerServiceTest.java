package com.luq.store.services;

import com.luq.store.domain.Department;
import com.luq.store.domain.Seller;
import com.luq.store.dto.request.seller.SellerRegisterDTO;
import com.luq.store.dto.request.seller.SellerUpdateDTO;
import com.luq.store.dto.response.seller.SellerResponseDTO;
import com.luq.store.mapper.SellerMapper;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SellerServiceTest {
    @Mock
    private SellerRepository sRepository;
    @InjectMocks
    private SellerService sService;
    @Mock
    private SellerMapper sMapper;

    private SellerResponseDTO fakeSeller1Response, fakeSeller2Response, result;
    private SellerRegisterDTO fakeSellerRegister;
    private SellerUpdateDTO fakeSellerUpdate;
    private Seller fakeSeller1, fakeSeller2;

    Authentication authentication;
    SecurityContext securityContext;

    String user;
    LocalDateTime now;

    @BeforeEach
    public void setUp(){
        user = "Jimmy McGill";
        now = LocalDateTime.now();

        fakeSeller1Response = new SellerResponseDTO(
            1, "Jesse Pinkman",
            "JessePinkman@Cooking.com", "11904040404", Department.TECHNOLOGY,
            user, now, user, now
        );
        fakeSeller2Response = new SellerResponseDTO(
            1, "Walter White",
            "WalterWhite@Cooking.com", "11901010101", Department.FOOD,
            user, now, user, now
        );
        fakeSellerRegister = new SellerRegisterDTO(
            "Jesse Pinkman", "JessePinkman@Cooking.com", "11904040404", Department.FOOD
        );

        fakeSellerUpdate = new SellerUpdateDTO(
            "Walter White", "WalterWhite@Cooking.com", "11901010101", Department.TECHNOLOGY
        );
        fakeSeller1 = new Seller(
            1, "Jesse Pinkman", new Mail("JessePinkman@Cooking.com"),
            new Phone("11904040404"), Department.TECHNOLOGY, user, now, user, now
        );

        fakeSeller2 = new Seller(
            1, "Walter White", new Mail("WalterWhite@Cooking.com"),
            new Phone("11901010101"), Department.FOOD, user, now, user, now
        );

        authentication = mock(Authentication.class);
        securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

    }
    
    @Test
    @DisplayName("Test if Seller is being registered correctly")
    public void testRegisterSeller(){
        when(authentication.getName()).thenReturn(user);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(sRepository.save(fakeSeller1)).thenReturn(fakeSeller1);
        when(sMapper.toEntity(fakeSellerRegister)).thenReturn(fakeSeller1);
        when(sMapper.toDTO(fakeSeller1)).thenReturn(fakeSeller1Response);

        result = sService.register(fakeSellerRegister);

        assertAll(
            () -> verify(sRepository, atMostOnce()).save(fakeSeller1),
            () -> assertNotNull(result),
            () -> assertInstanceOf(SellerResponseDTO.class, result),
            () -> assertEquals(fakeSeller1Response, result)
        );
    }

    @Test
    @DisplayName("Test if Seller is being updated correctly")
    public void testUpdateSeller(){
        when(authentication.getName()).thenReturn(user);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(sRepository.findById(fakeSeller1Response.id())).thenReturn(Optional.ofNullable(fakeSeller1));
        when(sRepository.save(fakeSeller2)).thenReturn(fakeSeller2);
        when(sMapper.toDTO(fakeSeller2)).thenReturn(fakeSeller2Response);

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
        sService.delete(fakeSeller1Response.id());

        verify(sRepository, atMostOnce()).deleteById(fakeSeller1Response.id());
    }

    @Test
    @DisplayName("Test if all Sellers registered are being returned on method getAll()")
    public void testGetAllSellers(){
        when(sRepository.findAll()).thenReturn(List.of(fakeSeller1, fakeSeller2));
        when(sMapper.toDTOList(List.of(fakeSeller1, fakeSeller2))).thenReturn(List.of(fakeSeller1Response, fakeSeller2Response));
        List<SellerResponseDTO> result = sService.getAll();

        assertEquals(2, result.size());
        verify(sRepository, atMostOnce()).findAll();
    }

    @Test
    @DisplayName("Test if Seller is being returned by id on method getById()")
    public void testGetSellerById(){
        when(sRepository.findById(1)).thenReturn(Optional.ofNullable(fakeSeller1));
        when(sMapper.toDTO(fakeSeller1)).thenReturn(fakeSeller1Response);

        result = sService.getById(1);
        assertAll(
            () -> verify(sRepository, atMostOnce()).findById(1),
            () -> assertNotNull(result),
            () -> assertInstanceOf(SellerResponseDTO.class, result),
            () -> assertEquals(fakeSeller1Response, result)
        );
    }
}