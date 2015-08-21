package simpleshop.common;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 * Unit tests for StringUtilsTest.
 */
public class StringUtilsTest {

    @Test
    public void subStrB4Test() {
        assertEquals("test", StringUtils.subStrB4("test", "."));
        assertEquals("test", StringUtils.subStrB4("test.exe", "."));
        assertEquals("test", StringUtils.subStrB4("test.exe.bbb", "."));
        assertEquals("", StringUtils.subStrB4("", "sep"));
    }

    @Test
    public void subStrBetweenLastAndFirstTest() {
        assertEquals("super test", StringUtils.subStrBetweenLastAndFirst("This is my super test.", "my ", "."));
        assertEquals("super test", StringUtils.subStrBetweenLastAndFirst("my This is my super test.", "my ", "."));
        assertEquals("super test", StringUtils.subStrBetweenLastAndFirst("my This is my super test.hbm.xml", "my ", "."));
    }

    @Test
    public void subStrAfterLastTest(){
         assertThat(StringUtils.subStrAfterLast(null, ""), nullValue());
         assertThat(StringUtils.subStrAfterLast("abc", ""), equalTo("abc"));
         assertThat(StringUtils.subStrAfterLast("test3.test2.test1", "."), equalTo("test1"));
         assertThat(StringUtils.subStrAfterLast("test3.test2.test1.", "."), equalTo(""));
    }

    @Test
    public void getPropertyNameTest() {
        assertEquals("name", StringUtils.getPropertyName("getName"));
        assertEquals("birthDate", StringUtils.getPropertyName("setBirthDate"));
        assertEquals("valid", StringUtils.getPropertyName("isValid"));
    }

    @Test
    public void friendlyNameToCamelNameTest() {

        assertEquals(null, StringUtils.friendlyNameToCamelName(null));
        assertEquals("", StringUtils.friendlyNameToCamelName(""));
        assertEquals("entity", StringUtils.friendlyNameToCamelName("Entity"));
        assertEquals("entityId", StringUtils.friendlyNameToCamelName("Entity Id"));
        assertEquals("memberAccountInvestment", StringUtils.friendlyNameToCamelName("Member Account Investment"));
    }

    @Test
    public void camelNameToFriendlyNameTest() {
        assertEquals(null, StringUtils.camelNameToFriendlyName(null));
        assertEquals("Model Name", StringUtils.camelNameToFriendlyName("modelName"));
        assertEquals("This Is It", StringUtils.camelNameToFriendlyName("thisIsIt"));
    }

    @Test
    public void friendlyModelNameFromUrlTest() {
        assertEquals(null, StringUtils.friendlyModelNameFromUrl(null));
        assertEquals("Test Name", StringUtils.friendlyModelNameFromUrl("test_name"));
        assertEquals("My Model Name", StringUtils.friendlyModelNameFromUrl("path/my_model_name-view.jsp"));
    }

    @Test
    public void camelNameToUrlNameTest() {
        assertEquals(null, StringUtils.camelNameToUrlName(null));
        assertEquals("camel_name", StringUtils.camelNameToUrlName("camelName"));
        assertEquals("my_super_model", StringUtils.camelNameToUrlName("mySuperModel"));
    }

    @Test
    public void subStrAfterFirstTest() {
        assertThat(StringUtils.subStrAfterFirst(null, null), equalTo(null));
        assertThat(StringUtils.subStrAfterFirst("", "."), equalTo(""));
        assertThat(StringUtils.subStrAfterFirst("aa.bb.cc", "."), equalTo("bb.cc"));
        assertThat(StringUtils.subStrAfterFirst(".ba", "."), equalTo("ba"));
    }

    @Test
    public void subStrB4LastTest() {

        assertThat(StringUtils.subStrB4Last(null, null), equalTo(null));
        assertThat(StringUtils.subStrB4Last("bab", null), equalTo("bab"));
        assertThat(StringUtils.subStrB4Last("bab.tt", "."), equalTo("bab"));
        assertThat(StringUtils.subStrB4Last("bab.tt.ss", "."), equalTo("bab.tt"));
    }

