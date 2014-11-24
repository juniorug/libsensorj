/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  IFBA
 * PROJECT       :  libsensorj
 * FILENAME      :  MockGpioFactory.java  
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
 * FILENAME      :  MockGpioFactory.java  
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


import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.impl.GpioControllerImpl;

/**
 * <p>
 * This factory class provides a static method to create new 'GpioController' instances.
 * </p>
 * 
 * <p>
 * Before using the Pi4J library, you need to ensure that the Java VM in configured with access to
 * the following system libraries:
 * <ul>
 * <li>pi4j</li>
 * <li>wiringPi</li>
 * </ul>
 * <blockquote> This library depends on the wiringPi native system library.</br> (developed by
 * Gordon Henderson @ <a href="https://projects.drogon.net/">https://projects.drogon.net/</a>)
 * </blockquote>
 * </p>
 *
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 * @see #com.pi4j.io.gpio.Gpio
 * @see <a href="http://www.pi4j.com/">http://www.pi4j.com/</a>
 */
public class MockGpioFactory {

    // we only allow a single default provider to exists
    /** The provider. */
    private static MockGpioProvider provider = null;
    
    // private constructor 
    /**
     * Instantiates a new mock gpio factory.
     */
    private MockGpioFactory() {
        // forbid object construction 
    }
    
    /**
     * Create New GPIO Controller instance.
     *
     * @return Return a new GpioController impl instance.
     */
    public static GpioController getInstance() {
        // return a new instance of the GPIO controller
        return new GpioControllerImpl(getMockProvider());
    }
    
    
    /**
     * Gets the mock provider.
     *
     * @return the mock provider
     */
    public static MockGpioProvider getMockProvider() {
        // if a provider has not been created, then create a new instance
        if (provider == null) {
            provider = new MockGpioProvider();
        }
        // return the provider instance
        return provider;
    }
}
