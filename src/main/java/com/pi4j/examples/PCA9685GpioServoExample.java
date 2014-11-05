package com.pi4j.examples;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Examples
 * FILENAME      :  PCA9685GpioServoExample.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2014 Pi4J
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pi4j.component.servo.Servo;
import com.pi4j.component.servo.impl.GenericServo;
import com.pi4j.component.servo.impl.GenericServo.Orientation;
import com.pi4j.component.servo.impl.PCA9685GpioServoProvider;
import com.pi4j.gpio.extension.pca.PCA9685GpioProvider;
import com.pi4j.gpio.extension.pca.PCA9685Pin;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinPwmOutput;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CFactory;

/**
 * Simple servo tester application demonstrating Pi4J's Servo component.
 * 
 * @author Christian Wehrli
 * @see Servo
 * @see com.pi4j.gpio.extension.pca.PCA9685GpioProvider
 */
public class PCA9685GpioServoExample {

    private static final Logger LOGGER = LogManager
            .getLogger(PCA9685GpioServoExample.class.getName());

    // ------------------------------------------------------------------------------------------------------------------
    // main
    // ------------------------------------------------------------------------------------------------------------------
    public static void main(String[] args) throws Exception {

        LOGGER.info("<--Pi4J--> PCA9685 Servo Tester Example ... started.");
        PCA9685GpioServoExample example = new PCA9685GpioServoExample();
        Scanner scanner = new Scanner(System.in);
        char command = ' ';
        while (command != 'x') {
            printUsage();
            command = readCommand(scanner);
            switch (command) {
            case 'c': // Choose Channel
                example.chooseChannel(scanner);
                break;
            case 'n': // Neutral Position
                example.approachNeutralPosition();
                break;
            case 'm': // Move
                example.move(scanner);
                break;
            case 's': // Sub Trim
                example.subtrim(scanner);
                break;
            case 'r': // Reverse
                example.reverse();
                break;
            case 't': // Travel (adjust endpoints)
                example.travel(scanner);
                break;
            case 'p': // Sweep
                example.sweep(scanner);
                break;
            case 'i': // Info
                example.info();
                break;
            case 'x': // Exit
                LOGGER.info("Servo Example - END.");
                break;
            case ' ':
                LOGGER.error("Invalid input.");
                break;
            default:
                LOGGER.error("Unknown command [" + command + "].");
                break;
            }
        }
    }

    private static char readCommand(Scanner scanner) {
        char result = ' ';
        String input = scanner.nextLine();
        if (!input.trim().isEmpty()) {
            result = input.trim().toLowerCase().charAt(0);
        }
        return result;
    }

    private static void printUsage() {
        LOGGER.info("");
        LOGGER.info("|- Commands ---------------------------------------------------------------------");
        LOGGER.info("| c : choose active servo channel                                                ");
        LOGGER.info("| n : neutral - approach neutral position                                        ");
        LOGGER.info("| m : move servo position                                                        ");
        LOGGER.info("| s : subtrim                                                                    ");
        LOGGER.info("| r : reverse servo direction                                                    ");
        LOGGER.info("| t : travel - adjust endpoints                                                  ");
        LOGGER.info("| p : sweep - continuously move between max left and max right position)         ");
        LOGGER.info("| i : info - provide info for all servo channels                                 ");
        LOGGER.info("| x : exit                                                                       ");
        LOGGER.info("|--------------------------------------------------------------------------------");
    }

    // ------------------------------------------------------------------------------------------------------------------
    // PCA9685GpioProvider
    // ------------------------------------------------------------------------------------------------------------------
    private final PCA9685GpioProvider gpioProvider;
    private final PCA9685GpioServoProvider gpioServoProvider;

    private final Servo[] servos;
    private int activeServo;

