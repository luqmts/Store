package com.luq.storevs.services;

import com.luq.storevs.repositories.UserRepository;
import com.luq.storevs.valueobjects.Mail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService implements UserDetailsService {
    @Autowired
    UserRepository uRepositroy;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Mail usernameMail = new Mail(username);
        return uRepositroy.findByLogin(usernameMail);
    }
}
