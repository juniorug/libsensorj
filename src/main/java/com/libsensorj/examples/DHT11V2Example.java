/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  IFBA
 * PROJECT       :  libsensorj
 * FILENAME      :  DHT11V2Example.java  
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

import com.libsensorj.concretefactory.DHT11V2Factory;
import com.libsensorj.concretesensor.DHT11V2;
import com.libsensorj.interfaces.ISensor;

/**
 * The Class DHT11V2Example.
 */
public class DHT11V2Example {

    /** The ISensor dht11. */
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

        dht11 = DHT11V2Factory.getInstance();

        do {
            // Get the range

            LOGGER.info("temperature in Celcius: "
                    + ((DHT11V2) dht11).getTemperatureInCelsius() + "°C");

            LOGGER.info("temperature in Kelvin: "
                    + ((DHT11V2) dht11).getTemperatureInKelvin() + "°C");

            LOGGER.info("temperature in Fahrenheit: "
                    + ((DHT11V2) dht11).getTemperatureInFahrenheit() + "°C");

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                LOGGER.error("InterruptedException: " + e.getMessage(), e);
            }
        } while (true);

    }

}
