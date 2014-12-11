/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  IFBA
 * PROJECT       :  libsensorj
 * FILENAME      :  DHT11Humidity.java  
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

import com.libsensorj.interfaces.ISensor;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

/**
 * The Class DHT11Humidity.
 */
public class DHT11Humidity implements ISensor {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LogManager
            .getLogger(DHT11Humidity.class.getName());

    /** The Constant TEMP_STR. */
    private static final String HUM_STR = "Hum =";
    
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
    
    public DHT11Humidity(int gpioPin) {
        this.gpioPin = gpioPin;
        this.lastCheck = System.currentTimeMillis() - LAST_CHECK_DIFF;
    }

    /**
     * Instantiates a new DHt11 temperature.
     */
    public DHT11Humidity() {
        this.gpioPin = DEFAULT_PIN;
        this.lastCheck = System.currentTimeMillis() - LAST_CHECK_DIFF;
    }
    
    /* (non-Javadoc)
     * @see com.libsensorj.interfaces.ISensor#getInstance()
     */
    @Override
    public void getInstance() {

        final GpioController gpio = GpioFactory.getInstance();
        final GpioPinDigitalInput dht11Hum = gpio.provisionDigitalInputPin(
                RaspiPin.GPIO_04, PinPullResistance.PULL_DOWN);

        
        
        
        // create and register gpio pin listener
        dht11Hum.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(
                    GpioPinDigitalStateChangeEvent event) {
                // display pin state on console
                LOGGER.info(" --> GPIO PIN STATE CHANGE: " + event.getPin()
                        + " = " + event.getState());
            }
        });
        
        // configure the pin shutdown behavior; these settings will be 
        // automatically applied to the pin when the application is terminated 
        dht11Hum.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                System.out.println("Oops!");
                gpio.shutdown();
                System.out.println("Exiting nicely.");
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
     * Gets the humidity.
     *
     * @return the humidity
     */
    public double getHumidity() {
        checkForUpdates();
        return parseHumidity(lastValue);
    }
    
    /**
     * Parses the Humidity.
     *
     * @param value the string returned by adafruit
     * @return the humidity in percent.
     */
    private double parseHumidity(String value) {
        if (value == null) {
            return Double.MIN_VALUE;
        }
        
        return Double.parseDouble(value.substring(value.indexOf(HUM_STR)
                + HUM_STR.length(), value.indexOf('%')));
    }
    
    /**
     * Read values.
     *
     * @return the value read as a string
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
