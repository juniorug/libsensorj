package com.libsensorj.concreteSensor;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.libsensorj.interfaces.ISensor;

public class DHT11Temperature implements ISensor {

    private final static String TEMP_STR = "Temperature =";
    private String lastValue;
    private long lastCheck;
    private final int gpioPin;
    private static final int DEFAULT_PIN = 4;
    private static final int FIVE = 5;
    private static final int NINE = 9;
    private static final int THIRTY_TWO = 32;
    private static final float KELVIN_CONSTANT = 273.15f;

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
                System.out.println(" --> GPIO PIN STATE CHANGE: "
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

    public synchronized float getTemperatureInCelsius() {
        checkForUpdates();
        return parseTemperature(lastValue);
    }

    public synchronized float getTemperatureInFahrenheit() {
        checkForUpdates();
        return ((parseTemperature(lastValue) * NINE) / FIVE) + THIRTY_TWO;
    }

    public synchronized float getTemperatureInKelvin() {
        checkForUpdates();
        return parseTemperature(lastValue) + KELVIN_CONSTANT;
    }

    private float parseTemperature(String value) {
        if (value == null) {
            return Float.MIN_VALUE;
        }
        return Float.parseFloat(value.substring(value.indexOf(TEMP_STR)
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
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.err.println(String.format(
                    "Could not read the sensor at pin %d", gpioPin));
            e.printStackTrace();
            return null;
        }
        return result;
    }

}
