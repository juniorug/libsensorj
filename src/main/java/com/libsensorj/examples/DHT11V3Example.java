package com.libsensorj.examples;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.libsensorj.concretefactory.DHT11V3Factory;
import com.libsensorj.concretesensor.DHT11V3;
import com.libsensorj.interfaces.ISensor;
import com.libsensorj.interfaces.ISensorFactory;

public class DHT11V3Example {

    private static ISensor dht11;
    private static final Logger LOGGER = LogManager
            .getLogger(DHT11TemperatureExample.class.getName());

    public static void main(String[] args) {

        ISensorFactory isf = new DHT11V3Factory();
        dht11 = isf.createSensor();
        dht11.getInstance();

        do {
            // Get the range

            LOGGER.info("temperature in Celcius: "
                    + ((DHT11V3) dht11).getTemperatureInCelsius()
                    + "°C");

            LOGGER.info("temperature in Kelvin: "
                    + ((DHT11V3) dht11).getTemperatureInKelvin()
                    + "°C");

            LOGGER.info("temperature in Fahrenheit: "
                    + ((DHT11V3) dht11).getTemperatureInFahrenheit()
                    + "°C");

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                LOGGER.error("InterruptedException: " + e.getMessage(), e);
            }
        } while (true);

    }

}
