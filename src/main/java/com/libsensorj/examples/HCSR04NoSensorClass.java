/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  IFBA
 * PROJECT       :  libsensorj
 * FILENAME      :  HCSR04NoSensorClass.java
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

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

import java.text.DecimalFormat;
import java.text.Format;

/**
 * The Class HCSR04NoSensorClass.
 *
 * @see https 
 *      ://www.modmypi.com/blog/hc-sr04-ultrasonic-range-sensor-on-the-raspberry
 *      -pi
 */
public class HCSR04NoSensorClass {

    /** The Constant DF22. */
    private final static Format DF22 = new DecimalFormat("#0.00");

    /** The Constant SOUND_SPEED. */
    private final static double SOUND_SPEED = 34300; // in cm, 343 m/s

    /** The Constant DIST_FACT. */
    private final static double DIST_FACT = SOUND_SPEED / 2; // round trip

    /** The Constant MIN_DIST. */
    private final static int MIN_DIST = 5;

    /**
     * The main method.
     *
     * @param args
     *            the arguments
     * @throws InterruptedException
     *             the interrupted exception
     */
    public static void main(String[] args) throws InterruptedException {
        System.out.println("GPIO Control - Range Sensor HC-SR04.");
        System.out.println("Will stop if distance is smaller than " + MIN_DIST
                + " cm");

        // create gpio controller
        final GpioController gpio = GpioFactory.getInstance();

        /*
         * final GpioPinDigitalOutput trigPin = gpio.provisionDigitalOutputPin(
         * RaspiPin.GPIO_04, "Trig", PinState.LOW); final GpioPinDigitalInput
         * echoPin = gpio.provisionDigitalInputPin( RaspiPin.GPIO_05, "Echo");
         */

        final GpioPinDigitalOutput trigPin = gpio.provisionDigitalOutputPin(
                RaspiPin.GPIO_01, "Trig", PinState.LOW);
        final GpioPinDigitalInput echoPin = gpio.provisionDigitalInputPin(
                RaspiPin.GPIO_02, "Echo");

        // configure the pins shutdown behavior; these settings will be
        // automatically applied to the pin when the application is terminated
        trigPin.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                System.out.println("Oops!");
                gpio.shutdown();
                System.out.println("Exiting nicely.");
            }
        });

        System.out.println("Waiting for the sensor to be ready (2s)...");
        Thread.sleep(2000);

        boolean go = true;
        System.out.println("Looping until the distance is less than "
                + MIN_DIST + " cm");
        while (go) {
            double start = 0d, end = 0d;
            System.out.println("trigger is going high");
            trigPin.high();
            // 10 microsec to trigger the module (8 ultrasound bursts at 40 kHz)
            // https://www.dropbox.com/s/615w1321sg9epjj/hc-sr04-ultrasound-timing-diagram.png
            try {
                Thread.sleep(0, 10000);
            } catch (Exception ex) {
                System.out.println(ex.getMessage() + ex);
            }
            trigPin.low();
            System.out
                    .println("trigger is low now. will receive data. echopin.islow = "
                            + echoPin.isLow());
            // Wait for the signal to return
            while (echoPin.isLow()) {
                start = System.nanoTime();
                // There it is
            }
            System.out.println("fora do primeiro while. echopin.ishigh = "
                    + echoPin.isHigh());
            while (echoPin.isHigh()) {
                end = System.nanoTime();
            }
            System.out.println("fora do segundo while. echopin.islow = "
                    + echoPin.isLow());
            if (end > 0 && start > 0) {
                double pulseDuration = (end - start) / 1000000000d; // in
                                                                    // seconds
                System.out.println("[start: " + start + "] [end: " + end
                        + "] [ pulseDuration: " + pulseDuration + "]");
                double distance = pulseDuration * DIST_FACT;
                System.out.println("distance: " + distance);
                if (distance < 1000) { // Less than 10 meters
                    System.out.println("Distance: " + DF22.format(distance)
                            + " cm."); // + " (" + pulseDuration + " = " + end +
                                       // " - " + start + ")");
                }
                if (distance > 0 && distance < MIN_DIST) {
                    go = false;
                } else {
                    if (distance < 0) {
                        System.out.println("Dist:" + distance + ", start:"
                                + start + ", end:" + end);
                    }
                    try {
                        Thread.sleep(1000L);
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage() + ex);
                    }
                }
            } else {
                System.out.println("Hiccup!");
                try {
                    Thread.sleep(2000L);
                } catch (Exception ex) {
                    System.out.println(ex.getMessage() + ex);
                }
            }
        }
        System.out.println("Done.");
        trigPin.low(); // Off

        gpio.shutdown();
    }
}