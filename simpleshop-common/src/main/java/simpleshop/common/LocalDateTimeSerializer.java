package simpleshop.common;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Serialise a LocalDateTime to a long to be compatible with AngularJS.
 */
public class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {

    @Override
    public void serialize(LocalDateTime arg0, JsonGenerator arg1, SerializerProvider arg2) throws IOException {

        Long epoch = arg0.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        arg1.writeNumber(epoch);
    }
}