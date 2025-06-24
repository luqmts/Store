package valueobjects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class MailTest {
    @Test
    public void testValidMail(){
        Mail mail = new Mail("lucas@mail.com");
        assertEquals("lucas@mail.com", mail.getMail());
    }

    @Test
    public void testNullMail(){
        Exception exception = assertThrows(NullPointerException.class, () -> new Mail(null));
        assertTrue(exception.getMessage().toLowerCase().contains("mail"));
    }

    @Test
    public void testInvalidMail(){
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new Mail("damnthismailisinvalid"));
        assertTrue(exception.getMessage().toLowerCase().contains("mail"));
    }
}
