package com.libsensorj.concretefactory;

import com.libsensorj.concreteevent.TemperatureEvent;
import com.libsensorj.concretesensor.DHT11V2;
import com.libsensorj.interfaces.IEvent;
import com.libsensorj.interfaces.ISensor;
import com.libsensorj.interfaces.ISensorFactory;

public class DHT11V2Factory implements ISensorFactory {

    private static DHT11V2 dht11;

    private DHT11V2Factory() {
        dht11 = new DHT11V2();
    }

    public static DHT11V2 getInstance() {
        if (dht11 == null) {
            dht11 = new DHT11V2();
        }

        return dht11;
    }

    @Override
    public ISensor createSensor() {
        dht11 = new DHT11V2();
        return dht11;
    }

    @Override
    public IEvent createEvent() {
        return new TemperatureEvent();
    }

    public synchronized double getTemperatureInCelsius() {
        return dht11.getTemperatureInCelsius();
    }

    public synchronized double getTemperatureInFahrenheit() {
        return dht11.getTemperatureInFahrenheit();

    }

    public synchronized double getTemperatureInKelvin() {
        return dht11.getTemperatureInKelvin();
    }

}
