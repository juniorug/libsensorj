/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  IFBA
 * PROJECT       :  libsensorj
 * FILENAME      :  IEvent.java  
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
package com.libsensorj.interfaces;

import com.libsensorj.model.Observer;
import com.pi4j.component.ObserveableComponentBase;

/**
 * The Class IEvent.
 */
public abstract class IEvent extends ObserveableComponentBase {

    /**
     * Attach.
     *
     * @param obsever
     *            the obsever
     */
    public abstract void attach(Observer obsever);

    /**
     * Detach.
     *
     * @param obsever
     *            the obsever
     */
    public abstract void detach(Observer obsever);

    /**
     * Trigger.
     */
    public abstract void trigger();
}
