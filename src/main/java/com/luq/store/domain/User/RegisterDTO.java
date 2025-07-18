package com.luq.store.domain.User;

import com.luq.store.valueobjects.Mail;

public record RegisterDTO(String name, Mail login, String password, UserRole role) {
}
