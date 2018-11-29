package com.example.mylibrary;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);

        Float f1 = new Float(-1.0 / 0.0);
        Float f2 = new Float(0.0 / 0.0);
        Double f3 = Math.sqrt(-1);

        // returns true if this Float value is a Not-a-Number(NaN), else false
        System.out.println(f1 + " = " + f1.isNaN());
        System.out.println(f2 + " = " + f2.isNaN());
        System.out.println(f3 + " = " + f3.isNaN());
        System.out.println(Double.isNaN(Double.longBitsToDouble(0x7ff0000000000011L)));
        System.out.println(Double.NaN==2.0);
    }
}