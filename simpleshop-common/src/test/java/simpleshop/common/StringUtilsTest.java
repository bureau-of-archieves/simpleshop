package simpleshop.common;

import org.junit.Test;

import java.lang.reflect.Array;
import java.util.*;

import static org.junit.Assert.*;
/**
 * Unit tests for StringUtilsTest.
 */
public class StringUtilsTest {

    @Test
    public void subStrB4Test(){
        assertEquals("test", StringUtils.subStrB4("test", "."));
        assertEquals("test", StringUtils.subStrB4("test.exe", "."));
        assertEquals("test", StringUtils.subStrB4("test.exe.bbb", "."));
        assertEquals("", StringUtils.subStrB4("", "sep"));
    }

    @Test
    public void subStrBetweenLastAndFirstTest(){
        assertEquals("super test", StringUtils.subStrBetweenLastAndFirst("This is my super test.", "my ", "."));
        assertEquals("super test", StringUtils.subStrBetweenLastAndFirst("my This is my super test.", "my ", "."));
        assertEquals("super test", StringUtils.subStrBetweenLastAndFirst("my This is my super test.hbm.xml", "my ", "."));
    }

    @Test
    public void getPropertyNameTest(){
        assertEquals("name", StringUtils.getPropertyName("getName"));
        assertEquals("birthDate", StringUtils.getPropertyName("setBirthDate"));
        assertEquals("valid", StringUtils.getPropertyName("isValid"));
    }

    @Test
    public void friendlyNameToCamelNameTest(){

        assertEquals(null, StringUtils.friendlyNameToCamelName(null));
        assertEquals("", StringUtils.friendlyNameToCamelName(""));
        assertEquals("entity", StringUtils.friendlyNameToCamelName("Entity"));
        assertEquals("entityId", StringUtils.friendlyNameToCamelName("Entity Id"));
        assertEquals("memberAccountInvestment", StringUtils.friendlyNameToCamelName("Member Account Investment"));
    }

    @Test
    public void camelNameToFriendlyNameTest(){
        assertEquals(null, StringUtils.camelNameToFriendlyName(null));
        assertEquals("Model Name", StringUtils.camelNameToFriendlyName("modelName"));
        assertEquals("This Is It", StringUtils.camelNameToFriendlyName("thisIsIt"));
    }

    @Test
    public void friendlyModelNameFromUrlTest(){
        assertEquals(null, StringUtils.friendlyModelNameFromUrl(null));
        assertEquals("Test Name", StringUtils.friendlyModelNameFromUrl("test_name"));
        assertEquals("My Model Name", StringUtils.friendlyModelNameFromUrl("path/my_model_name-view.jsp"));
    }

    @Test
    public void camelNameToUrlNameTest(){
        assertEquals(null, StringUtils.camelNameToUrlName(null));
        assertEquals("camel_name", StringUtils.camelNameToUrlName("camelName"));
        assertEquals("my_super_model", StringUtils.camelNameToUrlName("mySuperModel"));
    }
}
