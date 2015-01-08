/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  IFBA
 * PROJECT       :  libsensorj
 * FILENAME      :  TemperatureSensorFactory.java  
 * 
 * This file is part of the LibsensorJ project,
 * An extensible library for sensors / actuators using the Pi4J framework of the Raspberry Pi.
 * **********************************************************************
 * 
 * Created:      [yyyy/mm/dd creation date]
 * Last Changed: 20/11/2014 
 * 
 * @author: Júnior Mascarenhas       <A HREF="mailto:[juniorug@gmail.com]">[Júnior]</A>
 * @see [https://github.com/juniorug/libsensorj]
 * 
 * #L%
 */
package com.libsensorj.concretefactory;

import com.libsensorj.concreteevent.TemperatureEvent;
import com.libsensorj.concretesensor.DHT11Temperature;
import com.libsensorj.interfaces.IEvent;
import com.libsensorj.interfaces.ISensor;
import com.libsensorj.interfaces.ISensorFactory;

/**
 * A factory for creating TemperatureSensor objects.
 */
public class TemperatureSensorFactory implements ISensorFactory {

    /*
     * (non-Javadoc)
     * 
     * @see com.libsensorj.interfaces.ISensorFactory#createSensor()
     */
    @Override
    public ISensor createSensor() {
        /*
         * DHT11Temperature dHT11Temperature = new DHT11Temperature();
         * dHT11Temperature.getInstance(); return dHT11Temperature;
         */

        return new DHT11Temperature();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.libsensorj.interfaces.ISensorFactory#createEvent()
     */
    @Override
    public IEvent createEvent() {

        /*
         * TemperatureEvent temperatureEvent = new TemperatureEvent();
         * temperatureEvent.trigger(); return temperatureEvent;
         */

        return new TemperatureEvent();
    }

}
