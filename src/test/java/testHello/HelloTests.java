/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  IFBA
 * PROJECT       :  libsensorj
 * FILENAME      :  HelloTests.java  
 * 
 * This file is part of the LibsensorJ project,
 * An extensible library for sensors / actuators using the Pi4J framework of the Raspberry Pi.
 * **********************************************************************
 * 
 * Created:      [yyyy/mm/dd creation date]
 * Last Changed: 24/11/2014 
 * 
 * @author: Júnior Mascarenhas       <A HREF="mailto:[juniorug@gmail.com]">[Júnior]</A>
 * @see [https://github.com/juniorug/libsensorj]
 * 
 * #L%
 */
package testHello;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * The Class HelloTests.
 */
public class HelloTests {

    /**
     * Test passes.
     */
    @Test
    public void testPasses() {
        String expected = "Hello, JUnit!";
        String hello = "Hello, JUnit!";
        assertEquals(hello, expected);
    }

    /*
     * @Test public void testFails() { // The worlds most obvious bug:
     * assertTrue(false); }
     */

    /**
     * Test array.
     */
    @Test
    public void testArray() {
        int[] array1 = new int[] { 1, 2, 3 };
        int[] array2 = new int[] { 1, 2, 3 };
        assertArrayEquals(array1, array2);
    }
}