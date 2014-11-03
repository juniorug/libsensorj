package com.libsensorj.concreteSensor;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.libsensorj.interfaces.ISensor;

public class DHT11Humidity implements ISensor {

    @Override
    public void getInstance() {

        // create gpio controller
        final GpioController gpio = GpioFactory.getInstance();
        // provision gpio pin #04 as an input pin with its internal pull down
        // resistor enabled
        final GpioPinDigitalInput dht11Hum = gpio.provisionDigitalInputPin(
                RaspiPin.GPIO_04, PinPullResistance.PULL_DOWN);

        // create and register gpio pin listener
        dht11Hum.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(
                    GpioPinDigitalStateChangeEvent event) {
                // display pin state on console
                System.out.println(" --> GPIO PIN STATE CHANGE: "
                        + event.getPin() + " = " + event.getState());
            }
        });
        
        // return instance;
    }

}
