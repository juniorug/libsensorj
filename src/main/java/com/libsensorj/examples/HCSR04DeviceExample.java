package com.libsensorj.examples;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.libsensorj.concretefactory.HCSR04DeviceFactory;
import com.libsensorj.concretesensor.HCSR04Device;
import com.libsensorj.interfaces.ISensor;
import com.libsensorj.interfaces.ISensorFactory;

public class HCSR04DeviceExample {

    private static ISensor hcsr;
    private static final Logger LOGGER = LogManager
            .getLogger(HCSR04DeviceExample.class.getName());

    public static void main(String[] args) {

        ISensorFactory isf = new HCSR04DeviceFactory();
        hcsr = isf.createSensor();
        hcsr.getInstance();

        do {
            // Get the range
            double distance = ((HCSR04Device) hcsr).getDistance();

            LOGGER.info("RangeFinder result = " + distance + "mm");
        } while (true);
        // while (false != true)

    }

}
