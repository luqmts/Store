package models;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import valueobjects.CNPJ;
import valueobjects.Mail;
import valueobjects.Phone;

public class ProductTest {
    Product product;
    Supplier supplier;

    @BeforeEach
    public void setUp(){
        supplier = new Supplier(
            "Sony Brasil LTDA.", 
            new CNPJ("43.447.044/0004-10"), 
            new Mail("sony@mail.com"), 
            new Phone("11000001111")
        );
        product = new Product(1, "PS4", "Playstation 4", "Video Game Console by Sony.", supplier);
    }

    @Test    
    @DisplayName("Ensure toString method is returning correctly")
    public void testProductToStringMethod() {
        assertEquals(product.toString(), "[PS4] Playstation 4", "Product toString() method must return on right format");
    }

    @Test
    @DisplayName("Ensure object is being created correctly")
    public void testProductCreationGrouped() {
        assertAll(
            () -> assertEquals(product.getPid(), 1),
            () -> assertEquals(product.getSku(), "PS4"),
            () -> assertEquals(product.getName(), "Playstation 4"),
            () -> assertEquals(product.getDescription(), "Video Game Console by Sony.")
        );
    }

    @Test
    @DisplayName("Ensure object is being updated correctly")
    public void testProductUpdateGrouped() {
        product.setPid(2);
        product.setSku("PS5");
        product.setName("Playstation 5");
        product.setDescription("A new generation Video Game Console by Sony.");

        assertAll(
            () -> assertEquals(product.getPid(), 2),
            () -> assertEquals(product.getSku(), "PS5"),
            () -> assertEquals(product.getName(), "Playstation 5"),
            () -> assertEquals(product.getDescription(), "A new generation Video Game Console by Sony.")
        );
    }
    
    @Test
    @DisplayName("Ensure Product's Supplier is being instacied correctly")
    public void testProductSupplierIsIntanciedCorrect(){
        assertAll(
            () -> assertNotNull(product.getSupplier()),
            () -> assertInstanceOf(Supplier.class, product.getSupplier())
        );
    }

    @Test
    @DisplayName("Ensure Product's Supplier is with right values")
    public void testProductSupplierRightValues(){
        assertAll(
            () -> assertEquals("Sony Brasil LTDA.", product.getSupplier().getName()),
            () -> assertEquals("43.447.044/0004-10", product.getSupplier().getCNPJ().toString()),
            () -> assertEquals("sony@mail.com", product.getSupplier().getMail().toString()),
            () -> assertEquals("11000001111", product.getSupplier().getPhone().toString())
        );
    }

    @Test
    @DisplayName("Ensure Product's Supplier is updated and values are updated correctly")
    public void testProductSupplierUpdatedWithRightValues(){
        Supplier newSupplier = new Supplier(
            "Sanic Not Brasil LTDA.", 
            new CNPJ("57.437.753/0001-86"),
            new Mail("sanic@gmail.com"),
            new Phone("85111110000")
        );

        product.setSupplier(newSupplier);

        assertAll(
            () -> assertEquals("Sanic Not Brasil LTDA.", product.getSupplier().getName()),
            () -> assertEquals("57.437.753/0001-86", product.getSupplier().getCNPJ().toString()),
            () -> assertEquals("sanic@gmail.com", product.getSupplier().getMail().toString()),
            () -> assertEquals("85111110000", product.getSupplier().getPhone().toString())
        );
    }
}
