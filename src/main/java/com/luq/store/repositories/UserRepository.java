package com.luq.store.repositories;

import com.luq.store.domain.User.User;
import com.luq.store.domain.User.UserRole;
import com.luq.store.valueobjects.Mail;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface UserRepository extends JpaRepository<User, String> {
    UserDetails findByLogin(Mail login);

    @Query("""
        SELECT u FROM users u
        WHERE (:role IS NULL OR u.role = :role)
        AND (:name IS NULL OR u.name LIKE :name || '%')
        AND (:login IS NULL OR u.login LIKE :login || '%')
    """)
    List<User> findByNameAndLoginAndRole(
        Sort sort,
        @Param("name") String name,
        @Param("login") String login,
        @Param("role") UserRole role
    );
}
