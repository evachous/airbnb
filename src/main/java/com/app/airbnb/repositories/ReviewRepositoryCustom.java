package com.app.airbnb.repositories;

import com.app.airbnb.model.Reservation;
import com.app.airbnb.model.Review;

import java.util.List;

public interface ReviewRepositoryCustom {
    List<Review> findByReservations(List<Reservation> reservations);
    List<Review> findByAccommodation(Long accommodationID);
    List<Review> findByGuestUsername(String username);
    List<Review> findByHostUsername(String username);
}
