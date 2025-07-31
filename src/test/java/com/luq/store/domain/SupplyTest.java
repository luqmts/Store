package com.luq.store.domain;

import com.luq.store.valueobjects.Cnpj;
import com.luq.store.valueobjects.Mail;
import com.luq.store.valueobjects.Phone;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class SupplyTest {
    private Supply fakeSupply;
    private Product fakeProduct1, fakeProduct2;

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
        fakeProduct1 = new Product(
            1, "Xbox One Controller", "XOneCont", "Controller for Xbox One Console",
            BigDecimal.valueOf(200.00), fakeSupplier1, user, now, user, now
        );
        fakeProduct2 = new Product(
                2, "Playstation 5 Controller", "PS5Cont", "Controller for Playstation 5 Console",
                BigDecimal.valueOf(250.00), fakeSupplier2, user, now, user, now
        );

        fakeSupply = new Supply(1, 100, fakeProduct1, user, now, user, now);
    }

    @Test
    @DisplayName("Ensure toString method is returning correctly")
    public void testSupplyToStringMethod() {
        assertEquals(
            "Supply(id=1, quantity=100, product=Product(id=1, name=Xbox One Controller))",
            fakeSupply.toString(),
            "Supply toString() method must return on right format"
        );
    }

    @Test
    @DisplayName("Ensure object is being created correctly")
    public void testSupplyCreationGrouped(){
        assertAll(
            () -> assertEquals(1, fakeSupply.getId()),
            () -> assertEquals(100, fakeSupply.getQuantity()),
            () -> assertEquals(fakeProduct1, fakeSupply.getProduct()),
            () -> assertEquals("Jimmy McGill", fakeSupply.getCreatedBy()),
            () -> assertEquals("Jimmy McGill", fakeSupply.getModifiedBy())
        );
    }

    @Test
    @DisplayName("Ensure object is being updated correctly")
    public void testSupplyUpdatedGrouped(){
        LocalDateTime now = LocalDateTime.now();
        String user = "Kim Wexler";

        fakeSupply.setQuantity(200);
        fakeSupply.setProduct(fakeProduct2);
        fakeSupply.setCreatedBy(user);
        fakeSupply.setModifiedBy(user);
        fakeSupply.setCreated(now);
        fakeSupply.setModified(now);

        assertAll(
            () -> assertEquals(200, fakeSupply.getQuantity()),
            () -> assertEquals(fakeProduct2, fakeSupply.getProduct()),
            () -> assertEquals("Kim Wexler", fakeSupply.getCreatedBy()),
            () -> assertEquals("Kim Wexler", fakeSupply.getModifiedBy()),
            () -> assertEquals(now, fakeSupply.getCreated()),
            () -> assertEquals(now, fakeSupply.getModified())
        );
    }

    @Test
    @DisplayName("Supply attributes must not be null")
    public void testSupplyAttributesNotNull(){
        assertAll(
            () -> assertNotNull(fakeSupply.getId()),
            () -> assertNotNull(fakeSupply.getQuantity()),
            () -> assertNotNull(fakeSupply.getCreatedBy()),
            () -> assertNotNull(fakeSupply.getCreated()),
            () -> assertNotNull(fakeSupply.getModifiedBy()),
            () -> assertNotNull(fakeSupply.getModified())
        );
    }
}