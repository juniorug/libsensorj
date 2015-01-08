/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  IFBA
 * PROJECT       :  libsensorj
 * FILENAME      :  HCSR04V3Example.java  
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

/**
 * The Class HCSR04V3Example.
 */
public class HCSR04V3Example {

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		Gpio.wiringPiSetup();
		final GpioController gpio = GpioFactory.getInstance();

		// range sensor pins
		/*
		 * GpioPinDigitalOutput trigger =
		 * gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "Sensor Trigger",
		 * PinState.LOW);
		 * 
		 * GpioPinDigitalInput echo =
		 * gpio.provisionDigitalInputPin(RaspiPin.GPIO_04, "Sensor Result",
		 * PinPullResistance.PULL_DOWN);
		 */

		GpioPinDigitalOutput trigger = gpio.provisionDigitalOutputPin(
				RaspiPin.GPIO_01, "Sensor Trigger", PinState.LOW);

		GpioPinDigitalInput echo = gpio.provisionDigitalInputPin(
				RaspiPin.GPIO_02, "Sensor Result", PinPullResistance.PULL_DOWN);

		// configure the pins shutdown behavior; these settings will be 
        // automatically applied to the pin when the application is terminated 
        trigger.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
        echo.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				System.out.println("Oops!");
				gpio.shutdown();
				System.out.println("Exiting nicely.");
			}
		});
		// Create the range sensor
		HCSR04V3 rangesensor = new HCSR04V3(trigger, echo);

		do {
			// Get the range
			double distance = rangesensor.getRange();

			System.out.println("RangeSensorresult =" + distance + "cm");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			    System.out.println(e.getMessage());
			}

		} while (true);

	}

}
