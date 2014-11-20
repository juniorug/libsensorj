/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  IFBA
 * PROJECT       :  libsensorj
 * FILENAME      :  ISensorFactory.java  
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
package com.libsensorj.interfaces;

/**
 * A factory for creating ISensor objects.
 */
public interface ISensorFactory {

    /**
     * Creates a new ISensor object.
     *
     * @return the ISensor
     */
    ISensor createSensor();

    /**
     * Creates a new IEvent object.
     *
     * @return the IEvent
     */
    IEvent createEvent();

}
