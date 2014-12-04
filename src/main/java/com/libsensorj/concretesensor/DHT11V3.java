/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  IFBA
 * PROJECT       :  libsensorj
 * FILENAME      :  DHT11V3.java  
 * 
 * This file is part of the LibsensorJ project,
 * An extensible library for sensors / actuators using the Pi4J framework of the Raspberry Pi.
 * **********************************************************************
 * 
 * Created:      [yyyy/mm/dd creation date]
 * Last Changed: 20/11/2014 
 * 
 * @author: Júnior Mascarenhas       <A HREF="mailto:[juniorug@gmail.com]">[Júnior]</A>
 * @see [https://github.com/juniorug/libsensorj]
 * 
 * #L%
 */
package com.libsensorj.concretesensor;

import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.libsensorj.interfaces.ISensor;
import com.libsensorj.utils.LibPins;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalMultipurpose;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.temperature.TemperatureConversion;
import com.pi4j.temperature.TemperatureScale;

/**
 * The Class DHT11V3.
 */
public class DHT11V3 implements ISensor {

    /** The Constant DEFAULT_PIN. */
    private static final Pin DEFAULT_PIN = RaspiPin.GPIO_04;
    
    /** The temperature. */
    private int temperature;
    
    /** The dht11 pin. */
    private GpioPinDigitalMultipurpose dht11Pin;
    
    /** The Constant LOGGER. */
    private static final Logger LOGGER = LogManager.getLogger(DHT11V3.class
            .getName());

    /**
     * Instantiates a new DHt11 v3.
     */
    public DHT11V3() {
        this(DEFAULT_PIN);
    }

    /**
     * Instantiates a new DHt11 v3.
     *
     * @param pin the pin
     */
    public DHT11V3(int pin) {
        this(LibPins.getPin(pin));
    }

    /**
     * Instantiates a new DHt11 v3.
     *
     * @param pin the pin
     */
    public DHT11V3(Pin pin) {
        final GpioController gpio = GpioFactory.getInstance();
        dht11Pin = gpio.provisionDigitalMultipurposePin(pin,
                PinMode.DIGITAL_INPUT, PinPullResistance.PULL_UP);

        // create and register gpio pin listener
        dht11Pin.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(
                    GpioPinDigitalStateChangeEvent event) {
                // display pin state on console
            	System.out.println(" --> GPIO PIN STATE CHANGE: " + event.getPin()
                        + " = " + event.getState());
            }
        });
        
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                System.out.println("Oops!");
                gpio.shutdown();
                System.out.println("Exiting nicely.");
            }
        });
    }

    // returnvalues:
    // 0 : OK - returns temperature readed
    // -1 : checksum error
    // -2 : timeout
    /**
     * Read value.
     *
     * @return the value readed
     */
    public double readValue() {
        byte[] bits = new byte[] { 0, 0, 0, 0, 0 }; // empty buffer
        byte cnt = 7;
        byte idx = 0;

        try {
            // REQUEST SAMPLE
            dht11Pin.setMode(PinMode.DIGITAL_OUTPUT);
            dht11Pin.low();
            Thread.sleep(18);
            dht11Pin.high();
            TimeUnit.MICROSECONDS.sleep(40);
            dht11Pin.setMode(PinMode.DIGITAL_INPUT);
        } catch (InterruptedException e) {
            LOGGER.error("InterruptedException: " + e.getMessage(), e);
        }
        // ACKNOWLEDGE or TIMEOUT
        int loopCnt = 10000;
        while (dht11Pin.isLow()) {
            if (loopCnt-- == 0) {
                return -2;
            }
        }
        loopCnt = 10000;
        while (dht11Pin.isHigh()) {
            if (loopCnt-- == 0) {
                return -2;
            }
        }
        // READ OUTPUT - 40 BITS => 5 BYTES or TIMEOUT
        for (int i = 0; i < 40; i++) {
            loopCnt = 10000;
            while (dht11Pin.isLow()) {
                if (loopCnt-- == 0) {
                    return -2;
                }
            }
            long startTime = System.nanoTime(); // ns

            loopCnt = 10000;
            while (dht11Pin.isHigh()) {
                if (loopCnt-- == 0) {
                    return -2;
                }
            }
            // 40 microseconds
            if ((System.nanoTime() - startTime) > 40000) {
                bits[idx] |= (1 << cnt);
            }
            if (cnt == 0) // next byte?
            {
                cnt = 7; // restart at MSB
                idx++; // next byte!
            } else {
                cnt--;
            }
        }

        // WRITE TO RIGHT VARS
        // as bits[1] and bits[3] are always zero they are omitted in formulas.
        temperature = bits[2];

        byte sum = (byte) (bits[0] + bits[2]);

        if (bits[4] != sum) {
            return -1;
        }
        // processing OK!
        return temperature;

    }

    /**
     * Gets the temperature in celsius.
     *
     * @return the temperature in celsius
     */
    public synchronized double getTemperatureInCelsius() {
        return getTemperature(TemperatureScale.CELSIUS);
    }

    /**
     * Gets the temperature in fahrenheit.
     *
     * @return the temperature in fahrenheit
     */
    public synchronized double getTemperatureInFahrenheit() {
        return getTemperature(TemperatureScale.FARENHEIT);

    }

    /**
     * Gets the temperature in kelvin.
     *
     * @return the temperature in kelvin
     */
    public synchronized double getTemperatureInKelvin() {
        return getTemperature(TemperatureScale.KELVIN);
    }

    /**
     * Gets the temperature.
     *
     * @param from the TemperatureScale
     * @return the temperature
     */
    private double getTemperature(TemperatureScale from) {
        switch (from) {

        case FARENHEIT:
            return TemperatureConversion.convertCelsiusToFarenheit(readValue());
        case CELSIUS:
            return (readValue());
        case KELVIN:
            return TemperatureConversion.convertCelsiusToKelvin(readValue());
        default:
            throw new RuntimeException("Invalid temperature conversion");
        }
    }

    /* (non-Javadoc)
     * @see com.libsensorj.interfaces.ISensor#getInstance()
     */
    @Override
    public void getInstance() {

    }

}
