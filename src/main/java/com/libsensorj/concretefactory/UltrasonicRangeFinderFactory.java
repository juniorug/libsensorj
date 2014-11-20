/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  IFBA
 * PROJECT       :  libsensorj
 * FILENAME      :  UltrasonicRangeFinderFactory.java  
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

import com.libsensorj.concreteevent.UltrasonicRangeFinderEvent;
import com.libsensorj.concretesensor.UltrasonicHcsr04;
import com.libsensorj.interfaces.IEvent;
import com.libsensorj.interfaces.ISensor;
import com.libsensorj.interfaces.ISensorFactory;

/**
 * A factory for creating UltrasonicRangeFinder objects.
 */
public class UltrasonicRangeFinderFactory implements ISensorFactory {

    /* (non-Javadoc)
     * @see com.libsensorj.interfaces.ISensorFactory#createSensor()
     */
    @Override
    public ISensor createSensor() {
        return new UltrasonicHcsr04();
    }

    /* (non-Javadoc)
     * @see com.libsensorj.interfaces.ISensorFactory#createEvent()
     */
    @Override
    public IEvent createEvent() {
        return new UltrasonicRangeFinderEvent();
    }

}
