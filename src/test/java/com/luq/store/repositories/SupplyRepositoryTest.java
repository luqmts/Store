package com.luq.store.repositories;

import com.luq.store.domain.Product;
import com.luq.store.domain.Supplier;
import com.luq.store.domain.Supply;
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
public class SupplyRepositoryTest {
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private SupplyRepository supplyRepository;
    @Autowired
    private ProductRepository pRepository;

    Supply fakeSupply1;
    Supplier fakeSupplier1;
    Product fakeProduct1;

    @BeforeEach
    public void setUp(){
        fakeSupplier1 = supplierRepository.save(new Supplier(
            "Microsoft Brasil LTDA.", new Cnpj("43.447.044/0004-10"),
            new Mail("microsoft@mail.com"), new Phone("11000001111")
        ));

        fakeProduct1 = pRepository.save(new Product(
            "XOneCont", "Xbox One Controller", "Controller for Xbox One Console",
            fakeSupplier1, BigDecimal.valueOf(200.00)
        ));

        fakeSupply1 = supplyRepository.save(new Supply(20, fakeProduct1));
    }

    @Test
    @DisplayName("Test if Supply filtered by only one filter is being returned correctly")
    public void testFindByOneFilter() {
        Sort sort = Sort.by("id").ascending();
        List<Supply> result = supplyRepository.getAllByProductId(sort, fakeProduct1.getId());

        assertAll(
            () -> assertEquals(1, result.size()),
            () -> assertEquals(fakeSupply1, result.getFirst())
        );
    }
}
