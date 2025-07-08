package com.luq.storevs.valueobjects.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import com.luq.storevs.valueobjects.Mail;

@Component
public class StringToMailConverter implements Converter<String, Mail> {
    @Override
    public Mail convert(String source) {
        try {
            return new Mail(source);
        } catch (Exception e) {
            return null;
        }
    }
}