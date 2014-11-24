/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  IFBA
 * PROJECT       :  libsensorj
 * FILENAME      :  MockGpioProvider.java  
 * 
 * This file is part of the LibsensorJ project,
 * An extensible library for sensors / actuators using the Pi4J framework of the Raspberry Pi.
 * **********************************************************************
 * 
 * Created:      [yyyy/mm/dd creation date]
 * Last Changed: 24/11/2014 
 * 
 * @author: Júnior Mascarenhas       <A HREF="mailto:[juniorug@gmail.com]">[Júnior]</A>
 * @see [https://github.com/juniorug/libsensorj]
 * 
 * #L%
 */
package com.libsensorj.mock;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  MockGpioProvider.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2013 Pi4J
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


import com.pi4j.io.gpio.GpioProvider;
import com.pi4j.io.gpio.GpioProviderBase;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;

/**
 * The Class MockGpioProvider.
 */
public class MockGpioProvider extends GpioProviderBase implements GpioProvider {

    /** The Constant NAME. */
    public static final String NAME = "MockGpioProvider";

    /* (non-Javadoc)
     * @see com.pi4j.io.gpio.GpioProviderBase#getName()
     */
    @Override
    public String getName() {
        return NAME;
    }
    
    /**
     * Sets the mock state.
     *
     * @param pin the pin
     * @param state the state
     */
    public void setMockState(Pin pin, PinState state) {
        // cache pin state
        getPinCache(pin).setState(state);    
        
        // dispatch event
        dispatchPinDigitalStateChangeEvent(pin, state);
    }
    
    /**
     * Sets the mock analog value.
     *
     * @param pin the pin
     * @param value the value
     */
    public void setMockAnalogValue(Pin pin, double value) {
        // cache pin state
        getPinCache(pin).setAnalogValue(value);    
        
        // dispatch event
        dispatchPinAnalogValueChangeEvent(pin, value);
    }
    
}
