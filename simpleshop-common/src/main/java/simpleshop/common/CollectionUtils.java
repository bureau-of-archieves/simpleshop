package simpleshop.common;

import java.util.*;

/**
 * Util methods for collections.
 */
public final class CollectionUtils {

    private CollectionUtils(){}

    /**
     * Concatenate two String arrays.
     * @param array1 cannot be null.
     * @param array2 cannot be null.
     * @return result.
     */
    public static String[] concat(String[] array1, String[] array2){
        ArrayList<String> result = new ArrayList<>();
        result.addAll(Arrays.asList(array1));
        result.addAll(Arrays.asList(array2));
        String[] array = new String[result.size()];
        result.toArray(array);
        return array;
    }

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

    public static <T> boolean iterableEquals(Iterable<T> i1, Iterable<T> i2){

        if(Objects.equals(i1, i2))
            return true;

        if(i1 == null || i2 == null)
            return false;

        Iterator<T> iterator1 =  i1.iterator();
        Iterator<T> iterator2 =  i2.iterator();

        while (iterator1.hasNext()){

            if(!iterator2.hasNext())
                return false;

            T obj1 = iterator1.next();
            T obj2 = iterator2.next();

            if(!Objects.equals(obj1, obj2)){
                return false;
            }
        }

        if(iterator2.hasNext())
            return false;
        return true;
    }
}
