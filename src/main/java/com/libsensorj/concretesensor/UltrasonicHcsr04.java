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

public class UltrasonicHcsr04 implements ISensor {

    private double result = 0;
    private GpioPinDigitalOutput trigger;
    private GpioPinDigitalInput echo;
    private static final int TWENTY = 20;
    private static final int FORTY = 40;
    private static final int THIRTY_EIGHT = 38;
    private static final double DISTANCE_FACTOR = 165.7;
    private static final Logger LOGGER = LogManager
            .getLogger(UltrasonicHcsr04.class.getName());

    
    public UltrasonicHcsr04() {

        this(RaspiPin.GPIO_23,RaspiPin.GPIO_24);
    }

    public UltrasonicHcsr04(int _trigger, int _echo) {

        this(LibPins.getPin(_trigger),LibPins.getPin(_echo));
    }
    
    public UltrasonicHcsr04(Pin _trigger, Pin _echo) {
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

    public double getRange() {

        try {
            // fire the trigger pulse
            trigger.high();

            Thread.sleep(TWENTY);
        } catch (InterruptedException e) {

            // e.printStackTrace();
            LOGGER.error(
                    "InterruptedException: Exception triggering range finder "
                            + e.getMessage(), e);
        }
        trigger.low();

        // wait for the result

        double startTime = System.currentTimeMillis();
        double stopTime = 0;
        do {

            stopTime = System.currentTimeMillis();
            if ((System.currentTimeMillis() - startTime) >= FORTY) {
                break;
            }
        } while (!echo.isHigh());

        // calculate the range. If the loop stopped after 38 ms set the result
        // to -1 to show it timed out.

        if ((stopTime - startTime) <= THIRTY_EIGHT) {
            result = (stopTime - startTime) * DISTANCE_FACTOR;
        } else {
            LOGGER.info("Timed out");
            result = -1;
        }

        return result;

    }
}
