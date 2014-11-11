package com.libsensorj.concretefactory;

import com.libsensorj.concreteevent.UltrasonicRangeFinderEvent;
import com.libsensorj.concretesensor.HCSR04Device;
import com.libsensorj.interfaces.IEvent;
import com.libsensorj.interfaces.ISensor;
import com.libsensorj.interfaces.ISensorFactory;

public class HCSR04DeviceFactory implements ISensorFactory {

    @Override
    public ISensor createSensor() {
        return new HCSR04Device();
    }

    @Override
    public IEvent createEvent() {
        return new UltrasonicRangeFinderEvent();
    }

}