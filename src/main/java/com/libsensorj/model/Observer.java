package com.libsensorj.model;

public class Observer {

    private String observerName;

    public Observer() {
        this.setObserverName("");
    }

    public Observer(String observerName) {
        this.setObserverName(observerName);
    }

    public String getObserverName() {
        return observerName;
    }

    public void setObserverName(String observerName) {
        this.observerName = observerName;
    }
}
