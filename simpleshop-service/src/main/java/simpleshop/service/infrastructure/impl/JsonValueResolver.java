package simpleshop.service.infrastructure.impl;

import org.hibernate.Hibernate;
import org.hibernate.collection.internal.PersistentMap;
import org.springframework.util.ReflectionUtils;
import simpleshop.common.ObjectGraphInspectionResult;
import simpleshop.common.ObjectGraphVisitor;
import simpleshop.common.StringUtils;
import simpleshop.data.metadata.ModelMetadata;
import simpleshop.data.util.DomainUtils;
import simpleshop.domain.metadata.AutoLoad;
import simpleshop.domain.metadata.NullOut;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

/**
 * Traverse the object graph and make sure there will be no "no session" error when json is rendered from the object graph.
 * Recursively null out all lazy Hibernate properties except for the ones listed as AutoLoad.
 */
public class JsonValueResolver implements ObjectGraphVisitor<String> {

    private String inspectionGroup;

    @Override
    public void beforeInspection(String parameter) {
        inspectionGroup = parameter == null ? "" : parameter;
    }

    private Method getSetter(Object target, Method getter, Object value){
        Class<?> valueClass = DomainUtils.getProxiedClass(value);
        Class<?> targetClass = DomainUtils.getProxiedClass(target);
        String propertyName = StringUtils.getPropertyName(getter.getName());
        Method setter = simpleshop.common.ReflectionUtils.getSetter(targetClass, propertyName, valueClass);
        if (setter == null) {
            throw new RuntimeException("No setter found for uninitialised property:" + propertyName);
        }
        return setter;
    }

    /**
     * The root domain object of the current traversal.
     * Having this allows us to know if we are at the top level or not.
    */
    @Override
    public ObjectGraphInspectionResult visit(Object target, Method getter, Object value, Throwable exception, int index) {

        if (getter == null) //root case
            return ObjectGraphInspectionResult.CONTINUE;

        if(value == null)
            return ObjectGraphInspectionResult.BYPASS;

        NullOut nullOut = getter.getDeclaredAnnotation(NullOut.class);
        if(nullOut != null && Arrays.asList(nullOut.groups()).contains(inspectionGroup)){
            Method setter = getSetter(target, getter, value);
            ReflectionUtils.invokeMethod(setter, target, new Object[]{null});
            return ObjectGraphInspectionResult.BYPASS;
        }

        //go into initialised object only
        if (Hibernate.isInitialized(value)) {
            if (value instanceof Map)
                return ObjectGraphInspectionResult.BYPASS; //do not inspect map properties [convention]Map content is not inspected.
            return ObjectGraphInspectionResult.CONTINUE;
        }

        //for uninitialised properties, resolve lookup objects or trigger loading (lazy property) or set to null (all others).
        Method setter = getSetter(target, getter, value);
        AutoLoad autoLoad = getter.getAnnotation(AutoLoad.class);
        if(autoLoad != null && Arrays.asList(autoLoad.groups()).contains(inspectionGroup)){
            if (value instanceof PersistentMap) {
                Hibernate.initialize(value);
                return ObjectGraphInspectionResult.BYPASS;
            }
            return ObjectGraphInspectionResult.CONTINUE;
        } else {
            ReflectionUtils.invokeMethod(setter, target, new Object[]{null});
            return ObjectGraphInspectionResult.BYPASS;
        }
    }

}
