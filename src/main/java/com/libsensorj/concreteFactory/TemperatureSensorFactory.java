package com.libsensorj.concreteFactory;

import com.libsensorj.concreteEvent.TemperatureEvent;
import com.libsensorj.concreteSensor.DHT11Temperature;
import com.libsensorj.interfaces.IEvent;
import com.libsensorj.interfaces.ISensor;
import com.libsensorj.interfaces.ISensorFactory;

public class TemperatureSensorFactory implements ISensorFactory {

    @Override
    public ISensor createSensor() {
        /*DHT11Temperature dHT11Temperature = new DHT11Temperature();
        dHT11Temperature.getInstance();
        return dHT11Temperature;*/
        
        return new DHT11Temperature();
    }

    @Override
    public IEvent createEvent() {

/*        TemperatureEvent temperatureEvent = new TemperatureEvent();
        temperatureEvent.trigger();
        return temperatureEvent;*/
        
        return new TemperatureEvent();
    }

}
