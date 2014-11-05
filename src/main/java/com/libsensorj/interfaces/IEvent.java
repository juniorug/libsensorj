package com.libsensorj.interfaces;

import com.libsensorj.model.Observer;

public interface IEvent {

    void attach(Observer obsever);

    void detach(Observer obsever);

    void trigger();
}
