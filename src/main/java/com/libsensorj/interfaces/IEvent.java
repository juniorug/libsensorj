package com.libsensorj.interfaces;

import com.libsensorj.model.Observer;
import com.pi4j.component.ObserveableComponentBase;

public abstract class IEvent extends ObserveableComponentBase {

    public abstract void attach(Observer obsever);

    public abstract void detach(Observer obsever);

    public abstract void trigger();
}
