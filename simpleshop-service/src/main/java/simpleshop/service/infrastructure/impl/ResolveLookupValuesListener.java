package simpleshop.service.infrastructure.impl;

import org.hibernate.Hibernate;
import org.hibernate.collection.internal.PersistentMap;
import org.springframework.util.ReflectionUtils;
import simpleshop.common.PropertyReflector;
import simpleshop.common.StringUtils;
import simpleshop.data.metadata.ModelMetadata;
import simpleshop.data.util.DomainUtils;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * The name is misleading due to historical reason.
 * What this listener actually does is recursively null out all lazy Hibernate properties except for the ones listed as AutoLoad.
 */
public class ResolveLookupValuesListener implements PropertyReflector.PropertyValueListener {

    /**
     * The root domain object of the current traversal.
     * Having this allows us to know if we are at the top level or not.

    private Object rootObject;

    public ResolveLookupValuesListener(Object domainObject){
        rootObject = domainObject;
    }
    */

    @Override
    public PropertyReflector.InspectionResult visit(Object target, Method getter, Object value, Throwable exception, int index) {

        //root case
        if (getter == null)
            return PropertyReflector.InspectionResult.CONTINUE;

        //go into initialised object only
        if (Hibernate.isInitialized(value)) {
            if (value != null) { //exclude primitives and java types.
                if (value.getClass().getPackage() == null || value.getClass().getPackage().getName().startsWith("java.") || value instanceof Map)
                    return PropertyReflector.InspectionResult.BYPASS; //bypass system types
            }
            return PropertyReflector.InspectionResult.CONTINUE;
        }

        //for uninitialised properties, resolve lookup objects or trigger loading (lazy property) or set to null (all others).
        Class<?> valueClass = DomainUtils.getProxiedClass(value);
        Class<?> targetClass = DomainUtils.getProxiedClass(target);
        String propertyName = StringUtils.getPropertyName(getter.getName());
        Method setter = simpleshop.common.ReflectionUtils.getSetter(targetClass, propertyName, valueClass);
        if (setter != null) {
            ModelMetadata targetClassMetadata = DomainUtils.getModelMetadata(targetClass.getSimpleName());
            boolean exceptProperty = targetClassMetadata != null && targetClassMetadata.getAutoLoadProperties().contains(propertyName);
            if (exceptProperty) {
                if (value instanceof PersistentMap) {
                    Hibernate.initialize(value);
                    return PropertyReflector.InspectionResult.BYPASS;
                }
                return PropertyReflector.InspectionResult.CONTINUE;
            } else {
                ReflectionUtils.invokeMethod(setter, target, new Object[]{null});
            }
            return PropertyReflector.InspectionResult.BYPASS;
        } else {
            throw new RuntimeException("No setter found for uninitialised property getter:" + propertyName);
        }
    }

}
