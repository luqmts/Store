package valueobjects.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import valueobjects.CNPJ;

@Converter(autoApply = true)
public class CNPJConverter implements AttributeConverter<CNPJ, String> {
    @Override
    public String convertToDatabaseColumn(CNPJ attribute) {
        return attribute != null ? attribute.getValue() : null;    
    }

    @Override
    public CNPJ convertToEntityAttribute(String dbData) {
        return dbData != null ? new CNPJ(dbData) : null;
    }
    
}
