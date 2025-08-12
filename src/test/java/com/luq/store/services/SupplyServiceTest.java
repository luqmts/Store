package com.luq.store.services;

import com.luq.store.domain.Product;
import com.luq.store.domain.Supplier;
import com.luq.store.domain.Supply;
import com.luq.store.dto.request.supply.SupplyRegisterDTO;
import com.luq.store.dto.request.supply.SupplyUpdateDTO;
import com.luq.store.dto.response.supply.SupplyResponseDTO;
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

import java.math.BigDecimal;
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

    private SupplyResponseDTO fakeSupply1Response, fakeSupply2Response, result;
    private SupplyRegisterDTO fakeSupplyRegister;
    private SupplyUpdateDTO fakeSupplyUpdate;

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
            BigDecimal.valueOf(200.00), fakeSupplier1, user, now, user, now
        );
        Product fakeProduct2 = new Product(
            2, "Playstation 5 Controller", "PS5Cont", "Controller for Playstation 5 Console",
            BigDecimal.valueOf(250.00), fakeSupplier2, user, now, user, now
        );

        fakeSupply1Response = new SupplyResponseDTO(1, 100, fakeProduct1, user, now, user, now);
        fakeSupply2Response = new SupplyResponseDTO(2, 300, fakeProduct2, user, now, user, now);

        fakeSupplyRegister = new SupplyRegisterDTO(100, fakeProduct1.getId());

        fakeSupplyUpdate = new SupplyUpdateDTO(300, fakeProduct2.getId());
    }
    
    @Test
    @DisplayName("Test if Supply is being registered correctly")
    public void testRegisterSupply(){
        result = sService.register(fakeSupplyRegister);

        assertAll(
            () -> assertNotNull(result),
            () -> assertInstanceOf(SupplyResponseDTO.class, result),
            () -> assertEquals(fakeSupply1Response, result)
        );
    }

    @Test
    @DisplayName("Test if Supply is being updated correctly")
    public void testUpdateSupply(){
        sService.register(fakeSupplyRegister);
        result = sService.update(fakeSupply1Response.id(), fakeSupplyUpdate);

        assertAll(
            () -> assertNotNull(result),
            () -> assertInstanceOf(SupplyResponseDTO.class, result),
            () -> assertEquals(fakeSupply1Response, result)
        );
    }

    @Test
    @DisplayName("Test if Supply is being deleted correctly")
    public void testDeleteSupply(){
        sService.register(fakeSupplyRegister);

        sService.delete(fakeSupply1Response.id());

        verify(sRepository, atMostOnce()).deleteById(fakeSupply1Response.id());
    }

    @Test
    @DisplayName("Test if all Supply registered are being returned on method getALl()")
    public void testGetAllSupply() {
        sService.register(fakeSupplyRegister);
        sService.register(fakeSupplyRegister);
        assertEquals(2, sService.getAll().size());
        verify(sRepository, atMostOnce()).findAll();
    }

    @Test
    @DisplayName("Test if Seller is being returned by id on method getById()")
    public void testGetSellerById(){
        sService.register(fakeSupplyRegister);
        result = sService.getById(1);
        assertAll(
                () -> verify(sRepository, atMostOnce()).findById(1),
                () -> assertNotNull(result),
                () -> assertInstanceOf(SupplyResponseDTO.class, result),
                () -> assertEquals(fakeSupply1Response, result)
        );
    }
}