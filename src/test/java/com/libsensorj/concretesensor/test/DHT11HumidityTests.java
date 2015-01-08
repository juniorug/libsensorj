/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  IFBA
 * PROJECT       :  libsensorj
 * FILENAME      :  DHT11HumidityTests.java  
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

import com.libsensorj.concretesensor.DHT11Humidity;
import com.libsensorj.mock.MockGpioFactory;
import com.libsensorj.mock.MockPin;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

/**
 * The Class DHT11TemperatureTests.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(DHT11Humidity.class)
@PowerMockIgnore({ "javax.management.*" })
public class DHT11HumidityTests {

    /** The provider. */
    /* private static MockGpioProvider provider; */

    /** The gpio. */
    private static GpioController gpio;

    /** The pin. */
    private static GpioPinDigitalInput pin;

    /** The pin monitored state. */
    private static PinState pinMonitoredState;

    /** The Constant DATA_READED. */
    private static final String DATA_READED = "Using pin #4Data (40): 0x32 0x0 0x1d 0x0 0x4fTemp = 29 *C, Hum = 50 %";

    /** The Constant READVALUES_METHOD. */
    private static final String READVALUES_METHOD = "readValues";

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LogManager
            .getLogger(DHT11TemperatureTests.class.getName());

    /**
     * Setup.
     */
    @BeforeClass
    public static void setup() {
        // create a mock gpio provider and controller
        /* provider = MockGpioFactory.getMockProvider(); */
        gpio = MockGpioFactory.getInstance();

        // provision pin for testing
        pin = gpio.provisionDigitalInputPin(MockPin.DIGITAL_INPUT_PIN,
                "digitalInputPin", PinPullResistance.PULL_DOWN);

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
     * Test GetHumidity.
     */
    @Test
    public void testGetHumidity() {
        DHT11Humidity dht11 = PowerMockito.spy(new DHT11Humidity());

        try {
            when(dht11, method(DHT11Humidity.class, READVALUES_METHOD))
                    .withNoArguments().thenReturn(DATA_READED);
        } catch (Exception e) {
            LOGGER.error(
                    "Exception: Could not test the method " + e.getMessage(), e);
        }

        Assert.assertEquals(50, dht11.getHumidity(), 0);
    }

}
