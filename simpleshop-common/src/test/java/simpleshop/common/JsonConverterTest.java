package simpleshop.common;

import com.fasterxml.jackson.annotation.JsonFilter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestSpringConfig.class)
public class JsonConverterTest {

    @JsonFilter("propNameFilter")
    private static class Person {
        private String name;
        private Integer age;
        private Boolean gender;
        private List<Person> children;
        private LocalDate dateOfBirth;

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

        public LocalDate getDateOfBirth() {
            return dateOfBirth;
        }

        public void setDateOfBirth(LocalDate dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
        }
    }

    @Test
    public void toJsonTest(){
        JsonConverter converter = configuredJsonConverter();

        Person person = new Person();
        person.setName("Marsh");
        person.setGender(Boolean.TRUE);
        person.setAge(36);
        String result = converter.toJson(person);
        assertEquals("{\"name\":\"Marsh\",\"age\":36,\"gender\":true,\"children\":null,\"dateOfBirth\":null}", result);

        person.setChildren(new ArrayList<>());
        result = converter.toJson(person);
        assertEquals("{\"name\":\"Marsh\",\"age\":36,\"gender\":true,\"children\":[],\"dateOfBirth\":null}", result);

        Person stan = new Person();
        stan.setName("Stan");
        stan.setAge(8);
        stan.setGender(Boolean.TRUE);
        person.getChildren().add(stan);
        result = converter.toJson(person);
        assertEquals("{\"name\":\"Marsh\",\"age\":36,\"gender\":true,\"children\":[{\"name\":\"Stan\",\"age\":8,\"gender\":true,\"children\":null,\"dateOfBirth\":null}],\"dateOfBirth\":null}", result);

        stan.setDateOfBirth(LocalDate.of(1999,1,1));
        result = converter.toJson(person);
        assertEquals("{\"name\":\"Marsh\",\"age\":36,\"gender\":true,\"children\":[{\"name\":\"Stan\",\"age\":8,\"gender\":true,\"children\":null,\"dateOfBirth\":\"915109200000\"}],\"dateOfBirth\":null}", result);
    }


    @Autowired
    private JsonConverter converter;

    @Test
    public void springInjectionTest(){
        assertSame(converter, JsonConverter.getInstance());
    }

    @Test
    public void excludeFieldsTest(){
        Person person = new Person();
        person.setName("Marsh");
        person.setGender(Boolean.TRUE);
        person.setAge(36);

        String[] excluded = {"children", "dateOfBirth"};
        String result = converter.toJson(person, excluded);
        assertThat(result, equalTo("{\"name\":\"Marsh\",\"age\":36,\"gender\":true}"));
    }

    @Test(expected = RuntimeException.class)
    public void toJsonExceptionTest(){

        Person person = new Person();
        person.setChildren(new ArrayList<>());
        person.getChildren().add(person);

        converter.toJson(person);
    }

    @Test
    public void fromJsonTest(){

        JsonConverter converter = configuredJsonConverter();

        Person person = converter.fromJson("{\"name\":\"Marsh\",\"age\":36,\"gender\":true,\"dateOfBirth\":\"915109200000\"}", Person.class);

        assertThat(person.getName(), equalTo("Marsh"));
        assertThat(person.getAge(), equalTo(36));
        assertThat(person.getGender(), equalTo(true));
        assertThat(person.getDateOfBirth(), equalTo(LocalDate.of(1999, 1, 1)));
    }

    @Test(expected = RuntimeException.class)
    public void fromJsonExceptionTest(){

        converter.fromJson("{\"name\":\"Marsh\",\"age\":36,\"gender\":true,\"dateOfBirth\":\"915109200000\"}", Person.class);
    }

    @Test
    public void localDateTimeJsonTest(){
        Pair<LocalDateTime, LocalDateTime> pair = new Pair<>();
        LocalDateTime time1 = LocalDateTime.of(2011, 10, 7, 4, 55, 17);
        LocalDateTime time2 = time1.minusDays(10);
        pair.setKey(time1);
        pair.setValue(time2);

        JsonConverter converter = configuredJsonConverter();
        String result = converter.toJson(pair);

        assertThat(result, equalTo("{\"key\":\"1317923717000\",\"value\":\"1317063317000\"}"));
        assertThat(converter.fromJson("1317923717000", LocalDateTime.class), equalTo(time1));
        assertThat(converter.fromJson("1317063317000", LocalDateTime.class), equalTo(time2));
    }

    private JsonConverter configuredJsonConverter(){
        JsonConverter converter = new JsonConverter();
        converter.setObjectMapper(ObjectMapperFactory.create());
        return converter;
    }

}
