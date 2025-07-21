package com.luq.store.domain;

import com.luq.store.valueobjects.Mail;
import com.luq.store.valueobjects.Phone;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class SellerTest {
    private Seller fakeSeller;
    private final LocalDateTime now = LocalDateTime.now();

    @BeforeEach
    public void setUp(){
        String user = "Jimmy McGill";

        fakeSeller = new Seller(
            1, "Walter White",
            new Mail("WalterWhite@Cooking.com"), new Phone("11940028922"), Department.FOOD,
            user, now, user, now
        );
    }

    @Test
    @DisplayName("Ensure toString method is returning correctly")
    public void testSellerToStringMethod() {
        assertEquals(
            "Seller(id=1, name=Walter White)",
            fakeSeller.toString(),
            "Seller toString() method must return on right format"
        );
    }

    @Test
    @DisplayName("Ensure object is being created correctly")
    public void testSellerCreationGrouped(){
        assertAll(
            () -> assertEquals(1, fakeSeller.getId()),
            () -> assertEquals("Walter White", fakeSeller.getName()),
            () -> assertEquals("WalterWhite@Cooking.com", fakeSeller.getMail().toString()),
            () -> assertEquals("11940028922", fakeSeller.getPhone().toString()),
            () -> assertEquals("FOOD", fakeSeller.getDepartment().toString()),
            () -> assertEquals(now, fakeSeller.getCreated()),
            () -> assertEquals("Jimmy McGill", fakeSeller.getCreatedBy()),
            () -> assertEquals(now, fakeSeller.getModified()),
            () -> assertEquals("Jimmy McGill", fakeSeller.getModifiedBy())
        );
    }

    @Test
    @DisplayName("Ensure object is being updated correctly")
    public void testSellerUpdatedGrouped(){
        LocalDateTime now = LocalDateTime.now();
        String user = "Kim Wexler";

        fakeSeller.setName("Jesse Pinkman");
        fakeSeller.setMail(new Mail("JessePinkman@Cooking.com"));
        fakeSeller.setPhone(new Phone("11990909090"));
        fakeSeller.setDepartment(Department.CLOTHES);
        fakeSeller.setCreatedBy(user);
        fakeSeller.setModifiedBy(user);
        fakeSeller.setCreated(now);
        fakeSeller.setModified(now);

        assertAll(
            () -> assertEquals("Jesse Pinkman", fakeSeller.getName()),
            () -> assertEquals("JessePinkman@Cooking.com", fakeSeller.getMail().toString()),
            () -> assertEquals("11990909090", fakeSeller.getPhone().toString()),
            () -> assertEquals("CLOTHES", fakeSeller.getDepartment().toString()),
            () -> assertEquals("Kim Wexler", fakeSeller.getCreatedBy()),
            () -> assertEquals("Kim Wexler", fakeSeller.getModifiedBy()),
            () -> assertEquals(now, fakeSeller.getCreated()),
            () -> assertEquals(now, fakeSeller.getModified())
        );
    }

    @Test
    @DisplayName("Seller attributes must not be null")
    public void testSellerAttributesNotNull(){
        assertAll(
            () -> assertNotNull(fakeSeller.getId()),
            () -> assertNotNull(fakeSeller.getName()),
            () -> assertNotNull(fakeSeller.getMail()),
            () -> assertNotNull(fakeSeller.getPhone()),
            () -> assertNotNull(fakeSeller.getDepartment()),
            () -> assertNotNull(fakeSeller.getCreatedBy()),
            () -> assertNotNull(fakeSeller.getCreated()),
            () -> assertNotNull(fakeSeller.getModifiedBy()),
            () -> assertNotNull(fakeSeller.getModified())
        );
    }
}