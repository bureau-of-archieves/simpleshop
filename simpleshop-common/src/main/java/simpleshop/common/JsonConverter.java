package simpleshop.common;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.annotation.PostConstruct;

/**
 * User: JOHNZ
 * Date: 8/09/14
 * Time: 12:59 PM
 */
public class JsonConverter {


    public String toJson(Object obj) {
        return this.toJson(obj, null);
    }

    private static JsonConverter instance;


    private ObjectMapper objectMapper;

    @PostConstruct
    public void init(){
       instance = this;
    }

    public String toJson(Object obj, String[] excludeFields) {
        try {
            return objectMapper.writer(ObjectMapperFactory.createSerializeAllExceptFilterProvider(excludeFields)).writeValueAsString(obj);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public static JsonConverter getInstance() {
        return instance;
    }
}
