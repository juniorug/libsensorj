/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  IFBA
 * PROJECT       :  libsensorj
 * FILENAME      :  RotaryEncoderListener.java  
 * 
 * This file is part of the LibsensorJ project,
 * An extensible library for sensors / actuators using the Pi4J framework of the Raspberry Pi.
 * **********************************************************************
 * 
 * Created:      [yyyy/mm/dd creation date]
 * Last Changed: 07/01/2015 
 * 
 * @author: Júnior Mascarenhas       <A HREF="mailto:[juniorug@gmail.com]">[Júnior]</A>
 * @see [https://github.com/juniorug/libsensorj]
 * 
 * #L%
 */
package com.libsensorj.listeners;

/**
 * The listener interface for receiving rotaryEncoder events.
 * The class that is interested in processing a rotaryEncoder
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addRotaryEncoderListener<code> method. When
 * the rotaryEncoder event occurs, that object's appropriate
 * method is invoked.
 *
 * @see RotaryEncoderEvent
 */
public interface RotaryEncoderListener {
    
    /**
     * Turned clockwise.
     *
     * @param encoderValue the encoder value
     */
    void turnedClockwise(long encoderValue);
    
    /**
     * Turned counterclockwise.
     *
     * @param encoderValue the encoder value
     */
    void turnedCounterclockwise(long encoderValue);

}
