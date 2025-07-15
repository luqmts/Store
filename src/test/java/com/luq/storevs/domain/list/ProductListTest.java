/*package com.luq.storevs.model.list;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.luq.storevs.model.Product;
import com.luq.storevs.model.Supplier;
import com.luq.storevs.valueobjects.Cnpj;
import com.luq.storevs.valueobjects.Mail;
import com.luq.storevs.valueobjects.Phone;

public class ProductListTest {
    Product p1, p2, p3, p4;
    Supplier s1, s2, s3, s4;
    ProductList pList;

    @BeforeEach
    public void setUp(){
        s1 = new Supplier(
            1, "Sony Brasil LTDA.", new Cnpj("43.447.044/0004-10"), new Mail("sony@mail.com"), new Phone("11000001111")
        );
        s2 = new Supplier(
            2, "Microsoft Brasil LTDA.", new Cnpj("33.652.161/0001-19"), new Mail("microsoft@mail.com"), new Phone("21985855858")
        );
        s3 = new Supplier(
            3, "8BitDO LTDA.", new Cnpj("37.835.617/0001-37"), new Mail("8bitdo@mail.com"), new Phone("85990909090")
        );

        p1 = new Product(1, "PS4Cont", "PS4 Controller", "Controller for PS4 Console", s1.getId());
        p2 = new Product(2, "XOneCont", "Xbox One Controller", "Controller for Xbox One Console", s2.getId());
        p3 = new Product(3, "8BitDOUltCont", "8BitDO Ultimate Controller", "Controller from 8BitDO for Nintendo Switch", s3.getId());

        pList = new ProductList();

        pList.add(p1);
        pList.add(p2);
        pList.add(p3);
    }
    
    @Test
    @DisplayName("Assert all products created are being returned in getAllProducts method")
    public void testGetAllProducts(){
        assertAll(
            () -> assertNotNull(pList.get()),
            () -> assertEquals(3, pList.get().size())
        );
    }
    
    @Test
    @DisplayName("Assert product returned on method get by id is being returned correctly")
    public void testGetProductById(){
        Product product = pList.getById(2);
        assertAll(
            () -> assertInstanceOf(Product.class, product),
            () -> assertEquals(2, product.getId()),
            () -> assertEquals("XOneCont", product.getSku()),
            () -> assertEquals("Xbox One Controller", product.getName()),
            () -> assertEquals("Controller for Xbox One Console", product.getDescription()),
            () -> assertEquals(s2.getId(), product.getSupplier_id())
        );
    }

    @Test
    @DisplayName("Assert product returned on method get by sku is being returned correctly")
    public void testGetProductBySku(){
        Product product = pList.getBySku("XOneCont");
        assertAll(
            () -> assertInstanceOf(Product.class, product),
            () -> assertEquals(2, product.getId()),
            () -> assertEquals("XOneCont", product.getSku()),
            () -> assertEquals("Xbox One Controller", product.getName()),
            () -> assertEquals("Controller for Xbox One Console", product.getDescription()),
            () -> assertEquals(s2.getId(), product.getSupplier_id())
        );
    }

    @Test
    @DisplayName("Assert products returned on method get by supplier is being returned correctly")
    public void testGetProductBySupplier(){
        ArrayList<Product> pListBySupplier = pList.getBySupplier(s2);

        assertAll(
            () -> assertNotNull(pListBySupplier),
            () -> assertEquals(1, pListBySupplier.size()),
            () -> assertEquals(2, pListBySupplier.get(0).getId()),
            () -> assertEquals("XOneCont", pListBySupplier.get(0).getSku()),
            () -> assertEquals("Xbox One Controller", pListBySupplier.get(0).getName()),
            () -> assertEquals("Controller for Xbox One Console", pListBySupplier.get(0).getDescription()),
            () -> assertEquals(s2.getId(), pListBySupplier.get(0).getSupplier_id())
        );
    }
    
    @Test
    @DisplayName("Assert if no products are found in pList return NoSuchElementException")
    public void testgetNoItemFound(){
        pList.removeById(1);
        pList.removeById(2);
        pList.removeById(3);

        assertThrows(NoSuchElementException.class, () -> pList.get());
    }

    @Test
    @DisplayName("For a id that is not in Product List must return NoSuchElementException")
    public void testGetProductByIdNoItemFound(){
        assertThrows(NoSuchElementException.class, () -> pList.getById(10));
    }

    @Test
    @DisplayName("For a sku that is not in Product List must return NoSuchElementException")
    public void testGetProductBySkuNoItemFound(){
        assertThrows(NoSuchElementException.class, () -> pList.getBySku("NSwitch2Cont"));
    }

    @Test
    @DisplayName("For a supplier that don't have any products registered in Product List must return NoSuchElementException")
    public void testGetProductBySupplierNoItemFound(){
        s4 = new Supplier(
            "Nintendo", new Cnpj("08.592.899/0001-90"), new Mail("nintendo@mail.com"), new Phone("85943527697")
        );
        assertThrows(NoSuchElementException.class, () -> pList.getBySupplier(s4));
    }

    @Test
    @DisplayName("Assert pList not null and new product was added succesfully")
    public void testProductsAddedCorrectly(){
        s4 = new Supplier(
            "Nintendo", new Cnpj("08.592.899/0001-90"), new Mail("nintendo@mail.com"), new Phone("85943527697")
        );
        p4 = new Product(4, "NSwitch2Cont", "Nintendo Switch 2 Controller", "Controller from Nintendo for Nintendo Switch 2", s4.getId());
        pList.add(p4);

        assertAll(
            () -> assertNotNull(pList.get()),
            () -> assertEquals(4, pList.get().size())
        );
    }

    @Test
    @DisplayName("Assert product is correctly being removed by product's index")
    public void testProductRemovedByIndexCorrectly(){
        pList.removeByIndex(1);
        
        assertAll(
            () -> assertEquals(2, pList.get().size()),
            () -> assertThrows(IndexOutOfBoundsException.class, () -> pList.get().get(2))
        );
    }

    @Test
    @DisplayName("Assert product is correctly being removed by product's id and get by id is working")
    public void testProductRemovedByIdCorrectly(){
        pList.removeById(2);
        
        assertAll(
            () -> assertEquals(2, pList.get().size()),
            () -> assertThrows(NoSuchElementException.class, () -> pList.getById(2))
        );
    }
}
 */