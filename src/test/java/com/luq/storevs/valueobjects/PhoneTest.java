package com.luq.storevs.valueobjects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class PhoneTest {
    @Test
    public void testValidPhone(){
        Phone phone = new Phone("11940028922");
        assertEquals("11940028922", phone.getValue());
    }

    @Test
    public void testNullPhone(){
        Exception exception = assertThrows(NullPointerException.class, () -> new Phone(null));
        assertTrue(exception.getMessage().toLowerCase().contains("phone"));
    }

    @Test
    public void testInvalidPhone(){
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new Phone("1234"));
        assertTrue(exception.getMessage().toLowerCase().contains("phone"));
    }
}
