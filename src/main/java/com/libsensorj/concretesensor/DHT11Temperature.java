package com.libsensorj.concretesensor;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.temperature.TemperatureConversion;
import com.pi4j.temperature.TemperatureScale;
import com.libsensorj.interfaces.ISensor;

public class DHT11Temperature implements ISensor {

    private static final String TEMP_STR = "Temperature =";
    private String lastValue;
    private long lastCheck;
    private final int gpioPin;
    private static final int DEFAULT_PIN = 4;
    private static final Logger LOGGER = LogManager
            .getLogger(DHT11Temperature.class.getName());

    public DHT11Temperature(int gpioPin) {
        this.gpioPin = gpioPin;
        this.lastCheck = System.currentTimeMillis() - 3000;
    }

    public DHT11Temperature() {
        this.gpioPin = DEFAULT_PIN;
        this.lastCheck = System.currentTimeMillis() - 3000;
    }

    @Override
    public void getInstance() {

        // create gpio controller
        final GpioController gpio = GpioFactory.getInstance();

        // provision gpio pin #04 as an input pin with its internal pull down
        // resistor enabled
        final GpioPinDigitalInput dht11Temp = gpio.provisionDigitalInputPin(
                RaspiPin.GPIO_04, PinPullResistance.PULL_DOWN);

        // create and register gpio pin listener
        dht11Temp.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(
                    GpioPinDigitalStateChangeEvent event) {
                // display pin state on console
                LOGGER.info(" --> GPIO PIN STATE CHANGE: "
                        + event.getPin() + " = " + event.getState());
            }
        });

        // return instance;

    }

    private void checkForUpdates() {
        long now = System.currentTimeMillis();
        if (now - lastCheck > 3000) {
            String newValues = readValues();
            if (newValues.indexOf('%') > 0) {
                lastValue = newValues;
                lastCheck = now;
            }
        }
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
        checkForUpdates();
        switch (from) {

        case FARENHEIT:
            return TemperatureConversion
                    .convertCelsiusToFarenheit(parseTemperature(lastValue));
        case CELSIUS:
            return parseTemperature(lastValue);
        case KELVIN:
            return TemperatureConversion
                    .convertCelsiusToKelvin(parseTemperature(lastValue));
        default:
            throw new RuntimeException("Invalid temperature conversion");
        }
    }

    private double parseTemperature(String value) {
        if (value == null) {
            return Double.MIN_VALUE;
        }
        return Double.parseDouble(value.substring(value.indexOf(TEMP_STR)
                + TEMP_STR.length(), value.indexOf('*')));
    }

    private String readValues() {
        String result = "";
        try {
            Process p = Runtime.getRuntime().exec(
                    String.format("Adafruit_DHT %d", gpioPin));
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    p.getInputStream()));
            String line = null;
            line = in.readLine();
            while (line != null) {
                result += line;
                line = in.readLine();
            }
        } catch (Exception e) {

            LOGGER.error("Exception: Could not read the sensor at pin %d",
                    gpioPin + e.getMessage(), e);
            //e.printStackTrace();
            return null;
        }
        return result;
    }

}
