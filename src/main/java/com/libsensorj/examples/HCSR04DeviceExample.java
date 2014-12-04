/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  IFBA
 * PROJECT       :  libsensorj
 * FILENAME      :  HCSR04DeviceExample.java  
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

import com.libsensorj.concretefactory.HCSR04DeviceFactory;
import com.libsensorj.concretesensor.HCSR04Device;
import com.libsensorj.interfaces.ISensor;
import com.libsensorj.interfaces.ISensorFactory;

/**
 * The Class HCSR04DeviceExample.
 */
public class HCSR04DeviceExample {

    /** The ISensor hcsr. */
    private static ISensor hcsr;
    
    /** The Constant LOGGER. */
    private static final Logger LOGGER = LogManager
            .getLogger(HCSR04DeviceExample.class.getName());

    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {

        ISensorFactory isf = new HCSR04DeviceFactory();
        hcsr = isf.createSensor();
        hcsr.getInstance();

        do {
            // Get the range
            double distance = ((HCSR04Device) hcsr).getDistance();

            System.out.println("RangeFinder result = " + distance + "mm");
        } while (true);
        // while (false != true)

    }

}
