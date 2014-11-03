package com.libsensorj.interfaces;

import com.libsensorj.model.Observer;

public interface IEvent {

    public void attach(Observer obsever);

    public void detach(Observer obsever);

    public void trigger();
}
