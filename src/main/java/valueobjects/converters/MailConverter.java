package valueobjects.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import valueobjects.Mail;

@Converter(autoApply = true)
public class MailConverter implements AttributeConverter<Mail, String> {

    @Override
    public String convertToDatabaseColumn(Mail attribute) {
        return attribute != null ? attribute.getValue() : null;    
    }

    @Override
    public Mail convertToEntityAttribute(String dbData) {
        return dbData != null ? new Mail(dbData) : null;
    }
    
}
