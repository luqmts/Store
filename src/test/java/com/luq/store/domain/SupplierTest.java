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
    Supplier supplier;

    @BeforeEach
    public void setUp(){
        String user = "Jimmy McGill";
        LocalDateTime now = LocalDateTime.now();

        supplier = new Supplier(
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
                supplier.toString(),
                "Supplier toString() method must return on right format"
        );
    }

    @Test
    @DisplayName("Ensure object is being created correctly")
    public void testSupplierCreationGrouped(){
        assertAll(
            () -> assertEquals("Ooo Comércios e Vendas S/A", supplier.getName()),
            () -> assertEquals("94.907.898/0001-20", supplier.getCnpj().toString()),
            () -> assertEquals("Ooo@gmail.com", supplier.getMail().toString()),
            () -> assertEquals("11940028922", supplier.getPhone().toString()),
            () -> assertEquals("Jimmy McGill", supplier.getCreatedBy()),
            () -> assertEquals("Jimmy McGill", supplier.getModifiedBy())
        );
    }

    @Test
    @DisplayName("Ensure object is being updated correctly")
    public void testSupplierUpdatedGrouped(){
        LocalDateTime now = LocalDateTime.now();
        String user = "Kim Wexler";

        supplier.setName("Marceline Instrumentos Musicais S/A");
        supplier.setCnpj(new Cnpj("08.968.789/0001-80"));
        supplier.setMail(new Mail("marceline_213@mail.com"));
        supplier.setPhone(new Phone("11990909090"));
        supplier.setCreatedBy(user);
        supplier.setModifiedBy(user);
        supplier.setCreated(now);
        supplier.setModified(now);

        assertAll(
            () -> assertEquals("Marceline Instrumentos Musicais S/A", supplier.getName()),
            () -> assertEquals("08.968.789/0001-80", supplier.getCnpj().toString()),
            () -> assertEquals("marceline_213@mail.com", supplier.getMail().toString()),
            () -> assertEquals("11990909090", supplier.getPhone().toString()),
            () -> assertEquals("Kim Wexler", supplier.getCreatedBy()),
            () -> assertEquals("Kim Wexler", supplier.getModifiedBy()),
            () -> assertEquals(now, supplier.getCreated()),
            () -> assertEquals(now, supplier.getModified())
        );
    }

    @Test
    @DisplayName("Supplier attributes must not be null")
    public void testSupplierAttributesNotNull(){
        assertAll(
            () -> assertNotNull(supplier.getName()),
            () -> assertNotNull(supplier.getCnpj()),
            () -> assertNotNull(supplier.getMail()),
            () -> assertNotNull(supplier.getPhone()),
            () -> assertNotNull(supplier.getCreatedBy()),
            () -> assertNotNull(supplier.getCreated()),
            () -> assertNotNull(supplier.getModifiedBy()),
            () -> assertNotNull(supplier.getModified())
        );
    }
}