    public PCA9685GpioServoExample() throws Exception {
        gpioProvider = createProvider();

        // Define outputs in use for this example
        provisionPwmOutputs(gpioProvider);

        gpioServoProvider = new PCA9685GpioServoProvider(gpioProvider);

        servos = new Servo[16];

        // Provide servo on channel 0
        servos[0] = new GenericServo(
                gpioServoProvider.getServoDriver(PCA9685Pin.PWM_00),
                "Servo_1 (default settings)");

        // Provide servo on channel 1
        servos[1] = new GenericServo(
                gpioServoProvider.getServoDriver(PCA9685Pin.PWM_01),
                "Servo_2 (max. endpoints)");
        servos[1].setProperty(Servo.PROP_END_POINT_LEFT,
                Float.toString(Servo.END_POINT_MAX));
        servos[1].setProperty(Servo.PROP_END_POINT_RIGHT,
                Float.toString(Servo.END_POINT_MAX));

        // Provide servo on channel 2
        servos[2] = new GenericServo(
                gpioServoProvider.getServoDriver(PCA9685Pin.PWM_02),
                "Servo_3 (subtrim)");
        servos[2].setProperty(Servo.PROP_SUBTRIM,
                Float.toString(Servo.SUBTRIM_MAX_LEFT));

        // Provide servo on channel 3
        servos[3] = new GenericServo(
                gpioServoProvider.getServoDriver(PCA9685Pin.PWM_03),
                "Servo_4 (reverse)");
        servos[3].setProperty(Servo.PROP_IS_REVERSE, Boolean.toString(true));

        // Set active servo
        activeServo = 0;
    }

    public void chooseChannel(Scanner scanner) {
        LOGGER.info("");
        LOGGER.info("|- Choose channel ---------------------------------------------------------------");
        LOGGER.info("| Choose active servo channel [0..15]                                            ");
        LOGGER.info("| Example: 0<Enter>                                                              ");
        LOGGER.info("|--------------------------------------------------------------------------------");

        int channel = -1;
        boolean isValidChannel = false;
        while (!isValidChannel) {
            String input = null;
            try {
                input = scanner.nextLine();
                channel = Integer.parseInt(input);
                if (channel >= 0 && channel <= 15) {
                    isValidChannel = true;
                } else {
                    LOGGER.error("Unsupported servo channel [" + channel
                            + "], provide number between 0 and 15.");
                }
            } catch (NumberFormatException e) {
                LOGGER.error("Invalid input [" + input
                        + "], provide number between 0 and 15.", e);
            }
        }
        activeServo = channel;
        LOGGER.info("Active servo channel: " + activeServo);
    }

    public void approachNeutralPosition() {
        LOGGER.info("Approach neutral position");
        servos[activeServo].setPosition(0);
    }

