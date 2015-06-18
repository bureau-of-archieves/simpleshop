package simpleshop.common;

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
}
