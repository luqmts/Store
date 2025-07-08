package com.luq.storevs.valueobjects.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import com.luq.storevs.valueobjects.Phone;

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