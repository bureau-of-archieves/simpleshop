package simpleshop.common;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created with IntelliJ IDEA.
 * User: JOHNZ
 * Date: 23/09/14
 * Time: 5:14 PM
 */
public class DateUtilsTest {

    @Test
    public void createDate_DateOrderTest(){

        Date date1 = DateUtils.createDate(2011, 2, 21);
        Date date2 = DateUtils.createDate(2012, 9, 13);
        Date date3 = DateUtils.createDate(2011, 2, 21);
        assertTrue(date1.before(date2));
        assertFalse(date1.before(date3));
    }

    @Test(expected = IllegalArgumentException.class)
    public void createDate_ExceptionTest(){
       DateUtils.createDate(2011,14,3);
    }

    @Test
    public void calcAgeTest() {

        Date dob = DateUtils.createDate(1984, 5, 1);
        Date now = DateUtils.createDate(1984, 5, 1);
        Integer age  = DateUtils.calcAge(dob, now);
        assertEquals(Integer.valueOf(0), age);

        now = DateUtils.createDate(1984, 4, 30);
        age  = DateUtils.calcAge(dob, now);
        assertEquals(Integer.valueOf(-1), age);

        now = DateUtils.createDate(1985, 5, 1);
        age  = DateUtils.calcAge(dob, now);
        assertEquals(Integer.valueOf(1), age);

        now = DateUtils.createDate(1985, 5, 2);
        age  = DateUtils.calcAge(dob, now);
        assertEquals(Integer.valueOf(1), age);

        now = DateUtils.createDate(1986, 3, 2);
        age  = DateUtils.calcAge(dob, now);
        assertEquals(Integer.valueOf(1), age);

        now = DateUtils.createDate(1986, 12, 31);
        age  = DateUtils.calcAge(dob, now);
        assertEquals(Integer.valueOf(2), age);

        now = DateUtils.createDate(2009, 7, 13);
        age  = DateUtils.calcAge(dob, now);
        assertEquals(Integer.valueOf(25), age);

        dob = DateUtils.createDate(2008, 2, 29);
        now = DateUtils.createDate(2008, 2, 29);
        age  = DateUtils.calcAge(dob, now);
        assertEquals(Integer.valueOf(0), age);

        now = DateUtils.createDate(2008, 3, 1);
        age  = DateUtils.calcAge(dob, now);
        assertEquals(Integer.valueOf(0), age);

        now = DateUtils.createDate(2009, 2, 28);
        age  = DateUtils.calcAge(dob, now);
        assertEquals(Integer.valueOf(0), age);

        now = DateUtils.createDate(2009, 3, 1);
        age  = DateUtils.calcAge(dob, now);
        assertEquals(Integer.valueOf(1), age);

        now = DateUtils.createDate(2012, 2, 28);
        age  = DateUtils.calcAge(dob, now);
        assertEquals(Integer.valueOf(3), age);

        now = DateUtils.createDate(2012, 2, 29);
        age  = DateUtils.calcAge(dob, now);
        assertEquals(Integer.valueOf(4), age);
    }

}
