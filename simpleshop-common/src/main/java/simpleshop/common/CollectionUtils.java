package simpleshop.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by ZHY on 24/01/2015.
 */
public final class CollectionUtils {

    private CollectionUtils(){}

    public static <T> List<T> unmodifiableList(T[] values){
        return Collections.unmodifiableList(Arrays.asList(values));
    }
}
