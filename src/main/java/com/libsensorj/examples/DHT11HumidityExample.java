package com.libsensorj.examples;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.libsensorj.concretefactory.HumiditySensorFactory;
import com.libsensorj.concretesensor.DHT11Humidity;
import com.libsensorj.interfaces.ISensor;
import com.libsensorj.interfaces.ISensorFactory;

public class DHT11HumidityExample {

    /** The Isensor dht11. */
    private static ISensor dht11;

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LogManager
            .getLogger(DHT11TemperatureExample.class.getName());

    public static void main(String[] args) {
        ISensorFactory isf = new HumiditySensorFactory();
        dht11 = isf.createSensor();
        dht11.getInstance();

        do {

            System.out.println("Humidity in percent: "
                    + ((DHT11Humidity) dht11).getHumidity() + "%");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("InterruptedException: " + e.getMessage());
            }
        } while (true);

    }

}