    public void move(Scanner scanner) {
        LOGGER.info("");
        LOGGER.info("|- Move Position ----------------------------------------------------------------");
        LOGGER.info("| Move servo position to the left or to the right.                               ");
        LOGGER.info("| Example: l10<Enter> this would move the servo from its current position to the ");
        LOGGER.info("|          left by 10%                                                           ");
        LOGGER.info("| Example: r<Enter> this would move the servo from its current position to the   ");
        LOGGER.info("|          right by 1%                                                           ");
        LOGGER.info("| -> subsequent single <Enter> will repeat the previous command                  ");
        LOGGER.info("| -> max travel to either side is 100%                                           ");
        LOGGER.info("| Exit command: x<Enter>                                                         ");
        LOGGER.info("|--------------------------------------------------------------------------------");

        String command = null;
        while (!"x".equals(command)) {
            float currentPosition = servos[activeServo].getPosition();
            LOGGER.info("Current servo position: " + currentPosition);
            String input = scanner.nextLine();
            if (!input.trim().isEmpty()) {
                command = input.trim().toLowerCase();
            }
            if (command != null) {
                int sign;
                if (command.startsWith("l")) {
                    sign = -1;
                } else if (command.startsWith("r")) {
                    sign = 1;
                } else {
                    if (!"x".equals(command)) {
                        LOGGER.error("Unknown command [" + command + "].");
                        command = null;
                    }
                    continue;
                }

                int moveAmount = 1;
                try {
                    moveAmount = Integer.parseInt(command.substring(1));
                    if (moveAmount < 0 || moveAmount > 100) {
                        moveAmount = 1;
                        LOGGER.info("Move amount is out of range - defaulted to [1].");
                    }
                    LOGGER.info("Move amount is [" + moveAmount + "].");
                } catch (Exception e) {
                    LOGGER.info(
                            "Exception: Move amount defaulted to [1]."
                                    + e.getMessage(), e);
                }
                float newPosition = currentPosition + moveAmount * sign;
                if (newPosition < Servo.POS_MAX_LEFT) {
                    newPosition = Servo.POS_MAX_LEFT;
                    LOGGER.info("Max left position exceeded - set position to "
                            + Servo.POS_MAX_LEFT + "%");
                } else if (newPosition > Servo.POS_MAX_RIGHT) {
                    newPosition = Servo.POS_MAX_RIGHT;
                    LOGGER.info("Max right position exceeded - set position to "
                            + Servo.POS_MAX_RIGHT + "%");
                }
                servos[activeServo].setPosition(newPosition);
                command = (sign == 1 ? "r" : "l") + moveAmount;
            }
        }
    }

    public void subtrim(Scanner scanner) {
        LOGGER.info("");
        LOGGER.info("|- Subtrim, adjust servo neutral position ---------------------------------------");
        LOGGER.info("| Example: r<Enter> this would move the servos neutral position by one step to   ");
        LOGGER.info("|          the right                                                             ");
        LOGGER.info("| Example: l<Enter> this would move the servos neutral position by one step to   ");
        LOGGER.info("|          the left                                                              ");
        LOGGER.info("| -> subsequent single <Enter> will repeat the previous command                  ");
        LOGGER.info("| -> max adjustment to either side is 200 steps                                  ");
        LOGGER.info("| Exit command: x<Enter>                                                         ");
        LOGGER.info("|--------------------------------------------------------------------------------");
        LOGGER.info("| Current Servo position: "
                + servos[activeServo].getPosition() + "]             ");
        LOGGER.info("|--------------------------------------------------------------------------------");

        String command = null;
        while (!"x".equals(command)) {
            String propertySubtrim = servos[activeServo].getProperty(
                    Servo.PROP_SUBTRIM, Servo.PROP_SUBTRIM_DEFAULT);
            int currentSubtrim = Integer.parseInt(propertySubtrim);
            LOGGER.info("Current subtrim: " + currentSubtrim);
            String input = scanner.nextLine();
            if (!input.trim().isEmpty()) {
                command = input.trim().toLowerCase();
            }
            if (command != null) {
                int moveAmount;
                if (command.startsWith("l")) {
                    moveAmount = -1;
                } else if (command.startsWith("r")) {
                    moveAmount = 1;
                } else {
                    if (!"x".equals(command)) {
                        LOGGER.error("Unknown command [" + command + "].");
                        command = null;
                    }
                    continue;
                }

                float newSubtrim = currentSubtrim + moveAmount;
                if (newSubtrim < Servo.SUBTRIM_MAX_LEFT) {
                    newSubtrim = Servo.SUBTRIM_MAX_LEFT;
                    LOGGER.info("Max left subtrim exceeded - set value to "
                            + Servo.SUBTRIM_MAX_LEFT);
                } else if (newSubtrim > Servo.SUBTRIM_MAX_RIGHT) {
                    newSubtrim = Servo.SUBTRIM_MAX_RIGHT;
                    LOGGER.info("Max right subtrim exceeded - set value to "
                            + Servo.SUBTRIM_MAX_RIGHT);
                }
                servos[activeServo].setProperty(Servo.PROP_SUBTRIM,
                        Float.toString(newSubtrim));
            }
        }
    }

