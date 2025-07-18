package com.luq.store.domain.User;

import com.luq.store.valueobjects.Mail;

public record AuthenticationDTO(Mail login, String password) {

}
