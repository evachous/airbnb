package com.app.airbnb.repositories;

import com.app.airbnb.model.Accommodation;
import com.app.airbnb.model.AccommodationInfo;
import com.app.airbnb.model.AccommodationLocation;
import com.app.airbnb.model.AccommodationRules;

public interface AccommodationRepositoryCustom {
    double calcDistance(double lat1, double lng1, double lat2, double lng2);
    void replaceAccommodationInfo(Accommodation accommodation, AccommodationInfo newInfo);
    void replaceAccommodationLocation(Accommodation accommodation, AccommodationLocation newLocation);
    void replaceAccommodationRules(Accommodation accommodation, AccommodationRules newRules);
}
