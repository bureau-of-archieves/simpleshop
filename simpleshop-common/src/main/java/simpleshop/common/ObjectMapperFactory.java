package simpleshop.common;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Create and configure ObjectMapper.
 */
public class ObjectMapperFactory {

      public static ObjectMapper create(){
          ObjectMapper objectMapper = new ObjectMapper();

          objectMapper.registerModule(new JavaTimeModule());
          objectMapper.registerModule(createOverridingModule());
          objectMapper.setFilters(createSerializeAllExceptFilterProvider(null));

          return objectMapper;
      }

    private static SimpleModule createOverridingModule() {
        SimpleModule overridingModule = new SimpleModule("OverridingModule", new Version(1,0,0,null, null, null));  //first takes precedence
        overridingModule.addSerializer(LocalDate.class, new LocalDateSerializer());
        overridingModule.addDeserializer(LocalDate.class, new LocalDateDeserializer());
        overridingModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
        overridingModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());
        return overridingModule;
    }

    public static SimpleFilterProvider createSerializeAllExceptFilterProvider(String[] excludeFields){

        if (excludeFields == null)
            excludeFields = new String[0];

        SimpleFilterProvider filterProvider = new SimpleFilterProvider();
        filterProvider.addFilter("propNameFilter", SimpleBeanPropertyFilter.serializeAllExcept(excludeFields));
        return filterProvider;
    }

}
