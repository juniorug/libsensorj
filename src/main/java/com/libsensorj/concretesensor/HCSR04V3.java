/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  IFBA
 * PROJECT       :  libsensorj
 * FILENAME      :  HCSR04V3.java  
 * 
 * This file is part of the LibsensorJ project,
 * An extensible library for sensors / actuators using the Pi4J framework of the Raspberry Pi.
 * **********************************************************************
 * 
 * Created:      [yyyy/mm/dd creation date]
 * Last Changed: 07/01/2015 
 * 
 * @author: Júnior Mascarenhas       <A HREF="mailto:[juniorug@gmail.com]">[Júnior]</A>
 * @see [https://github.com/juniorug/libsensorj]
 * 
 * #L%
 */
package com.libsensorj.concretesensor;

import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;

/**
 * The Class HCSR04V3.
 */
public class HCSR04V3 {

    /** The trigger. */
    GpioPinDigitalOutput trigger;

    /** The echo. */
    GpioPinDigitalInput echo;

    /**
     * Instantiates a new HCS r04 v3.
     *
     * @param trigger
     *            the trigger
     * @param echo
     *            the echo
     */
    public HCSR04V3(GpioPinDigitalOutput trigger, GpioPinDigitalInput echo) {
        this.trigger = trigger;
        this.echo = echo;
    }

    /**
     * Gets the range.
     *
     * @return the range
     */
    public double getRange() {
        System.out.println("inside getrange. Range Sensor Triggered");

        long start = 0;
        long diff = 0;

        try {
            trigger.high();
            System.out.println("trigger is high = " + trigger.isHigh());
            Thread.sleep(10);
            trigger.low();
            System.out.println("trigger is low = " + trigger.isLow());

            while (echo.isLow()) {
                start = System.nanoTime();
            }

            System.out.println("fora do primeiro while");
            while (echo.isHigh()) {
            }

            diff = (System.nanoTime() - start) / 58000;
            System.out.println("diff calculado: " + diff);

            return diff;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
