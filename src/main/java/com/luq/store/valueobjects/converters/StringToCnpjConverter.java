package com.luq.store.valueobjects.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import com.luq.store.valueobjects.Cnpj;

@Component
public class StringToCnpjConverter implements Converter<String, Cnpj> {
    @Override
    public Cnpj convert(String source) {
        try {
            return new Cnpj(source);
        } catch (Exception e) {
            return null;
        }
    }
}