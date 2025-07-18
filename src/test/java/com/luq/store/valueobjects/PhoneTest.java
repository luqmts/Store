package com.luq.store.valueobjects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PhoneTest {
    @Test
    @DisplayName("Validate if valid phone is being created correctly")
    public void testValidPhone(){
        Phone phone = new Phone("11940028922");
        assertEquals("11940028922", phone.getValue());
    }

    @Test
    @DisplayName("Validate if null phone is not being created and returning a exception for")
    public void testNullPhone(){
        Exception exception = assertThrows(NullPointerException.class, () -> new Phone(null));
        assertTrue(exception.getMessage().toLowerCase().contains("phone"));
    }

    @Test
    @DisplayName("Validate if invalid phone is not being created and returning a exception for")
    public void testInvalidPhone(){
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new Phone("1234"));
        assertTrue(exception.getMessage().toLowerCase().contains("phone"));
    }
}
