/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  IFBA
 * PROJECT       :  libsensorj
 * FILENAME      :  UltrasonicHcsr04.java  
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
package com.libsensorj.concretesensor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.libsensorj.interfaces.ISensor;
import com.libsensorj.utils.LibPins;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

/**
 * The Class UltrasonicHcsr04.
 */
public class UltrasonicHcsr04 implements ISensor {

    /** The result. */
    private double result = 0;

    /** The trigger. */
    private GpioPinDigitalOutput trigger;

    /** The echo. */
    private GpioPinDigitalInput echo;

    /** The Constant TWENTY. */
    private static final int TWENTY = 20;

    /** The Constant FORTY. */
    private static final int FORTY = 40;

    /** The Constant THIRTY_EIGHT. */
    private static final int THIRTY_EIGHT = 38;

    /** The Constant DISTANCE_FACTOR. */
    private static final double DISTANCE_FACTOR = 165.7;

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LogManager
            .getLogger(UltrasonicHcsr04.class.getName());

    /**
     * Instantiates a new ultrasonic hcsr04.
     */
    public UltrasonicHcsr04() {

        this(RaspiPin.GPIO_01, RaspiPin.GPIO_02);
    }

    /**
     * Instantiates a new ultrasonic hcsr04.
     *
     * @param trigger
     *            the trigger pin
     * @param echo
     *            the echo pin
     */
    public UltrasonicHcsr04(int trigger, int echo) {

        this(LibPins.getPin(trigger), LibPins.getPin(echo));
    }

    /**
     * Instantiates a new ultrasonic hcsr04.
     *
     * @param _trigger
     *            the trigger pin
     * @param _echo
     *            the echo pin
     */
    public UltrasonicHcsr04(Pin trigger, Pin echo) {
        final GpioController gpio = GpioFactory.getInstance();
        this.trigger = gpio.provisionDigitalOutputPin(trigger, PinState.LOW);
        this.echo = gpio.provisionDigitalInputPin(echo,
                PinPullResistance.PULL_DOWN);

        // create and register gpio pin listener
        this.echo.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(
                    GpioPinDigitalStateChangeEvent event) {
                // display pin state on console
                System.out.println(" --> GPIO ECHO PIN STATE CHANGE: "
                        + event.getPin() + " = " + event.getState());
            }
        });

        this.trigger.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(
                    GpioPinDigitalStateChangeEvent event) {
                // display pin state on console
                System.out.println(" --> GPIO TRIGGER PIN STATE CHANGE: "
                        + event.getPin() + " = " + event.getState());
            }
        });

        // configure the pins shutdown behavior; these settings will be
        // automatically applied to the pin when the application is terminated
        this.echo.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
        this.trigger.setShutdownOptions(true, PinState.LOW,
                PinPullResistance.OFF);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                System.out.println("Oops!");
                gpio.shutdown();
                System.out.println("Exiting nicely.");
            }
        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.libsensorj.interfaces.ISensor#getInstance()
     */
    public void getInstance() {

    }

    /**
     * Gets the range.
     *
     * @return the range
     */
    public double getRange() {

        try {
            System.out.println("inside getRange, trigger is going high");
            // fire the trigger pulse
            trigger.high();

            Thread.sleep(TWENTY);
        } catch (InterruptedException e) {

            LOGGER.error(
                    "InterruptedException: Exception triggering range finder "
                            + e.getMessage(), e);
        }
        trigger.low();
        System.out.println("inside getRange. trigger is low now = "
                + trigger.isLow());
        // wait for the result

        double startTime = System.currentTimeMillis();
        double stopTime = 0;
        do {

            stopTime = System.currentTimeMillis();
            if ((System.currentTimeMillis() - startTime) >= FORTY) {
                System.out.println("timeout. will break");
                break;
            }
        } while (!echo.isHigh());

        // calculate the range. If the loop stopped after 38 ms set the result
        // to -1 to show it timed out.

        if ((stopTime - startTime) <= THIRTY_EIGHT) {
            System.out.println("stop - start < 38. result = "
                    + ((stopTime - startTime) * DISTANCE_FACTOR));
            result = (stopTime - startTime) * DISTANCE_FACTOR;
        } else {
            System.out.println("Timed out");
            result = -1;
        }

        return result;

    }
}
