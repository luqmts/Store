/*package com.luq.storevs.controller;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.luq.storevs.model.Product;
import com.luq.storevs.service.ProductService;

@Disabled
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
    @Disabled
    public void testRegisterProduct() {
        String pSku = "XOneCont";
        String pName = "Xbox One Controller";
        String pDescription = "Controller for Xbox One Console";
        int sId = 1;

        Product fakeProduct = new Product(1, pSku, pName, pDescription, sId);
        when(pService.register(new Product(pSku, pName, pDescription, sId))).thenReturn(fakeProduct);
        Product result = pController.registerProduct(new Product(pSku, pName, pDescription, sId));

        assertAll(
            () -> assertNotNull(result),
            () -> assertEquals(result.getSku(), pSku),
            () -> assertEquals(result.getName(), pName),
            () -> assertEquals(pDescription, result.getDescription()),
            () -> assertEquals(sId, result.getSupplier_id())
        );
    }

    @Test
    @DisplayName("Test if products are being returned on showAllProducts() method")
    @Disabled
    public void testShowAllProducts() {
        List<Product> pList;
        Product fakeProduct = new Product(1, "PS5Cont", "PS5 Controller", "Controller for PS5 Console", 1);
        pList.add(fakeProduct);
        
        when(pService.getAll()).thenReturn(pList);
        List<Product> result = pController.getProducts();

        assertAll(
            () -> assertNotNull(result),
            () -> assertEquals(1, pList.size())
        );
    }

    @Test
    @DisplayName("Test if products are being updated correctly on updateProduct() method")
    @Disabled
    public void testUpdateProduct() {
        Product fakeProduct = new Product(1, "PS5Cont", "PS5 Controller", "Controller for PS5 Console", 2);
        
        pController.registerProduct(new Product("XOneCont", "Xbox One Controller", "Controller for Xbox One Console", 1));
        when(pService.update(1, new Product("PS5Cont", "PS5 Controller", "Controller for PS5 Console", 2))).thenReturn(fakeProduct);
        Product result = pController.updateProduct(1, new Product("PS5Cont", "PS5 Controller", "Controller for PS5 Console", 2));

        assertAll(
            () -> assertNotNull(result),
            () -> assertEquals(fakeProduct.getSku(), result.getSku()),
            () -> assertEquals(fakeProduct.getName(), result.getName()),
            () -> assertEquals(fakeProduct.getDescription(), result.getDescription()),
            () -> assertEquals(fakeProduct.getSupplier_id(), result.getSupplier_id())
        );
    }

    @Test
    @DisplayName("Test if invalid products are being not updated and throwed a exception for")
    @Disabled
    public void testUpdateInvalidProduct() {
        when(pService.update(1, new Product("PS5Cont", "PS5 Controller", "Controller for PS5 Console", 2)))
            .thenThrow(new IllegalArgumentException("Product not found"));
            
        assertThrows(
            IllegalArgumentException.class, 
            () -> pController.updateProduct(1, new Product(1, "PS5Cont", "PS5 Controller", "Controller for PS5 Console", 2)),
            "Product not found"
        );
    }

    @Test
    @DisplayName("Test if products with invalid supplier are being not updated and throwed a exception for")
    @Disabled
    public void testUpdateInvalidSupplier() {
        when(pService.update(1, new Product(1, "PS5Cont", "PS5 Controller", "Controller for PS5 Console", 5)))
            .thenThrow(new IllegalArgumentException("Supplier not found"));

        assertThrows(
            IllegalArgumentException.class, 
            () -> pController.updateProduct(1, new Product(1, "PS5Cont", "PS5 Controller", "Controller for PS5 Console", 5)),
            "Supplier not found" 
        );
    }

    @Test
    @DisplayName("Test if products are being removed correctly on removeProduct() method")
    @Disabled
    public void testRemoveProduct() {
        Product fakeProduct = new Product(1, "XOneCont", "Xbox One Controller", "Controller for Xbox One Console", 1);
        
        pController.registerProduct(new Product("XOneCont", "Xbox One Controller", "Controller for Xbox One Console", 1));
        pController.removeProduct(1);
    }
}
*/