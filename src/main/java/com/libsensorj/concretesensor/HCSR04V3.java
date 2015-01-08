package com.libsensorj.concretesensor;

import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;

public class HCSR04V3 {

    GpioPinDigitalOutput trigger;
    GpioPinDigitalInput echo;
    
    public HCSR04V3(GpioPinDigitalOutput trigger, GpioPinDigitalInput echo) {
        this.trigger = trigger;
        this.echo = echo;
      }
    
    /**
     * Trigger the Range Sensor and return the result
     */
    public double getRange() {
      System.out.println("inside getrange. Range Sensor Triggered");

      long start = 0;
      long diff = 0;

      try {
        trigger.high();
        System.out.println("trigger is high = " + trigger.isHigh());
        Thread.sleep(10);
        trigger.low();
        System.out.println("trigger is low = " + trigger.isLow());

        while (echo.isLow()) {
          start = System.nanoTime();
        }

        System.out.println("fora do primeiro while");
        while (echo.isHigh()) {
        }

        diff = (System.nanoTime() - start) / 58000;
        System.out.println("diff calculado: " + diff);

        return diff;
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      return -1;
    }
}
