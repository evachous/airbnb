package com.app.airbnb.repositories;

import com.app.airbnb.model.Address;

public interface AddressRepositoryCustom {
    Address findByCoordinates(Double lat, Double lng);
}
