package com.libsensorj.examples;

import com.libsensorj.concretefactory.TemperatureSensorFactory;
import com.libsensorj.interfaces.ISensor;
import com.libsensorj.interfaces.ISensorFactory;

public class DHT11TemperatureExample {

    private static ISensor dht11;

    public static void main(String[] args) {
        
        ISensorFactory isf = new TemperatureSensorFactory();
        dht11 = isf.createSensor();
        dht11.getInstance();

    }

}
