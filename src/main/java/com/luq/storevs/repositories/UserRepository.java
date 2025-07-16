package com.luq.storevs.repositories;

import com.luq.storevs.domain.User.User;
import com.luq.storevs.valueobjects.Mail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository extends JpaRepository<User, String> {
    UserDetails findByLogin(Mail login);
}
