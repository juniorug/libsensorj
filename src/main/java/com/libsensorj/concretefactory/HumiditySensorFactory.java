package com.libsensorj.concretefactory;

import com.libsensorj.concreteevent.HumidityEvent;
import com.libsensorj.concretesensor.DHT11Humidity;
import com.libsensorj.interfaces.IEvent;
import com.libsensorj.interfaces.ISensor;
import com.libsensorj.interfaces.ISensorFactory;

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
