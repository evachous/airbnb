package com.app.airbnb.model;

public class AccommodationNotFoundException extends RuntimeException {

    public AccommodationNotFoundException(Long id) {
        super("Could not find accommodation with id " + id);
    }
}
