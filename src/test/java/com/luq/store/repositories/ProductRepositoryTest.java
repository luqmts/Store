package com.luq.store.repositories;

import com.luq.store.domain.*;
import com.luq.store.valueobjects.Cnpj;
import com.luq.store.valueobjects.Mail;
import com.luq.store.valueobjects.Phone;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ProductRepositoryTest {
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private SupplyRepository supplyRepository;
    @Autowired
    private ProductRepository pRepository;

    Supplier fakeSupplier1, fakeSupplier2;
    Product fakeProduct1, fakeProduct2;
    Supply fakeSupply1, fakeSupply2;

    @BeforeEach
    public void setUp(){
        fakeSupplier1 = supplierRepository.save(new Supplier(
            "Microsoft Brasil LTDA.", new Cnpj("43.447.044/0004-10"),
            new Mail("microsoft@mail.com"), new Phone("11000001111")
        ));
        fakeSupplier2 = supplierRepository.save(new Supplier(
            "Sony Brasil LTDA.", new Cnpj("04.542.534/0001-09"),
            new Mail("sony@mail.com"), new Phone("11222225555")
        ));

        fakeProduct1 = pRepository.save(new Product(
            "XOneCont", "Xbox One Controller", "Controller for Xbox One Console",
            fakeSupplier1, BigDecimal.valueOf(200.00)
        ));
        fakeProduct2 = pRepository.save(new Product(
            "PS5Cont", "Playstation 5 Controller", "Controller for Playstation 5 Console",
            fakeSupplier2, BigDecimal.valueOf(250.00)
        ));
    }

    @Test
    @DisplayName("Test if Product filtered by only one filter is being returned correctly")
    public void testFindBySupplierIdAndNameAndSkuOneFilter() {
        Sort sort = Sort.by("id").ascending();
        List<Product> result = pRepository.findBySupplierIdAndNameAndSku(sort, 1, null, null);

        assertAll(
            () -> assertEquals(1, result.size()),
            () -> assertEquals(fakeProduct1, result.getFirst())
        );
    }

    @Test
    @DisplayName("Test if Product filtered by no filter is being returned correctly")
    public void testFindBySupplierIdAndNameAndSkuNoFilter() {
        Sort sort = Sort.by("id").ascending();
        List<Product> result = pRepository.findBySupplierIdAndNameAndSku(sort, null, null, null);

        assertAll(
            () -> assertEquals(2, result.size()),
            () -> assertEquals(this.fakeProduct1, result.getFirst()),
            () -> assertEquals(this.fakeProduct2, result.getLast())
        );
    }

    @Test
    @DisplayName("Test if Product filtered by all filters is being returned correctly")
    public void testFindBySupplierIdAndNameAndSkuAllFilters() {
        Sort sort = Sort.by("id").ascending();
        List<Product> result = pRepository.findBySupplierIdAndNameAndSku(sort, 2, "Playstation 5 Controller", "PS5Cont");

        assertAll(
            () -> assertEquals(1, result.size()),
            () -> assertEquals(fakeProduct2, result.getFirst())
        );
    }

    @Test
    @DisplayName("Test if fakeProduct2 is being returned because this product has no register on supply")
    public void testFindAllProductsNotRegisteredOnSupplyWithProducts(){
        supplyRepository.save(new Supply(
            20, fakeProduct1
        ));

        List<Product> result = pRepository.findAllNotRegisteredOnSupply();
        assertAll(
            () -> assertEquals(1, result.size()),
            () -> assertEquals(fakeProduct2, result.getFirst())
        );
    }

    @Test
    @DisplayName("Test if no product is being returned because every product is registered on Supply")
    public void testFindAllProductsNotRegisteredOnSupplyNoProducts(){
        supplyRepository.save(new Supply(
                20, fakeProduct1
        ));
        supplyRepository.save(new Supply(
            50, fakeProduct2
        ));

        List<Product> result = pRepository.findAllNotRegisteredOnSupply();
        assertEquals(0, result.size());
    }

    @Test
    @DisplayName("Test if only fakeProduct1 is returned, fakeProduct2 has no quantity registered and is not returned")
    public void testFindAllProductsRegisteredOnSupply(){
        supplyRepository.save(new Supply(
            20, fakeProduct1
        ));
        supplyRepository.save(new Supply(
            0, fakeProduct2
        ));

        List<Product> result = pRepository.findAllRegisteredOnSupply();
        assertAll(
            () -> assertEquals(1, result.size()),
            () -> assertEquals(fakeProduct1, result.getFirst())
        );
    }

    @Test
    @DisplayName("Test if a product is being returned by supply id on supply table")
    public void testFindAllProductsRegisteredOnSupplyById(){
        supplyRepository.save(new Supply(
            20, fakeProduct1
        ));

        List<Product> result = pRepository.findAllRegisteredOnSupply(1);
        assertAll(
            () -> assertEquals(1, result.size()),
            () -> assertEquals(fakeProduct1, result.getFirst())
        );
    }
}
