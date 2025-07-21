package com.luq.store.services;

import com.luq.store.domain.User.User;
import com.luq.store.domain.User.UserRole;
import com.luq.store.repositories.UserRepository;
import com.luq.store.valueobjects.Mail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.atMostOnce;

@ExtendWith(MockitoExtension.class)
public class AuthorizationServiceTest {
    @Mock
    UserRepository uRepository;
    @InjectMocks
    AuthorizationService uService;

    User fakeUser1, fakeUser2, result;

    @BeforeEach
    public void setUp(){
        String user = "Jimmy McGill";
        LocalDateTime now = LocalDateTime.now();

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
    @DisplayName("Test if User is being registered correctly")
    public void testRegisterUser(){
        when(uRepository.save(fakeUser1)).thenReturn(fakeUser1);
        result = uService.register(fakeUser1);

        assertAll(
            () -> verify(uRepository, atMostOnce()).save(fakeUser1),
            () -> assertNotNull(result),
            () -> assertInstanceOf(User.class, result),
            () -> assertEquals(fakeUser1, result)
        );
    }

    @Test
    @DisplayName("Test if User is being updated correctly")
    public void testUpdateUser(){
        when(uRepository.save(fakeUser1)).thenReturn(fakeUser1);
        when(uRepository.findById(fakeUser1.getId())).thenReturn(Optional.ofNullable(fakeUser1));
        when(uRepository.save(fakeUser2)).thenReturn(fakeUser2);

        uService.register(fakeUser1);
        result = uService.update(fakeUser1.getId(), fakeUser2);

        assertAll(
            () -> verify(uRepository, times(1)).save(fakeUser1),
            () -> verify(uRepository, times(1)).save(fakeUser2),
            () -> verify(uRepository, atMostOnce()).findById(fakeUser1.getId()),
            () -> assertNotNull(result),
            () -> assertInstanceOf(User.class, result),
            () -> assertEquals(fakeUser2, result)
        );
    }

    @Test
    @DisplayName("Test if User is being deleted correctly")
    public void testDeleteUser(){
        when(uRepository.save(fakeUser1)).thenReturn(fakeUser1);
        uService.register(fakeUser1);

        uService.delete(fakeUser1.getId());

        verify(uRepository, atMostOnce()).deleteById(fakeUser1.getId());
    }

    @Test
    @DisplayName("Test if all Users registered are being returned on method getALl()")
    public void testGetAllUsers() {
        when(uRepository.save(fakeUser1)).thenReturn(fakeUser1);
        when(uRepository.save(fakeUser2)).thenReturn(fakeUser2);
        when(uRepository.findAll()).thenReturn(List.of(fakeUser1, fakeUser2));

        uService.register(fakeUser1);
        uService.register(fakeUser2);
        assertEquals(2, uService.getAll().size());
        verify(uRepository, atMostOnce()).findAll();
    }

    @Test
    @DisplayName("Test if User is being returned by id on method getById()")
    public void testGetUserById() {
        String uuid = fakeUser1.getId();
        when(uRepository.save(fakeUser1)).thenReturn(fakeUser1);
        when(uRepository.findById(uuid)).thenReturn(Optional.ofNullable(fakeUser1));

        uService.register(fakeUser1);
        result = uService.getById(uuid);
        assertAll(
            () -> verify(uRepository, atMostOnce()).findById(uuid),
            () -> assertNotNull(result),
            () -> assertInstanceOf(User.class, result),
            () -> assertEquals(fakeUser1, result)
        );
    }
}
