package com.libsensorj.examples;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.libsensorj.concretefactory.TemperatureSensorFactory;
import com.libsensorj.concretesensor.DHT11Temperature;
import com.libsensorj.interfaces.ISensor;
import com.libsensorj.interfaces.ISensorFactory;

public class DHT11TemperatureExample {

    private static ISensor dht11;
    private static final Logger LOGGER = LogManager
            .getLogger(DHT11TemperatureExample.class.getName());

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
