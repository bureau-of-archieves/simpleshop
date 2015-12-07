package simpleshop.common;

import java.lang.reflect.Method;

/**
 * The visitor whose methods are called during the object graph search.
 */
public interface ObjectGraphVisitor<T> {

    /**
     * Called before the inspection is started.
     * @param parameter the parameter for this pass of object graph traversal.
     */
    void beforeInspection(T parameter);

    /**
     * This method is call for all public getter values accessed.
     * @param target owner of the property.
     * @param getter the getter of the property. This is null for the root target object itself.
     * @param value the value of the property.
     * @param exception the exception if one occurred while retrieve the property value.
     * @param index of the property value is a collection, index is the index of the value. index == -1 for non-collection property values.
     * @return indicate whether to stop, continue or bypass the inspection process.
     */
    ObjectGraphInspectionResult visit(Object target, Method getter, Object value, Throwable exception, int index);
}
