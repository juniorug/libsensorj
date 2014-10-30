package com.source.interfaces;

import com.source.model.Observer;

public interface IEvent {

    public void attach(Observer obsever);

    public void detach(Observer obsever);

    public void trigger();
}
