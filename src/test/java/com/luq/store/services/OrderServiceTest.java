package com.luq.store.services;

import com.luq.store.domain.*;
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

    Supplier fakeSupplier1, fakeSupplier2;
    Product fakeProduct1, fakeProduct2;
    Seller fakeSeller1, fakeSeller2;
    Order fakeOrder1, fakeOrder2, result;

    @BeforeEach
    public void setUp(){
        String user = "Jimmy McGill";
        LocalDateTime now = LocalDateTime.now();

        Customer fakeCustomer1 = new Customer(1, "Test Customer 01", user, now, user, now);
        Customer fakeCustomer2 = new Customer(2, "Test Customer 02", user, now, user, now);

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
            200.00F, fakeSupplier1, user, now, user, now
        );
        fakeProduct2 = new Product(
            2, "Playstation 5 Controller", "PS5Cont", "Controller for Playstation 5 Console",
            250.00F, fakeSupplier2, user, now, user, now
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

        fakeOrder1 = new Order(
                1, 400.00F, 2, LocalDate.now(),
                fakeProduct1, fakeSeller1, fakeCustomer1, user, now, user, now
        );
        fakeOrder2 = new Order(
                2, 1000.00F, 4, LocalDate.now(),
                fakeProduct2, fakeSeller2, fakeCustomer2, user, now, user, now
        );
    }
    
    @Test
    @DisplayName("Test if Order is being registered correctly")
    public void testRegisterOrder(){
        when(oRepository.save(fakeOrder1)).thenReturn(fakeOrder1);
        result = oService.register(fakeOrder1);

        assertAll(
            () -> verify(oRepository, atMostOnce()).save(fakeOrder1),
            () -> assertNotNull(result),
            () -> assertInstanceOf(Order.class, result),
            () -> assertEquals(fakeOrder1, result)
        );
    }

    @Test
    @DisplayName("Test if Order is being updated correctly")
    public void testUpdateOrder(){
        when(oRepository.save(fakeOrder1)).thenReturn(fakeOrder1);
        when(oRepository.findById(fakeOrder1.getId())).thenReturn(Optional.ofNullable(fakeOrder1));
        when(oRepository.save(fakeOrder2)).thenReturn(fakeOrder2);

        oService.register(fakeOrder1);
        result = oService.update(fakeOrder1.getId(), fakeOrder2);

        assertAll(
            () -> verify(oRepository, times(2)).save(fakeOrder1),
            () -> verify(oRepository, times(2)).save(fakeOrder2),
            () -> verify(oRepository, atMostOnce()).findById(fakeOrder1.getId()),
            () -> assertNotNull(result),
            () -> assertInstanceOf(Order.class, result),
            () -> assertEquals(fakeOrder2, result)
        );
    }

    @Test
    @DisplayName("Test if Order is being deleted correctly")
    public void testDeleteOrder(){
        when(oRepository.save(fakeOrder1)).thenReturn(fakeOrder1);
        oService.register(fakeOrder1);

        oService.delete(fakeOrder1.getId());

        verify(oRepository, atMostOnce()).deleteById(fakeOrder1.getId());
    }

    @Test
    @DisplayName("Test if all Orders registered are being returned on method getAll()")
    public void testGetAllOrders(){
        when(oRepository.save(fakeOrder1)).thenReturn(fakeOrder1);
        when(oRepository.save(fakeOrder2)).thenReturn(fakeOrder2);
        when(oRepository.findAll()).thenReturn(List.of(fakeOrder1, fakeOrder2));

        oService.register(fakeOrder1);
        oService.register(fakeOrder2);
        assertEquals(2, oService.getAll().size());
        verify(oRepository, atMostOnce()).findAll();
    }

    @Test
    @DisplayName("Test if Order is being returned by id on method getById()")
    public void testGetOrderById(){
        when(oRepository.save(fakeOrder1)).thenReturn(fakeOrder1);
        when(oRepository.findById(1)).thenReturn(Optional.ofNullable(fakeOrder1));

        oService.register(fakeOrder1);
        result = oService.getById(1);
        assertAll(
                () -> verify(oRepository, atMostOnce()).findById(1),
                () -> assertNotNull(result),
                () -> assertInstanceOf(Order.class, result),
                () -> assertEquals(fakeOrder1, result)
        );
    }
}