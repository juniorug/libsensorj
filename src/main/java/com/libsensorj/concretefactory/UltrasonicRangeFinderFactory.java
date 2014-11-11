package com.libsensorj.concretefactory;

import com.libsensorj.concreteevent.UltrasonicRangeFinderEvent;
import com.libsensorj.concretesensor.UltrasonicHcsr04;
import com.libsensorj.interfaces.IEvent;
import com.libsensorj.interfaces.ISensor;
import com.libsensorj.interfaces.ISensorFactory;

public class UltrasonicRangeFinderFactory implements ISensorFactory {

    @Override
    public ISensor createSensor() {
        return new UltrasonicHcsr04();
    }

    @Override
    public IEvent createEvent() {
        return new UltrasonicRangeFinderEvent();
    }

}