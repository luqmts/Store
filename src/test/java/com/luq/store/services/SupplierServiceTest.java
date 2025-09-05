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

import com.luq.store.dto.request.supplier.SupplierRegisterDTO;
import com.luq.store.dto.request.supplier.SupplierUpdateDTO;
import com.luq.store.dto.response.supplier.SupplierResponseDTO;
import com.luq.store.mapper.SupplierMapper;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
public class SupplierServiceTest {
    @Mock
    SupplierRepository sRepository;
    @InjectMocks
    SupplierService sService;
    @Mock
    private SupplierMapper sMapper;

    private SupplierResponseDTO fakeSupplier1Response, fakeSupplier2Response, result;
    private SupplierRegisterDTO fakeSupplierRegister;
    private SupplierUpdateDTO fakeSupplierUpdate;
    private Supplier fakeSupplier1, fakeSupplier2;

    Authentication authentication;
    SecurityContext securityContext;

    String user;
    LocalDateTime now;

    @BeforeEach
    public void setUp(){
        user = "Jimmy McGill";
        now = LocalDateTime.now();

        fakeSupplier1Response = new SupplierResponseDTO(
            1, "Microsoft Brasil LTDA.", "43.447.044/0004-10",
            "microsoft@mail.com", "11000001111",
            user, now, user, now
        );
        fakeSupplier2Response = new SupplierResponseDTO(
            1, "Sony Brasil LTDA.", "04.542.534/0001-09",
            "sony@mail.com", "11222225555",
            user, now, user, now
        );
        fakeSupplierRegister = new SupplierRegisterDTO(
            "Microsoft Brasil LTDA.", "43.447.044/0004-10",
            "microsoft@mail.com", "11000001111"
        );
        fakeSupplierUpdate = new SupplierUpdateDTO(
            "Sony Brasil LTDA.", "04.542.534/0001-09",
            "sony@mail.com", "11222225555"
        );
        fakeSupplier1 = new Supplier(
            1, "Microsoft Brasil LTDA.", new Cnpj("43.447.044/0004-10"),
            new Mail("microsoft@mail.com"), new Phone("11000001111"),
            user, now, user, now
        );
        fakeSupplier2 = new Supplier(
            1, "Sony Brasil LTDA.", new Cnpj("04.542.534/0001-09"),
            new Mail("sony@mail.com"), new Phone("11222225555"),
            user, now, user, now
        );

        authentication = mock(Authentication.class);
        securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
    }
    
    @Test
    @DisplayName("Test if Supplier is being registered correctly")
    public void testRegisterSupplier(){
        when(authentication.getName()).thenReturn(user);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(sRepository.save(fakeSupplier1)).thenReturn(fakeSupplier1);
        when(sMapper.toEntity(fakeSupplierRegister)).thenReturn(fakeSupplier1);
        when(sMapper.toDTO(fakeSupplier1)).thenReturn(fakeSupplier1Response);

        result = sService.register(fakeSupplierRegister);

        assertAll(
            () -> assertNotNull(result),
            () -> assertInstanceOf(SupplierResponseDTO.class, result),
            () -> assertEquals(fakeSupplier1Response, result)
        );
    }

    @Test
    @DisplayName("Test if Supplier is being updated correctly")
    public void testUpdateSupplier(){
        when(authentication.getName()).thenReturn(user);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(sRepository.findById(fakeSupplier1Response.id())).thenReturn(Optional.ofNullable(fakeSupplier1));
        when(sRepository.save(fakeSupplier2)).thenReturn(fakeSupplier2);
        when(sMapper.toDTO(fakeSupplier2)).thenReturn(fakeSupplier2Response);

        result = sService.update(fakeSupplier1Response.id(), fakeSupplierUpdate);

        assertAll(
            () -> assertNotNull(result),
            () -> assertInstanceOf(SupplierResponseDTO.class, result),
            () -> assertEquals(fakeSupplier2Response, result)
        );
    }

    @Test
    @DisplayName("Test if Supplier is being deleted correctly")
    public void testDeleteSupplier(){
        sService.delete(fakeSupplier1Response.id());

        verify(sRepository, atMostOnce()).deleteById(fakeSupplier1Response.id());
    }

    @Test
    @DisplayName("Test if all Suppliers registered are being returned on method getAll()")
    public void testGetAllSuppliers(){
        when(sRepository.findAll()).thenReturn(List.of(fakeSupplier1, fakeSupplier2));
        when(sMapper.toDTOList(List.of(fakeSupplier1, fakeSupplier2))).thenReturn(List.of(fakeSupplier1Response, fakeSupplier2Response));
        List<SupplierResponseDTO> result = sService.getAll();

        assertEquals(2, result.size());
        verify(sRepository, atMostOnce()).findAll();
    }

    @Test
    @DisplayName("Test if Supplier is being returned by id on method getById()")
    public void testGetSupplierById(){
        when(sRepository.findById(1)).thenReturn(Optional.ofNullable(fakeSupplier1));
        when(sMapper.toDTO(fakeSupplier1)).thenReturn(fakeSupplier1Response);

        result = sService.getById(1);

        assertAll(
            () -> verify(sRepository, atMostOnce()).findById(1),
            () -> assertNotNull(result),
            () -> assertInstanceOf(SupplierResponseDTO.class, result),
            () -> assertEquals(fakeSupplier1Response, result)
        );
    }
}