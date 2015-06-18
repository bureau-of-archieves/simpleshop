package simpleshop.common;

import org.junit.Assert;
import org.junit.Test;

import javax.imageio.stream.ImageOutputStream;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Stream;
import static org.junit.Assert.*;

/**
 * Unit tests for ReflectionUtils.
 */
public class ReflectionUtilsTest {

    private static class TestObject
    {
        private Node node;

        public int getAge(){
            return 0;
        }

        public String getName(){
            return "test";
        }

        public boolean isMale(){
            return true;
        }

        public void setStream(Stream stream){
            if(stream == null)
                throw new IllegalArgumentException();
        }

        public Node getNode() {
            return node;
        }

        public void setNode(Node node) {
            this.node = node;
        }
    }

    private static class Node{
        private Node next;
        private int value;

        public Node getNext() {
            return next;
        }

        public void setNext(Node next) {
            this.next = next;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }

    private interface MyStream extends Stream{
    }

    @Test
    public void isPublicGetterTest(){
        Method[] methods = Object.class.getMethods();
        for(Method method : methods){
            boolean result = ReflectionUtils.isPublicInstanceGetter(method);
            Assert.assertFalse(result);
        }

        methods = TestObject.class.getMethods();
        for(Method method : methods){
            boolean result = ReflectionUtils.isPublicInstanceGetter(method);
            if(Arrays.asList(new String[]{"getAge", "getName", "getNode"}).contains(method.getName()) )
                Assert.assertTrue(result);
            else
                Assert.assertFalse(result);
        }

    }

    @Test
    public void getSetterTest() throws NoSuchMethodException{
        Method getter = TestObject.class.getMethod("setStream",Stream.class);
        Method result = ReflectionUtils.getSetter(TestObject.class, "stream", Stream.class);
        assertEquals(getter, result);

        result = ReflectionUtils.getSetter(TestObject.class, "stream", ImageOutputStream.class);
        assertNull(result);

        result = ReflectionUtils.getSetter(TestObject.class, "stream", MyStream.class);
        assertEquals(getter, result);
    }

    @Test
    public void parseStringTest(){
        Boolean value = ReflectionUtils.parseString("true", Boolean.class);
        assertEquals(Boolean.TRUE, value);

        ReflectionUtils.parseString("True", Boolean.class);
        assertEquals(Boolean.TRUE, value);

        Integer integer = ReflectionUtils.parseString("123", Integer.class);
        assertEquals(Integer.valueOf("123"), integer);

    }

    @Test
    public void getPropertyTest(){

        assertNull(ReflectionUtils.getProperty(null, "whatever"));

        TestObject testObject = new TestObject();
        assertEquals("test", ReflectionUtils.getProperty(testObject, "name"));

        testObject.setNode(new Node());
        testObject.getNode().setValue(5);
        assertEquals(5, ReflectionUtils.getProperty(testObject, "node.value"));

        testObject.getNode().setNext(new Node());
        testObject.getNode().getNext().setValue(999);
        assertEquals(999, ReflectionUtils.getProperty(testObject, "node.next.value"));
    }

    @Test
    public void setPropertyTest(){

        assertFalse(ReflectionUtils.setProperty(null, "whatever", "anything"));

        TestObject testObject = new TestObject();
        assertTrue(ReflectionUtils.setProperty(testObject, "node", new Node()));
        assertNotNull(testObject.getNode());

        assertTrue(ReflectionUtils.setProperty(testObject, "node.value", 999));
        assertEquals(999, testObject.getNode().getValue());

        assertFalse(ReflectionUtils.setProperty(testObject, "node.next.value", 999));

        assertTrue(ReflectionUtils.setProperty(testObject, "node.next", new Node()));
        assertTrue(ReflectionUtils.setProperty(testObject, "node.next.value", 888));
        assertEquals(888, testObject.getNode().getNext().getValue());
    }
}
