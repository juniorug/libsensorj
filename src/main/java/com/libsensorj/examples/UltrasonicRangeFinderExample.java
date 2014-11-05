package com.libsensorj.examples;

import com.libsensorj.concretefactory.UltrasonicRangeFinderFactory;
import com.libsensorj.concretesensor.UltrasonicHcsr04;
import com.libsensorj.interfaces.ISensor;
import com.libsensorj.interfaces.ISensorFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UltrasonicRangeFinderExample {
    private static ISensor hcsr;
    private static final Logger LOGGER = LogManager
            .getLogger(UltrasonicRangeFinderExample.class.getName());

    public static void main(String[] args) {

        ISensorFactory isf = new UltrasonicRangeFinderFactory();
        hcsr = isf.createSensor();
        hcsr.getInstance();

        do {
            // Get the range
            double distance = ((UltrasonicHcsr04) hcsr).getRange();

            LOGGER.info("RangeFinder result = " + distance + "mm");
        } while (true); //false != true

    }

}
