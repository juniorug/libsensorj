/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  IFBA
 * PROJECT       :  libsensorj
 * FILENAME      :  UltrasonicRangeFinderEvent.java  
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
package com.libsensorj.concreteevent;

import com.libsensorj.interfaces.IEvent;
import com.libsensorj.model.Observer;

/**
 * The Class UltrasonicRangeFinderEvent.
 */
public class UltrasonicRangeFinderEvent extends IEvent {

    /* (non-Javadoc)
     * @see com.libsensorj.interfaces.IEvent#attach(com.libsensorj.model.Observer)
     */
    @Override
    public void attach(Observer obsever) {
        //

    }

    /* (non-Javadoc)
     * @see com.libsensorj.interfaces.IEvent#detach(com.libsensorj.model.Observer)
     */
    @Override
    public void detach(Observer obsever) {
        //

    }

    /* (non-Javadoc)
     * @see com.libsensorj.interfaces.IEvent#trigger()
     */
    @Override
    public void trigger() {
        //

    }

}
