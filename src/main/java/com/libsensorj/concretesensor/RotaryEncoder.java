/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  IFBA
 * PROJECT       :  libsensorj
 * FILENAME      :  RotaryEncoder.java  
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
package com.libsensorj.concretesensor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.libsensorj.interfaces.ISensor;
import com.libsensorj.listeners.RotaryEncoderListener;
import com.libsensorj.utils.LibPins;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

/**
 * The Class RotaryEncoder.
 */
public class RotaryEncoder implements ISensor{
    
    /** The input a. */
    private final GpioPinDigitalInput inputA;
    
    /** The input b. */
    private final GpioPinDigitalInput inputB;
    
    /** The gpio. */
    private final GpioController gpio;
    
    /** The Constant DEFAULT_PIN_A. */
    private static final Pin DEFAULT_PIN_A = RaspiPin.GPIO_10;
    
    /** The Constant DEFAULT_PIN_B. */
    private static final Pin DEFAULT_PIN_B = RaspiPin.GPIO_08;
    
    /** The Constant DEFAULT_INITIAL_VALUE. */
    private static final long DEFAULT_INITIAL_VALUE = 0; 
    
    /** The encoder value. */
    private long encoderValue = 0;
    
    /** The last encoded. */
    private int lastEncoded = 0;
    
    /** The first pass. */
    private boolean firstPass = true;
    
    /** The listener. */
    private RotaryEncoderListener listener;
    
    // based on [lastEncoded][encoded] lookup
    /** The Constant STATE_TABLE. */
    private static final int STATE_TABLE[][]= {
        {0, 1, 1, -1},
        {-1, 0, 1, -1},
        {-1, 1, 0, -1},
        {-1, 1, 1, 0}
    };
    
    /** The Constant LOGGER. */
    private static final Logger LOGGER = LogManager.getLogger(RotaryEncoder.class.getName());
    
    
    /**
     * Instantiates a new rotary encoder.
     */
    public RotaryEncoder() {
        this(DEFAULT_PIN_A,DEFAULT_PIN_B,DEFAULT_INITIAL_VALUE);
    }

    
    /**
     * Instantiates a new rotary encoder.
     *
     * @param pinA the pin a
     * @param pinB the pin b
     */
    public RotaryEncoder(int pinA, int pinB) {
        this(LibPins.getPin(pinA),LibPins.getPin(pinB),DEFAULT_INITIAL_VALUE);
    }
    
    /**
     * Instantiates a new rotary encoder.
     *
     * @param pinA the pin a
     * @param pinB the pin b
     * @param initalValue the inital value
     */
    public RotaryEncoder(int pinA, int pinB, long initalValue) {
        this(LibPins.getPin(pinA),LibPins.getPin(pinB), initalValue);
    }
    
    /**
     * Instantiates a new rotary encoder.
     *
     * @param pinA the pin a
     * @param pinB the pin b
     * @param initalValue the inital value
     */
    public RotaryEncoder(Pin pinA, Pin pinB, long initalValue) {
 
        encoderValue = initalValue;
        gpio = GpioFactory.getInstance();
 
        inputA = gpio.provisionDigitalInputPin(pinA, "PinA", PinPullResistance.PULL_UP);
        inputB = gpio.provisionDigitalInputPin(pinB, "PinB", PinPullResistance.PULL_UP);
        
        GpioPinListenerDigital inputAListener = new GpioPinListenerDigital() {
 
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent gpdsce) {
                
                int stateA = gpdsce.getState().getValue();
                int stateB = inputB.getState().getValue();
                calcEncoderValue(stateA, stateB);
                LOGGER.info("{0}{1} encodedValue: {2}", new Object[]{stateA, stateB, encoderValue});
            }
        };
        
        inputA.addListener(inputAListener);
 
        // configure the pins shutdown behavior; these settings will be 
        // automatically applied to the pin when the application is terminated 
        inputA.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
        inputB.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                LOGGER.info("RotarySwitch: Shutting down....");
                if (gpio != null) {
                    gpio.removeAllListeners();
                    gpio.shutdown();
                }
            }
        });
        LOGGER.info("RotarySwitch initialised on pinA {0} and pinB {1}", 
               ((Object[]) new String[]{pinA.getName(), pinB.getName()}));
    }
        
    /**
     * Gets the value.
     *
     * @return the value
     */
    public long getValue() {
        return encoderValue;
    }
    
    /**
     * Sets the listener.
     *
     * @param listener the new listener
     */
    public void setListener(RotaryEncoderListener listener) {
        this.listener = listener;
    }
    
    /**
     * Calc encoder value.
     *
     * @param stateA the state a
     * @param stateB the state b
     */
    private void calcEncoderValue(int stateA, int stateB) {
        
        // converting the 2 pin value to single number to end up with 00, 01, 10 or 11
        int encoded = (stateA << 1) | stateB;
        
        if (firstPass) {
            firstPass = false;
        } else {
            // going up states, 01, 11
            // going down states 00, 10
            int state = STATE_TABLE[lastEncoded][encoded];
            encoderValue += state;
            if (listener != null) {
                if (state == -1) {
                    listener.turnedCounterclockwise(encoderValue);
                }
                if (state == 1) {
                    listener.turnedClockwise(encoderValue);
                }
            }
        }
        
        lastEncoded = encoded;
    }

    /* (non-Javadoc)
     * @see com.libsensorj.interfaces.ISensor#getInstance()
     */
    @Override
    public void getInstance() {
        // 
    }
}
