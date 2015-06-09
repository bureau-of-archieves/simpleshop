package simpleshop.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.stereotype.Component;

/**
 * User: JOHNZ
 * Date: 8/09/14
 * Time: 12:59 PM
 */
@Component
public class JsonConverter {

    public String toJson(Object obj) {
        return this.toJson(obj, null);
    }

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String toJson(Object obj, String[] excludeFields) {
        try {
            return objectMapper.writer(createSerializeAllExceptFilterProvider(excludeFields)).writeValueAsString(obj);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static SimpleFilterProvider createSerializeAllExceptFilterProvider(String[] excludeFields){

        if (excludeFields == null)
            excludeFields = new String[0];

        SimpleFilterProvider filterProvider = new SimpleFilterProvider();
        filterProvider.addFilter("propNameFilter", SimpleBeanPropertyFilter.serializeAllExcept(excludeFields));
        return filterProvider;
    }
}
