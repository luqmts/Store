package valueobjects.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import valueobjects.Phone;

@Converter(autoApply = true)
public class PhoneConverter implements AttributeConverter<Phone, String> {

    @Override
    public String convertToDatabaseColumn(Phone attribute) {
        return attribute != null ? attribute.getValue() : null;    
    }

    @Override
    public Phone convertToEntityAttribute(String dbData) {
        return dbData != null ? new Phone(dbData) : null;
    }
    
}
