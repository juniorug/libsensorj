/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  IFBA
 * PROJECT       :  libsensorj
 * FILENAME      :  DHT11V2.java  
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
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.temperature.TemperatureConversion;
import com.pi4j.temperature.TemperatureScale;

/**
 * The Class DHT11V2.
 */
public class DHT11V2 implements ISensor {

    /** The Constant DEFAULT_PIN. */
    private static final Pin DEFAULT_PIN = RaspiPin.GPIO_04;

    /** The Constant MAXTIMINGS. */
    private static final int MAXTIMINGS = 85;

    /** The dht11Dat. */
    private int[] dht11Dat = { 0, 0, 0, 0, 0 };

    /** The dht11 pin. */
    private GpioPinDigitalMultipurpose dht11Pin;

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LogManager.getLogger(DHT11V2.class
            .getName());

    /**
     * Instantiates a new DHt11 v2.
     */
    public DHT11V2() {
        this(DEFAULT_PIN);
    }

    /**
     * Instantiates a new DHt11 v2.
     *
     * @param pin
     *            the pin
     */
    public DHT11V2(int pin) {
        this(LibPins.getPin(pin));
    }

    /**
     * Instantiates a new DHt11 v2.
     *
     * @param pin
     *            the pin
     */
    public DHT11V2(Pin pin) {
        final GpioController gpio = GpioFactory.getInstance();
        dht11Pin = gpio.provisionDigitalMultipurposePin(pin,
                PinMode.DIGITAL_INPUT, PinPullResistance.PULL_UP);

        // create and register gpio pin listener
        dht11Pin.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(
                    GpioPinDigitalStateChangeEvent event) {
                // display pin state on console
                System.out.println(" --> GPIO PIN STATE CHANGE: "
                        + event.getPin() + " = " + event.getState());
            }
        });

        // configure the pin shutdown behavior; these settings will be
        // automatically applied to the pin when the application is terminated
        dht11Pin.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                System.out.println("Oops!");
                gpio.shutdown();
                System.out.println("Exiting nicely.");
            }
        });
    }

    /**
     * Read value.
     *
     * @return the value readed
     */
    public double readValue() {
        PinState laststate = PinState.HIGH;
        int j = 0;
        dht11Dat[0] = dht11Dat[1] = dht11Dat[2] = dht11Dat[3] = dht11Dat[4] = 0;
        StringBuilder value = new StringBuilder();
        try {

            dht11Pin.setMode(PinMode.DIGITAL_OUTPUT);
            dht11Pin.low();
            Thread.sleep(18);
            dht11Pin.high();
           // TimeUnit.MICROSECONDS.sleep(40);
            dht11Pin.setMode(PinMode.DIGITAL_INPUT);

            for (int i = 0; i < MAXTIMINGS; i++) {
                int counter = 0;
                while (dht11Pin.getState() == laststate) {
                    counter++;
                    TimeUnit.MICROSECONDS.sleep(1);
                    if (counter == 255) {
                        System.out.println("first counter ==255");
                        break;
                    }
                }

                laststate = dht11Pin.getState();

                if (counter == 255) {
                    System.out.println("second counter ==255. will break");
                    break;
                }

                /* ignore first 3 transitions */
                if ((i >= 4) && (i % 2 == 0)) {
                    /* shove each bit into the storage bytes */
                    dht11Dat[j / 8] <<= 1;
                    if (counter > 16) {
                        dht11Dat[j / 8] |= 1;
                    }
                    j++;
                }
            }
            // check we read 40 bits (8bit x 5 ) + verify checksum in the last
            // byte
            if ((j >= 40) && checkParity()) {
                value.append(dht11Dat[2]).append(".").append(dht11Dat[3]);
                System.out.println("temperature value readed: "
                        + value.toString());
            }

        } catch (InterruptedException e) {

            LOGGER.error("InterruptedException: " + e.getMessage(), e);
        }
        if (value.toString().isEmpty()) {
            System.out.println("value is empty");
            value.append(-1);
        }
        return Double.parseDouble(value.toString());
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
     * @param from
     *            the from
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

    /**
     * Check parity.
     *
     * @return true, if successful
     */
    private boolean checkParity() {
        return (dht11Dat[4] == ((dht11Dat[0] + dht11Dat[1] + dht11Dat[2] + dht11Dat[3]) & 0xFF));
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.libsensorj.interfaces.ISensor#getInstance()
     */
    @Override
    public void getInstance() {
        //

    }

}
