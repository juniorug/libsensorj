/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  IFBA
 * PROJECT       :  libsensorj
 * FILENAME      :  HCSR04Device.java  
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
 * The Class HCSR04Device.
 */
public class HCSR04Device implements ISensor {

    // Speed of sound = 34029 cm/s
    /** The Constant SPEEDOFSOUND. */
    private static final int SPEEDOFSOUND = 34029;
    
    /** The Constant TWO. */
    private static final int TWO = 2;
    // #10 µs pulse = 1ms
    /** The Constant PULSE_TIME. */
    private static final long PULSE_TIME = 1;
    
    /** The Constant DELAY. */
    private static final long DELAY = 1000000000L;

    /** The trigger. */
    private GpioPinDigitalOutput trigger = null;
    
    /** The echo. */
    private GpioPinDigitalInput echo = null;

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LogManager
            .getLogger(HCSR04Device.class.getName());

    /**
     * Instantiates a new HCS r04 device.
     */
    public HCSR04Device() {

        this(RaspiPin.GPIO_01, RaspiPin.GPIO_02);
    }

    /**
     * Instantiates a new HCS r04 device.
     *
     * @param _trigger the trigger
     * @param _echo the echo
     */
    public HCSR04Device(int _trigger, int _echo) {

        this(LibPins.getPin(_trigger), LibPins.getPin(_echo));
    }

    /**
     * Instantiates a new HCS r04 device.
     *
     * @param _trigger the _trigger
     * @param _echo the _echo
     */
    public HCSR04Device(Pin _trigger, Pin _echo) {
        final GpioController gpio = GpioFactory.getInstance();
        trigger = gpio.provisionDigitalOutputPin(_trigger, PinState.LOW);
        echo = gpio
                .provisionDigitalInputPin(_echo, PinPullResistance.PULL_DOWN);

        // create and register gpio pin listener
        echo.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(
                    GpioPinDigitalStateChangeEvent event) {
                // display pin state on console
            	System.out.println(" --> GPIO ECHO PIN "+ echo.getPin().toString() + " STATE CHANGE: " + event.getPin()
                        + " = " + event.getState());
            }
        });
        
        trigger.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(
                    GpioPinDigitalStateChangeEvent event) {
                // display pin state on console
            	System.out.println(" --> GPIO TRIGGER PIN "+ trigger.getPin().toString() + " STATE CHANGE: " + event.getPin()
                        + " = " + event.getState());
            }
        });
        
        // configure the pins shutdown behavior; these settings will be 
        // automatically applied to the pin when the application is terminated 
        echo.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
        trigger.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                System.out.println("Oops!");
                gpio.shutdown();
                System.out.println("Exiting nicely.");
            }
        });
    }

    /* (non-Javadoc)
     * @see com.libsensorj.interfaces.ISensor#getInstance()
     */
    public void getInstance() {

    }

    /**
     * Gets the distance.
     *
     * @return the distance
     */
    public double getDistance() {
        long distance = 0;
        // Send a pulse trigger; must be 1 and 0 with a 10 µs wait
        trigger.pulse(PULSE_TIME);
        long starttime = System.nanoTime(); // ns
        //System.out.println("start time: " + starttime );
        long stop = starttime;
        long start = starttime;
        // echo will go 0 to 1 and need to save time for that. 2 seconds
        // difference
        while ((!echo.isHigh()) && (start < starttime + DELAY * TWO)) {
            //System.out.println("dentro do ṕrimeiro while. echo.state:" + echo.getState() + " trigger.state: " + trigger.getState());
            start = System.nanoTime();
        }
        //System.out.println("saiu do primeiro while. [echo.state:" + echo.getState() + "] [trigger.state: " + trigger.getState() + "]");
        while ((echo.isHigh()) && (stop < starttime + DELAY * TWO)) {
            //System.out.println("dentro do segundo while. echo.state:" + echo.getState() + " trigger.state: " + trigger.getState());
            stop = System.nanoTime();
        }
        long delta = (stop - start);
        //System.out.println("saiu do segundo while. [start: " + start + "] [stop: " + stop + "] [delta: " + delta);
        // echo from 0 to 1 depending on object distance cm/s
        distance = delta * SPEEDOFSOUND;
        System.out.println("distance: " + distance);
        System.out.println("distance calculated: " + (distance / 2.0 / (DELAY)));
        return distance / 2.0 / (DELAY);
    }

    /**
     * Close.
     */
    public void close() {
        if ((trigger != null) && (echo != null)) {
            trigger.setShutdownOptions(true, PinState.LOW,
                    PinPullResistance.OFF);
            echo.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
        }
        System.out.println("closing sensor pins");
    }
}
