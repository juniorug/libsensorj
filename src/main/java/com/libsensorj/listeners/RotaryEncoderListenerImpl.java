/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  IFBA
 * PROJECT       :  libsensorj
 * FILENAME      :  RotaryEncoderListenerImpl.java  
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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The Class RotaryEncoderListenerImpl.
 */
public class RotaryEncoderListenerImpl implements RotaryEncoderListener {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LogManager
            .getLogger(RotaryEncoderListenerImpl.class.getName());

    /*
     * (non-Javadoc)
     * 
     * @see com.libsensorj.listeners.RotaryEncoderListener#turnedClockwise(long)
     */
    @Override
    public void turnedClockwise(long encoderValue) {
        LOGGER.info("turned clockwise. Value: " + encoderValue);

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.libsensorj.listeners.RotaryEncoderListener#turnedCounterclockwise
     * (long)
     */
    @Override
    public void turnedCounterclockwise(long encoderValue) {
        LOGGER.info("turned Counterclockwise. Value: " + encoderValue);

    }

}
