package simpleshop.common;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * User: JOHNZ
 * Date: 8/09/14
 * Time: 12:59 PM
 */
public class JsonConverter {

    private static JsonConverter instance;

    /**
     * Singleton.
     * @return singleton constructed in Spring.
     */
    public static JsonConverter getInstance() {
        return instance;
    }

    private ObjectMapper objectMapper;

    /**
     * Inject the objectMapper.
     * @param objectMapper constructed in Spring.
     */
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void init(){
        instance = this;
    }

    /**
     * Convert an object to json.
     * @param obj the object.
     * @return json string.
     */
    public String toJson(Object obj) {
        return this.toJson(obj, null);
    }

    /**
     * Convert an object to Json with exclusion of specified properties.
     * @param obj object.
     * @param excludeFields name of properties which should not be serialized.
     * @return  json string.
     */
    public String toJson(Object obj, String[] excludeFields) {
        try {
            return objectMapper.writer(ObjectMapperFactory.createSerializeAllExceptFilterProvider(excludeFields)).writeValueAsString(obj);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Deserialize an object from json string.
     * @param json json string.
     * @return the object.
     */
    public <T> T fromJson(String json, Class<T> objectClass){
        try {
            return objectMapper.readValue(json, objectClass);
        }catch (IOException ex){
            throw new RuntimeException(ex);
        }
    }

}
