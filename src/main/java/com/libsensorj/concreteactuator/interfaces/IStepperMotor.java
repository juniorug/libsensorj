package com.libsensorj.concreteactuator.interfaces;

import com.pi4j.component.motor.Motor;

public interface IStepperMotor extends Motor {

    float getStepsPerRevolution();
    void setStepsPerRevolution(int steps);    
    void setStepInterval(long milliseconds);
    void setStepInterval(long milliseconds, int nanoseconds);
    void setStepSequence(byte[] sequence);
    byte[] getStepSequence();
    void rotate(double revolutions);
    void step(long steps);
}