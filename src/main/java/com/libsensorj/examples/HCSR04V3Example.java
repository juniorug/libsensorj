package com.libsensorj.examples;

import com.libsensorj.concretesensor.HCSR04V3;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.wiringpi.Gpio;

public class HCSR04V3Example {

	public static void main(String[] args) {
		Gpio.wiringPiSetup();
		final GpioController gpio = GpioFactory.getInstance();

		// range sensor pins
		/*
		 * GpioPinDigitalOutput sensor_trigger =
		 * gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "Sensor Trigger",
		 * PinState.LOW);
		 * 
		 * GpioPinDigitalInput sensor_result =
		 * gpio.provisionDigitalInputPin(RaspiPin.GPIO_04, "Sensor Result",
		 * PinPullResistance.PULL_DOWN);
		 */

		GpioPinDigitalOutput sensor_trigger = gpio.provisionDigitalOutputPin(
				RaspiPin.GPIO_01, "Sensor Trigger", PinState.LOW);

		GpioPinDigitalInput sensor_result = gpio.provisionDigitalInputPin(
				RaspiPin.GPIO_02, "Sensor Result", PinPullResistance.PULL_DOWN);

		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				System.out.println("Oops!");
				gpio.shutdown();
				System.out.println("Exiting nicely.");
			}
		});
		// Create the range sensor
		HCSR04V3 rangesensor = new HCSR04V3(sensor_trigger, sensor_result);

		do {
			// Get the range
			double distance = rangesensor.getRange();

			System.out.println("RangeSensorresult =" + distance + "cm");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		} while (true);

	}

}
