package simpleshop.common;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Deserialize from a long.
 */
public class LocalDateDeserializer extends JsonDeserializer<LocalDate> {

    @Override
    public LocalDate deserialize(JsonParser arg0, DeserializationContext arg1) throws IOException {

        if(StringUtils.isNullOrEmpty(arg0.getText()))
            return null;

        long milli = Long.parseLong(arg0.getText());
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(milli), ZoneId.systemDefault()).toLocalDate();
    }
}