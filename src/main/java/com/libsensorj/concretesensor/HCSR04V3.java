package com.libsensorj.concretesensor;

import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;

public class HCSR04V3 {

    GpioPinDigitalOutput firepulse;
    GpioPinDigitalInput result_pin;
    
    public HCSR04V3(GpioPinDigitalOutput trigger, GpioPinDigitalInput result_pin) {
        this.firepulse = trigger;
        this.result_pin = result_pin;
      }
    
    /**
     * Trigger the Range Sensor and return the result
     */
    public double getRange() {
      System.out.println("Range Sensor Triggered");

      long start = 0;
      long diff = 0;

      try {
        firepulse.high();
        Thread.sleep(10);
        firepulse.low();

        while (result_pin.isLow()) {
          start = System.nanoTime();
        }

        while (result_pin.isHigh()) {
        }

        diff = (System.nanoTime() - start) / 58000;

        return diff;
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      return -1;
    }
}
