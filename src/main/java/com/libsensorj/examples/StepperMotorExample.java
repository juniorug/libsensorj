package com.libsensorj.examples;

import com.libsensorj.concreteactuator.StepperMotor;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

public class StepperMotorExample {

    public static void main(String[] args) throws InterruptedException {
        System.out
                .println("<--Pi4J--> GPIO Stepper Motor Example ... started.");

        // create gpio controller
        final GpioController gpio = GpioFactory.getInstance();

        // provision gpio pins #00 to #03 as output pins and ensure in LOW state
        final GpioPinDigitalOutput[] pins = {
                gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, PinState.LOW),
                gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, PinState.LOW),
                gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, PinState.LOW),
                gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03, PinState.LOW) };

        // this will ensure that the motor is stopped when the program
        // terminates
        gpio.setShutdownOptions(true, PinState.LOW, pins);

        // create motor component
        StepperMotor motor = new StepperMotor(pins);

        // @see http://www.lirtex.com/robotics/stepper-motor-controller-circuit/
        // for additional details on stepping techniques

        // create byte array to demonstrate a single-step sequencing
        // (This is the most basic method, turning on a single electromagnet
        // every time.
        // This sequence requires the least amount of energy and generates the
        // smoothest movement.)
        byte[] singleStepSequence = new byte[4];
        singleStepSequence[0] = (byte) 0b0001;
        singleStepSequence[1] = (byte) 0b0010;
        singleStepSequence[2] = (byte) 0b0100;
        singleStepSequence[3] = (byte) 0b1000;

        // create byte array to demonstrate a double-step sequencing
        // (In this method two coils are turned on simultaneously. This method
        // does not generate
        // a smooth movement as the previous method, and it requires double the
        // current, but as
        // return it generates double the torque.)
        byte[] doubleStepSequence = new byte[4];
        doubleStepSequence[0] = (byte) 0b0011;
        doubleStepSequence[1] = (byte) 0b0110;
        doubleStepSequence[2] = (byte) 0b1100;
        doubleStepSequence[3] = (byte) 0b1001;

        // create byte array to demonstrate a half-step sequencing
        // (In this method two coils are turned on simultaneously. This method
        // does not generate
        // a smooth movement as the previous method, and it requires double the
        // current, but as
        // return it generates double the torque.)
        byte[] halfStepSequence = new byte[8];
        halfStepSequence[0] = (byte) 0b0001;
        halfStepSequence[1] = (byte) 0b0011;
        halfStepSequence[2] = (byte) 0b0010;
        halfStepSequence[3] = (byte) 0b0110;
        halfStepSequence[4] = (byte) 0b0100;
        halfStepSequence[5] = (byte) 0b1100;
        halfStepSequence[6] = (byte) 0b1000;
        halfStepSequence[7] = (byte) 0b1001;

        // define stepper parameters before attempting to control motor
        // anything lower than 2 ms does not work for my sample motor using
        // single step sequence
        motor.setStepInterval(2);
        motor.setStepSequence(singleStepSequence);

        // There are 32 steps per revolution on my sample motor,
        // and inside is a ~1/64 reduction gear set.
        // Gear reduction is actually:
        // (32/9)/(22/11)x(26/9)x(31/10)=63.683950617
        // This means is that there are really 32*63.683950617 steps per
        // revolution
        // = 2037.88641975 ~ 2038 steps!
        motor.setStepsPerRevolution(2038);

        // test motor control : STEPPING FORWARD
        System.out.println("   Motor FORWARD for 2038 steps.");
        motor.step(2038);
        System.out.println("   Motor STOPPED for 2 seconds.");
        Thread.sleep(2000);

        // test motor control : STEPPING REVERSE
        System.out.println("   Motor REVERSE for 2038 steps.");
        motor.step(-2038);
        System.out.println("   Motor STOPPED for 2 seconds.");
        Thread.sleep(2000);

        // test motor control : ROTATE FORWARD
        System.out.println("   Motor FORWARD for 2 revolutions.");
        motor.rotate(2);
        System.out.println("   Motor STOPPED for 2 seconds.");
        Thread.sleep(2000);

        // test motor control : ROTATE REVERSE
        System.out.println("   Motor REVERSE for 2 revolutions.");
        motor.rotate(-2);
        System.out.println("   Motor STOPPED for 2 seconds.");
        Thread.sleep(2000);

        // test motor control : TIMED FORWARD
        System.out.println("   Motor FORWARD for 5 seconds.");
        motor.forward(5000);
        System.out.println("   Motor STOPPED for 2 seconds.");
        Thread.sleep(2000);

        // test motor control : TIMED REVERSE
        System.out.println("   Motor REVERSE for 5 seconds.");
        motor.reverse(5000);
        System.out.println("   Motor STOPPED for 2 seconds.");
        Thread.sleep(2000);

        // test motor control : ROTATE FORWARD with different timing and
        // sequence
        System.out
                .println("   Motor FORWARD with slower speed and higher torque for 1 revolution.");
        motor.setStepSequence(doubleStepSequence);
        motor.setStepInterval(10);
        motor.rotate(1);
        System.out.println("   Motor STOPPED.");

        // final stop to ensure no motor activity
        motor.stop();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                System.out.println("Oops!");
                gpio.shutdown();
                System.out.println("Exiting nicely.");
            }
        });
    }
}
