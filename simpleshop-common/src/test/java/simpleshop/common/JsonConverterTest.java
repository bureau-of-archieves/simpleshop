package simpleshop.common;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import simpleshop.common.test.DomainNameTestObject;
import simpleshop.common.test.PersonTestObject;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestSpringConfig.class)
public class JsonConverterTest {

    @Autowired
    private JsonConverter converter;

    @Test
    public void toJsonTest(){
        JsonConverter converter = configuredJsonConverter();

        PersonTestObject personTestObject = new PersonTestObject();
        personTestObject.setName("Marsh");
        personTestObject.setGender(Boolean.TRUE);
        personTestObject.setAge(36);
        String result = converter.toJson(personTestObject);
        assertEquals("{\"name\":\"Marsh\",\"age\":36,\"gender\":true,\"children\":null,\"dateOfBirth\":null}", result);

        personTestObject.setChildren(new ArrayList<>());
        result = converter.toJson(personTestObject);
        assertEquals("{\"name\":\"Marsh\",\"age\":36,\"gender\":true,\"children\":[],\"dateOfBirth\":null}", result);

        PersonTestObject stan = new PersonTestObject();
        stan.setName("Stan");
        stan.setAge(8);
        stan.setGender(Boolean.TRUE);
        personTestObject.getChildren().add(stan);
        result = converter.toJson(personTestObject);
        assertEquals("{\"name\":\"Marsh\",\"age\":36,\"gender\":true,\"children\":[{\"name\":\"Stan\",\"age\":8,\"gender\":true,\"children\":null,\"dateOfBirth\":null}],\"dateOfBirth\":null}", result);

        stan.setDateOfBirth(LocalDate.of(1999,1,1));
        result = converter.toJson(personTestObject);
        assertEquals("{\"name\":\"Marsh\",\"age\":36,\"gender\":true,\"children\":[{\"name\":\"Stan\",\"age\":8,\"gender\":true,\"children\":null,\"dateOfBirth\":915109200000}],\"dateOfBirth\":null}", result);
    }

    @Test
    public void springInjectionTest(){
        assertSame(converter, JsonConverter.getInstance());
    }

    @Test
    public void excludeFieldsTest(){
        PersonTestObject personTestObject = new PersonTestObject();
        personTestObject.setName("Marsh");
        personTestObject.setGender(Boolean.TRUE);
        personTestObject.setAge(36);

        String[] excluded = {"children", "dateOfBirth"};
        String result = converter.toJson(personTestObject, excluded);
        assertThat(result, equalTo("{\"name\":\"Marsh\",\"age\":36,\"gender\":true}"));
    }

    @Test(expected = RuntimeException.class)
    public void toJsonExceptionTest(){

        PersonTestObject personTestObject = new PersonTestObject();
        personTestObject.setChildren(new ArrayList<>());
        personTestObject.getChildren().add(personTestObject);

        converter.toJson(personTestObject);
    }

    @Test
    public void fromJsonTest(){

        JsonConverter converter = configuredJsonConverter();

        PersonTestObject personTestObject = converter.fromJson("{\"name\":\"Marsh\",\"age\":36,\"gender\":true,\"dateOfBirth\":\"915109200000\"}", PersonTestObject.class);

        assertThat(personTestObject.getName(), equalTo("Marsh"));
        assertThat(personTestObject.getAge(), equalTo(36));
        assertThat(personTestObject.getGender(), equalTo(true));
        assertThat(personTestObject.getDateOfBirth(), equalTo(LocalDate.of(1999, 1, 1)));

        personTestObject = converter.fromJson("{\"name\":\"Marsh\",\"age\":36,\"gender\":true,\"dateOfBirth\":915109200000}", PersonTestObject.class);

        assertThat(personTestObject.getName(), equalTo("Marsh"));
        assertThat(personTestObject.getAge(), equalTo(36));
        assertThat(personTestObject.getGender(), equalTo(true));
        assertThat(personTestObject.getDateOfBirth(), equalTo(LocalDate.of(1999, 1, 1)));

        personTestObject = converter.fromJson("{\"name\":\"Marsh\",\"age\":36,\"gender\":true,\"dateOfBirth\":\"\"}", PersonTestObject.class);
        assertThat(personTestObject.getDateOfBirth(), equalTo(null));

        personTestObject = converter.fromJson("{\"name\":\"Marsh\",\"age\":36,\"gender\":true,\"dateOfBirth\":null}", PersonTestObject.class);
        assertThat(personTestObject.getDateOfBirth(), equalTo(null));
    }

    @Test(expected = RuntimeException.class)
    public void fromJsonExceptionTest(){

        converter.fromJson("{\"name\":\"Marsh\",\"age\":36,\"gender\":true,\"dateOfBirth\":\"915109200000\"}", PersonTestObject.class);
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

        assertThat(result, equalTo("{\"key\":1317923717000,\"value\":1317063317000}"));
        assertThat(converter.fromJson("1317923717000", LocalDateTime.class), equalTo(time1));
        assertThat(converter.fromJson("1317063317000", LocalDateTime.class), equalTo(time2));

        DomainNameTestObject domainName = converter.fromJson("{\"name\":\"test.org\",\"registrationDateTime\":\"\"}", DomainNameTestObject.class);
        assertThat(domainName.getRegistrationDateTime(), equalTo(null));

        domainName = converter.fromJson("{\"name\":\"test.org\",\"registrationDateTime\":null}", DomainNameTestObject.class);
        assertThat(domainName.getRegistrationDateTime(), equalTo(null));

    }

    private JsonConverter configuredJsonConverter(){
        JsonConverter converter = new JsonConverter();
        converter.setObjectMapper(ObjectMapperFactory.create());
        return converter;
    }

}
