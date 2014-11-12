package com.libsensorj.concretesensor;

import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.libsensorj.interfaces.ISensor;
import com.libsensorj.utils.LibPins;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalMultipurpose;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.temperature.TemperatureConversion;
import com.pi4j.temperature.TemperatureScale;

public class DHT11V3 implements ISensor {

    private static final Pin DEFAULT_PIN = RaspiPin.GPIO_04;
    private int temperature;
    private GpioPinDigitalMultipurpose dht11Pin;
    private static final Logger LOGGER = LogManager.getLogger(DHT11V3.class
            .getName());

    public DHT11V3() {
        final GpioController gpio = GpioFactory.getInstance();
        dht11Pin = gpio.provisionDigitalMultipurposePin(DEFAULT_PIN,
                PinMode.DIGITAL_INPUT, PinPullResistance.PULL_UP);
    }

    public DHT11V3(int pin) {
        final GpioController gpio = GpioFactory.getInstance();
        dht11Pin = gpio.provisionDigitalMultipurposePin(LibPins.getPin(pin),
                PinMode.DIGITAL_INPUT, PinPullResistance.PULL_UP);
    }

    // returnvalues:
    // 0 : OK - returns temperature readed
    // -1 : checksum error
    // -2 : timeout
    public double readValue() {
        byte[] bits = new byte[] { 0, 0, 0, 0, 0 }; // empty buffer
        byte cnt = 7;
        byte idx = 0;

        try {
            // REQUEST SAMPLE
            dht11Pin.setMode(PinMode.DIGITAL_OUTPUT);
            dht11Pin.low();
            Thread.sleep(18);
            dht11Pin.high();
            TimeUnit.MICROSECONDS.sleep(40);
            dht11Pin.setMode(PinMode.DIGITAL_INPUT);
        } catch (InterruptedException e) {
            LOGGER.error("InterruptedException: " + e.getMessage(), e);
        }
        // ACKNOWLEDGE or TIMEOUT
        int loopCnt = 10000;
        while (dht11Pin.isLow()) {
            if (loopCnt-- == 0) {
                return -2;
            }
        }
        loopCnt = 10000;
        while (dht11Pin.isHigh()) {
            if (loopCnt-- == 0) {
                return -2;
            }
        }
        // READ OUTPUT - 40 BITS => 5 BYTES or TIMEOUT
        for (int i = 0; i < 40; i++) {
            loopCnt = 10000;
            while (dht11Pin.isLow()) {
                if (loopCnt-- == 0) {
                    return -2;
                }
            }
            long startTime = System.nanoTime(); // ns

            loopCnt = 10000;
            while (dht11Pin.isHigh()) {
                if (loopCnt-- == 0) {
                    return -2;
                }
            }
            // 40 microseconds
            if ((System.nanoTime() - startTime) > 40000) {
                bits[idx] |= (1 << cnt);
            }
            if (cnt == 0) // next byte?
            {
                cnt = 7; // restart at MSB
                idx++; // next byte!
            } else {
                cnt--;
            }
        }

        // WRITE TO RIGHT VARS
        // as bits[1] and bits[3] are always zero they are omitted in formulas.
        temperature = bits[2];

        byte sum = (byte) (bits[0] + bits[2]);

        if (bits[4] != sum) {
            return -1;
        }
        // processing OK!
        return temperature;

    }

    public synchronized double getTemperatureInCelsius() {
        return getTemperature(TemperatureScale.CELSIUS);
    }

    public synchronized double getTemperatureInFahrenheit() {
        return getTemperature(TemperatureScale.FARENHEIT);

    }

    public synchronized double getTemperatureInKelvin() {
        return getTemperature(TemperatureScale.KELVIN);
    }

    private double getTemperature(TemperatureScale from) {
        switch (from) {

        case FARENHEIT:
            return TemperatureConversion.convertCelsiusToFarenheit(readValue());
        case CELSIUS:
            return (readValue());
        case KELVIN:
            return TemperatureConversion.convertCelsiusToKelvin(readValue());
        default:
            throw new RuntimeException("Invalid temperature conversion");
        }
    }


    @Override
    public void getInstance() {

    }

}
