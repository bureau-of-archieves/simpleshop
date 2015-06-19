package simpleshop.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Inspect the given target object and all its public getter values recursively. If a value is a collection, all the items are visited as well (if visitor does not return BYPASS on the collection itself).
 * A listener object is required to examine the values.
 * A class locator object can be passed in the resolve the real class (rather than the proxy class) of the values.
 * Properties that are listed with JsonIgnoreProperties metadata are excluded.
 */
public class PropertyReflector {
    private PropertyValueListener listener;
    private HashMap<Integer, Object> visited = new HashMap<>();
    private ClassLocator classLocator;

    public PropertyReflector(PropertyValueListener listener, ClassLocator classLocator) {
        if (listener == null)
            throw new IllegalArgumentException("Parameter listener cannot be null.");
        this.listener = listener;
        this.classLocator = classLocator;
    }

    private Class<?> getObjectClass(Object target) {
        Class<?> clazz;
        if (this.classLocator != null) {
            clazz = this.classLocator.find(target);
        } else {
            clazz = target.getClass();
        }
        return clazz;
    }

    private HashSet<String> getIgnoredGetters(Object target) {

        Class<?> clazz = getObjectClass(target);
        JsonIgnoreProperties ignoreProperties = clazz.getAnnotation(JsonIgnoreProperties.class);
        HashSet<String> result = null;
        if (ignoreProperties != null) {
            result = new HashSet<>();
            for (String prop : ignoreProperties.value()) {
                result.add("get" + prop.substring(0, 1).toUpperCase() + prop.substring(1));
            }
        }
        return result;
    }

    private boolean isInspected(Object target) {
        if (target == null)
            return true;
        Integer identityHashCode = System.identityHashCode(target);
        return visited.containsKey(identityHashCode);
    }

    private void setInspected(Object target) {
        Integer identityHashCode = System.identityHashCode(target);
        visited.put(identityHashCode, target);
    }

    public boolean inspect(Object target) {
        boolean hasNoError = true;
        visited.clear();
        if (target != null) {
            hasNoError = listener.visit(null, null, target, null, -1) == InspectionResult.CONTINUE;
            if (hasNoError) {
                setInspected(target);
                hasNoError = depthFirstTraversal(target);
            }
            visited.clear();
        }
        return hasNoError;
    }

    private boolean depthFirstTraversal(Object target) {

        Method[] methods = getObjectClass(target).getMethods();
        HashSet<String> ignoredGetters = getIgnoredGetters(target);

        for (Method method : methods) {
            if (!ReflectionUtils.isPublicInstanceGetter(method))
                continue;

            if (ignoredGetters != null && ignoredGetters.contains(method.getName()))
                continue;
//fetch value
            Object value = null;
            Throwable exception = null;
            try {
                value = method.invoke(target);
            } catch (Throwable ex) {
                exception = ex;
            }
//visit
            InspectionResult inspectionResult = listener.visit(target, method, value, exception, -1);
            if (inspectionResult == InspectionResult.ABORT)
                return false;

            if (inspectionResult == InspectionResult.BYPASS || isInspected(value) || method.getReturnType().isPrimitive())
                continue;

            setInspected(value);
            if (value instanceof Iterable<?>) {
                int index = 0;
                for (Object item : (Iterable<?>) value) {
                    inspectionResult = listener.visit(target, method, item, null /*if we have value here then exception was not thrown*/, index++);
                    if (inspectionResult == InspectionResult.ABORT)
                        return false;

                    if (inspectionResult == InspectionResult.BYPASS || isInspected(item) || method.getReturnType().isPrimitive())
                        continue;

                    setInspected(item);
                    if (!depthFirstTraversal(item))
                        return false;

                }
            } else {
                if (!depthFirstTraversal(value))
                    return false;
            }
        }

        return true;
    }

    //find the metadata class of the target class
    public interface ClassLocator {

        Class<?> find(Object target);
    }

    public enum InspectionResult {
        CONTINUE,
        BYPASS,
        ABORT
    }

    public interface PropertyValueListener {

        /**
         * This method is call for all public getter values accessed.
         * @param target owner of the property.
         * @param getter the getter of the property. This is null for the root target object itself.
         * @param value the value of the property.
         * @param exception the exception if one occurred while retrieve the property value.
         * @param index of the property value is a collection, index is the index of the value. index == -1 for non-collection property values.
         * @return indicate whether to stop, continue or bypass the inspection process.
         */
        InspectionResult visit(Object target, Method getter, Object value, Throwable exception, int index);
    }
}