package simpleshop.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for unit tests.
 */
@Configuration
public class TestSpringConfig {

    @Bean
    public ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public JsonConverter getJsonConverter() {
        JsonConverter jsonConverter = new JsonConverter();
        jsonConverter.setObjectMapper(getObjectMapper());
        return jsonConverter;
    }
}
