/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  IFBA
 * PROJECT       :  libsensorj
 * FILENAME      :  Observer.java  
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
package com.libsensorj.model;

/**
 * The Class Observer.
 */
public class Observer {

    /** The observer name. */
    private String observerName;

    /**
     * Instantiates a new observer.
     */
    public Observer() {
        this.setObserverName("");
    }

    /**
     * Instantiates a new observer.
     *
     * @param observerName
     *            the observer name
     */
    public Observer(String observerName) {
        this.setObserverName(observerName);
    }

    /**
     * Gets the observer name.
     *
     * @return the observer name
     */
    public String getObserverName() {
        return observerName;
    }

    /**
     * Sets the observer name.
     *
     * @param observerName
     *            the new observer name
     */
    public void setObserverName(String observerName) {
        this.observerName = observerName;
    }
}
