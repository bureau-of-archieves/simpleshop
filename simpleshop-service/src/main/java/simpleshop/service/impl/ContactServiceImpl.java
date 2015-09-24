package simpleshop.service.impl;

import simpleshop.dto.ContactSearch;
import simpleshop.service.infrastructure.impl.ModelServiceImpl;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Contact model service base class.
 */
public abstract class ContactServiceImpl<T, S extends ContactSearch> extends ModelServiceImpl<T, S> {

    private Collection<String> lazyLoadedProperties;

    @PostConstruct
    public void init(){
        lazyLoadedProperties = new ArrayList<>();
        lazyLoadedProperties.add("contactNumbers");
    }

    @Override
    public Collection<String> lazyLoadedProperties() {
        return lazyLoadedProperties;
    }
}
