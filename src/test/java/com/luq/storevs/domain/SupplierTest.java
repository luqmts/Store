/*package com.luq.storevs.model;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.luq.storevs.valueobjects.Cnpj;
import com.luq.storevs.valueobjects.Mail;
import com.luq.storevs.valueobjects.Phone;

public class SupplierTest {
    Supplier supplier;

    @BeforeEach
    public void setUp(){
        supplier = new Supplier(
            1,
            "Ooo Comércios e Vendas S/A", 
            new Cnpj("94.907.898/0001-20"), 
            new Mail("Ooo@gmail.com"), 
            new Phone("11940028922")
        );
    }

    @Test
    @DisplayName("Ensure object is being created correctly")
    public void testSupplierCreationGrouped(){
        assertAll(
            () -> assertEquals(1, supplier.getId()),
            () -> assertEquals("Ooo Comércios e Vendas S/A", supplier.getName()),
            () -> assertEquals("94.907.898/0001-20", supplier.getCnpj().toString()),
            () -> assertEquals("Ooo@gmail.com", supplier.getMail().toString()),
            () -> assertEquals("11940028922", supplier.getPhone().toString())
        );
    }

    @Test
    @DisplayName("Ensure object is being updated correctly")
    public void testSupplierUpdatedGrouped(){
        supplier.setId(2);
        supplier.setName("Marceline Instrumentos Musicais S/A");
        supplier.setCnpj(new Cnpj("08.968.789/0001-80"));
        supplier.setMail(new Mail("marceline_213@mail.com"));
        supplier.setPhone(new Phone("11990909090"));

        assertAll(
            () -> assertEquals(2, supplier.getId()),
            () -> assertEquals("Marceline Instrumentos Musicais S/A", supplier.getName()),
            () -> assertEquals("08.968.789/0001-80", supplier.getCnpj().toString()),
            () -> assertEquals("marceline_213@mail.com", supplier.getMail().toString()),
            () -> assertEquals("11990909090", supplier.getPhone().toString())
        );
    }

    @Test
    @DisplayName("Supplier must not be null")
    public void testSupplierNotNull(){
        assertNotNull(supplier);
    }

    @Test
    @DisplayName("Supplier attributes must not be null")
    public void testSupplierAttributesNotNull(){
        assertAll(
            () -> assertNotNull(supplier.getId()),
            () -> assertNotNull(supplier.getName()),
            () -> assertNotNull(supplier.getCnpj()),
            () -> assertNotNull(supplier.getMail()),
            () -> assertNotNull(supplier.getPhone())
        );
    }
}*/