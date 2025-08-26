package com.luq.store.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.luq.store.domain.*;
import com.luq.store.domain.Customer;
import com.luq.store.dto.request.order.OrderRegisterDTO;
import com.luq.store.dto.request.order.OrderUpdateDTO;
import com.luq.store.dto.response.order.OrderResponseDTO;
import com.luq.store.mapper.OrderMapper;
import com.luq.store.repositories.OrderRepository;
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
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    OrderRepository oRepository;
    @InjectMocks
    OrderService oService;

    @Autowired
    OrderMapper oMapper;

    Supplier fakeSupplier1, fakeSupplier2;
    Product fakeProduct1, fakeProduct2;
    Seller fakeSeller1, fakeSeller2;
    private OrderResponseDTO fakeOrder1Response, fakeOrder2Response, result;
    private OrderRegisterDTO fakeOrderRegister;
    private OrderUpdateDTO fakeOrderUpdate;

    @BeforeEach
    public void setUp(){
        String user = "Jimmy McGill";
        LocalDateTime now = LocalDateTime.now();

        Customer fakeCustomer1 = new Customer(1, "Test Customer 01", user, now, user, now);
        Customer fakeCustomer2 = new Customer(2, "Test Customer 02", user, now, user, now);

        fakeSupplier1 = new Supplier(
            1, "Microsoft Brasil LTDA.", new Cnpj("43.447.044/0004-10"),
            new Mail("microsoft@mail.com"), new Phone("11000001111"),
            user, now, user, now
        );
        fakeSupplier2 = new Supplier(
            2, "Sony Brasil LTDA.", new Cnpj("04.542.534/0001-09"),
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

        fakeOrder1Response = new OrderResponseDTO(
            1, BigDecimal.valueOf(400.00), BigDecimal.valueOf(200.00),2, LocalDate.now(),
            fakeProduct1, fakeSeller1, fakeCustomer1, user, now, user, now
        );
        fakeOrder2Response = new OrderResponseDTO(
            1, BigDecimal.valueOf(600.00), BigDecimal.valueOf(150.00), 4, LocalDate.now().plusDays(5),
            fakeProduct1, fakeSeller1, fakeCustomer1, user, now, user, now
        );

        fakeOrderRegister = new OrderRegisterDTO(
            BigDecimal.valueOf(400.00), BigDecimal.valueOf(200.00), 2, LocalDate.now(), fakeProduct1.getId(), fakeSeller1.getId(), fakeCustomer1.getId()
        );

        fakeOrderUpdate = new OrderUpdateDTO(
            1, BigDecimal.valueOf(600.00), BigDecimal.valueOf(150.00), 4, LocalDate.now().plusDays(5), fakeProduct2.getId(), fakeSeller2.getId(), fakeCustomer2.getId()
        );
    }
    
    @Test
    @DisplayName("Test if Order is being registered correctly")
    public void testRegisterOrder() throws JsonProcessingException {
        when(oRepository.save(oMapper.toEntity(fakeOrderRegister))).thenReturn(oMapper.toEntity(fakeOrderRegister));
        result = oService.register(fakeOrderRegister);

        assertAll(
            () -> verify(oRepository, atMostOnce()).save(oMapper.toEntity(fakeOrderRegister)),
            () -> assertNotNull(result),
            () -> assertInstanceOf(OrderResponseDTO.class, result),
            () -> assertEquals(fakeOrder1Response, result)
        );
    }

    @Test
    @DisplayName("Test if Order is being updated correctly")
    public void testUpdateOrder() throws JsonProcessingException {
        oService.register(fakeOrderRegister);
        result = oService.update(fakeOrder1Response.id(), fakeOrderUpdate);

        assertAll(
            () -> assertNotNull(result),
            () -> assertInstanceOf(OrderResponseDTO.class, result),
            () -> assertEquals(fakeOrder2Response, result)
        );
    }

    @Test
    @DisplayName("Test if Order is being deleted correctly")
    public void testDeleteOrder() throws JsonProcessingException {
        oService.register(fakeOrderRegister);

        oService.delete(fakeOrder1Response.id());

        verify(oRepository, atMostOnce()).deleteById(fakeOrder1Response.id());
    }

    @Test
    @DisplayName("Test if all Orders registered are being returned on method getAll()")
    public void testGetAllOrders() throws JsonProcessingException {
        oService.register(fakeOrderRegister);
        oService.register(fakeOrderRegister);
        assertEquals(2, oService.getAll().size());
        verify(oRepository, atMostOnce()).findAll();
    }

    @Test
    @DisplayName("Test if Order is being returned by id on method getById()")
    public void testGetOrderById() throws JsonProcessingException {
        oService.register(fakeOrderRegister);
        result = oService.getById(1);
        assertAll(
                () -> verify(oRepository, atMostOnce()).findById(1),
                () -> assertNotNull(result),
                () -> assertInstanceOf(OrderResponseDTO.class, result),
                () -> assertEquals(fakeOrder1Response, result)
        );
    }
}