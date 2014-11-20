/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  IFBA
 * PROJECT       :  libsensorj
 * FILENAME      :  DHT11V2Factory.java  
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
import com.libsensorj.concretesensor.DHT11V2;
import com.libsensorj.interfaces.IEvent;
import com.libsensorj.interfaces.ISensor;
import com.libsensorj.interfaces.ISensorFactory;

/**
 * A factory for creating DHT11V2 objects.
 */
public class DHT11V2Factory implements ISensorFactory {

    /** The dht11. */
    private static DHT11V2 dht11;

    /**
     * Instantiates a new DHt11 v2 factory.
     */
    private DHT11V2Factory() {
        dht11 = new DHT11V2();
    }

    /**
     * Gets the single instance of DHT11V2Factory.
     *
     * @return single instance of DHT11V2Factory
     */
    public static DHT11V2 getInstance() {
        if (dht11 == null) {
            dht11 = new DHT11V2();
        }

        return dht11;
    }

    /* (non-Javadoc)
     * @see com.libsensorj.interfaces.ISensorFactory#createSensor()
     */
    @Override
    public ISensor createSensor() {
        dht11 = new DHT11V2();
        return dht11;
    }

    /* (non-Javadoc)
     * @see com.libsensorj.interfaces.ISensorFactory#createEvent()
     */
    @Override
    public IEvent createEvent() {
        return new TemperatureEvent();
    }

    /**
     * Gets the temperature in celsius.
     *
     * @return the temperature in celsius
     */
    public synchronized double getTemperatureInCelsius() {
        return dht11.getTemperatureInCelsius();
    }

    /**
     * Gets the temperature in fahrenheit.
     *
     * @return the temperature in fahrenheit
     */
    public synchronized double getTemperatureInFahrenheit() {
        return dht11.getTemperatureInFahrenheit();

    }

    /**
     * Gets the temperature in kelvin.
     *
     * @return the temperature in kelvin
     */
    public synchronized double getTemperatureInKelvin() {
        return dht11.getTemperatureInKelvin();
    }

}
