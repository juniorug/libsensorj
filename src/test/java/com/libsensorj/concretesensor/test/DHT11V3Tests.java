/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  IFBA
 * PROJECT       :  libsensorj
 * FILENAME      :  DHT11V3Tests.java  
 * 
 * This file is part of the LibsensorJ project,
 * An extensible library for sensors / actuators using the Pi4J framework of the Raspberry Pi.
 * **********************************************************************
 * 
 * Created:      [yyyy/mm/dd creation date]
 * Last Changed: 25/11/2014 
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

import com.libsensorj.concretesensor.DHT11V3;
import com.libsensorj.mock.MockGpioFactory;
import com.libsensorj.mock.MockPin;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

/**
 * The Class DHT11V3Tests.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(DHT11V3.class)
@PowerMockIgnore({ "javax.management.*" })
public class DHT11V3Tests {

    /** The provider. */
    /* private static MockGpioProvider provider; */

    /** The gpio. */
    private static GpioController gpio;

    /** The pin. */
    private static GpioPinDigitalInput pin;

    /** The pin monitored state. */
    private static PinState pinMonitoredState;

    /** The Constant DATA_READED. */
    private static final double DATA_READED = 29;

    /** The Constant READVALUES_METHOD. */
    private static final String READVALUE_METHOD = "readValue";

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LogManager
            .getLogger(DHT11V3Tests.class.getName());

    /**
     * Setup.
     */
    @BeforeClass
    public static void setup() {
        // create a mock gpio provider and controller
        /* provider = MockGpioFactory.getMockProvider(); */
        gpio = MockGpioFactory.getInstance();

        // provision pin for testing
        pin = gpio.provisionDigitalMultipurposePin(MockPin.DIGITAL_INPUT_PIN,
                PinMode.DIGITAL_INPUT, PinPullResistance.PULL_UP);

        // register pin listener
        pin.addListener(new GpioPinListenerDigital() {
            public void handleGpioPinDigitalStateChangeEvent(
                    GpioPinDigitalStateChangeEvent event) {
                // set pin state
                if (event.getPin() == pin) {
                    pinMonitoredState = event.getState();
                    LOGGER.debug("pin state changed to state:"
                            + pinMonitoredState);
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
        assertTrue(pins.contains(pin));
    }

    /**
     * Test GetTemperatureInCelsius.
     */
    @Test
    public void testGetTemperatureInCelsius() {
        DHT11V3 dht11 = PowerMockito.spy(new DHT11V3());

        try {
            when(dht11, method(DHT11V3.class, READVALUE_METHOD))
                    .withNoArguments().thenReturn(DATA_READED);
        } catch (Exception e) {
            LOGGER.error(
                    "Exception: Could not test the method " + e.getMessage(), e);
        }

        Assert.assertEquals(29, dht11.getTemperatureInCelsius(), 0);

        /*
         * DHT11Temperature dht11 =
         * PowerMock.createPartialMock(DHT11Temperature.class,
         * READVALUES_METHOD); PowerMock.expectPrivate(dht11,
         * READVALUES_METHOD,null).andReturn(DATA_READED);
         * 
         * replay(dht11); assertequals(29,dht11.getTemperatureInCelsius());
         * verify(dht11);
         */
    }

    /**
     * Test GetTemperatureInFahrenheit.
     */
    @Test
    public void testGetTemperatureInFahrenheit() {
        DHT11V3 dht11 = PowerMockito.spy(new DHT11V3());

        try {
            when(dht11, method(DHT11V3.class, READVALUE_METHOD))
                    .withNoArguments().thenReturn(DATA_READED);
        } catch (Exception e) {
            LOGGER.error(
                    "Exception: Could not test the method " + e.getMessage(), e);
        }

        Assert.assertEquals(84.2, dht11.getTemperatureInFahrenheit(), 0);

    }

    /**
     * Test GetTemperatureInKelvin.
     */
    @Test
    public void testGetTemperatureInKelvin() {
        DHT11V3 dht11 = PowerMockito.spy(new DHT11V3());

        try {
            when(dht11, method(DHT11V3.class, READVALUE_METHOD))
                    .withNoArguments().thenReturn(DATA_READED);
        } catch (Exception e) {
            LOGGER.error(
                    "Exception: Could not test the method " + e.getMessage(), e);
        }

        Assert.assertEquals(302.15, dht11.getTemperatureInKelvin(), 0);

    }
}
