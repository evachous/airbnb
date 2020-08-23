package com.app.airbnb.model;

public class ChatNotFoundException extends RuntimeException {

    public ChatNotFoundException() {
        super("Could not find chat!");
    }
}
