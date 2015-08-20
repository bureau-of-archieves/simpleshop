package simpleshop.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Util methods for collections.
 */
public final class CollectionUtils {

    private CollectionUtils(){}

    /**
     * Array to unmodifiable list.
     * @param values an array.
     * @param <T> type of array element.
     * @return unmodifiable list.
     */
    public static <T> List<T> unmodifiableList(T[] values){
        return Collections.unmodifiableList(Arrays.asList(values));
    }

    /**
     * Convert an iterable to a list.
     * @param iterable iterable.
     * @return a list of objects.
     */
    public static <T> List iterableToList(Iterable<T> iterable){
        List<T> list = new ArrayList<>();
        for(T obj : iterable){
            list.add(obj);
        }
        return list;
    }

    /**
     * Convert a string to an object array of target type.
     * @param value the string value which represents an target type array. If an object is passed the toString method will be called.
     * @param separator how the values are speparated in the string.
     * @param targetType target object type.
     * @return the result object array. Will not return null.
     */
    public static Object[] stringToObjectArray(Object value, String separator, Class<?> targetType){
        Object[] values = value.toString().split(separator);
        for(int i=0; i<values.length;i++)
            values[i] = simpleshop.common.ReflectionUtils.parseObject(values[i],targetType);
        return values;
    }

    public static Object[] objectToObjectArray(Object value, Class<?> targetType){
        Object[] values;
        if(value instanceof Iterable){
            values = CollectionUtils.iterableToList((Iterable)value).toArray();
        } else {
            values = CollectionUtils.stringToObjectArray(value.toString(), ",", targetType);
        }
        return values;
    }
}
