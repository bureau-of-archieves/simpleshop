package simpleshop.common;

import java.lang.reflect.Method;

/**
 * Determines if continue to inspect a property.
 */
public interface ObjectGraphFilter<T> {

    /**
     * Called before the inspection is started.
     * @param parameter the parameter for this pass of object graph traversal.
     */
    void beforeInspection(T parameter);

    /**
     * Determine if the traversal should ignore this property.
     * @return true if should ignore, false to continue to invoke the getter.
     */
    boolean shouldIgnore(Object target, Method method);

    /**
     * Determine if the traversal should continue to examine the properties of a target value.
     * @param target an object in the object graph.
     * @param clazz the un-proxied class of the target object.
     * @return true if continue to inspect the properties of the target object.
     */
    boolean shouldInspect(Object target, Class<?> clazz);
}
