/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  IFBA
 * PROJECT       :  libsensorj
 * FILENAME      :  HCSR04V2.java  
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

import java.util.concurrent.TimeoutException;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

/**
 * The Class HCSR04V2.
 */
public class HCSR04V2 {

    /** The Constant SOUND_SPEED. */
    private final static float SOUND_SPEED = 340.29f; // speed of sound in m/s

    /** The Constant TRIG_DURATION_IN_MICROS. */
    private final static int TRIG_DURATION_IN_MICROS = 10; // trigger duration

    /** The Constant TIMEOUT. */
    private final static int TIMEOUT = 2100;

    /** The Constant GPIO. */
    private final static GpioController GPIO = GpioFactory.getInstance();

    /** The echo pin. */
    private final GpioPinDigitalInput echoPin;

    /** The trig pin. */
    private final GpioPinDigitalOutput trigPin;

    /**
     * Instantiates a new HCS r04 v2.
     *
     * @param echoPin
     *            the echo pin
     * @param trigPin
     *            the trig pin
     */
    public HCSR04V2(Pin echoPin, Pin trigPin) {
        this.echoPin = GPIO.provisionDigitalInputPin(echoPin);
        this.trigPin = GPIO.provisionDigitalOutputPin(trigPin);
        this.trigPin.low();

        // create and register GPIO pin listener
        this.echoPin.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(
                    GpioPinDigitalStateChangeEvent event) {
                // display pin state on console
                System.out.println(" --> GPIO ECHO PIN STATE CHANGE: "
                        + event.getPin() + " = " + event.getState());
            }

        });

        this.trigPin.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(
                    GpioPinDigitalStateChangeEvent event) {
                // display pin state on console
                System.out.println(" --> GPIO TRIGGER PIN STATE CHANGE: "
                        + event.getPin() + " = " + event.getState());
            }
        });

        // configure the pins shutdown behavior; these settings will be
        // automatically applied to the pin when the application is terminated
        this.echoPin.setShutdownOptions(true, PinState.LOW,
                PinPullResistance.OFF);
        this.trigPin.setShutdownOptions(true, PinState.LOW,
                PinPullResistance.OFF);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                System.out.println("Oops!");
                GPIO.shutdown();
                System.out.println("Exiting nicely.");
            }
        });
    }

    /*
     * This method returns the distance measured by the sensor in cm
     * 
     * @throws TimeoutException if a timeout occurs
     */
    /**
     * Measure distance.
     *
     * @return the float
     * @throws TimeoutException
     *             the timeout exception
     */
    public float measureDistance() throws TimeoutException {
        System.out
                .println("dentro do measureDistance. chamando método triggerSensor");
        this.triggerSensor();
        System.out
                .println("dentro do measureDistance.retornou do metodo triggerSensor, chamando método waitForSignal");
        this.waitForSignal();
        System.out
                .println("dentro do measureDistance.retornou do metodo waitForSignal");
        long duration = this.measureSignal();
        System.out.println("dentro do measureDistance.duration: " + duration);
        System.out.println("dentro do measureDistance. valor de retorno: "
                + (duration * SOUND_SPEED / (2 * 10000)));
        return duration * SOUND_SPEED / (2 * 10000);
    }

    /**
     * Put a high on the trig pin for TRIG_DURATION_IN_MICROS.
     */
    private void triggerSensor() {
        try {
            this.trigPin.high();
            System.out.println("inside triggerSensor. trigpin is hight = "
                    + trigPin.isHigh());
            Thread.sleep(0, TRIG_DURATION_IN_MICROS * 1000);
            this.trigPin.low();
            System.out.println("inside triggerSensor. trigpin is low = "
                    + trigPin.isLow());
        } catch (InterruptedException ex) {
            System.err.println("Interrupt during trigger");
        }
    }

    /**
     * Wait for a high on the echo pin.
     *
     * @throws TimeoutException
     *             the timeout exception
     */
    private void waitForSignal() throws TimeoutException {
        System.out.println("inside waitforsingnal");
        int countdown = TIMEOUT;
        System.out.println("inside waitforsingnal. entrando no while");
        while (this.echoPin.isLow() && countdown > 0) {
            countdown--;

        }
        System.out.println("inside waitforsingnal.saiu do while ehcopin.islow="
                + echoPin.isLow() + " and countdown:" + countdown);
        if (countdown <= 0) {
            throw new TimeoutException("Timeout waiting for signal start");
        }
    }

    /**
     * Measure signal.
     *
     * @return the duration of the signal in micro seconds
     * @throws TimeoutException
     *             the timeout exception
     */
    private long measureSignal() throws TimeoutException {
        System.out.println("inside measureSignal");
        int countdown = TIMEOUT;

        long start = System.nanoTime();
        System.out.println("inside measuresingnal. entrando no while");
        while (this.echoPin.isHigh() && countdown > 0) {
            countdown--;
        }
        System.out
                .println("inside measuresingnal.saiu do while ehcopin.ishigh="
                        + echoPin.isHigh() + " and countdown:" + countdown);
        long end = System.nanoTime();

        if (countdown <= 0) {
            throw new TimeoutException("Timeout waiting for signal end");
        }
        System.out.println("retornando o valor em microsegundos:"
                + (long) Math.ceil((end - start) / 1000.0));
        return (long) Math.ceil((end - start) / 1000.0); // Return micro seconds
    }
}
