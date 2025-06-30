package service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import database.DAO.DAO;
import model.Product;
import model.list.ProductList;
import model.Supplier;
import model.list.SupplierList;
import valueobjects.CNPJ;
import valueobjects.Mail;
import valueobjects.Phone;

public class ProductServiceTest {
    ProductService pService;
    DAO<Product, ProductList> pDao;
    DAO<Supplier, SupplierList> sDao;
    Supplier fakeSupplier1, fakeSupplier2;
    Product fakeProduct, result;

    @BeforeEach
    public void setUp(){
        pDao = mock(DAO.class);
        sDao = mock(DAO.class);

        pService = new ProductService(pDao, sDao);

        fakeSupplier1 = new Supplier(
            1,
            "Microsoft Brasil LTDA.", 
            new CNPJ("43.447.044/0004-10"), 
            new Mail("microsoft@mail.com"), 
            new Phone("11000001111")
        );
        fakeSupplier2 = new Supplier(
            2,
            "Sony Brasil LTDA.", 
            new CNPJ("04.542.534/0001-09"), 
            new Mail("sony@mail.com"), 
            new Phone("11222225555")
        );
    }
    
    @Test
    @DisplayName("Test if product with correct parameters is being registered successfully")
    public void testRegisterProduct(){
        when(sDao.getById(1)).thenReturn(fakeSupplier1);
        result = pService.registerProduct("XOneCont","Xbox One Controller", "Controller for Xbox One Console", 1);
        assertAll(
            () -> assertNotNull(result),
            () -> assertInstanceOf(Product.class, result),
            () -> assertEquals("XOneCont", result.getSku()),
            () -> assertEquals("Xbox One Controller", result.getName()),
            () -> assertEquals("Controller for Xbox One Console", result.getDescription()),
            () -> assertEquals(1, result.getSupplierId())
        );
    }

    @Test
    @DisplayName("Test if product with invalid supplier is not being registered and throwed a exception for")
    public void testRegisterProductInvalidSupplier() {
        assertThrows(
            IllegalArgumentException.class,
            () ->  pService.registerProduct("XOneCont","Xbox One Controller", "Controller for Xbox One Console", 1),
            "Supplier not found"
        );
    }

    @Test
    @DisplayName("Test if product with correct parameters is being updated successfully")
    public void testUpdateProduct(){
        when(sDao.getById(1)).thenReturn(fakeSupplier1);
        when(sDao.getById(2)).thenReturn(fakeSupplier2);
        fakeProduct = pService.registerProduct("XOneCont","Xbox One Controller", "Controller for Xbox One Console", 1);

        when(pDao.getById(1)).thenReturn(fakeProduct);
        Product result = pService.updateProduct(1, "PS5Cont", "PS5 Controller", "Controller for PS5 Console", 2);
        assertAll(
            () -> assertNotNull(result),
            () -> assertInstanceOf(Product.class, result),
            () -> assertEquals("PS5Cont", result.getSku()),
            () -> assertEquals("PS5 Controller", result.getName()),
            () -> assertEquals("Controller for PS5 Console", result.getDescription()),
            () -> assertEquals(2, result.getSupplierId())
        );
    }

    @Test 
    @DisplayName("Test if a invalid product is not being updated successfully because product id is invalid")
    void testUpdateProductInvalidProduct(){
        when(sDao.getById(1)).thenReturn(fakeSupplier1);
        when(sDao.getById(2)).thenReturn(fakeSupplier2);
        Product product = pService.registerProduct("XOneCont","Xbox One Controller", "Controller for Xbox One Console", 1);
        when(pDao.getById(1)).thenReturn(product);

        assertThrows(
            IllegalArgumentException.class, 
            () -> pService.updateProduct(2, "PS5Cont", "PS5 Controller", "Controller for PS5 Console", 2),
            "Product not found"
        );
    }

    @Test 
    @DisplayName("Test if a invalid product is not being updated successfully because supplier id is invalid")
    void testUpdateProductInvalidSupplier(){
        when(sDao.getById(1)).thenReturn(fakeSupplier1);
        Product product = pService.registerProduct("XOneCont","Xbox One Controller", "Controller for Xbox One Console", 1);
        when(pDao.getById(1)).thenReturn(product);

        assertThrows(
            IllegalArgumentException.class,
            () -> pService.updateProduct(1, "PS5Cont", "PS5 Controller", "Controller for PS5 Console", 2),
            "Product not found"
        );
    }

    @Test
    @DisplayName("Test if a product is being deleted correctly")
    void testDeleteProduct(){
        when(sDao.getById(1)).thenReturn(fakeSupplier1);
        Product product = pService.registerProduct("XOneCont","Xbox One Controller", "Controller for Xbox One Console", 1);
        when(pDao.getById(1)).thenReturn(product);
        int id = pService.deleteProduct(1);

        assertAll(
            () -> assertNotNull(id),
            () -> assertEquals(1, id)
        );
    }

    @Test
    @DisplayName("Test if a product is being deleted correctly")
    void testDeleteInvalidProduct(){
       assertThrows(
            IllegalArgumentException.class,
            () ->  pService.deleteProduct(1),
            "Product not found"
        );
    }

    @Test
    @DisplayName("Test if showAllProducts method is returning correctly")
    void testShowAllProducts(){
        ProductList pList = new ProductList();
        Product fakeProduct1 = new Product(1, "XOneCont","Xbox One Controller", "Controller for Xbox One Console", 1);
        Product fakeProduct2 = new Product(2, "PS5Cont", "PS5 Controller", "Controller for PS5 Console", 2);
        pList.add(fakeProduct1);
        pList.add(fakeProduct2);

        when(pDao.get()).thenReturn(pList);

        String result = pService.showAllProducts();

        assertAll(
            () -> assertNotNull(result),
            () -> assertEquals(fakeProduct1.toString() + "\n" + fakeProduct2.toString(), result)
        );
    }

    @Test
    @DisplayName("Test if showAllProducts method is returning message that no product is registered if there are no items.")
    void testShowAllProductsNoItem(){
        ProductList pList = new ProductList();

        when(pDao.get()).thenReturn(pList);
        
        assertThrows(
            NoSuchElementException.class, 
            () -> pService.showAllProducts(),
            "No items registered"
        );
    }
}