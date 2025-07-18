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
    Product product;
    Supplier supplier;

    @BeforeEach
    public void setUp(){
        String user = "Jimmy McGill";
        LocalDateTime now = LocalDateTime.now();

        supplier = new Supplier(
            1, "Sony Brasil LTDA.", new Cnpj("43.447.044/0004-10"),
            new Mail("sony@mail2.com"), new Phone("11000001111"),
            user, now, user, now
        );
        product = new Product(
            1, "Playstation 4", "PS4", "Video Game Console by Sony.", 2000.00F, supplier,
            user, now, user, now
        );
    }

    @Test
    @DisplayName("Ensure toString method is returning correctly")
    public void testProductToStringMethod() {
        assertEquals(
            "Product(id=1, name=Playstation 4)",
            product.toString(),
            "Product toString() method must return on right format"
        );
    }

    @Test
    @DisplayName("Ensure object is being created correctly")
    public void testProductCreationGrouped() {
        assertAll(
            () -> assertEquals("PS4", product.getSku()),
            () -> assertEquals("Playstation 4", product.getName()),
            () -> assertEquals("Video Game Console by Sony.", product.getDescription()),
            () -> assertEquals(2000.00F, product.getPrice()),
            () -> assertEquals("Jimmy McGill", product.getCreatedBy()),
            () -> assertEquals("Jimmy McGill", product.getModifiedBy())
        );
    }

    @Test
    @DisplayName("Ensure object is being updated correctly")
    public void testProductUpdateGrouped() {
        LocalDateTime now = LocalDateTime.now();
        String user = "Kim Wexler";

        product.setSku("PS5");
        product.setName("Playstation 5");
        product.setDescription("A new generation Video Game Console by Sony.");
        product.setPrice(4000.00F);
        product.setCreatedBy(user);
        product.setModifiedBy(user);
        product.setCreated(now);
        product.setModified(now);

        assertAll(
            () -> assertEquals("PS5", product.getSku()),
            () -> assertEquals("Playstation 5", product.getName()),
            () -> assertEquals(4000.00F, product.getPrice()),
            () -> assertEquals("A new generation Video Game Console by Sony.", product.getDescription()),
            () -> assertEquals("Kim Wexler", product.getCreatedBy()),
            () -> assertEquals("Kim Wexler", product.getModifiedBy()),
            () -> assertEquals(now, product.getCreated()),
            () -> assertEquals(now, product.getModified())
        );
    }

    @Test
    @DisplayName("Product attributes must not be null")
    public void testProductAttributesNotNull(){
        assertAll(
            () -> assertNotNull(product.getName()),
            () -> assertNotNull(product.getSku()),
            () -> assertNotNull(product.getDescription()),
            () -> assertNotNull(product.getSupplier()),
            () -> assertNotNull(product.getPrice()),
            () -> assertNotNull(product.getCreatedBy()),
            () -> assertNotNull(product.getCreated()),
            () -> assertNotNull(product.getModifiedBy()),
            () -> assertNotNull(product.getModified())
        );
    }

    @Test
    @DisplayName("Ensure Product's Supplier is being instanced correctly")
    public void testProductSupplierIsValid(){
        assertAll(
            () -> assertEquals(supplier.getId(), product.getSupplier().getId()),
            () -> assertInstanceOf(Supplier.class, product.getSupplier())
        );
    }
}