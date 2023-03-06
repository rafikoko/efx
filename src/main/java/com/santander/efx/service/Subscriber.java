package com.santander.efx.service;

//to cover requirement: all you have to do is implement an interface, e.g. void onMessage(String message)
public interface Subscriber {
    public void onMessage(String message);
}
