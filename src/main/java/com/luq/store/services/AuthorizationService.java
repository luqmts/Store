package com.luq.store.services;

import com.luq.store.domain.User.User;
import com.luq.store.domain.User.UserRole;
import com.luq.store.repositories.UserRepository;
import com.luq.store.valueobjects.Mail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorizationService implements UserDetailsService {
    @Autowired
    UserRepository uRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Mail usernameMail = new Mail(username);
        return uRepository.findByLogin(usernameMail);
    }

    public User getById(String id) { return uRepository.findById(id).orElse(null); }

    public List<User> getAllSorted(String sortBy, String direction, String name, String login, String role) {
        Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        if (name != null || login != null || role != null){
            return uRepository.findByNameAndLoginAndRole(sort, name, login, UserRole.getUserRole(role));
        }
        return uRepository.findAll(sort);
    }

    public User register(User user) {
        return uRepository.save(user);
    }

    public User update(String id, User user) {
        User user_to_update = uRepository.findById(id).orElse(null);

        if (user_to_update == null) return null;

        user_to_update.setId(id);
        return uRepository.save(user);
    }

    public void delete(String id) {
        uRepository.deleteById(id);
    }
}
