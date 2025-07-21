package
 com.luq.store.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerTest {
    private Customer fakeCustomer, fakeCustomer2;
    private final LocalDateTime now = LocalDateTime.now();

    @BeforeEach
    public void setUp(){
        String user = "Jimmy McGill";

        fakeCustomer = new Customer(1, "Test Customer 01", user, now, user, now);
    }

    @Test
    @DisplayName("Ensure toString method is returning correctly")
    public void testCustomerToStringMethod() {
        assertEquals(
            "Customer(id=1, name=Test Customer 01)",
            fakeCustomer.toString(),
            "Customer toString() method must return on right format"
        );
    }

    @Test
    @DisplayName("Ensure object is being created correctly")
    public void testCustomerCreationGrouped(){
        assertAll(
            () -> assertEquals(1, fakeCustomer.getId()),
            () -> assertEquals("Test Customer 01", fakeCustomer.getName()),
            () -> assertEquals(now, fakeCustomer.getCreated()),
            () -> assertEquals("Jimmy McGill", fakeCustomer.getCreatedBy()),
            () -> assertEquals(now, fakeCustomer.getModified()),
            () -> assertEquals("Jimmy McGill", fakeCustomer.getModifiedBy())
        );
    }

    @Test
    @DisplayName("Ensure object is being updated correctly")
    public void testCustomerUpdatedGrouped(){
        LocalDateTime now = LocalDateTime.now();
        String user = "Kim Wexler";

        fakeCustomer.setName("Test Customer 02");
        fakeCustomer.setCreatedBy(user);
        fakeCustomer.setModifiedBy(user);
        fakeCustomer.setCreated(now);
        fakeCustomer.setModified(now);

        assertAll(
            () -> assertEquals("Test Customer 02", fakeCustomer.getName()),
            () -> assertEquals("Kim Wexler", fakeCustomer.getCreatedBy()),
            () -> assertEquals("Kim Wexler", fakeCustomer.getModifiedBy()),
            () -> assertEquals(now, fakeCustomer.getCreated()),
            () -> assertEquals(now, fakeCustomer.getModified())
        );
    }

    @Test
    @DisplayName("Customer attributes must not be null")
    public void testCustomerAttributesNotNull(){
        assertAll(
            () -> assertNotNull(fakeCustomer.getId()),
            () -> assertNotNull(fakeCustomer.getName()),
            () -> assertNotNull(fakeCustomer.getCreatedBy()),
            () -> assertNotNull(fakeCustomer.getCreated()),
            () -> assertNotNull(fakeCustomer.getModifiedBy()),
            () -> assertNotNull(fakeCustomer.getModified())
        );
    }
}