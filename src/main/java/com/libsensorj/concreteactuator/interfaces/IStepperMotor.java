/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  IFBA
 * PROJECT       :  libsensorj
 * FILENAME      :  IStepperMotor.java  
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
package com.libsensorj.concreteactuator.interfaces;

import com.pi4j.component.motor.Motor;

/**
 * The Interface IStepperMotor.
 */
public interface IStepperMotor extends Motor {

    /**
     * Gets the steps per revolution.
     *
     * @return the steps per revolution
     */
    float getStepsPerRevolution();
    
    /**
     * Sets the steps per revolution.
     *
     * @param steps the new steps per revolution
     */
    void setStepsPerRevolution(int steps);    
    
    /**
     * Sets the step interval.
     *
     * @param milliseconds the new step interval
     */
    void setStepInterval(long milliseconds);
    
    /**
     * Sets the step interval.
     *
     * @param milliseconds the milliseconds
     * @param nanoseconds the nanoseconds
     */
    void setStepInterval(long milliseconds, int nanoseconds);
    
    /**
     * Sets the step sequence.
     *
     * @param sequence the new step sequence
     */
    void setStepSequence(byte[] sequence);
    
    /**
     * Gets the step sequence.
     *
     * @return the step sequence
     */
    byte[] getStepSequence();
    
    /**
     * Rotate.
     *
     * @param revolutions the revolutions
     */
    void rotate(double revolutions);
    
    /**
     * Step.
     *
     * @param steps the steps
     */
    void step(long steps);
}