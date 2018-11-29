package com.example.a80002584.userguide;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws ParseException {
        assertEquals(4, 2 + 2);
        //EEE MMM dd HH:mm:ss zzz yyyy
        String da = "Sat Oct 20 06:12:58 GMT+08:00 2018";
//          Sat Oct 20 06:12:58 GMT+08:00 2018
//        SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        SimpleDateFormat sdf = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));

        long time = sdf.parse(da).getTime();
        String format1 = sdf.format(new Date(time));
        System.out.println(format1);
        System.out.println(new Date(time));
    }
}
