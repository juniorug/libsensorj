package com.libsensorj.concretesensor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.libsensorj.interfaces.ISensor;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

public class UltrasonicHcsr04 implements ISensor {

    private double result = 0;
    private GpioPinDigitalOutput firepulse;
    private GpioPinDigitalInput resultPin;
    private static final String RANGE_PULSE_RESULT = "Range Pulse Result";
    private static final String RANGE_FINDER_TRIGGER = "Range Finder Trigger";
    private static final int TWENTY = 20;
    private static final int FORTY = 40;
    private static final int THIRTY_EIGHT = 38;
    private static final double DISTANCE_FACTOR = 165.7;
    private static final Logger LOGGER = LogManager
            .getLogger(UltrasonicHcsr04.class.getName());

    public UltrasonicHcsr04() {

        GpioController gpio = GpioFactory.getInstance();

        // range finder pins
        firepulse = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_23,
                RANGE_FINDER_TRIGGER, PinState.LOW);
        resultPin = gpio.provisionDigitalInputPin(RaspiPin.GPIO_24,
                RANGE_PULSE_RESULT, PinPullResistance.PULL_DOWN);

    }

    public void getInstance() {


    }

    /*
     * public UltrasonicHcsr04(GpioPinDigitalOutput trigger, GpioPinDigitalInput
     * result_pin) {
     * 
     * this.firepulse = trigger; this.result_pin = result_pin;
     * 
     * }
     */

    public double getRange() {

        try {
            // fire the trigger pulse
            firepulse.high();

            Thread.sleep(TWENTY);
        } catch (InterruptedException e) {

            // e.printStackTrace();
            LOGGER.error(
                    "InterruptedException: Exception triggering range finder "
                            + e.getMessage(), e);
        }
        firepulse.low();

        // wait for the result

        double startTime = System.currentTimeMillis();
        double stopTime = 0;
        do {

            stopTime = System.currentTimeMillis();
            if ((System.currentTimeMillis() - startTime) >= FORTY) {
                break;
            }
        } while (!resultPin.isHigh());

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
