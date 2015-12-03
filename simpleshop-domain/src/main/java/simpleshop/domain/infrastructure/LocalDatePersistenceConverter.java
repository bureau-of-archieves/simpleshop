package simpleshop.domain.infrastructure;

import java.time.LocalDate;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * See here: http://stackoverflow.com/questions/23890687/hibernate-4-with-java-time-localdate-and-date-construct
 * Hibernate recognises @Converter.
 */
@Converter
public class LocalDatePersistenceConverter implements AttributeConverter<LocalDate, java.sql.Date> {

    @Override
    public java.sql.Date convertToDatabaseColumn(LocalDate entityValue) {
        if (entityValue != null) {
            return java.sql.Date.valueOf(entityValue);
        }
        return null;
    }

    @Override
    public LocalDate convertToEntityAttribute(java.sql.Date databaseValue) {
        if (databaseValue != null) {
            return databaseValue.toLocalDate();
        }
        return null;
    }
}