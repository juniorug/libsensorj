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

import com.libsensorj.concretesensor.DHT11Temperature;
import com.libsensorj.mock.MockGpioFactory;
import com.libsensorj.mock.MockGpioProvider;
import com.libsensorj.mock.MockPin;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;


@RunWith(PowerMockRunner.class)
@PrepareForTest(DHT11Temperature.class)
@PowerMockIgnore( {"javax.management.*"}) 
public class DHT11TemperatureTests {

    private static MockGpioProvider provider;
    private static GpioController gpio;
    private static GpioPinDigitalInput pin;
    private static PinState pinMonitoredState;
    private static final String DATA_READED = "Using pin #4Data (40): 0x32 0x0 0x1d 0x0 0x4fTemp = 29 *C, Hum = 50 %";
    private static final String READVALUES_METHOD = "readValues";

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LogManager
            .getLogger(DHT11TemperatureTests.class.getName());
    
    @BeforeClass 
    public static void setup() {
        // create a mock gpio provider and controller
        provider = MockGpioFactory.getMockProvider();
        gpio = MockGpioFactory.getInstance();

        // provision pin for testing
        pin = gpio.provisionDigitalInputPin(MockPin.DIGITAL_INPUT_PIN,  "digitalInputPin", PinPullResistance.PULL_DOWN);
        
        // register pin listener
        pin.addListener(new GpioPinListenerDigital() {
                public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                    // set pin state
                    if (event.getPin() == pin) {
                        pinMonitoredState = event.getState();
                    }
                }                
            });
    }
    
    @Test
    public void testPinProvisioned()  {
        // make sure that pin is provisioned
        Collection<GpioPin> pins = gpio.getProvisionedPins();        
        assertTrue(pins.contains(pin));
    }  
    
    @Test 
    public void testDHT11(){
        DHT11Temperature dht11 =  PowerMockito.spy(new DHT11Temperature());
        
        try {
            when(dht11, method(DHT11Temperature.class, READVALUES_METHOD)).withNoArguments().thenReturn(DATA_READED);
        } catch (Exception e) {
            LOGGER.error("Exception: Could not test the method "+ e.getMessage(), e );
        }
        
        Assert.assertEquals(29,dht11.getTemperatureInCelsius(),0);
        
        /*DHT11Temperature dht11 = PowerMock.createPartialMock(DHT11Temperature.class, READVALUES_METHOD);
        PowerMock.expectPrivate(dht11, READVALUES_METHOD,null).andReturn(DATA_READED);
        
        replay(dht11);
        assertequals(29,dht11.getTemperatureInCelsius());
        verify(dht11);*/
    }
    
    
    
    

}
