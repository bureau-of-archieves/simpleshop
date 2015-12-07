package simpleshop.common;

import org.junit.Test;
import simpleshop.common.test.EntityTestObject;

import java.lang.reflect.Method;

class TestObjectGraphVisitor implements ObjectGraphVisitor<String> {

    @Override
    public void beforeInspection(String parameter) {

    }

    @Override
    public ObjectGraphInspectionResult visit(Object target, Method getter, Object value, Throwable exception, int index) {
        System.out.printf("target=%s, getter=%s, value=%s,exception=%s, index=%d\n", target, getter == null ? "null" : getter.getName(), value, exception == null ? "null" : exception.getMessage(), index);
        return ObjectGraphInspectionResult.CONTINUE;
    }
}

/**
 * Created with IntelliJ IDEA.
 * User: JOHNZ
 * Date: 11/09/14
 * Time: 2:29 PM
 */
public class PropertyReflectorTest {

    @Test
    public void testInspection() {
        ObjectGraphDFS<String> inspector = new ObjectGraphDFS<>(new TestObjectGraphVisitor(), new AnnotationObjectGraphFilter(), null);

        EntityTestObject entityTestObject = new EntityTestObject();
        entityTestObject.setName("John");
        entityTestObject.setId(12);
        entityTestObject.setSpouse(entityTestObject);
        entityTestObject.getRelatives().add(entityTestObject);

        EntityTestObject entityTestObject1 = new EntityTestObject();
        entityTestObject1.setName("Steve");
        entityTestObject.getRelatives().add(entityTestObject1);
        entityTestObject.getRelatives().add(entityTestObject1);
        entityTestObject1.getRelatives().add(entityTestObject);

        inspector.inspect(entityTestObject);
    }

}
