package com.app.airbnb.repositories;

import com.app.airbnb.model.Reservation;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepositoryCustom {
    List<Reservation> findByAccommodation(Long accommodationID);
    List<Reservation> findByGuestUsername(String username);
    boolean checkDateAvailability(Long accommodationID, LocalDate checkin, LocalDate checkout);
}
