package com.luq.storevs.domain.User;

import com.luq.storevs.valueobjects.Mail;

public record RegisterDTO(String name, Mail login, String password, UserRole role) {
}
