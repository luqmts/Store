package models;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SupplierTest {
    Supplier supplier;

    @BeforeEach
    void setUp(){
        supplier = new Supplier("Ooo Comércios e Vendas S/A", "94.907.898/0001-20", "Ooo@gmail.com", "11940028922");
    }

    @Test
    @DisplayName("Ensure object is being created correctly")
    public void testSupplierCreationGrouped(){
        assertAll(
            () -> assertEquals("Ooo Comércios e Vendas S/A", supplier.getName()),
            () -> assertEquals("94.907.898/0001-20", supplier.getCnpj()),
            () -> assertEquals("Ooo@gmail.com", supplier.getMail()),
            () -> assertEquals("11940028922", supplier.getPhone())
        );
    }

    @Test
    @DisplayName("Ensure object is being updated correctly")
    public void testSupplierUpdatedGrouped(){
        supplier.setName("Marceline Instrumentos Musicais S/A");
        supplier.setCnpj("08.968.789/0001-80");
        supplier.setMail("marceline_213@mail.com");
        supplier.setPhone("11990909090");

        assertAll(
            () -> assertEquals("Marceline Instrumentos Musicais S/A", supplier.getName()),
            () -> assertEquals("08.968.789/0001-80", supplier.getCnpj()),
            () -> assertEquals("marceline_213@mail.com", supplier.getMail()),
            () -> assertEquals("11990909090", supplier.getPhone())
        );
    }
}
