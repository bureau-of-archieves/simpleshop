package simpleshop.common;

import org.junit.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: JOHNZ
 * Date: 11/09/14
 * Time: 2:29 PM
 */
public class PropertyReflectorTest {

    @Test
    public void testInspection() {
        PropertyReflector inspector = new PropertyReflector(
                new PropertyReflector.PropertyValueListener() {
                    @Override
                    public PropertyReflector.InspectionResult visit(Object target, Method getter, Object value, Throwable exception, int index) {
                        System.out.printf("target=%s, getter=%s, value=%s,exception=%s, index=%d\n", target, getter == null ? "null" : getter.getName(), value, exception == null ? "null" : exception.getMessage(), index);
                        return PropertyReflector.InspectionResult.CONTINUE;
                    }
                }, null
        );

        Entity entity = new Entity();
        entity.setName("John");
        entity.setId(Integer.valueOf(12));
        entity.setSpouse(entity);
        entity.getRelatives().add(entity);

        Entity entity1 = new Entity();
        entity1.setName("Steve");
        entity.getRelatives().add(entity1);
        entity.getRelatives().add(entity1);
        entity1.getRelatives().add(entity);

        inspector.inspect(entity);
    }

    //test class
    private static class Entity {

        private Integer id;
        private String name;
        private String type;
        private Entity spouse;
        private ArrayList<Entity> relatives = new ArrayList<>();

        public ArrayList<Entity> getRelatives() {
            return relatives;
        }

        public void setRelatives(ArrayList<Entity> relatives) {
            this.relatives = relatives;
        }

        public Entity getSpouse() {
            return spouse;
        }

        public void setSpouse(Entity spouse) {
            this.spouse = spouse;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getScore() {
            return 99;
        }

    }
}
