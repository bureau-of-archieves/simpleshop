package simpleshop.service.impl;

import org.springframework.stereotype.Service;
import simpleshop.common.*;
import simpleshop.data.metadata.*;
import simpleshop.data.util.*;
import simpleshop.domain.metadata.*;
import simpleshop.service.*;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.TreeMap;

/**
 * Get metadata of the domain models.
 */
@Service
public class MetadataServiceImpl extends BaseServiceImpl implements MetadataService {

    private final Class<?>[] classes;
    private Map<String, ModelMetadata> metadataMap;

    public MetadataServiceImpl(Class<?>[] classes){
        this.classes = classes;
    }

    @PostConstruct
    public void init(){
        metadataMap = DomainUtils.createModelMetadataMap(classes);
    }

    @Override
    public Map<String, ModelMetadata> get() {
        return metadataMap;
    }

    @Override
    public ModelMetadata get(String modelName) {
        if(metadataMap.containsKey(modelName))
            return metadataMap.get(modelName);
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String extractItemText(Object domainObject) {
        if(domainObject == null)
            return null;

        TreeMap<Integer, String> treeMap = new TreeMap<>();
        for(Method method : domainObject.getClass().getMethods()){
            if(!ReflectionUtils.isPublicInstanceGetter(method))
                continue;

            ItemText text = method.getAnnotation(ItemText.class);
            if(text == null)
                continue;

            try {
                Object value = method.invoke(domainObject);
                if(value != null){
                    treeMap.put(text.order() * 2 + 1,text.separator());
                    treeMap.put(text.order() * 2 + 2, value.toString());
                }
            }catch (IllegalAccessException | InvocationTargetException ex){
                throw new RuntimeException(ex);
            }
        }

        String result = "";
        for (Integer key : treeMap.keySet()){
            if(key % 2 == 1){ //separator
                if(result.length() > 0)
                    result += treeMap.get(key);
            } else {
                result += treeMap.get(key);
            }
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String extractItemValue(Object domainObject) {
        if(domainObject == null)
            return null;

        TreeMap<Integer, String> treeMap = new TreeMap<>();
        for(Method method : domainObject.getClass().getMethods()){
            if(!ReflectionUtils.isPublicInstanceGetter(method))
                continue;

            ItemValue text = method.getAnnotation(ItemValue.class);
            if(text == null)
                continue;

            try {
                Object value = method.invoke(domainObject);
                if(value != null){
                    treeMap.put(text.order() * 2 + 1,text.separator());
                    treeMap.put(text.order() * 2 + 2, value.toString());
                }
            }catch (IllegalAccessException | InvocationTargetException ex){
                throw new RuntimeException(ex);
            }
        }

        String result = "";
        for (Integer key : treeMap.keySet()){
            if(key % 2 == 1){ //separator
                if(result.length() > 0)
                    result += treeMap.get(key);
            } else {
                result += treeMap.get(key);
            }
        }
        return result;
    }
}
