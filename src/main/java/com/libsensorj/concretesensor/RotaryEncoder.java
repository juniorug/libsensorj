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
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

public class RotaryEncoder implements ISensor{
    
    private final GpioPinDigitalInput inputA;
    private final GpioPinDigitalInput inputB;
    private final GpioController gpio;
    private static final Pin DEFAULT_PIN_A = RaspiPin.GPIO_10;
    private static final Pin DEFAULT_PIN_B = RaspiPin.GPIO_08;
    private static final long DEFAULT_INITIAL_VALUE = 0; 
    private long encoderValue = 0;
    private int lastEncoded = 0;
    private boolean firstPass = true;
    
    private RotaryEncoderListener listener;
    
    // based on [lastEncoded][encoded] lookup
    private static final int stateTable[][]= {
        {0, 1, 1, -1},
        {-1, 0, 1, -1},
        {-1, 1, 0, -1},
        {-1, 1, 1, 0}
    };
    
    private static final Logger LOGGER = LogManager.getLogger(RotaryEncoder.class.getName());
    
    
    public RotaryEncoder() {
        this(DEFAULT_PIN_A,DEFAULT_PIN_B,DEFAULT_INITIAL_VALUE);
    }

    
    public RotaryEncoder(int pinA, int pinB) {
        this(LibPins.getPin(pinA),LibPins.getPin(pinB),DEFAULT_INITIAL_VALUE);
    }
    
    public RotaryEncoder(int pinA, int pinB, long initalValue) {
        this(LibPins.getPin(pinA),LibPins.getPin(pinB), initalValue);
    }
    
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
        
    public long getValue() {
        return encoderValue;
    }
    
    public void setListener(RotaryEncoderListener listener) {
        this.listener = listener;
    }
    
    private void calcEncoderValue(int stateA, int stateB) {
        
        // converting the 2 pin value to single number to end up with 00, 01, 10 or 11
        int encoded = (stateA << 1) | stateB;
        
        if (firstPass) {
            firstPass = false;
        } else {
            // going up states, 01, 11
            // going down states 00, 10
            int state = stateTable[lastEncoded][encoded];
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

    @Override
    public void getInstance() {
        // 
    }
}
