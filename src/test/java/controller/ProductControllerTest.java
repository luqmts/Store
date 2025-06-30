package controller;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import model.Product;
import service.ProductService;

public class ProductControllerTest {
    private ProductController pController;
    private ProductService pService;

    @BeforeEach
    public void setUp() {
        pService = mock(ProductService.class);  
        pController = new ProductController(pService);
    }

    @Test
    @DisplayName("Test if products are being registered correctly")
    public void testRegisterProduct() {
        String pSku = "XOneCont";
        String pName = "Xbox One Controller";
        String pDescription = "Controller for Xbox One Console";
        int sId = 1;

        Product fakeProduct = new Product(1, pSku, pName, pDescription, sId);
        when(pService.registerProduct(pSku, pName, pDescription, sId)).thenReturn(fakeProduct);
        Product result = pController.registerProduct(pSku, pName, pDescription, sId);

        assertAll(
            () -> assertNotNull(result),
            () -> assertEquals(result.getSku(), pSku),
            () -> assertEquals(result.getName(), pName),
            () -> assertEquals(pDescription, result.getDescription()),
            () -> assertEquals(sId, result.getSupplierId())
        );
    }

    @Test
    @DisplayName("Test if products are being returned on showAllProducts() method")
    public void testShowAllProducts() {
        Product fakeProduct = new Product(1, "PS5Cont", "PS5 Controller", "Controller for PS5 Console", 1);
        when(pService.showAllProducts()).thenReturn(fakeProduct.toString());
        String result = pController.showAllProducts();

        assertAll(
            () -> assertNotNull(result),
            () -> assertEquals(fakeProduct.toString(), result)
        );
    }

    @Test
    @DisplayName("Test if products are being updated correctly on updateProduct() method")
    public void testUpdateProduct() {
        Product fakeProduct = new Product(1, "PS5Cont", "PS5 Controller", "Controller for PS5 Console", 2);
        
        pController.registerProduct("XOneCont", "Xbox One Controller", "Controller for Xbox One Console", 1);
        when(pService.updateProduct(1, "PS5Cont", "PS5 Controller", "Controller for PS5 Console", 2)).thenReturn(fakeProduct);
        Product result = pController.updateProduct(1, "PS5Cont", "PS5 Controller", "Controller for PS5 Console", 2);

        assertAll(
            () -> assertNotNull(result),
            () -> assertEquals(fakeProduct.getSku(), result.getSku()),
            () -> assertEquals(fakeProduct.getName(), result.getName()),
            () -> assertEquals(fakeProduct.getDescription(), result.getDescription()),
            () -> assertEquals(fakeProduct.getSupplierId(), result.getSupplierId())
        );
    }

    @Test
    @DisplayName("Test if invalid products are being not updated and throwed a exception for")
    public void testUpdateInvalidProduct() {
        when(pService.updateProduct(1, "PS5Cont", "PS5 Controller", "Controller for PS5 Console", 2))
            .thenThrow(new IllegalArgumentException("Product not found"));
            
        assertThrows(
            IllegalArgumentException.class, 
            () -> pController.updateProduct(1, "PS5Cont", "PS5 Controller", "Controller for PS5 Console", 2),
            "Product not found"
        );
    }

    @Test
    @DisplayName("Test if products with invalid supplier are being not updated and throwed a exception for")
    public void testUpdateInvalidSupplier() {
        when(pService.updateProduct(1, "PS5Cont", "PS5 Controller", "Controller for PS5 Console", 5))
            .thenThrow(new IllegalArgumentException("Supplier not found"));

        assertThrows(
            IllegalArgumentException.class, 
            () -> pController.updateProduct(1, "PS5Cont", "PS5 Controller", "Controller for PS5 Console", 5),
            "Supplier not found" 
        );
    }

    @Test
    @DisplayName("Test if products are being removed correctly on removeProduct() method")
    public void testRemoveProduct() {
        Product fakeProduct = new Product(1, "XOneCont", "Xbox One Controller", "Controller for Xbox One Console", 1);
        
        pController.registerProduct("XOneCont", "Xbox One Controller", "Controller for Xbox One Console", 1);
        when(pService.deleteProduct(1)).thenReturn(fakeProduct.getId());
        int id = pController.removeProduct(1);
        
        assertAll(
            () -> assertNotNull(id),
            () -> assertEquals(fakeProduct.getId(), id)
        );
    }

    @Test
    @DisplayName("Test if invalid products are not being removed and throwed a exception for")
    public void testRemoveInvalidProduct() {
        when(pService.deleteProduct(2)).thenThrow(new IllegalArgumentException("Product not found"));
        assertThrows(
            IllegalArgumentException.class, 
            () -> pController.removeProduct(2),
            "Product not found"
        );
    }
}
