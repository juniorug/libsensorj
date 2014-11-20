/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  IFBA
 * PROJECT       :  libsensorj
 * FILENAME      :  DHT11TemperatureExample.java  
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
package com.libsensorj.examples;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.libsensorj.concretefactory.TemperatureSensorFactory;
import com.libsensorj.concretesensor.DHT11Temperature;
import com.libsensorj.interfaces.ISensor;
import com.libsensorj.interfaces.ISensorFactory;

/**
 * The Class DHT11TemperatureExample.
 */
public class DHT11TemperatureExample {

    /** The Isensor dht11. */
    private static ISensor dht11;
    
    /** The Constant LOGGER. */
    private static final Logger LOGGER = LogManager
            .getLogger(DHT11TemperatureExample.class.getName());

    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {

        ISensorFactory isf = new TemperatureSensorFactory();
        dht11 = isf.createSensor();
        dht11.getInstance();

        do {
            // Get the range

            LOGGER.info("temperature in Celcius: "
                    + ((DHT11Temperature) dht11).getTemperatureInCelsius()
                    + "°C");

            LOGGER.info("temperature in Kelvin: "
                    + ((DHT11Temperature) dht11).getTemperatureInKelvin()
                    + "°C");

            LOGGER.info("temperature in Fahrenheit: "
                    + ((DHT11Temperature) dht11).getTemperatureInFahrenheit()
                    + "°C");

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                LOGGER.error("InterruptedException: " + e.getMessage(), e);
            }
        } while (true);

    }

}
