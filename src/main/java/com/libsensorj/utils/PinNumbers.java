package com.libsensorj.utils;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;

public enum PinNumbers {
   
    PIN_00(RaspiPin.GPIO_00),
    PIN_01(RaspiPin.GPIO_01),
    PIN_02(RaspiPin.GPIO_02),
    PIN_03(RaspiPin.GPIO_03),
    PIN_04(RaspiPin.GPIO_04),
    PIN_05(RaspiPin.GPIO_05),
    PIN_06(RaspiPin.GPIO_06),
    PIN_07(RaspiPin.GPIO_07),
    PIN_08(RaspiPin.GPIO_08),
    PIN_09(RaspiPin.GPIO_09),
    PIN_10(RaspiPin.GPIO_10),
    PIN_11(RaspiPin.GPIO_11),
    PIN_12(RaspiPin.GPIO_12),
    PIN_13(RaspiPin.GPIO_13),
    PIN_14(RaspiPin.GPIO_14),
    PIN_15(RaspiPin.GPIO_15),
    PIN_16(RaspiPin.GPIO_16),
    PIN_17(RaspiPin.GPIO_17),
    PIN_18(RaspiPin.GPIO_18),
    PIN_19(RaspiPin.GPIO_19),
    PIN_20(RaspiPin.GPIO_20),
    PIN_21(RaspiPin.GPIO_21),
    PIN_22(RaspiPin.GPIO_22),
    PIN_23(RaspiPin.GPIO_23),
    PIN_24(RaspiPin.GPIO_24),
    PIN_25(RaspiPin.GPIO_25),
    PIN_26(RaspiPin.GPIO_26),
    PIN_27(RaspiPin.GPIO_27),
    PIN_28(RaspiPin.GPIO_28),
    PIN_29(RaspiPin.GPIO_29);

    public Pin pinNumber; 
    
    PinNumbers(Pin pin) { 
        pinNumber = pin; 
    }
}
