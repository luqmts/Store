package valueobjects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class CNPJTest {    
    @Test
    public void testValidCNPJ(){
        CNPJ cnpj = new CNPJ("58.329.240/0001-14");
        assertEquals("58.329.240/0001-14", cnpj.getCNPJ());
    }

    @Test
    public void testNullCNPJ(){
        Exception exception = assertThrows(NullPointerException.class, () -> new CNPJ(null));
        assertTrue(exception.getMessage().toLowerCase().contains("cnpj"));
    }

    @Test
    public void testInvalidCNPJ(){
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new CNPJ("123456789000190"));
        assertTrue(exception.getMessage().toLowerCase().contains("cnpj"));
    }
}
