package simpleshop.data.infrastructure;

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

    public String toJson(Object obj, String[] excludeFields) {
        try {
            if (excludeFields == null)
                excludeFields = new String[0];

            ObjectMapper objectMapper = new ObjectMapper();
            SimpleFilterProvider filterProvider = new SimpleFilterProvider();
            filterProvider.addFilter("propNameFilter", SimpleBeanPropertyFilter.serializeAllExcept(excludeFields));
            return objectMapper.writer(filterProvider).writeValueAsString(obj);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
