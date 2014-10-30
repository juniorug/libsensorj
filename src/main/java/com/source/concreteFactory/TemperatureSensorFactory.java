package com.source.concreteFactory;

import com.source.concreteEvent.TemperatureEvent;
import com.source.concreteSensor.DHT11Temperature;
import com.source.interfaces.IEvent;
import com.source.interfaces.ISensor;
import com.source.interfaces.ISensorFactory;

public class TemperatureSensorFactory implements ISensorFactory {

    @Override
    public ISensor createSensor() {
        DHT11Temperature dHT11Temperature = new DHT11Temperature();
        dHT11Temperature.getInstance();
        return dHT11Temperature;
    }

    @Override
    public IEvent createEvent() {

        TemperatureEvent temperatureEvent = new TemperatureEvent();
        temperatureEvent.trigger();
        return temperatureEvent;
    }

}
