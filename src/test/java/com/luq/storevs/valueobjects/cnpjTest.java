package com.luq.storevs.valueobjects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class cnpjTest {    
    @Test
    public void testValidcnpj(){
        Cnpj cnpj = new Cnpj("58.329.240/0001-14");
        assertEquals("58.329.240/0001-14", cnpj.getValue());
    }

    @Test
    public void testNullcnpj(){
        Exception exception = assertThrows(NullPointerException.class, () -> new Cnpj(null));
        assertTrue(exception.getMessage().toLowerCase().contains("cnpj"));
    }

    @Test
    public void testInvalidcnpj(){
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new Cnpj("123456789000190"));
        assertTrue(exception.getMessage().toLowerCase().contains("cnpj"));
    }
}
