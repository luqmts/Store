package com.luq.store.valueobjects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MailTest {
    @Test
    @DisplayName("Validate if valid mail is being created correctly")
    public void testValidMail(){
        Mail mail = new Mail("lucas@mail.com");
        assertEquals("lucas@mail.com", mail.getValue());
    }

    @Test
    @DisplayName("Validate if null mail is not being created and returning a exception for")
    public void testNullMail(){
        Exception exception = assertThrows(NullPointerException.class, () -> new Mail(null));
        assertTrue(exception.getMessage().toLowerCase().contains("mail"));
    }

    @Test
    @DisplayName("Validate if invalid mail is not being created and returning a exception for")
    public void testInvalidMail(){
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new Mail("damnthismailisinvalid"));
        assertTrue(exception.getMessage().toLowerCase().contains("mail"));
    }
}
