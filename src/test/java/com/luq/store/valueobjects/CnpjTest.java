package com.luq.store.valueobjects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.luq.store.exceptions.InvalidCnpjException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CnpjTest {
    @Test
    @DisplayName("Validate if valid cnpj is being created correctly")
    public void testValidCnpj(){
        Cnpj Cnpj = new Cnpj("58.329.240/0001-14");
        assertEquals("58.329.240/0001-14", Cnpj.getValue());
    }

    @Test
    @DisplayName("Validate if null cnpj is not being created and returning a exception for")
    public void testNullCnpj(){
        Exception exception = assertThrows(InvalidCnpjException.class, () -> new Cnpj(null));
        assertTrue(exception.getMessage().toLowerCase().contains("cnpj"));
    }

    @Test
    @DisplayName("Validate if invalid cnpj is not being created and returning a exception for")
    public void testInvalidCnpj(){
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new Cnpj("123456789000190"));
        assertTrue(exception.getMessage().toLowerCase().contains("cnpj"));
    }
}
