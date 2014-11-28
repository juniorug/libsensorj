package com.libsensorj.concretefactory;

import com.libsensorj.concreteevent.TemperatureEvent;
import com.libsensorj.concretesensor.RotaryEncoder;
import com.libsensorj.interfaces.IEvent;
import com.libsensorj.interfaces.ISensor;
import com.libsensorj.interfaces.ISensorFactory;

public class RotaryEncoderFactory implements ISensorFactory{

    @Override
    public ISensor createSensor() {
        return new RotaryEncoder();
    }

    @Override
    public IEvent createEvent() {
        //CHANGE IT!!!
        return new TemperatureEvent();
    }

}
