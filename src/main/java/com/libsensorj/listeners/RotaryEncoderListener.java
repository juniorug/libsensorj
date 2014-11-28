package com.libsensorj.listeners;

public interface RotaryEncoderListener {
    
    void turnedClockwise(long encoderValue);
    void turnedCounterclockwise(long encoderValue);

}
