package com.libsensorj.concretesensor;

import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.libsensorj.utils.LibPins;
import com.pi4j.component.ObserveableComponentBase;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalMultipurpose;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.temperature.TemperatureConversion;
import com.pi4j.temperature.TemperatureScale;

public class DHT11 extends ObserveableComponentBase {

    private static final Pin DEFAULT_PIN = RaspiPin.GPIO_04;
    private static final int MAXTIMINGS = 85;
    private int[] dht11_dat = { 0, 0, 0, 0, 0 };
    private GpioPinDigitalMultipurpose dht11Pin;
    private static final Logger LOGGER = LogManager.getLogger(DHT11.class
            .getName());

    public DHT11() {
        final GpioController gpio = GpioFactory.getInstance();
        dht11Pin = gpio.provisionDigitalMultipurposePin(DEFAULT_PIN,
                PinMode.DIGITAL_INPUT, PinPullResistance.PULL_UP);
    }

    public DHT11(int pin) {
        final GpioController gpio = GpioFactory.getInstance();
        dht11Pin = gpio.provisionDigitalMultipurposePin(LibPins.getPin(pin),
                PinMode.DIGITAL_INPUT, PinPullResistance.PULL_UP);
    }

    public double readValue() {
        PinState laststate = PinState.HIGH;
        int j = 0;
        dht11_dat[0] = dht11_dat[1] = dht11_dat[2] = dht11_dat[3] = dht11_dat[4] = 0;
        StringBuilder value = new StringBuilder();
        try {

            dht11Pin.setMode(PinMode.DIGITAL_OUTPUT);
            dht11Pin.low();
            Thread.sleep(18);
            dht11Pin.high();
            TimeUnit.MICROSECONDS.sleep(40);
            dht11Pin.setMode(PinMode.DIGITAL_INPUT);

            for (int i = 0; i < MAXTIMINGS; i++) {
                int counter = 0;
                while (dht11Pin.getState() == laststate) {
                    counter++;
                    TimeUnit.MICROSECONDS.sleep(1);
                    if (counter == 255) {
                        break;
                    }
                }

                laststate = dht11Pin.getState();

                if (counter == 255) {
                    break;
                }

                /* ignore first 3 transitions */
                if ((i >= 4) && (i % 2 == 0)) {
                    /* shove each bit into the storage bytes */
                    dht11_dat[j / 8] <<= 1;
                    if (counter > 16) {
                        dht11_dat[j / 8] |= 1;
                    }
                    j++;
                }
            }
            // check we read 40 bits (8bit x 5 ) + verify checksum in the last
            // byte
            if ((j >= 40) && checkParity()) {
                value.append(dht11_dat[2]).append(".").append(dht11_dat[3]);
                LOGGER.info("temperature value readed: " + value.toString());
            }

        } catch (InterruptedException e) {

            LOGGER.error("InterruptedException: " + e.getMessage(), e);
        }
        if (value.toString().isEmpty()) {
            value.append(-1);
        }
        return Double.parseDouble(value.toString());
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

    private boolean checkParity() {
        return (dht11_dat[4] == ((dht11_dat[0] + dht11_dat[1] + dht11_dat[2] + dht11_dat[3]) & 0xFF));
    }

}
