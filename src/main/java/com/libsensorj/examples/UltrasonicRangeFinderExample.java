package com.libsensorj.examples;

import com.libsensorj.concreteFactory.UltrasonicRangeFinderFactory;
import com.libsensorj.concreteSensor.UltrasonicHcsr04;
import com.libsensorj.interfaces.ISensor;
import com.libsensorj.interfaces.ISensorFactory;

public class UltrasonicRangeFinderExample {
    private static ISensor hcsr;

    public static void main(String[] args) {

        ISensorFactory isf = new UltrasonicRangeFinderFactory();
        hcsr = isf.createSensor();
        hcsr.getInstance();

        do {
            // Get the range
            double distance = ((UltrasonicHcsr04) hcsr).getRange();

            System.out.println("RangeFinder result = " + distance + "mm");
        } while (false != true);

    }

}
