package simpleshop.common;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Util methods for date and time data types.
 */
public final class DateUtils {

    private DateUtils(){}

    /**
     * Create a date with all 0s in the time components.
     * @param year year.
     * @param month month.
     * @param day day.
     * @return the constructed date.
     */
    public static Date createDate(int year, int month, int day) {

        month--;
        if(month < 0 || month > 11)
            throw new IllegalArgumentException("month");

        GregorianCalendar calendar = new GregorianCalendar();
        calendar.set(year, month, day, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * Calculate the age from 2 dates.
     * @param birthDate birth date.
     * @param nowDate now.
     * @return age.
     */
    public static int calcAge(Date birthDate, Date nowDate){

        int age = 0;
        if (birthDate != null) {
            GregorianCalendar now = new GregorianCalendar();
            now.setTime(nowDate);
            GregorianCalendar dob = new GregorianCalendar();
            dob.setTime(birthDate);

            age = now.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
            now.add(Calendar.YEAR, -age);
            Date then = now.getTime();

            if (then.before(birthDate))
                age--;
        }
        return age;
    }

}