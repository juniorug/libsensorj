package com.pi4j.examples;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Examples
 * FILENAME      :  WiringPiLcdExample.java  
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
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.Lcd;

public class WiringPiLcdExample {

    public static final int LCD_ROWS = 2;
    public static final int LCD_COLUMNS = 16;
    public static final int LCD_BITS = 4;

    private static final Logger LOGGER = LogManager
            .getLogger(WiringPiLcdExample.class.getName());

    public static void main(String[] args) throws InterruptedException,
            UnsupportedEncodingException {
        LOGGER.info("<--Pi4J--> Wiring Pi LCD test program");

        // setup wiringPi
        if (Gpio.wiringPiSetup() == -1) {
            LOGGER.info(" ==>> GPIO SETUP FAILED");
            return;
        }

      /* initialize LCD
         LCD_ROWS: number of row supported by LCD
         LCD_COLUMNS: number of columns supported by LCD
         LCD_BITS: number of bits used to communicate to LCD*/
        int lcdHandle = Lcd.lcdInit(LCD_ROWS, LCD_COLUMNS, LCD_BITS, 
                // LCD RS pin
                11, 
                // LCD strobe pin
                10, 
                // LCD data bit 1
                0, 
                // LCD data bit 2
                1, 
                // LCD data bit 3
                2, 
                // LCD data bit 4
                3, 
                // LCD data bit 5 (set to 0 if using 4 bit communication)
                0, 
                // LCD data bit 6 (set to 0 if using 4 bit communication)
                0, 
                // LCD data bit 7 (set to 0 if using 4 bit communication)
                0, 
                // LCD data bit 8 (set to 0 if using 4 bit communication)
                0); 

        // verify initialization
        if (lcdHandle == -1) {
            LOGGER.info(" ==>> LCD INIT FAILED");
            return;
        }

        // clear LCD
        Lcd.lcdClear(lcdHandle);
        Thread.sleep(1000);

        // write line 1 to LCD
        Lcd.lcdHome(lcdHandle);
        // Lcd.lcdPosition (lcdHandle, 0, 0) ;
        Lcd.lcdPuts(lcdHandle, "The Pi4J Project");

        // write line 2 to LCD
        Lcd.lcdPosition(lcdHandle, 0, 1);
        Lcd.lcdPuts(lcdHandle, "----------------");

        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");

        // update time every one second
        while (true) {
            // write time to line 2 on LCD
            Lcd.lcdPosition(lcdHandle, 0, 1);
            Lcd.lcdPuts(lcdHandle, "--- " + formatter.format(new Date())
                    + " ---");
            Thread.sleep(1000);
        }
    }
}
