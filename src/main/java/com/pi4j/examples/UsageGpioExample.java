package com.pi4j.examples;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Examples
 * FILENAME      :  UsageGpioExample.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2014 Pi4J
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the License. You may obtain a copy of the License
 * at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 * #L%
 */

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// START SNIPPET: usage-import-snippet
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.io.gpio.trigger.GpioSyncStateTrigger;

// END SNIPPET: usage-import-snippet

/**
 * This example code demonstrates how to setup simple triggers for GPIO pins on
 * the Raspberry Pi.
 * 
 * @author Robert Savage
 */
@SuppressWarnings("unused")
public class UsageGpioExample {

    private static final Logger LOGGER = LogManager
            .getLogger(UsageGpioExample.class.getName());

    public static void main(String[] args) throws InterruptedException {

        // START SNIPPET: usage-create-controller-snippet
        // create gpio controller instance
        final GpioController gpio = GpioFactory.getInstance();
        // END SNIPPET: usage-create-controller-snippet

        // START SNIPPET: usage-provision-input-pin-snippet
        // provision gpio pin #02 as an input pin with its internal pull down
        // resistor enabled
        // (configure pin edge to both rising and falling to get notified for
        // HIGH and LOW state
        // changes)
        // RaspiPin.GPIO_02: PIN NUMBER
        // "MyButton": PIN FRIENDLY NAME (optional)
        // PinPullResistance.PULL_DOWN: PIN RESISTANCE (optional)
        GpioPinDigitalInput myButton = gpio.provisionDigitalInputPin(
                RaspiPin.GPIO_02, "MyButton", PinPullResistance.PULL_DOWN);

        // END SNIPPET: usage-provision-input-pin-snippet
        // START SNIPPET: usage-provision-output-pin-snippet
        // provision gpio pins #04 as an output pin and make sure is is set to
        // LOW at startup
        // RaspiPin.GPIO_04: PIN NUMBER
        // "My LED": PIN FRIENDLY NAME (optional)
        // PinState.LOW: PIN STARTUP STATE (optional)
        GpioPinDigitalOutput myLed = gpio.provisionDigitalOutputPin(
                RaspiPin.GPIO_04,
                "My LED", 
                PinState.LOW); 
        // END SNIPPET: usage-provision-output-pin-snippet

        // START SNIPPET: usage-shutdown-pin-snippet
        // configure the pin shutdown behavior; these settings will be
        // automatically applied to the pin when the application is terminated
        // ensure that the LED is turned OFF when the application is shutdown
        myLed.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
        // END SNIPPET: usage-shutdown-pin-snippet

        // START SNIPPET: usage-control-pin-snippet
        // explicitly set a state on the pin object
        myLed.setState(PinState.HIGH);

        // use convenience wrapper method to set state on the pin object
        myLed.low();
        myLed.high();

        // use toggle method to apply inverse state on the pin object
        myLed.toggle();

        // use pulse method to set the pin to the HIGH state for
        // an explicit length of time in milliseconds
        myLed.pulse(1000);
        // END SNIPPET: usage-control-pin-snippet

        // START SNIPPET: usage-read-pin-snippet
        // get explicit state enumeration for the GPIO pin associated with the
        // button
        PinState myButtonState = myButton.getState();

        // use convenience wrapper method to interrogate the button state
        boolean buttonPressed = myButton.isHigh();
        // END SNIPPET: usage-read-pin-snippet

        // START SNIPPET: usage-register-listener-snippet
        // create and register gpio pin listener
        myButton.addListener(new GpioUsageExampleListener());
        // END SNIPPET: usage-register-listener-snippet

        // START SNIPPET: usage-trigger-snippet
        // create a gpio synchronization trigger on the input pin
        // when the input state changes, also set LED controlling gpio pin to
        // same state
        myButton.addTrigger(new GpioSyncStateTrigger(myLed));
        // END SNIPPET: usage-trigger-snippet

        // keep program running until user aborts (CTRL-C)
        for (;;) {
            Thread.sleep(500);
        }

        // stop all GPIO activity/threads by shutting down the GPIO controller
        // (this method will forcefully shutdown all GPIO monitoring threads and
        // scheduled tasks)
        // gpio.shutdown(); <--- implement this method call if you wish to
        // terminate the Pi4J GPIO controller
    }

    // START SNIPPET: usage-listener-snippet
    public static class GpioUsageExampleListener implements
            GpioPinListenerDigital {
        @Override
        public void handleGpioPinDigitalStateChangeEvent(
                GpioPinDigitalStateChangeEvent event) {
            // display pin state on console
            LOGGER.info(" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = "
                    + event.getState());
        }
    }
    // END SNIPPET: usage-listener-snippet
}
