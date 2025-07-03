package com.luq.storevs.valueobjects;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public class Mail {
    private String mail;
    
    @JsonCreator
    public Mail(String stringMail) {
        setMail(stringMail);
    }

    @JsonValue
    public String getValue() {
        return mail;
    }

    public void setMail(String stringMail){
        if (validateMail(stringMail)) this.mail = stringMail;
        else throw new IllegalArgumentException("Invalid mail format.");   
    }

    public boolean validateMail(String stringMail){
        if (stringMail == null) throw new NullPointerException("Mail must not be null!");

        Pattern pattern = Pattern.compile("^.+@.+\\..+$");
        Matcher matcher = pattern.matcher(stringMail);

        return matcher.matches(); 
    }

    @Override
    public String toString() {
        return getValue();
    }
}
