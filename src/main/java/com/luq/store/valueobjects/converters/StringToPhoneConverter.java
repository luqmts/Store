package com.luq.store.valueobjects.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import com.luq.store.valueobjects.Phone;

@Component
public class StringToPhoneConverter implements Converter<String, Phone> {
    @Override
    public Phone convert(String source) {
        
        try {
            return new Phone(source);
        } catch (Exception e) {
            return null;
        }
    }
}