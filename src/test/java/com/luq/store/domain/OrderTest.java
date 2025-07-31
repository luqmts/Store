package com.luq.store.domain;

import com.luq.store.valueobjects.Cnpj;
import com.luq.store.valueobjects.Mail;
import com.luq.store.valueobjects.Phone;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class OrderTest {
    private final LocalDateTime now = LocalDateTime.now();
    private Order fakeOrder;

    private Product fakeProduct1, fakeProduct2;
    private Customer fakeCustomer1, fakeCustomer2;
    private Seller fakeSeller1, fakeSeller2;

    @BeforeEach
    public void setUp(){
        String user = "Jimmy McGill";

        fakeCustomer1 = new Customer(1, "Test Customer 01", user, now, user, now);

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

        fakeProduct1 = new Product(
            1, "Xbox One Controller", "XOneCont", "Controller for Xbox One Console",
            BigDecimal.valueOf(200.00), fakeSupplier1, user, now, user, now
        );
        fakeProduct2 = new Product(
                2, "Playstation 5 Controller", "PS5Cont", "Controller for Playstation 5 Console",
                BigDecimal.valueOf(250.00), fakeSupplier2, user, now, user, now
        );

        fakeOrder = new Order(
            1, BigDecimal.valueOf(400.00), 2, LocalDate.now(),
            fakeProduct1, fakeSeller1, fakeCustomer1, user, now, user, now
        );
    }

    @Test
    @DisplayName("Ensure toString method is returning correctly")
    public void testOrderToStringMethod() {
        assertEquals(
            "Order(id=1, totalPrice=400.0, quantity=2, product=Product(id=1, name=Xbox One Controller), seller=Seller(id=1, name=Walter White), customer=Customer(id=1, name=Test Customer 01))",
            fakeOrder.toString(),
            "Order toString() method must return on right format"
        );
    }

    @Test
    @DisplayName("Ensure object is being created correctly")
    public void testOrderCreationGrouped(){
        assertAll(
            () -> assertEquals(1, fakeOrder.getId()),
            () -> assertEquals(LocalDate.now(), fakeOrder.getOrderDate()),
            () -> assertEquals(BigDecimal.valueOf(400.00), fakeOrder.getTotalPrice()),
            () -> assertEquals(fakeProduct1, fakeOrder.getProduct()),
            () -> assertEquals(fakeSeller1, fakeOrder.getSeller()),
            () -> assertEquals(fakeCustomer1, fakeOrder.getCustomer()),
            () -> assertEquals(now, fakeOrder.getCreated()),
            () -> assertEquals("Jimmy McGill", fakeOrder.getCreatedBy()),
            () -> assertEquals(now, fakeOrder.getModified()),
            () -> assertEquals("Jimmy McGill", fakeOrder.getModifiedBy())
        );
    }

    @Test
    @DisplayName("Ensure object is being updated correctly")
    public void testOrderUpdatedGrouped(){
        LocalDateTime now = LocalDateTime.now();
        String user = "Kim Wexler";

        fakeOrder.setQuantity(4);
        fakeOrder.setOrderDate(LocalDate.now().plusDays(5));
        fakeOrder.setTotalPrice(BigDecimal.valueOf(1000.00));
        fakeOrder.setProduct(fakeProduct2);
        fakeOrder.setCustomer(fakeCustomer2);
        fakeOrder.setSeller(fakeSeller2);
        fakeOrder.setCreatedBy(user);
        fakeOrder.setModifiedBy(user);
        fakeOrder.setCreated(now);
        fakeOrder.setModified(now);

        assertAll(
            () -> assertEquals(4, fakeOrder.getQuantity()),
            () -> assertEquals(LocalDate.now().plusDays(5), fakeOrder.getOrderDate()),
            () -> assertEquals(BigDecimal.valueOf(1000.00), fakeOrder.getTotalPrice()),
            () -> assertEquals(fakeProduct2, fakeOrder.getProduct()),
            () -> assertEquals(fakeCustomer2, fakeOrder.getCustomer()),
            () -> assertEquals(fakeSeller2, fakeOrder.getSeller()),
            () -> assertEquals("Kim Wexler", fakeOrder.getCreatedBy()),
            () -> assertEquals("Kim Wexler", fakeOrder.getModifiedBy()),
            () -> assertEquals(now, fakeOrder.getCreated()),
            () -> assertEquals(now, fakeOrder.getModified())
        );
    }

    @Test
    @DisplayName("Customer attributes must not be null")
    public void testOrderAttributesNotNull(){
        assertAll(
            () -> assertNotNull(fakeOrder.getId()),
            () -> assertNotNull(fakeOrder.getQuantity()),
            () -> assertNotNull(fakeOrder.getTotalPrice()),
            () -> assertNotNull(fakeOrder.getOrderDate()),
            () -> assertNotNull(fakeOrder.getProduct()),
            () -> assertNotNull(fakeOrder.getSeller()),
            () -> assertNotNull(fakeOrder.getCustomer()),
            () -> assertNotNull(fakeOrder.getCreatedBy()),
            () -> assertNotNull(fakeOrder.getCreated()),
            () -> assertNotNull(fakeOrder.getModifiedBy()),
            () -> assertNotNull(fakeOrder.getModified())
        );
    }
}