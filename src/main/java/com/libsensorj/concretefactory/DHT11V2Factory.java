package com.libsensorj.concretefactory;

import com.libsensorj.concreteevent.TemperatureEvent;
import com.libsensorj.concretesensor.DHT11V2;
import com.libsensorj.interfaces.IEvent;
import com.libsensorj.interfaces.ISensor;
import com.libsensorj.interfaces.ISensorFactory;

public class DHT11V2Factory implements ISensorFactory {

    @Override
    public ISensor createSensor() {
        return new DHT11V2();
    }

    @Override
    public IEvent createEvent() {

        return new TemperatureEvent();
    }

}
