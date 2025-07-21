package com.luq.store.domain;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.luq.store.valueobjects.Cnpj;
import com.luq.store.valueobjects.Mail;
import com.luq.store.valueobjects.Phone;

import java.time.LocalDateTime;

public class SupplierTest {
    private final LocalDateTime now = LocalDateTime.now();
    private Supplier fakeSupplier;

    @BeforeEach
    public void setUp(){
        String user = "Jimmy McGill";

        fakeSupplier = new Supplier(
            1, "Ooo Comércios e Vendas S/A", new Cnpj("94.907.898/0001-20"),
            new Mail("Ooo@gmail.com"), new Phone("11940028922"),
            user, now, user, now
        );
    }

    @Test
    @DisplayName("Ensure toString method is returning correctly")
    public void testSupplierToStringMethod() {
        assertEquals(
            "Supplier(id=1, name=Ooo Comércios e Vendas S/A)",
            fakeSupplier.toString(),
            "Supplier toString() method must return on right format"
        );
    }

    @Test
    @DisplayName("Ensure object is being created correctly")
    public void testSupplierCreationGrouped(){
        assertAll(
            () -> assertEquals(1, fakeSupplier.getId()),
            () -> assertEquals("Ooo Comércios e Vendas S/A", fakeSupplier.getName()),
            () -> assertEquals("94.907.898/0001-20", fakeSupplier.getCnpj().toString()),
            () -> assertEquals("Ooo@gmail.com", fakeSupplier.getMail().toString()),
            () -> assertEquals("11940028922", fakeSupplier.getPhone().toString()),
            () -> assertEquals(now, fakeSupplier.getCreated()),
            () -> assertEquals("Jimmy McGill", fakeSupplier.getCreatedBy()),
            () -> assertEquals(now, fakeSupplier.getModified()),
            () -> assertEquals("Jimmy McGill", fakeSupplier.getModifiedBy())
        );
    }

    @Test
    @DisplayName("Ensure object is being updated correctly")
    public void testSupplierUpdatedGrouped(){
        LocalDateTime now = LocalDateTime.now();
        String user = "Kim Wexler";

        fakeSupplier.setName("Marceline Instrumentos Musicais S/A");
        fakeSupplier.setCnpj(new Cnpj("08.968.789/0001-80"));
        fakeSupplier.setMail(new Mail("marceline_213@mail.com"));
        fakeSupplier.setPhone(new Phone("11990909090"));
        fakeSupplier.setCreatedBy(user);
        fakeSupplier.setModifiedBy(user);
        fakeSupplier.setCreated(now);
        fakeSupplier.setModified(now);

        assertAll(
            () -> assertEquals("Marceline Instrumentos Musicais S/A", fakeSupplier.getName()),
            () -> assertEquals("08.968.789/0001-80", fakeSupplier.getCnpj().toString()),
            () -> assertEquals("marceline_213@mail.com", fakeSupplier.getMail().toString()),
            () -> assertEquals("11990909090", fakeSupplier.getPhone().toString()),
            () -> assertEquals("Kim Wexler", fakeSupplier.getCreatedBy()),
            () -> assertEquals("Kim Wexler", fakeSupplier.getModifiedBy()),
            () -> assertEquals(now, fakeSupplier.getCreated()),
            () -> assertEquals(now, fakeSupplier.getModified())
        );
    }

    @Test
    @DisplayName("Supplier attributes must not be null")
    public void testSupplierAttributesNotNull(){
        assertAll(
            () -> assertNotNull(fakeSupplier.getId()),
            () -> assertNotNull(fakeSupplier.getName()),
            () -> assertNotNull(fakeSupplier.getCnpj()),
            () -> assertNotNull(fakeSupplier.getMail()),
            () -> assertNotNull(fakeSupplier.getPhone()),
            () -> assertNotNull(fakeSupplier.getCreatedBy()),
            () -> assertNotNull(fakeSupplier.getCreated()),
            () -> assertNotNull(fakeSupplier.getModifiedBy()),
            () -> assertNotNull(fakeSupplier.getModified())
        );
    }
}