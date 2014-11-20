/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  IFBA
 * PROJECT       :  libsensorj
 * FILENAME      :  DHT11Temperature.java  
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

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.temperature.TemperatureConversion;
import com.pi4j.temperature.TemperatureScale;
import com.libsensorj.interfaces.ISensor;

/**
 * The Class DHT11Temperature.
 */
public class DHT11Temperature implements ISensor {

    /** The Constant TEMP_STR. */
    private static final String TEMP_STR = "Temp =";
    
    /** The last value. */
    private String lastValue;
    
    /** The last check. */
    private long lastCheck;
    
    /** The gpio pin. */
    private final int gpioPin;
    
    /** The Constant DEFAULT_PIN. */
    private static final int DEFAULT_PIN = 4;
    
    /** The Constant LAST_CHECK_DIFF. */
    private static final long LAST_CHECK_DIFF = 3000;
    
    /** The Constant LOGGER. */
    private static final Logger LOGGER = LogManager
            .getLogger(DHT11Temperature.class.getName());

    /**
     * Instantiates a new DHt11 temperature.
     *
     * @param gpioPin the gpio pin
     */
    public DHT11Temperature(int gpioPin) {
        this.gpioPin = gpioPin;
        this.lastCheck = System.currentTimeMillis() - LAST_CHECK_DIFF;
    }

    /**
     * Instantiates a new DHt11 temperature.
     */
    public DHT11Temperature() {
        this.gpioPin = DEFAULT_PIN;
        this.lastCheck = System.currentTimeMillis() - LAST_CHECK_DIFF;
    }

    /* (non-Javadoc)
     * @see com.libsensorj.interfaces.ISensor#getInstance()
     */
    @Override
    public void getInstance() {

        // create gpio controller
        final GpioController gpio = GpioFactory.getInstance();

        // provision gpio pin #04 as an input pin with its internal pull down
        // resistor enabled
        final GpioPinDigitalInput dht11Temp = gpio.provisionDigitalInputPin(
                RaspiPin.GPIO_04, PinPullResistance.PULL_DOWN);

        // create and register gpio pin listener
        dht11Temp.addListener(new GpioPinListenerDigital() {
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

    /**
     * Check for updates.
     */
    private void checkForUpdates() {
        long now = System.currentTimeMillis();
        if (now - lastCheck > LAST_CHECK_DIFF) {
            String newValues = readValues();
            if (newValues.indexOf('%') > 0) {
                lastValue = newValues;
                lastCheck = now;
            }
        }
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
     * @param from the from
     * @return the temperature
     */
    private double getTemperature(TemperatureScale from) {
        checkForUpdates();
        switch (from) {

        case FARENHEIT:
            return TemperatureConversion
                    .convertCelsiusToFarenheit(parseTemperature(lastValue));
        case CELSIUS:
            return parseTemperature(lastValue);
        case KELVIN:
            return TemperatureConversion
                    .convertCelsiusToKelvin(parseTemperature(lastValue));
        default:
            throw new RuntimeException("Invalid temperature conversion");
        }
    }

    /**
     * Parses the temperature.
     *
     * @param value the value
     * @return the double
     */
    private double parseTemperature(String value) {
        if (value == null) {
            return Double.MIN_VALUE;
        }
        System.out.println("dentro do parseTemperature. value: " + value);
        System.out.println("meu parsedouble:");
        System.out.println("[value = " + value + "]");
        System.out.println("[TEMP_STR = " + TEMP_STR + "]");
        System.out.println("[value.indexOf(TEMP_STR) = "
                + value.indexOf(TEMP_STR) + "]");
        System.out.println("[TEMP_STR.length() = " + TEMP_STR.length() + "]");
        System.out.println("[value.indexOf('*') = " + value.indexOf('*') + "]");
        System.out
                .println("[value.substring(value.indexOf(TEMP_STR) PLUS TEMP_STR.length(),value.indexOf('*')) = "
                        + value.substring(
                                value.indexOf(TEMP_STR) + TEMP_STR.length(),
                                value.indexOf('*')) + "]");
        return Double.parseDouble(value.substring(value.indexOf(TEMP_STR)
                + TEMP_STR.length(), value.indexOf('*')));
    }

    /**
     * Read values.
     *
     * @return the value readed as a string
     */
    private String readValues() {
        String result = "";
        try {
            Process p = Runtime.getRuntime().exec(
                    String.format("Adafruit_DHT 11 %d", gpioPin));
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    p.getInputStream()));
            String line = null;
            line = in.readLine();
            while (line != null) {
                result += line;
                line = in.readLine();
            }
        } catch (Exception e) {

            LOGGER.error("Exception: Could not read the sensor at pin %d",
                    gpioPin + e.getMessage(), e);
            return null;
        }
        return result;
    }

}
