package com.luq.store.repositories;

import com.luq.store.domain.Customer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CustomerRepositoryTest {
    @Autowired
    private CustomerRepository cRepository;

    @Test
    @DisplayName("Test if Customer filtered by name is being returned correctly")
    public void testFindByName() {
        Customer fakeCustomer1 = new Customer("Test Customer 01");
        Customer fakeCustomer2 = new Customer("Test Customer 02");

        cRepository.save(fakeCustomer1);
        cRepository.save(fakeCustomer2);

        Sort sort = Sort.by("name").ascending();
        List<Customer> result = cRepository.findByNameIgnoreCase(sort, "Test Customer 01");

        assertAll(
            () -> assertEquals(1, result.size()),
            () -> assertEquals(fakeCustomer1, result.getFirst())
        );
    }
}
