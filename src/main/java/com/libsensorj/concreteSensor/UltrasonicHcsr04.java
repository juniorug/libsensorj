package com.libsensorj.concreteSensor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.libsensorj.interfaces.ISensor;
import com.pi4j.examples.I2CWiiMotionPlusExample;
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
    private GpioPinDigitalInput result_pin;
    private static final Logger logger = LogManager
            .getLogger(I2CWiiMotionPlusExample.class.getName());

    @Override
    public void getInstance() {
        
        // Setup GPIO Pins
        GpioController gpio = GpioFactory.getInstance();

        // range finder pins
        firepulse = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_23,
                "Range Finder Trigger", PinState.LOW);
        result_pin = gpio.provisionDigitalInputPin(RaspiPin.GPIO_24,
                "Range Pulse Result", PinPullResistance.PULL_DOWN);

    }

    public UltrasonicHcsr04() {
        
        GpioController gpio = GpioFactory.getInstance();

        // range finder pins
        firepulse = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_23,
                "Range Finder Trigger", PinState.LOW);
        result_pin = gpio.provisionDigitalInputPin(RaspiPin.GPIO_24,
                "Range Pulse Result", PinPullResistance.PULL_DOWN);

    }

    
    
    
    /*public UltrasonicHcsr04(GpioPinDigitalOutput trigger,
            GpioPinDigitalInput result_pin) {

        this.firepulse = trigger;
        this.result_pin = result_pin;

    }*/

    public double getRange() {

        try {
            // fire the trigger pulse
            firepulse.high();

            Thread.sleep(20);
        } catch (InterruptedException e) {

            e.printStackTrace();
            logger.error("InterruptedException: Exception triggering range finder "
                    + e.getMessage());
        }
        firepulse.low();

        // wait for the result

        double startTime = System.currentTimeMillis();
        double stopTime = 0;
        do {

            stopTime = System.currentTimeMillis();
            if ((System.currentTimeMillis() - startTime) >= 40) {
                break;
            }
        } while (result_pin.getState() != PinState.HIGH);

        // calculate the range. If the loop stopped after 38 ms set the result
        // to -1 to show it timed out.

        if ((stopTime - startTime) <= 38) {
            result = (stopTime - startTime) * 165.7;
        } else {
            System.out.println("Timed out");
            result = -1;
        }

        return result;

    }
}
