package com.libsensorj.concretesensor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.libsensorj.interfaces.ISensor;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

public class DHT11Humidity implements ISensor {

    private static final Logger LOGGER = LogManager
            .getLogger(DHT11Humidity.class.getName());

    @Override
    public void getInstance() {

        final GpioController gpio = GpioFactory.getInstance();
        final GpioPinDigitalInput dht11Hum = gpio.provisionDigitalInputPin(
                RaspiPin.GPIO_04, PinPullResistance.PULL_DOWN);

        // create and register gpio pin listener
        dht11Hum.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(
                    GpioPinDigitalStateChangeEvent event) {
                // display pin state on console
                LOGGER.info(" --> GPIO PIN STATE CHANGE: " + event.getPin()
                        + " = " + event.getState());
            }
        });

        // return instance;
    }

}
