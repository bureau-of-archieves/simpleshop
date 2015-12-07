package simpleshop.common;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Annotation based object graph filter.
 */
public class AnnotationObjectGraphFilter implements ObjectGraphFilter<String>  {

    private String inspectionGroup; //default group

    @Override
    public void beforeInspection(String parameter) {
        inspectionGroup = parameter == null ? "" : parameter;
    }

    @Override
    public boolean shouldIgnore(Object target, Method method) {
        ObjectGraphIgnore objectGraphIgnore = method.getAnnotation(ObjectGraphIgnore.class);
        if(objectGraphIgnore != null && Arrays.asList(objectGraphIgnore.groups()).contains(inspectionGroup)){
            return true;
        }

        return false;
    }

    @Override
    public boolean shouldInspect(Object target, Class<?> clazz) {
        ObjectGraphIgnore objectGraphIgnore = clazz.getAnnotation(ObjectGraphIgnore.class);
        if(objectGraphIgnore != null && Arrays.asList(objectGraphIgnore.groups()).contains(inspectionGroup)){
            return false;
        }

        return true;
    }
}
