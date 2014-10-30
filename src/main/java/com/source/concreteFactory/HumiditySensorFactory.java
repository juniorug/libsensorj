package com.source.concreteFactory;

import com.source.concreteEvent.HumidityEvent;
import com.source.concreteSensor.DHT11Humidity;
import com.source.interfaces.IEvent;
import com.source.interfaces.ISensor;
import com.source.interfaces.ISensorFactory;

public class HumiditySensorFactory implements ISensorFactory {

    @Override
    public ISensor createSensor() {
        DHT11Humidity dHT11Humidity = new DHT11Humidity();
        dHT11Humidity.getInstance();
        return dHT11Humidity;
    }

    @Override
    public IEvent createEvent() {
        HumidityEvent humidityEvent = new HumidityEvent();
        humidityEvent.trigger();
        return humidityEvent;
    }

}
