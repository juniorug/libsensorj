package com.pi4j.examples;

// START SNIPPET: serial-snippet

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Examples
 * FILENAME      :  SerialExample.java  
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

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pi4j.io.serial.Serial;
import com.pi4j.io.serial.SerialDataEvent;
import com.pi4j.io.serial.SerialDataListener;
import com.pi4j.io.serial.SerialFactory;
import com.pi4j.io.serial.SerialPortException;

/**
 * This example code demonstrates how to perform serial communications using the
 * Raspberry Pi.
 * 
 * @author Robert Savage
 */
public class SerialExample {

    private static final Logger LOGGER = LogManager
            .getLogger(SerialExample.class.getName());

    public static void main(String[] args) throws InterruptedException {

        // !! ATTENTION !!
        // By default, the serial port is configured as a console port
        // for interacting with the Linux OS shell. If you want to use
        // the serial port in a software program, you must disable the
        // OS from using this port. Please see this blog article by
        // Clayton Smith for step-by-step instructions on how to disable
        // the OS console for this port:
        // http://www.irrational.net/2012/04/19/using-the-raspberry-pis-serial-port/

        LOGGER.info("<--Pi4J--> Serial Communication Example ... started.");
        LOGGER.info(" ... connect using settings: 38400, N, 8, 1.");
        LOGGER.info(" ... data received on serial port should be displayed below.");

        // create an instance of the serial communications class
        final Serial serial = SerialFactory.createInstance();

        // create and register the serial data listener
        serial.addListener(new SerialDataListener() {
            public void dataReceived(SerialDataEvent event) {
                // print out the data received to the console
                LOGGER.info(event.getData());
            }
        });

        try {
            // open the default serial port provided on the GPIO header
            serial.open(Serial.DEFAULT_COM_PORT, 38400);

            // continuous loop to keep the program running until the user
            // terminates the program
            for (;;) {
                // write a formatted string to the serial transmit buffer
                serial.write("CURRENT TIME: %s", new Date().toString());

                // write a individual bytes to the serial transmit buffer
                serial.write((byte) 13);
                serial.write((byte) 10);

                // write a simple string to the serial transmit buffer
                serial.write("Second Line");

                // write a individual characters to the serial transmit
                // buffer
                serial.write('\r');
                serial.write('\n');

                // write a string terminating with CR+LF to the serial
                // transmit buffer
                serial.writeln("Third Line");

                // wait 1 second before continuing
                Thread.sleep(1000);
            }

        } catch (SerialPortException ex) {
            LOGGER.info(
                    "SerialPortException: ==>> SERIAL SETUP FAILED : "
                            + ex.getMessage(), ex);
            return;
        } catch (IllegalStateException ex) {
            // ex.printStackTrace();
            LOGGER.error("IllegalStateException: " + ex.getMessage(), ex);
        }
    }
}

// END SNIPPET: serial-snippet
