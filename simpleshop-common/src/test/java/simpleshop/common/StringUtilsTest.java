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
//
//    public List<List<Integer>> permute3(int[] num){
//        List<List<Integer>> result = new ArrayList<>();
//        permute3(result, 0, num);
//        return result;
//    }
//
//    private void permute3(List<List<Integer>> result, int start, int[] num){
//
//        if(start == num.length){
//            ArrayList<Integer> item = new ArrayList<>();
//            for(int i=0; i< num.length; i++)
//                item.add(num[i]);
//            result.add(item);
//            return;
//        }
//
//        for(int i=start; i<num.length; i++){
//            int temp = num[i];
//            num[i] = num[start];
//            num[start] = temp;
//            permute3(result, start + 1, num);
//            temp = num[i];//swap back
//            num[i] = num[start];
//            num[start] = temp;
//        }
//    }
//
//
//    public List<List<Integer>> permute2(int[] num) {
//        ArrayList<List<Integer>> result = new ArrayList<>();
//        if(num == null)
//            return result;
//
//        ArrayList<Integer> solution = new ArrayList<>();
//        ArrayList<Integer> remaining = new ArrayList<>();
//        for(int i=0; i<num.length; i++){
//            remaining.add(num[i]);
//        }
//        permute2(result, solution, remaining);
//
//        return result;
//    }
//
//    private void permute2(ArrayList<List<Integer>> result, ArrayList<Integer> solution, ArrayList<Integer> remaining) {
//        if(remaining.size() == 1){
//            solution.add(remaining.get(0));
//            result.add(solution);
//            return;
//        }
//
//        for(int i=0; i<remaining.size();i++){
//            ArrayList<Integer> newRemaining = new ArrayList<>();
//            for(int j=0; j<remaining.size(); j++)
//                if(j != i)
//                    newRemaining.add(remaining.get(j));
//
//            ArrayList<Integer> newSolution = new ArrayList<>(solution);
//            newSolution.add(remaining.get(i));
//            permute2(result, newSolution, newRemaining);
//        }
//    }


}
