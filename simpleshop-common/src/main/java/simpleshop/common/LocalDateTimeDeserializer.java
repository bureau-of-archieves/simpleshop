package simpleshop.common;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Deserialize from a long.
 */
public class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
    @Override
    public LocalDateTime deserialize(JsonParser arg0, DeserializationContext arg1) throws IOException {

        if(StringUtils.isNullOrEmpty(arg0.getText()))
            return null;

        return LocalDateTime.ofInstant(Instant.ofEpochMilli(arg0.getLongValue()), ZoneId.systemDefault());
    }
}
