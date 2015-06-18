package simpleshop.common;

import org.junit.Test;

import java.util.List;

/**
 * Unit tests for <code>CollectionUtils</code>.
 */
public class CollectionUtilsTest {

    @Test(expected = UnsupportedOperationException.class)
    public void unmodifiableList_CannotAddTest(){
        List<Integer> list = CollectionUtils.unmodifiableList(new Integer[]{1,2,3,4});
        list.add(5);
    }
}
