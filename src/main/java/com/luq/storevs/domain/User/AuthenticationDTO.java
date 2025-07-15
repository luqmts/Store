package com.luq.storevs.domain.User;

import com.luq.storevs.valueobjects.Mail;

public record AuthenticationDTO(Mail login, String password) {

}
