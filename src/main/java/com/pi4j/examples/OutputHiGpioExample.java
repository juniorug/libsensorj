package com.pi4j.examples;

// START SNIPPET: control-gpio-snippet

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Examples
 * FILENAME      :  OutputHiGpioExample.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2014 Pi4J
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

/**
 * This example code demonstrates how to perform simple state control of a GPIO
 * pin on the Raspberry Pi.
 * 
 * @author Robert Savage
 */
public class OutputHiGpioExample {

    private static final Logger LOGGER = LogManager
            .getLogger(OutputHiGpioExample.class.getName());

    public static void main(String[] args) throws InterruptedException {

        LOGGER.info("<--Pi4J--> GPIO Output HI Example ... started.");

        // create gpio controller
        final GpioController gpio = GpioFactory.getInstance();

        // provision gpio pin #01 as an output pin and turn on
        final GpioPinDigitalOutput pin = gpio.provisionDigitalOutputPin(
                RaspiPin.GPIO_01, "MyLED", PinState.HIGH);
        LOGGER.info("--> GPIO state should be: ON");

        Thread.sleep(10000);

        pin.export(PinMode.DIGITAL_INPUT);
        LOGGER.info("--> GPIO pin now is INPUT");

        ((GpioPinDigitalOutput) pin).addListener(new GpioPinListenerDigital() {
            public void handleGpioPinDigitalStateChangeEvent(
                    GpioPinDigitalStateChangeEvent event) {
                LOGGER.info("--> GPIO pin INPUT event: " + event.toString());
            }
        });
        Thread.sleep(10000);
        LOGGER.info("--> GPIO state should be: ON");
        pin.export(PinMode.DIGITAL_OUTPUT);
        pin.setState(true);

        Thread.sleep(10000);

        // stop all GPIO activity/threads by shutting down the GPIO controller
        // (this method will forcefully shutdown all GPIO monitoring threads and
        // scheduled tasks)
        gpio.shutdown();
    }
}
// END SNIPPET: control-gpio-snippet
