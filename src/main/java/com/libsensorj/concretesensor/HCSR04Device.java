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

public class HCSR04Device implements ISensor {

    // Speed of sound = 34029 cm/s
    private static final int SPEEDOFSOUND = 34029;
    private static final int TWO = 2;
    // #10 µs pulse = 1ms
    private static final long PULSE_TIME = 1;
    private static final long DELAY = 1000000000L;

    private GpioPinDigitalOutput trigger = null;
    private GpioPinDigitalInput echo = null;

    private static final Logger LOGGER = LogManager
            .getLogger(HCSR04Device.class.getName());

    public HCSR04Device() {

        this(RaspiPin.GPIO_23,RaspiPin.GPIO_24);
    }

    public HCSR04Device(int _trigger, int _echo) {

        this(LibPins.getPin(_trigger),LibPins.getPin(_echo));
    }
    
    public HCSR04Device(Pin _trigger, Pin _echo) {
        final GpioController gpio = GpioFactory.getInstance();
        trigger = gpio.provisionDigitalOutputPin(_trigger,
                PinState.LOW);
        echo = gpio.provisionDigitalInputPin(_echo,
                PinPullResistance.PULL_DOWN);
        
        // create and register gpio pin listener
        echo.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(
                    GpioPinDigitalStateChangeEvent event) {
                // display pin state on console
                LOGGER.info(" --> GPIO PIN STATE CHANGE: " + event.getPin()
                        + " = " + event.getState());
            }
        });
    }
    
    public void getInstance() {
        
    }
    public double getDistance() {
        long distance = 0;
        // Send a pulse trigger; must be 1 and 0 with a 10 µs wait
        trigger.pulse(PULSE_TIME);
        long starttime = System.nanoTime(); // ns
        long stop = starttime;
        long start = starttime;
        // echo will go 0 to 1 and need to save time for that. 2 seconds
        // difference
        while ((!echo.isHigh()) && (start < starttime + DELAY * TWO)) {
            start = System.nanoTime();
        }
        while ((echo.isHigh()) && (stop < starttime + DELAY * TWO)) {
            stop = System.nanoTime();
        }
        long delta = (stop - start);
        // echo from 0 to 1 depending on object distance  cm/s
        distance = delta * SPEEDOFSOUND;  
        LOGGER.info("distance calculated: " + (distance / 2.0 / (DELAY)));
        return distance / 2.0 / (DELAY); 
    }

    public void close() {
        if ((trigger != null) && (echo != null)) {
            trigger.setShutdownOptions(true, PinState.LOW,
                    PinPullResistance.OFF);
            echo.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
            ;
        }
        LOGGER.info("closing sensor pins");
    }
}