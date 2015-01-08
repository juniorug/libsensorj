/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  IFBA
 * PROJECT       :  libsensorj
 * FILENAME      :  LibPins.java  
 * 
 * This file is part of the LibsensorJ project,
 * An extensible library for sensors / actuators using the Pi4J framework of the Raspberry Pi.
 * **********************************************************************
 * 
 * Created:      [yyyy/mm/dd creation date]
 * Last Changed: 20/11/2014 
 * 
 * @author: Júnior Mascarenhas       <A HREF="mailto:[juniorug@gmail.com]">[Júnior]</A>
 * @see [https://github.com/juniorug/libsensorj]
 * 
 * #L%
 */
package com.libsensorj.utils;

import java.util.HashMap;
import java.util.Map;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;

/**
 * The Class LibPins.
 */
public class LibPins {

    /** The gpio pins. */
    private static Map<Integer, Pin> gpioPins;

    static {
        gpioPins = new HashMap<Integer, Pin>();

        gpioPins.put(0, RaspiPin.GPIO_00);
        gpioPins.put(1, RaspiPin.GPIO_01);
        gpioPins.put(2, RaspiPin.GPIO_02);
        gpioPins.put(3, RaspiPin.GPIO_03);
        gpioPins.put(4, RaspiPin.GPIO_04);
        gpioPins.put(5, RaspiPin.GPIO_05);
        gpioPins.put(6, RaspiPin.GPIO_06);
        gpioPins.put(7, RaspiPin.GPIO_07);
        gpioPins.put(8, RaspiPin.GPIO_08);
        gpioPins.put(9, RaspiPin.GPIO_09);
        gpioPins.put(10, RaspiPin.GPIO_10);
        gpioPins.put(11, RaspiPin.GPIO_11);
        gpioPins.put(12, RaspiPin.GPIO_12);
        gpioPins.put(13, RaspiPin.GPIO_13);
        gpioPins.put(14, RaspiPin.GPIO_14);
        gpioPins.put(15, RaspiPin.GPIO_15);
        gpioPins.put(16, RaspiPin.GPIO_16);
        gpioPins.put(17, RaspiPin.GPIO_17);
        gpioPins.put(18, RaspiPin.GPIO_18);
        gpioPins.put(19, RaspiPin.GPIO_19);
        gpioPins.put(20, RaspiPin.GPIO_20);
        gpioPins.put(21, RaspiPin.GPIO_21);
        gpioPins.put(22, RaspiPin.GPIO_22);
        gpioPins.put(23, RaspiPin.GPIO_23);
        gpioPins.put(24, RaspiPin.GPIO_24);
        gpioPins.put(25, RaspiPin.GPIO_25);
        gpioPins.put(26, RaspiPin.GPIO_26);
        gpioPins.put(27, RaspiPin.GPIO_27);
        gpioPins.put(28, RaspiPin.GPIO_28);
        gpioPins.put(29, RaspiPin.GPIO_29);

    }

    /**
     * Gets the pin.
     *
     * @param pinNumber
     *            the pin number
     * @return the pin
     */
    public static Pin getPin(int pinNumber) {
        return (Pin) gpioPins.get(pinNumber);
    }

}
