package com.libsensorj.listeners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RotaryEncoderListenerImpl implements RotaryEncoderListener {

    private static final Logger LOGGER = LogManager.getLogger(RotaryEncoderListenerImpl.class.getName());
    
    @Override
    public void turnedClockwise(long encoderValue) {
        LOGGER.info("turned clockwise. Value: " + encoderValue);
        
    }

    @Override
    public void turnedCounterclockwise(long encoderValue) {
       LOGGER.info("turned Counterclockwise. Value: " + encoderValue);
        
    }

}
