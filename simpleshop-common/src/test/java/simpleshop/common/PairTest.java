package simpleshop.common;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import org.junit.Test;

/**
 * Test if pair works ok.
 */
public class PairTest {

    @Test
    public void hashCodeTest() {

        Pair<Integer, String> pair = new Pair<>();
        pair.setKey(199);
        pair.setValue("My Value");
        int hashCode = pair.hashCode();
        pair.setKey(876);
        int hashCode2 = pair.hashCode();
        pair.setValue("Watch");
        int hashCode3 = pair.hashCode();

        assertThat(hashCode == hashCode2, equalTo(false));
        assertThat(hashCode2 == hashCode3, equalTo(false));
        assertThat(hashCode == hashCode3, equalTo(false));

        int hashCode4 = new Pair<>(876, "Watch").hashCode();
        assertThat(hashCode4, equalTo(hashCode3));
    }

    @Test
    public void equalsTest() {

        assertThat(new Pair<>(), equalTo(new Pair<>()));
        assertThat(new Pair<>(123, null), equalTo(new Pair<>(123, null)));
        assertThat(new Pair<>(123, "bad"), equalTo(new Pair<>(123, "bad")));
        assertThat(new Pair<>(123, "bad1").equals(new Pair<>(123, "bad")) , equalTo(false));
        assertThat(new Pair<>(123, "bad1").equals(new Pair<>(1232, "bad1")) , equalTo(false));
    }


}
