package com.libsensorj.concreteactuator;

import com.libsensorj.interfaces.IActuator;
import com.pi4j.component.motor.MotorState;
import com.pi4j.component.motor.StepperMotorBase;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;

public class StepperMotor extends StepperMotorBase implements IActuator {

    // internal class members
    private GpioPinDigitalOutput pins[];
    private PinState onState = PinState.HIGH;
    private PinState offState = PinState.LOW;
    private MotorState currentState = MotorState.STOP;
    private GpioStepperMotorControl controlThread = new GpioStepperMotorControl();
    private int sequenceIndex = 0;

    /**
     * using this constructor requires that the consumer define the STEP ON and
     * STEP OFF pin states
     * 
     * @param pins
     *            GPIO digital output pins for each controller in the stepper
     *            motor
     * @param onState
     *            pin state to set when MOTOR STEP is ON
     * @param offState
     *            pin state to set when MOTOR STEP is OFF
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
     * of HIGH is MOTOR STEP ON (2) a pin state of LOW is MOTOR STEP OFF
     * 
     * @param pins
     *            GPIO digital output pins for each controller in the stepper
     *            motor
     */
    public StepperMotor(GpioPinDigitalOutput pins[]) {
        this.pins = pins;

        for (GpioPinDigitalOutput pin : pins) {
            pin.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
        }
    }

    /**
     * Return the current motor state
     * 
     * @return MotorState
     */
    @Override
    public MotorState getState() {
        return currentState;
    }

    /**
     * change the current stepper motor state
     * 
     * @param state
     *            new motor state to apply
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

    private void motorStop() {
        // set internal tracking state
        currentState = MotorState.STOP;

        // turn all GPIO pins to OFF state
        for (GpioPinDigitalOutput pin : pins) {
            pin.setState(offState);
        }
    }

    private void motorStart(MotorState state) {
        // set internal tracking state
        currentState = state;

        // start control thread if not already running
        if (!controlThread.isAlive()) {
            controlThread = new GpioStepperMotorControl();
            controlThread.start();
        }

    }

    private class GpioStepperMotorControl extends Thread {
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
     * to move the stepper motor forward or reverse
     * 
     * @param forward
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