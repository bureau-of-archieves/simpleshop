package simpleshop.common;

import org.junit.Assert;
import org.junit.Test;
import simpleshop.common.test.DomainNameTestObject;

import javax.imageio.stream.ImageOutputStream;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Stream;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

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
        Method getter = TestObject.class.getMethod("setStream", Stream.class);
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
    public void parseObjectTest(){

         assertThat(ReflectionUtils.parseObject(null, Boolean.class), equalTo(null));
         assertThat(ReflectionUtils.parseObject(Boolean.TRUE, Boolean.class), equalTo(Boolean.TRUE));
         assertThat(ReflectionUtils.parseObject("true", Boolean.class), equalTo(Boolean.TRUE));

    }

    @Test(expected = IllegalArgumentException.class)
    public void parseStringExceptionTest(){

        ReflectionUtils.parseString("abc", this.getClass());
    }

    @Test(expected = RuntimeException.class)
    public void parseStringNotSupportedTest(){

        ReflectionUtils.parseString("abc", Integer.class);
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

    @Test(expected = RuntimeException.class)
    public void getPropertyExceptionTest(){

        ReflectionUtils.getProperty("test1", "class.xxx");
    }

    @Test
    public void getProperty_IsEmptyTest(){
        Boolean value = (Boolean)ReflectionUtils.getProperty("", "empty");

        assertThat(value, equalTo(true));

        value = (Boolean)ReflectionUtils.getProperty("aaa", "empty");

        assertThat(value, equalTo(false));
    }

    @Test
    public void setPropertyTest(){

        assertFalse(ReflectionUtils.setProperty(null, "whatever", "anything"));

        TestObject testObject = new TestObject();
        assertTrue(ReflectionUtils.setProperty(testObject, "node", new Node()));
        assertNotNull(testObject.getNode());

        assertTrue(ReflectionUtils.setProperty(testObject, "node.value", 999));
        assertThat(testObject.getNode().getValue(), equalTo(999));

        assertFalse(ReflectionUtils.setProperty(testObject, "node.next.value", 999));

        assertTrue(ReflectionUtils.setProperty(testObject, "node.next", new Node()));
        assertTrue(ReflectionUtils.setProperty(testObject, "node.next.value", 888));
        assertEquals(888, testObject.getNode().getNext().getValue());
    }

    @Test
    public void setPropertyReturnValueTest(){

        boolean valueSet = ReflectionUtils.setProperty(new Object(), "class", "can't do");
        assertThat(valueSet, equalTo(false));

        valueSet = ReflectionUtils.setProperty(new Pair<String, String>(), "key", "can do");
        assertThat(valueSet, equalTo(true));

    }



    @Test(expected = RuntimeException.class)
    public void setPropertyExceptionTest(){
        DomainNameTestObject domainNameTestObject = new DomainNameTestObject();
        ReflectionUtils.setProperty(domainNameTestObject, "name", "*invalid*");
    }



}
