/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  IFBA
 * PROJECT       :  libsensorj
 * FILENAME      :  HCSR04V2Example.java  
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
package com.libsensorj.examples;

import java.util.concurrent.TimeoutException;

import com.libsensorj.concretesensor.HCSR04V2;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;

/**
 * The Class HCSR04V2Example.
 */
public class HCSR04V2Example {

    /** The Constant WAIT_DURATION_IN_MILLIS. */
    private final static int WAIT_DURATION_IN_MILLIS = 60; // wait 60 milli s
    
    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {
        /*Pin echoPin = RaspiPin.GPIO_00; // PI4J custom numbering (pin 11)
        Pin trigPin = RaspiPin.GPIO_07; // PI4J custom numbering (pin 7) */
        Pin echoPin = RaspiPin.GPIO_02; // PI4J custom numbering (pin 11)
        Pin trigPin = RaspiPin.GPIO_01; // PI4J custom numbering (pin 7)
        HCSR04V2 monitor = new HCSR04V2(echoPin, trigPin);

        while (true) {
            try {
                System.out.printf("%1$d,%2$.3f%n", System.currentTimeMillis(),
                        monitor.measureDistance());
            } catch (TimeoutException e) {
                System.err.println(e);
            }

            try {
                Thread.sleep(WAIT_DURATION_IN_MILLIS);
            } catch (InterruptedException ex) {
                System.err.println("Interrupt during trigger");
            }
        }
    }

}
