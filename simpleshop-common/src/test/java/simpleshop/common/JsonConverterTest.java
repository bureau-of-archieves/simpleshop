package simpleshop.common;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class JsonConverterTest {

    private static class Person {
        private String name;
        private Integer age;
        private Boolean gender;
        private List<Person> children;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        public Boolean getGender() {
            return gender;
        }

        public void setGender(Boolean gender) {
            this.gender = gender;
        }

        public List<Person> getChildren() {
            return children;
        }

        public void setChildren(List<Person> children) {
            this.children = children;
        }
    }

    @Test
    public void toJsonTest(){
        JsonConverter converter = new JsonConverter();
        Person person = new Person();
        person.setName("Marsh");
        person.setGender(Boolean.TRUE);
        person.setAge(36);
        String result = converter.toJson(person);
        assertEquals("{\"name\":\"Marsh\",\"age\":36,\"gender\":true,\"children\":null}", result);

        person.setChildren(new ArrayList<>());
        result = converter.toJson(person);
        assertEquals("{\"name\":\"Marsh\",\"age\":36,\"gender\":true,\"children\":[]}", result);

        Person stan = new Person();
        stan.setName("Stan");
        stan.setAge(8);
        stan.setGender(Boolean.TRUE);
        person.getChildren().add(stan);
        result = converter.toJson(person);
        assertEquals("{\"name\":\"Marsh\",\"age\":36,\"gender\":true,\"children\":[{\"name\":\"Stan\",\"age\":8,\"gender\":true,\"children\":null}]}", result);

    }
}
