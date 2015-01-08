/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  IFBA
 * PROJECT       :  libsensorj
 * FILENAME      :  UltrasonicHcsr04Tests.java  
 * 
 * This file is part of the LibsensorJ project,
 * An extensible library for sensors / actuators using the Pi4J framework of the Raspberry Pi.
 * **********************************************************************
 * 
 * Created:      [yyyy/mm/dd creation date]
 * Last Changed: 07/01/2015 
 * 
 * @author: Júnior Mascarenhas       <A HREF="mailto:[juniorug@gmail.com]">[Júnior]</A>
 * @see [https://github.com/juniorug/libsensorj]
 * 
 * #L%
 */
package com.libsensorj.concretesensor.test;


import static org.junit.Assert.assertTrue;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.support.membermodification.MemberMatcher.method;

import java.util.Collection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.libsensorj.concretesensor.UltrasonicHcsr04;
import com.libsensorj.mock.MockGpioFactory;
import com.libsensorj.mock.MockPin;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

/**
 * The Class UltrasonicHcsr04Tests.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(UltrasonicHcsr04Tests.class)
@PowerMockIgnore({ "javax.management.*" })

public class UltrasonicHcsr04Tests {

    /** The provider. */
    /* private static MockGpioProvider provider; */

    /** The gpio. */
    private static GpioController gpio;

    /** The pin. */
    private static GpioPinDigitalOutput trigger;

    /** The pin. */
    private static GpioPinDigitalInput echo;

    /** The pin monitored state. */
    private static PinState echoMonitoredState;

    /** The pin monitored state. */
    private static PinState triggerMonitoredState;

    /** The Constant DATA_READED. */
    private static final String DATA_READED = "10";

    /** The Constant READVALUES_METHOD. */
    private static final String DISTANCE_METHOD = "getRange";

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LogManager
            .getLogger(HCSR04DeviceTests.class.getName());

    /**
     * Setup.
     */
    @BeforeClass
    public static void setup() {
        // create a mock gpio provider and controller
        /* provider = MockGpioFactory.getMockProvider(); */
        gpio = MockGpioFactory.getInstance();

        // provision pin for testing
        trigger = gpio.provisionDigitalOutputPin(MockPin.DIGITAL_OUTPUT_PIN,
                "digitalOutputPin", PinState.LOW);

        echo = gpio.provisionDigitalInputPin(MockPin.DIGITAL_INPUT_PIN,
                "digitalInputPin", PinPullResistance.PULL_DOWN);

        // register pin listener
        echo.addListener(new GpioPinListenerDigital() {
            public void handleGpioPinDigitalStateChangeEvent(
                    GpioPinDigitalStateChangeEvent event) {
                // set pin state
                if (event.getPin() == echo) {
                    echoMonitoredState = event.getState();
                    LOGGER.debug("echo state changed to state:"
                            + echoMonitoredState);
                }
            }
        });

        trigger.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(
                    GpioPinDigitalStateChangeEvent event) {
                // set pin state
                if (event.getPin() == trigger) {
                    triggerMonitoredState = event.getState();
                    LOGGER.debug("trigger state changed to state:"
                            + triggerMonitoredState);
                }
            }
        });
    }

    /**
     * Test pin provisioned.
     */
    @Test
    public void testPinProvisioned() {
        // make sure that pin is provisioned
        Collection<GpioPin> pins = gpio.getProvisionedPins();
        assertTrue(pins.contains(echo));
        assertTrue(pins.contains(trigger));
    }

    /**
     * Test GetHumidity.
     */
    @Test
    public void testGetDistance() {
        UltrasonicHcsr04 device = PowerMockito.spy(new UltrasonicHcsr04());

        try {
            when(device, method(UltrasonicHcsr04.class, DISTANCE_METHOD))
                    .withNoArguments().thenReturn(DATA_READED);
        } catch (Exception e) {
            LOGGER.error(
                    "Exception: Could not test the method " + e.getMessage(), e);
        }

        Assert.assertEquals(10, device.getRange(), 0);
    }

}