    public void reverse() {
        boolean isReverse = Boolean.parseBoolean(servos[activeServo]
                .getProperty(Servo.PROP_IS_REVERSE));
        Boolean newValue = isReverse ? Boolean.FALSE : Boolean.TRUE;
        servos[activeServo].setProperty(Servo.PROP_IS_REVERSE,
                newValue.toString());
        LOGGER.info("is reverse: " + newValue);
    }

    public void travel(Scanner scanner) {
        LOGGER.info("");
        LOGGER.info("|- Travel -----------------------------------------------------------------------");
        LOGGER.info("| Adjust endpoints.                                                              ");
        LOGGER.info("| Example: r125<Enter>  adjust RIGHT endpoint to 125                             ");
        LOGGER.info("| -> min: 0, max: 150, default 100                                               ");
        LOGGER.info("| Exit command: x<Enter>                                                         ");
        LOGGER.info("|--------------------------------------------------------------------------------");

        String command = null;
        while (!"x".equals(command)) {
            String propertyEndpointLeft = servos[activeServo].getProperty(
                    Servo.PROP_END_POINT_LEFT, Servo.PROP_END_POINT_DEFAULT);
            String propertyEndpointRight = servos[activeServo].getProperty(
                    Servo.PROP_END_POINT_RIGHT, Servo.PROP_END_POINT_DEFAULT);
            LOGGER.info("Current endpoints: LEFT [" + propertyEndpointLeft
                    + "], RIGHT [" + propertyEndpointRight + "]");

            String input = scanner.nextLine();
            if (!input.trim().isEmpty()) {
                command = input.trim().toLowerCase();
            }
            if (command != null) {
                String propertyToAdjust;
                if (command.startsWith("l")) {
                    propertyToAdjust = Servo.PROP_END_POINT_LEFT;
                } else if (command.startsWith("r")) {
                    propertyToAdjust = Servo.PROP_END_POINT_RIGHT;
                } else {
                    if (!"x".equals(command)) {
                        LOGGER.error("Unknown command [" + command + "].");
                        command = null;
                    }
                    continue;
                }

                int newEndpointValue;
                try {
                    newEndpointValue = Integer.parseInt(command.substring(1));
                    if (newEndpointValue < Servo.END_POINT_MIN
                            || newEndpointValue > Servo.END_POINT_MAX) {
                        LOGGER.info("Endpoint value is out of range - defaulted to ["
                                + Servo.PROP_END_POINT_DEFAULT + "].");
                        newEndpointValue = Integer
                                .parseInt(Servo.PROP_END_POINT_DEFAULT);
                    }
                    LOGGER.info("New value for property [" + propertyToAdjust
                            + "]: " + newEndpointValue + "");
                } catch (Exception e) {

                    LOGGER.info("Endpoint value for property ["
                            + propertyToAdjust + "] defaulted to ["
                            + Servo.PROP_END_POINT_DEFAULT + "].", e);
                    newEndpointValue = Integer
                            .parseInt(Servo.PROP_END_POINT_DEFAULT);
                }
                servos[activeServo].setProperty(propertyToAdjust,
                        Integer.toString(newEndpointValue));
            }
        }
    }

    public void sweep(Scanner scanner) throws Exception {
        LOGGER.info("");
        LOGGER.info("|- Sweep ------------------------------------------------------------------------");
        LOGGER.info("| Continuously moves the servo between POS_MAX_LEFT and POS_MAX_RIGHT.           ");
        LOGGER.info("| To change speed provide value between 1 and 10 (10 for max speed)              ");
        LOGGER.info("| Example: 7<Enter>                                                              ");
        LOGGER.info("| Default speed: 5                                                               ");
        LOGGER.info("| Exit command: x<Enter>                                                         ");
        LOGGER.info("|--------------------------------------------------------------------------------");

        // create and start sweeper thread
        Sweeper sweeper = new Sweeper();
        sweeper.start();

        // handle user commands
        String command = null;
        while (!"x".equals(command)) {
            String input = scanner.nextLine();
            if (!input.trim().isEmpty()) {
                command = input.trim().toLowerCase();
            }
            if (command != null) {
                if ("x".equals(command)) {
                    continue;
                }
                try {
                    int speed = Integer.parseInt(command);
                    sweeper.setSpeed(speed);
                } catch (NumberFormatException e) {
                    LOGGER.error("NumberFormatException: Invalid speed value ["
                            + command + "]. Allowed values [1..10] ", e);
                }
            }
        }
        sweeper.interrupt();
        servos[activeServo].setPosition(Servo.POS_NEUTRAL);
    }

