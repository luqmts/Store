package com.luq.store.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.luq.store.valueobjects.Cnpj;
import com.luq.store.valueobjects.Mail;
import com.luq.store.valueobjects.Phone;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class ProductTest {
    private final LocalDateTime now = LocalDateTime.now();

    private Product fakeProduct;
    private Supplier fakeSupplier;

    @BeforeEach
    public void setUp(){
        String user = "Jimmy McGill";

        fakeSupplier = new Supplier(
            1, "Sony Brasil LTDA.", new Cnpj("43.447.044/0004-10"),
            new Mail("sony@mail2.com"), new Phone("11000001111"),
            user, now, user, now
        );
        fakeProduct = new Product(
            1, "Playstation 4", "PS4", "Video Game Console by Sony.", 2000.00F, fakeSupplier,
            user, now, user, now
        );
    }

    @Test
    @DisplayName("Ensure toString method is returning correctly")
    public void testProductToStringMethod() {
        assertEquals(
            "Product(id=1, name=Playstation 4)",
            fakeProduct.toString(),
            "Product toString() method must return on right format"
        );
    }

    @Test
    @DisplayName("Ensure object is being created correctly")
    public void testProductCreationGrouped() {
        assertAll(
            () -> assertEquals(1, fakeProduct.getId()),
            () -> assertEquals("PS4", fakeProduct.getSku()),
            () -> assertEquals("Playstation 4", fakeProduct.getName()),
            () -> assertEquals("Video Game Console by Sony.", fakeProduct.getDescription()),
            () -> assertEquals(2000.00F, fakeProduct.getPrice()),
            () -> assertEquals(now, fakeProduct.getCreated()),
            () -> assertEquals("Jimmy McGill", fakeProduct.getModifiedBy()),
            () -> assertEquals(now, fakeProduct.getModified()),
            () -> assertEquals("Jimmy McGill", fakeProduct.getModifiedBy())
        );
    }

    @Test
    @DisplayName("Ensure object is being updated correctly")
    public void testProductUpdateGrouped() {
        LocalDateTime now = LocalDateTime.now();
        String user = "Kim Wexler";

        fakeProduct.setSku("PS5");
        fakeProduct.setName("Playstation 5");
        fakeProduct.setDescription("A new generation Video Game Console by Sony.");
        fakeProduct.setPrice(4000.00F);
        fakeProduct.setCreatedBy(user);
        fakeProduct.setModifiedBy(user);
        fakeProduct.setCreated(now);
        fakeProduct.setModified(now);

        assertAll(
            () -> assertEquals("PS5", fakeProduct.getSku()),
            () -> assertEquals("Playstation 5", fakeProduct.getName()),
            () -> assertEquals(4000.00F, fakeProduct.getPrice()),
            () -> assertEquals("A new generation Video Game Console by Sony.", fakeProduct.getDescription()),
            () -> assertEquals("Kim Wexler", fakeProduct.getCreatedBy()),
            () -> assertEquals(now, fakeProduct.getCreated()),
            () -> assertEquals("Kim Wexler", fakeProduct.getModifiedBy()),
            () -> assertEquals(now, fakeProduct.getModified())
        );
    }

    @Test
    @DisplayName("Product attributes must not be null")
    public void testProductAttributesNotNull(){
        assertAll(
            () -> assertNotNull(fakeProduct.getId()),
            () -> assertNotNull(fakeProduct.getName()),
            () -> assertNotNull(fakeProduct.getSku()),
            () -> assertNotNull(fakeProduct.getDescription()),
            () -> assertNotNull(fakeProduct.getSupplier()),
            () -> assertNotNull(fakeProduct.getPrice()),
            () -> assertNotNull(fakeProduct.getCreatedBy()),
            () -> assertNotNull(fakeProduct.getCreated()),
            () -> assertNotNull(fakeProduct.getModifiedBy()),
            () -> assertNotNull(fakeProduct.getModified())
        );
    }

    @Test
    @DisplayName("Ensure Product's Supplier is being instanced correctly")
    public void testProductSupplierIsValid(){
        assertAll(
            () -> assertEquals(fakeSupplier.getId(), fakeProduct.getSupplier().getId()),
            () -> assertInstanceOf(Supplier.class, fakeProduct.getSupplier())
        );
    }
}