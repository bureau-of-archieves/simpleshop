package simpleshop.common;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;

/**
 * Serialise a LocalDate to a long to be compatible with AngularJS.
 */
public class LocalDateSerializer extends JsonSerializer<LocalDate> {

    @Override
    public void serialize(LocalDate arg0, JsonGenerator arg1, SerializerProvider arg2) throws IOException {

        Long epoch = arg0.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
        arg1.writeString(epoch.toString());
    }
}
