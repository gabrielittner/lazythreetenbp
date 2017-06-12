package com.gabrielittner.threetenbp;

import android.os.Build;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDate;
import org.threeten.bp.Month;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.temporal.TemporalAdjusters;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assume.assumeTrue;

@RunWith(AndroidJUnit4.class)
public class DayOfWeekFormattingTest {

    private static Locale ruLocale;
    private static Object[][] data;

    @BeforeClass
    public static void init() throws Exception {
        LazyThreeTen.init(InstrumentationRegistry.getTargetContext());
        ruLocale = new Locale("ru");

        //Not using Parameterized tests because they are slower,
        //and if for one month test fails then usually others fail as well.
        data = new Object[][]{
                {Calendar.SUNDAY, DayOfWeek.SUNDAY},
                {Calendar.MONDAY, DayOfWeek.MONDAY},
                {Calendar.TUESDAY, DayOfWeek.TUESDAY},
                {Calendar.WEDNESDAY, DayOfWeek.WEDNESDAY},
                {Calendar.THURSDAY, DayOfWeek.THURSDAY},
                {Calendar.FRIDAY, DayOfWeek.FRIDAY},
                {Calendar.SATURDAY, DayOfWeek.SATURDAY},
        };
    }

    private GregorianCalendar getCalendar(int calendarDay) {
        GregorianCalendar calendar = new GregorianCalendar(1970, 0, 1);
        //Force recalculation.
        calendar.getTime();
        calendar.set(Calendar.DAY_OF_WEEK, calendarDay);
        return calendar;
    }

    private LocalDate getLocalDate(DayOfWeek dayOfWeek) {
        return LocalDate.of(1970, 1, 1).with(TemporalAdjusters.nextOrSame(dayOfWeek));
    }

    private void testPattern(String pattern) throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, ruLocale);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern, ruLocale);

        for (Object[] entry : data) {
            int calendarDay = (Integer) entry[0];
            DayOfWeek dayOfWeek = (DayOfWeek) entry[1];

            String javaText = simpleDateFormat.format(getCalendar(calendarDay).getTime());
            String threeTenText = getLocalDate(dayOfWeek).format(formatter);

            assertEquals(javaText, threeTenText);
        }
    }

    private void assumeNarrowStyleSupported() {
        assumeTrue(Build.VERSION.SDK_INT >= 18);
    }

    @Test
    public void testFull() throws Exception {
        testPattern("EEEE");
    }

    @Test
    public void testShort() throws Exception {
        testPattern("EEE");
    }

    @Test
    public void testNarrow() throws Exception {
        assumeNarrowStyleSupported();
        testPattern("EEEEE");
    }

    @Test
    public void testFullStandalone() throws Exception {
        testPattern("cccc");
    }

    @Test
    public void testShortStandalone() throws Exception {
        testPattern("ccc");
    }

    @Test
    public void testNarrowStandalone() throws Exception {
        assumeNarrowStyleSupported();
        testPattern("ccccc");
    }
}