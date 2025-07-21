package com.luq.store.repositories;

import com.luq.store.domain.*;
import com.luq.store.valueobjects.Mail;
import com.luq.store.valueobjects.Phone;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class SellerRepositoryTest {
    @Autowired
    private SellerRepository sRepository;

    Seller fakeSeller1, fakeSeller2;

    @BeforeEach
    public void setUp(){
        fakeSeller1 = sRepository.save(new Seller(
            "Walter White", new Mail("WalterWhite@Cooking.com"),
            new Phone("11901010101"), Department.FOOD
        ));
        fakeSeller2 = sRepository.save(new Seller(
            "Jesse Pinkman", new Mail("Jesse Pinkman@Cooking.com"),
            new Phone("11904040404"), Department.FOOD
        ));
    }

    @Test
    @DisplayName("Test if Seller filtered by only one filter is being returned correctly")
    public void testFindByOneFilter() {
        sRepository.save(this.fakeSeller1);
        sRepository.save(this.fakeSeller2);
        Sort sort = Sort.by("id").ascending();
        List<Seller> result = sRepository.findByDepartmentAndNameAndMailAndPhone(
            sort, null, "Walter White", null, null
        );

        assertAll(
            () -> assertEquals(1, result.size()),
            () -> assertEquals(fakeSeller1, result.getFirst())
        );
    }

    @Test
    @DisplayName("Test if Seller filtered by no filter is being returned correctly")
    public void testFindByNoFilter() {
        sRepository.save(this.fakeSeller1);
        sRepository.save(this.fakeSeller2);

        Sort sort = Sort.by("id").ascending();
        List<Seller> result = sRepository.findByDepartmentAndNameAndMailAndPhone(
            sort, null, null, null, null
        );

        assertAll(
            () -> assertEquals(2, result.size()),
            () -> assertEquals(this.fakeSeller1, result.getFirst()),
            () -> assertEquals(this.fakeSeller2, result.getLast())
        );
    }

    @Test
    @DisplayName("Test if Seller filtered by all filters is being returned correctly")
    public void testFindByAllFilters() {
        sRepository.save(this.fakeSeller1);
        sRepository.save(this.fakeSeller2);

        Sort sort = Sort.by("id").ascending();
        List<Seller> result = sRepository.findByDepartmentAndNameAndMailAndPhone(
            sort, Department.FOOD, "Jesse Pinkman", "Jesse Pinkman@Cooking.com", "11904040404"
        );

        assertAll(
            () -> assertEquals(1, result.size()),
            () -> assertEquals(this.fakeSeller2, result.getFirst())
        );
    }
}
