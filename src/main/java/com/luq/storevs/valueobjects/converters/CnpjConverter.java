package com.luq.storevs.valueobjects.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import com.luq.storevs.valueobjects.Cnpj;

@Converter(autoApply = true)
public class CnpjConverter implements AttributeConverter<Cnpj, String> {
    
    @Override
    public String convertToDatabaseColumn(Cnpj attribute) {
        return attribute != null ? attribute.getValue() : null;    
    }

    @Override
    public Cnpj convertToEntityAttribute(String dbData) {
        return dbData != null ? new Cnpj(dbData) : null;
    }
    
}
