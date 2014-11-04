package com.libsensorj.concreteFactory;

import com.libsensorj.concreteEvent.UltrasonicRangeFinderEvent;
import com.libsensorj.concreteSensor.UltrasonicHcsr04;
import com.libsensorj.interfaces.IEvent;
import com.libsensorj.interfaces.ISensor;
import com.libsensorj.interfaces.ISensorFactory;

public class UltrasonicRangeFinderFactory implements ISensorFactory {

    @Override
    public ISensor createSensor() {
        /*DHT11Temperature dHT11Temperature = new DHT11Temperature();
        dHT11Temperature.getInstance();
        return dHT11Temperature;*/
        
        return new UltrasonicHcsr04();
    }

    @Override
    public IEvent createEvent() {

/*        TemperatureEvent temperatureEvent = new TemperatureEvent();
        temperatureEvent.trigger();
        return temperatureEvent;*/
        
        return new UltrasonicRangeFinderEvent();
    }

}