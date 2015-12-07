package simpleshop.common;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * Inspect the given target object and all its public getter values recursively. If a value is a collection, all the items are visited as well (if visitor does not return BYPASS on the collection itself).
 * A listener object is required to examine the values.
 * Instances of this class is not thread safe.
 */
public class ObjectGraphDFS<T> {

    private final ObjectGraphVisitor<T> listener;
    private final ObjectGraphFilter<T> filter;
    private final DeProxyStrategy deProxyStrategy;

    private Set<Object> visited = new HashSet<>();

    public ObjectGraphDFS(ObjectGraphVisitor<T> listener, ObjectGraphFilter<T> filter, DeProxyStrategy deProxyStrategy) {
        if (listener == null)
            throw new IllegalArgumentException("Parameter listener cannot be null.");
        if (filter == null)
            throw new IllegalArgumentException("Parameter filter cannot be null.");

        this.listener = listener;
        this.deProxyStrategy = deProxyStrategy;
        this.filter = filter;
    }

    private Class<?> getObjectClass(Object target) {
        assert target != null;
        Class<?> clazz;
        if (this.deProxyStrategy != null) {
            clazz = deProxyStrategy.getProxiedClass(target);
        } else {
            clazz = target.getClass();
        }
        return clazz;
    }

    private void setInspected(Object target) {
        if(target == null)
            return;
        visited.add(target);
    }

    private boolean canInspect(Object target){
        if(target == null)
            return false;

        Class<?> clazz = getObjectClass(target);
        if(clazz.isPrimitive() || clazz.isEnum() || ReflectionUtils.isBasicValueType(clazz))
            return false;

        return !visited.contains(target) && filter.shouldInspect(target, clazz);
    }

    public boolean inspect(Object target){
        return inspect(target, null);
    }

    public boolean inspect(Object target, T parameter) {
        boolean hasNoError = true;
        if (target != null) {
            listener.beforeInspection(parameter);
            filter.beforeInspection(parameter);
            ObjectGraphInspectionResult inspectionResult = listener.visit(null, null, target, null, -1);
            if(inspectionResult == ObjectGraphInspectionResult.ABORT){
                hasNoError = false;
            }
            if(inspectionResult == ObjectGraphInspectionResult.CONTINUE && canInspect(target)){
                setInspected(target);
                hasNoError = depthFirstTraversal(target);
            }
            visited.clear();
        }
        return hasNoError;
    }

    /**
     * Check the object graph with DFS.
     * - Only public getters are inspected
     * - Iterable is inspected and expanded
     * - Map is not expanded

     * @param target the root object.
     * @return true if there is no error, false if error has occurred (search will abort).
     */
    private boolean depthFirstTraversal(Object target) {

        assert target != null;

        Class<?> realClass = getObjectClass(target);
        for (Method method : realClass.getMethods()) {
            if (!ReflectionUtils.isPublicInstanceGetter(method) || filter.shouldIgnore(target, method))
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
            ObjectGraphInspectionResult inspectionResult = listener.visit(target, method, value, exception, -1);
            if (inspectionResult == ObjectGraphInspectionResult.ABORT)
                return false;

            if (inspectionResult == ObjectGraphInspectionResult.BYPASS || !canInspect(value))
                continue;

            setInspected(value);//inspected means the properties of the object have been checked
            if (value instanceof Iterable<?>) {
                int index = 0; //only mean something in list though
                for (Object item : (Iterable<?>) value) {
                    inspectionResult = listener.visit(target, method, item, null /*if we have value here then exception was not thrown*/, index++);
                    if (inspectionResult == ObjectGraphInspectionResult.ABORT)
                        return false;

                    if (inspectionResult == ObjectGraphInspectionResult.BYPASS || !canInspect(item))
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

}