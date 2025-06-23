package models;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


public class ProductTest {
    Product product;

    @BeforeEach
    void setUp(){
        product = new Product(1, "PS4", "Playstation 4", "Video Game Console by Sony.");
    }

    @Test    
    @DisplayName("Ensure toString method is returning correctly")
    void testProductToStringMethod() {
        assertEquals(product.toString(), "[PS4] Playstation 4", "Product toString() method must return on right format");
    }

    @Test
    @DisplayName("Ensure object is being created correctly")
    void testProductCreationGrouped() {
        assertAll(
            () -> assertEquals(product.getPid(), 1),
            () -> assertEquals(product.getSku(), "PS4"),
            () -> assertEquals(product.getName(), "Playstation 4"),
            () -> assertEquals(product.getDescription(), "Video Game Console by Sony.")
        );
    }

    @Test
    @DisplayName("Ensure object is being updated correctly")
    void testProductUpdateGrouped() {
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
}
