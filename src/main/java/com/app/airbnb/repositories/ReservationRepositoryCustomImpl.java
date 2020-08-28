package com.app.airbnb.repositories;

import com.app.airbnb.model.Reservation;
import com.app.airbnb.model.Review;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class ReservationRepositoryCustomImpl implements ReservationRepositoryCustom {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<Reservation> findByAccommodation(Long accommodationID) {
        Query query = entityManager.createQuery("SELECT r FROM Reservation r WHERE r.accommodation.id = ?1");
        query.setParameter(1, accommodationID);
        return query.getResultList();
    }

    @Override
    public List<Reservation> findByGuestUsername(String username) {
        Query query = entityManager.createQuery("SELECT r FROM Reservation r WHERE r.guest.username = ?1");
        query.setParameter(1, username);
        return query.getResultList();
    }

    @Override
    public List<Reservation> findByHostUsername(String username) {
        Query query = entityManager.createQuery("SELECT r FROM Reservation r WHERE r.accommodation.host.username = ?1");
        query.setParameter(1, username);
        return query.getResultList();
    }

    public boolean checkDateAvailability(Long accommodationID, LocalDate checkin, LocalDate checkout) {
        List<Reservation> reservations = findByAccommodation(accommodationID);

        for (Reservation reservation : reservations) {
            if (!(checkin.isBefore(reservation.getStartDate()) || checkin.isAfter(reservation.getEndDate())) ||
                    !(checkout.isBefore(reservation.getStartDate()) || checkout.isAfter(reservation.getEndDate())) ||
                    (checkin.isBefore(reservation.getStartDate()) && checkout.isAfter(reservation.getEndDate())))
                return false;
        }
        return true;
    }

    public List<Review> findAllReviews(List<Reservation> reservations) {
        List<Review> reviews = new ArrayList<>();

        for (Reservation reservation : reservations) {
            if (reservation.getReview() != null) {
                reviews.add(reservation.getReview());
            }
        }
        return reviews;
    }
}
