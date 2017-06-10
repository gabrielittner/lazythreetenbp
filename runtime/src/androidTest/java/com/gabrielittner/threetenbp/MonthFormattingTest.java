package com.gabrielittner.threetenbp;

import android.os.Build;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.threeten.bp.LocalDate;
import org.threeten.bp.Month;
import org.threeten.bp.format.DateTimeFormatter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import static org.junit.Assert.*;
import static org.junit.Assume.*;

@RunWith(AndroidJUnit4.class)
public class MonthFormattingTest {

    private static Locale ruLocale;
    private static Object[][] data;

    @BeforeClass
    public static void init() throws Exception {
        LazyThreeTen.init(InstrumentationRegistry.getTargetContext());
        ruLocale = new Locale("ru");

        //Not using Parameterized tests because they are slower,
        //and if for one month test fails then usually others fail as well.
        data = new Object[][]{
                {Calendar.JANUARY, Month.JANUARY},
                {Calendar.FEBRUARY, Month.FEBRUARY},
                {Calendar.MARCH, Month.MARCH},
                {Calendar.APRIL, Month.APRIL},
                {Calendar.MAY, Month.MAY},
                {Calendar.JUNE, Month.JUNE},
                {Calendar.JULY, Month.JULY},
                {Calendar.AUGUST, Month.AUGUST},
                {Calendar.SEPTEMBER, Month.SEPTEMBER},
                {Calendar.OCTOBER, Month.OCTOBER},
                {Calendar.NOVEMBER, Month.NOVEMBER},
                {Calendar.DECEMBER, Month.DECEMBER}
        };
    }

    private GregorianCalendar getCalendar(int calendarMonth) {
        GregorianCalendar calendar = new GregorianCalendar(1970, 0, 1);
        calendar.set(Calendar.MONTH, calendarMonth);
        return calendar;
    }

    private LocalDate getLocalDate(Month month) {
        return LocalDate.of(1970, month, 1);
    }

    private void testPattern(String pattern) throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, ruLocale);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern, ruLocale);


        for (Object[] entry : data) {
            int calendarMonth = (Integer) entry[0];
            Month month = (Month) entry[1];

            String javaText = simpleDateFormat.format(getCalendar(calendarMonth).getTime());
            String threeTenText = getLocalDate(month).format(formatter);

            assertEquals(javaText, threeTenText);
        }
    }

    private void assumeNarrowStyleSupported() {
        assumeTrue(Build.VERSION.SDK_INT >= 18);
    }

    @Test
    public void testFull() throws Exception {
        testPattern("MMMM");
    }

    @Test
    public void testShort() throws Exception {
        testPattern("MMM");
    }

    @Test
    public void testNarrow() throws Exception {
        assumeNarrowStyleSupported();
        testPattern("MMMMM");
    }

    @Test
    public void testFullStandalone() throws Exception {
        testPattern("LLLL");
    }

    @Test
    public void testShortStandalone() throws Exception {
        testPattern("LLL");
    }

    @Test
    public void testNarrowStandalone() throws Exception {
        assumeNarrowStyleSupported();
        testPattern("LLLLL");
    }
}