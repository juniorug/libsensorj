/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  IFBA
 * PROJECT       :  libsensorj
 * FILENAME      :  PinNumbers.java  
 * 
 * This file is part of the LibsensorJ project,
 * An extensible library for sensors / actuators using the Pi4J framework of the Raspberry Pi.
 * **********************************************************************
 * 
 * Created:      [yyyy/mm/dd creation date]
 * Last Changed: 20/11/2014 
 * 
 * @author: Júnior Mascarenhas       <A HREF="mailto:[juniorug@gmail.com]">[Júnior]</A>
 * @see [https://github.com/juniorug/libsensorj]
 * 
 * #L%
 */
package com.libsensorj.utils;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;

/**
 * The Enum PinNumbers.
 */
public enum PinNumbers {

    /** The PI n_00. */
    PIN_00(RaspiPin.GPIO_00),

    /** The PI n_01. */
    PIN_01(RaspiPin.GPIO_01),

    /** The PI n_02. */
    PIN_02(RaspiPin.GPIO_02),

    /** The PI n_03. */
    PIN_03(RaspiPin.GPIO_03),

    /** The PI n_04. */
    PIN_04(RaspiPin.GPIO_04),

    /** The PI n_05. */
    PIN_05(RaspiPin.GPIO_05),

    /** The PI n_06. */
    PIN_06(RaspiPin.GPIO_06),

    /** The PI n_07. */
    PIN_07(RaspiPin.GPIO_07),

    /** The PI n_08. */
    PIN_08(RaspiPin.GPIO_08),

    /** The PI n_09. */
    PIN_09(RaspiPin.GPIO_09),

    /** The PI n_10. */
    PIN_10(RaspiPin.GPIO_10),

    /** The PI n_11. */
    PIN_11(RaspiPin.GPIO_11),

    /** The PI n_12. */
    PIN_12(RaspiPin.GPIO_12),

    /** The PI n_13. */
    PIN_13(RaspiPin.GPIO_13),

    /** The PI n_14. */
    PIN_14(RaspiPin.GPIO_14),

    /** The PI n_15. */
    PIN_15(RaspiPin.GPIO_15),

    /** The PI n_16. */
    PIN_16(RaspiPin.GPIO_16),

    /** The PI n_17. */
    PIN_17(RaspiPin.GPIO_17),

    /** The PI n_18. */
    PIN_18(RaspiPin.GPIO_18),

    /** The PI n_19. */
    PIN_19(RaspiPin.GPIO_19),

    /** The PI n_20. */
    PIN_20(RaspiPin.GPIO_20),

    /** The PI n_21. */
    PIN_21(RaspiPin.GPIO_21),

    /** The PI n_22. */
    PIN_22(RaspiPin.GPIO_22),

    /** The PI n_23. */
    PIN_23(RaspiPin.GPIO_23),

    /** The PI n_24. */
    PIN_24(RaspiPin.GPIO_24),

    /** The PI n_25. */
    PIN_25(RaspiPin.GPIO_25),

    /** The PI n_26. */
    PIN_26(RaspiPin.GPIO_26),

    /** The PI n_27. */
    PIN_27(RaspiPin.GPIO_27),

    /** The PI n_28. */
    PIN_28(RaspiPin.GPIO_28),

    /** The PI n_29. */
    PIN_29(RaspiPin.GPIO_29);

    /** The pin number. */
    private Pin pinNumber;

    /**
     * Instantiates new pin numbers.
     *
     * @param pin
     *            the pin
     */
    PinNumbers(Pin pin) {
        pinNumber = pin;
    }
}
