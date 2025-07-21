package com.luq.store.repositories;

import com.luq.store.domain.User.User;
import com.luq.store.domain.User.UserRole;
import com.luq.store.valueobjects.Mail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository uRepository;

    User fakeUser1, fakeUser2;

    @BeforeEach
    public void setUp(){
        fakeUser1 = new User("Test User 01", new Mail("testUser1@Mail.com"), "passworduser", UserRole.USER);
        fakeUser2 = new User("Test User 02", new Mail("testUser2@Mail.com"), "passwordadmin", UserRole.ADMIN);
    }

    @Test
    @DisplayName("Test if User filtered by name is being returned correctly")
    public void testFindByOneFilter() {
        uRepository.save(fakeUser1);
        uRepository.save(fakeUser2);

        Sort sort = Sort.by("name").ascending();
        List<User> result = uRepository.findByNameAndLoginAndRole(sort, "Test User 01", null, null);

        assertAll(
            () -> assertEquals(1, result.size()),
            () -> assertEquals(fakeUser1, result.getFirst())
        );
    }

    @Test
    @DisplayName("Test if User filtered by no filter is being returned correctly")
    public void testFindByNoFilter() {
        uRepository.save(this.fakeUser1);
        uRepository.save(this.fakeUser2);

        Sort sort = Sort.by("id").ascending();
        List<User> result = uRepository.findByNameAndLoginAndRole(sort, null, null, null);

        assertAll(
            () -> assertEquals(2, result.size()),
            () -> assertEquals(this.fakeUser1, result.getFirst()),
            () -> assertEquals(this.fakeUser2, result.getLast())
        );
    }

    @Test
    @DisplayName("Test if User filtered by all filters is being returned correctly")
    public void testFindByAllFilters() {
        uRepository.save(this.fakeUser1);
        uRepository.save(this.fakeUser2);

        Sort sort = Sort.by("id").ascending();
        List<User> result = uRepository.findByNameAndLoginAndRole(sort, "Test User 02", "testUser2@Mail.com", UserRole.ADMIN);

        assertAll(
            () -> assertEquals(1, result.size()),
            () -> assertEquals(this.fakeUser2, result.getFirst())
        );
    }
}