    public void info() {
        for (int i = 0; i < servos.length; i++) {
            Servo servo = servos[i];
            LOGGER.info("Channel " + (i < 10 ? " " : "") + i + ": "
                    + (servo != null ? servo.toString() : "N.A."));
        }
    }

    // ------------------------------------------------------------------------------------------------------------------
    // Helpers
    // ------------------------------------------------------------------------------------------------------------------
    private PCA9685GpioProvider createProvider() throws IOException {
        BigDecimal frequency = PCA9685GpioProvider.ANALOG_SERVO_FREQUENCY;
        BigDecimal frequencyCorrectionFactor = new BigDecimal("1.0578");

        I2CBus bus = I2CFactory.getInstance(I2CBus.BUS_1);
        return new PCA9685GpioProvider(bus, 0x40, frequency,
                frequencyCorrectionFactor);
    }

    private GpioPinPwmOutput[] provisionPwmOutputs(
            final PCA9685GpioProvider gpioProvider) {
        GpioController gpio = GpioFactory.getInstance();
        GpioPinPwmOutput myOutputs[] = {
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_00,
                        "Servo 00"),
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_01,
                        "not used"),
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_02,
                        "not used"),
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_03,
                        "not used"),
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_04,
                        "not used"),
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_05,
                        "not used"),
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_06,
                        "not used"),
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_07,
                        "not used"),
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_08,
                        "not used"),
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_09,
                        "not used"),
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_10,
                        "not used"),
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_11,
                        "not used"),
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_12,
                        "not used"),
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_13,
                        "not used"),
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_14,
                        "not used"),
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_15,
                        "not used") };
        return myOutputs;
    }

    private class Sweeper extends Thread {

        private int speed = 5;
        private static final int STEP = 1; // make sure this is always true: 100 % step
                                    // = 0
        private static final int MAXSLEEPBETWEENSTEPS = 100;

        @Override
        public void run() {
            int position = 0;
            Orientation orientation = Orientation.RIGHT;
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    if (orientation == Orientation.RIGHT) {
                        if (position < Servo.POS_MAX_RIGHT) {
                            position += STEP;
                        } else {
                            orientation = Orientation.LEFT;
                            position -= STEP;
                        }
                    } else if (orientation == Orientation.LEFT) {
                        if (position > Servo.POS_MAX_LEFT) {
                            position -= STEP;
                        } else {
                            orientation = Orientation.RIGHT;
                            position += STEP;
                        }
                    } else {
                        LOGGER.error("Unsupported value for enum <ServoBase.Orientation>: ["
                                + orientation + "].");
                    }

                    servos[activeServo].setPosition(position);
                    Thread.currentThread();
                    if (position % 10 == 0) {
                        LOGGER.info("Position: " + position);
                    }
                    Thread.sleep(MAXSLEEPBETWEENSTEPS / speed);
                } catch (InterruptedException ex) {
                    LOGGER.error("InterruptedException: " + ex.getMessage(), ex);
                    Thread.currentThread().interrupt();
                }
            }
        }

        public void setSpeed(int speed) {
            if (speed < 1) {
                this.speed = 1;
            } else if (speed > 10) {
                this.speed = 10;
            } else {
                this.speed = speed;
            }
        }
    }
}
