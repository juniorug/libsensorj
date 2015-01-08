/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  IFBA
 * PROJECT       :  libsensorj
 * FILENAME      :  UltrasonicRangeFinderExample.java  
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
package com.libsensorj.examples;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.libsensorj.concretefactory.UltrasonicRangeFinderFactory;
import com.libsensorj.concretesensor.UltrasonicHcsr04;
import com.libsensorj.interfaces.ISensor;
import com.libsensorj.interfaces.ISensorFactory;

/**
 * The Class UltrasonicRangeFinderExample.
 */
public class UltrasonicRangeFinderExample {

    /** The ISensor hcsr. */
    private static ISensor hcsr;

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LogManager
            .getLogger(UltrasonicRangeFinderExample.class.getName());

    /**
     * The main method.
     *
     * @param args
     *            the arguments
     */
    public static void main(String[] args) {

        ISensorFactory isf = new UltrasonicRangeFinderFactory();
        hcsr = isf.createSensor();
        hcsr.getInstance();

        do {
            // Get the range
            double distance = ((UltrasonicHcsr04) hcsr).getRange();

            LOGGER.info("RangeFinder result = " + distance + "mm");
        } while (true);

    }

}
