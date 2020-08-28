package com.app.airbnb.repositories;

import com.app.airbnb.model.Reservation;
import com.app.airbnb.model.Review;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepositoryCustom {
    List<Reservation> findByAccommodation(Long accommodationID);
    List<Reservation> findByGuestUsername(String username);
    List<Reservation> findByHostUsername(String username);
    boolean checkDateAvailability(Long accommodationID, LocalDate checkin, LocalDate checkout);
    List<Review> findAllReviews(List<Reservation> reservations);
}