    @Test
    public void htmlEncodeSingleQuoteTest() {
        assertThat(StringUtils.htmlEncodeSingleQuote(null), equalTo(null));
        assertThat(StringUtils.htmlEncodeSingleQuote("This is a test."), equalTo("This is a test."));
        assertThat(StringUtils.htmlEncodeSingleQuote("This is a' test."), equalTo("This is a&#39; test."));
        assertThat(StringUtils.htmlEncodeSingleQuote("\"This is a' test.\""), equalTo("\\&#34;This is a&#39; test.\\&#34;"));

    }

    @Test
    public void firstCharLowerTest(){
        assertThat(StringUtils.firstCharLower(null), equalTo(null));
        assertThat(StringUtils.firstCharLower(""), equalTo(""));
        assertThat(StringUtils.firstCharLower("A"), equalTo("a"));
        assertThat(StringUtils.firstCharLower("About"), equalTo("about"));
    }

    @Test
    public void firstCharUpperTest(){
        assertThat(StringUtils.firstCharUpper(null), equalTo(null));
        assertThat(StringUtils.firstCharUpper(""), equalTo(""));
        assertThat(StringUtils.firstCharUpper("a"), equalTo("A"));
        assertThat(StringUtils.firstCharUpper("about"), equalTo("About"));
    }

    @Test
    public void ngEscapeTest(){
        assertThat(StringUtils.ngEscape(null), equalTo(null));
        assertThat(StringUtils.ngEscape(""), equalTo(""));
        assertThat(StringUtils.ngEscape("{test}"), equalTo("\\{test\\}"));
    }

    @Test
    public void concatTest(){
        assertThat(StringUtils.concat("Robo", "Cop"), equalTo("RoboCop"));
        assertThat(StringUtils.concat("Actual", "Test", "Bad"), equalTo("ActualTestBad"));
    }

    @Test
    public void toEmptyStringTest(){
        assertThat(StringUtils.toEmptyString(null), equalTo(null));
        assertThat(StringUtils.toEmptyString(new BigDecimal("321")), equalTo(""));
    }

    @Test
    public void wrapLikeKeywords(){

        assertThat(StringUtils.wrapLikeKeywords(null), equalTo("%"));
        assertThat(StringUtils.wrapLikeKeywords("%"), equalTo("%"));
        assertThat(StringUtils.wrapLikeKeywords("abc def"), equalTo("%abc def%"));
    }

    @Test
    public void friendlyNameToPascalNameTest(){

        assertThat(StringUtils.friendlyNameToPascalName(null), equalTo(null));
        assertThat(StringUtils.friendlyNameToPascalName(""), equalTo(""));
        assertThat(StringUtils.friendlyNameToPascalName("Computer"), equalTo("Computer"));
        assertThat(StringUtils.friendlyNameToPascalName("Computer Game "), equalTo("ComputerGame"));
    }

    @Test
    public void pascalNameToUrlNameTest(){
        assertThat(StringUtils.pascalNameToUrlName(null), equalTo(null));
        assertThat(StringUtils.pascalNameToUrlName(""), equalTo(""));
        assertThat(StringUtils.pascalNameToUrlName("TestWebsite"), equalTo("test_website"));
        assertThat(StringUtils.pascalNameToUrlName("TheCompanyName"), equalTo("the_company_name"));
    }

    @Test(expected = NumberFormatException.class)
    public void parseIdTest(){
        StringUtils.parseId("pp");
    }

    @Test
    public void viewNameFromUrlTest(){
       assertThat(StringUtils.viewNameFromUrl("test/category/product-special_view.php"), equalTo("product-special_view"));
    }

    @Test
    public void viewTypeFromUrlTest(){
        assertThat(StringUtils.viewTypeFromUrl("test/category/product-special_view.php"), equalTo("special_view"));
    }

    @Test
    public void getFieldId(){
        assertThat(StringUtils.getFieldId("myDiv", "contact.name"), equalTo("myDiv-contact_name"));
    }

    @Test
    public void camelNameToPascalNameTest(){
        assertThat(StringUtils.camelNameToPascalName(""), equalTo(""));
        assertThat(StringUtils.camelNameToPascalName("testMainPage"), equalTo("TestMainPage"));
    }


}
