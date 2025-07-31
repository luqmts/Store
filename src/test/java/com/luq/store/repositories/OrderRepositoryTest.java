package com.luq.store.repositories;

import com.luq.store.domain.*;
import com.luq.store.domain.Customer;
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
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class OrderRepositoryTest {
    @Autowired
    private OrderRepository oRepository;
    @Autowired
    private CustomerRepository cRepository;
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private ProductRepository pRepository;
    @Autowired
    private SellerRepository sellerRepository;

    Customer fakeCustomer1, fakeCustomer2;
    Supplier fakeSupplier1, fakeSupplier2;
    Product fakeProduct1, fakeProduct2;
    Seller fakeSeller1, fakeSeller2;
    Order fakeOrder1, fakeOrder2;

    @BeforeEach
    public void setUp(){
        fakeCustomer1 = cRepository.save(new Customer("Test Customer 01"));
        fakeCustomer2 = cRepository.save(new Customer("Test Customer 02"));

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

        fakeSeller1 = sellerRepository.save(new Seller(
            "Walter White", new Mail("WalterWhite@Cooking.com"),
            new Phone("11901010101"), Department.FOOD
        ));
        fakeSeller2 = sellerRepository.save(new Seller(
            "Jesse Pinkman", new Mail("Jesse Pinkman@Cooking.com"),
            new Phone("11904040404"), Department.FOOD
        ));

        fakeOrder1 = oRepository.save(new Order(
            BigDecimal.valueOf(400.00F), 5, LocalDate.now(), fakeProduct1, fakeSeller1, fakeCustomer1
        ));
        fakeOrder2 = oRepository.save(new Order(
            BigDecimal.valueOf(800.00F), 8, LocalDate.now(), fakeProduct2, fakeSeller2, fakeCustomer2
        ));
    }

    @Test
    @DisplayName("Test if Order filtered by only one filter is being returned correctly")
    public void testFindByOneFilter() {
        Sort sort = Sort.by("id").ascending();
        List<Order> result = oRepository.findByProductSellerCustomer(sort, null, 1, null);

        assertAll(
            () -> assertEquals(1, result.size()),
            () -> assertEquals(this.fakeOrder1, result.getFirst())
        );
    }

    @Test
    @DisplayName("Test if Order filtered by no filter is being returned correctly")
    public void testFindByNoFilter() {
        Sort sort = Sort.by("id").ascending();
        List<Order> result = oRepository.findByProductSellerCustomer(sort, null, null, null);

        assertAll(
            () -> assertEquals(2, result.size()),
            () -> assertEquals(this.fakeOrder1, result.getFirst()),
            () -> assertEquals(this.fakeOrder2, result.getLast())
        );
    }

    @Test
    @DisplayName("Test if Order filtered by all filters is being returned correctly")
    public void testFindByAllFilters() {
        Sort sort = Sort.by("id").ascending();
        List<Order> result = oRepository.findByProductSellerCustomer(sort, 2, 2, 2);

        assertAll(
            () -> assertEquals(1, result.size()),
            () -> assertEquals(this.fakeOrder2, result.getFirst())
        );
    }
}
