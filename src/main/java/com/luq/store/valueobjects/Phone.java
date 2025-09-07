package com.luq.store.valueobjects;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.luq.store.exceptions.InvalidPhoneException;

public class Phone {
    private String phone;
    
    @JsonCreator
    public Phone(String stringPhone){
        setPhone(stringPhone);
    }

    @JsonValue
    public String getValue() {
        return phone;
    }

    public void setPhone(String stringPhone){
        if (validatePhone(stringPhone)) this.phone = stringPhone;
        else throw new InvalidPhoneException("Invalid phone format.");
    }

    public boolean validatePhone(String stringPhone){
        if (stringPhone == null) throw new InvalidPhoneException("Phone must not be null!");

        Pattern pattern = Pattern.compile("[0-9]{2}[0-9]{8,9}");
        Matcher matcher = pattern.matcher(stringPhone);

        return matcher.matches();
    }

    @Override
    public String toString() {
        return getValue();
    }
}
