package com.luq.store.domain.User;

import com.luq.store.valueobjects.Mail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserTest {
    private User fakeUser1, fakeUser2;
    private final LocalDateTime now = LocalDateTime.now();

    @BeforeEach
    public void setUp(){
        String user = "Jimmy McGill";

        fakeUser1 = new User(
            UUID.randomUUID().toString(), "Test User 01", new Mail("testUser1@mail.com"),
            "strongpassword", UserRole.USER, "strongpassword",
            user, now, user, now
        );

        fakeUser2 = new User(
            UUID.randomUUID().toString(), "Test User 02", new Mail("testUser2@mail.com"),
            "adminstrongpassword", UserRole.ADMIN, "adminstrongpassword",
            user, now, user, now
        );
    }

    @Test
    @DisplayName("Ensure toString method is returning correctly")
    public void testUserToStringMethod() {
        String userUuid = fakeUser1.getId();

        assertEquals(
            "User(id="+ userUuid +", name=Test User 01, login=testUser1@mail.com, role=USER)",
            fakeUser1.toString(),
            "User toString() method must return on right format"
        );
    }

    @Test
    @DisplayName("Ensure object is being created correctly")
    public void testUserCreationGrouped(){
        String userUuid = fakeUser1.getId();

        assertAll(
            () -> assertEquals(userUuid, fakeUser1.getId()),
            () -> assertEquals("Test User 01", fakeUser1.getName()),
            () -> assertEquals("testUser1@mail.com", fakeUser1.getLogin().getValue()),
            () -> assertEquals("strongpassword", fakeUser1.getPassword()),
            () -> assertEquals("USER", fakeUser1.getRole().toString()),
            () -> assertEquals(now, fakeUser1.getCreated()),
            () -> assertEquals("Jimmy McGill", fakeUser1.getCreatedBy()),
            () -> assertEquals(now, fakeUser1.getModified()),
            () -> assertEquals("Jimmy McGill", fakeUser1.getModifiedBy())
        );
    }

    @Test
    @DisplayName("Ensure object is being updated correctly")
    public void testUserUpdatedGrouped(){
        LocalDateTime now = LocalDateTime.now();
        String user = "Kim Wexler";

        fakeUser1.setName("Test User 02");
        fakeUser1.setLogin(new Mail("testUser2@mail.com"));
        fakeUser1.setPassword("adminstrongpassword");
        fakeUser1.setRole(UserRole.ADMIN);
        fakeUser1.setCreatedBy(user);
        fakeUser1.setModifiedBy(user);
        fakeUser1.setCreated(now);
        fakeUser1.setModified(now);

        assertAll(
            () -> assertEquals("Test User 02", fakeUser1.getName()),
            () -> assertEquals("testUser2@mail.com", fakeUser1.getLogin().getValue()),
            () -> assertEquals("adminstrongpassword", fakeUser1.getPassword()),
            () -> assertEquals("ADMIN", fakeUser1.getRole().toString()),
            () -> assertEquals("Kim Wexler", fakeUser1.getCreatedBy()),
            () -> assertEquals("Kim Wexler", fakeUser1.getModifiedBy()),
            () -> assertEquals(now, fakeUser1.getCreated()),
            () -> assertEquals(now, fakeUser1.getModified())
        );
    }

    @Test
    @DisplayName("User attributes must not be null")
    public void testUserAttributesNotNull(){
        assertAll(
            () -> assertNotNull(fakeUser1.getId()),
            () -> assertNotNull(fakeUser1.getName()),
            () -> assertNotNull(fakeUser1.getCreatedBy()),
            () -> assertNotNull(fakeUser1.getCreated()),
            () -> assertNotNull(fakeUser1.getModifiedBy()),
            () -> assertNotNull(fakeUser1.getModified())
        );
    }

    @Test
    @DisplayName("Test if user with user role have right permissions")
    public void testUserGrantedAuthorityUser() {
        assertAll(
            () -> assertEquals(1, fakeUser1.getAuthorities().size()),
            () -> assertTrue(fakeUser1.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER"))),
            () -> assertFalse(fakeUser1.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")))
        );
    }

    @Test
    @DisplayName("Test if user with admin role have right permissions")
    public void testUserGrantedAuthorityAdmin() {
        assertAll(
            () -> assertEquals(2, fakeUser2.getAuthorities().size()),
            () -> assertTrue(fakeUser2.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER"))),
            () -> assertTrue(fakeUser2.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")))
        );
    }
}
