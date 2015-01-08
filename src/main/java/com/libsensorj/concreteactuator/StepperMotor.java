/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  IFBA
 * PROJECT       :  libsensorj
 * FILENAME      :  StepperMotor.java  
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
package com.libsensorj.concreteactuator;

import com.libsensorj.interfaces.IActuator;
import com.pi4j.component.motor.MotorState;
import com.pi4j.component.motor.StepperMotorBase;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;

/**
 * The Class StepperMotor.
 */
public class StepperMotor extends StepperMotorBase implements IActuator {

    // internal class members
    /** The pins. */
    private GpioPinDigitalOutput pins[];
    
    /** The on state. */
    private PinState onState = PinState.HIGH;
    
    /** The off state. */
    private PinState offState = PinState.LOW;
    
    /** The current state. */
    private MotorState currentState = MotorState.STOP;
    
    /** The control thread. */
    private GpioStepperMotorControl controlThread = new GpioStepperMotorControl();
    
    /** The sequence index. */
    private int sequenceIndex = 0;

    /**
     * using this constructor requires that the consumer define the STEP ON and
     * STEP OFF pin states.
     *
     * @param pins            GPIO digital output pins for each controller in the stepper
     *            motor
     * @param onState            pin state to set when MOTOR STEP is ON
     * @param offState            pin state to set when MOTOR STEP is OFF
     */
    public StepperMotor(GpioPinDigitalOutput pins[], PinState onState,
            PinState offState) {
        this.pins = pins;
        this.onState = onState;
        this.offState = offState;

        for (GpioPinDigitalOutput pin : pins) {
            pin.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
        }

    }

    /**
     * default constructor; using this constructor assumes that: (1) a pin state
     * of HIGH is MOTOR STEP ON (2) a pin state of LOW is MOTOR STEP OFF.
     *
     * @param pins            GPIO digital output pins for each controller in the stepper
     *            motor
     */
    public StepperMotor(GpioPinDigitalOutput pins[]) {
        this.pins = pins;

        for (GpioPinDigitalOutput pin : pins) {
            pin.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
        }
    }

    /**
     * Return the current motor state.
     *
     * @return MotorState
     */
    @Override
    public MotorState getState() {
        return currentState;
    }

    /**
     * change the current stepper motor state.
     *
     * @param state            new motor state to apply
     */
    @Override
    public void setState(MotorState state) {

        switch (state) {
        case STOP: {
            motorStop();
            break;
        }
        case FORWARD: {
            // set internal tracking state
            motorStart(MotorState.FORWARD);
            break;
        }
        case REVERSE: {
            // set internal tracking state
            motorStart(MotorState.REVERSE);
            break;
        }
        default: {
            throw new UnsupportedOperationException("Cannot set motor state: "
                    + state.toString());
        }
        }
    }

    /**
     * Motor stop.
     */
    private void motorStop() {
        // set internal tracking state
        currentState = MotorState.STOP;

        // turn all GPIO pins to OFF state
        for (GpioPinDigitalOutput pin : pins) {
            pin.setState(offState);
        }
    }

    /**
     * Motor start.
     *
     * @param state the state
     */
    private void motorStart(MotorState state) {
        // set internal tracking state
        currentState = state;

        // start control thread if not already running
        if (!controlThread.isAlive()) {
            controlThread = new GpioStepperMotorControl();
            controlThread.start();
        }

    }

    /**
     * The Class GpioStepperMotorControl.
     */
    private class GpioStepperMotorControl extends Thread {
        
        /* (non-Javadoc)
         * @see java.lang.Thread#run()
         */
        public void run() {

            // continuous loop until stopped
            while (currentState != MotorState.STOP) {

                // control direction
                if (currentState == MotorState.FORWARD) {
                    doStep(true);
                } else if (currentState == MotorState.REVERSE) {
                    doStep(false);
                }
            }

            // turn all GPIO pins to OFF state
            for (GpioPinDigitalOutput pin : pins) {
                pin.setState(offState);
            }
        }
    }

    /* (non-Javadoc)
     * @see com.pi4j.component.motor.StepperMotorBase#step(long)
     */
    @Override
    public void step(long steps) {
        // validate parameters
        if (steps == 0) {
            setState(MotorState.STOP);
            return;
        }

        // perform step in positive or negative direction from current position
        if (steps > 0) {
            for (long index = 1; index <= steps; index++) {
                doStep(true);
            }
        } else {
            for (long index = steps; index < 0; index++) {
                doStep(false);
            }
        }

        // stop motor movement
        this.stop();
    }

    /**
     * this method performs the calculations and work to control the GPIO pins
     * to move the stepper motor forward or reverse.
     *
     * @param forward the forward
     */
    private void doStep(boolean forward) {

        // increment or decrement sequence
        if (forward) {
            sequenceIndex++;
        } else {
            sequenceIndex--;
        }

        // check sequence bounds; rollover if needed
        if (sequenceIndex >= stepSequence.length) {
            sequenceIndex = 0;
        } else if (sequenceIndex < 0) {
            sequenceIndex = (stepSequence.length - 1);
        }
        // start cycling GPIO pins to move the motor forward or reverse
        for (int pinIndex = 0; pinIndex < pins.length; pinIndex++) {
            // apply step sequence
            double nib = Math.pow(2, pinIndex);
            if ((stepSequence[sequenceIndex] & (int) nib) > 0) {
                pins[pinIndex].setState(onState);
            } else {
                pins[pinIndex].setState(offState);
            }
        }
        try {
            Thread.sleep(stepIntervalMilliseconds, stepIntervalNanoseconds);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }
}