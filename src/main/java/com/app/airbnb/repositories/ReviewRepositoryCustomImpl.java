package com.app.airbnb.repositories;

import com.app.airbnb.model.Reservation;
import com.app.airbnb.model.Review;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class ReviewRepositoryCustomImpl implements ReviewRepositoryCustom {

    @PersistenceContext
    EntityManager entityManager;

    public List<Review> findByReservations(List<Reservation> reservations) {
        List<Review> reviews = new ArrayList<>();

        for (Reservation reservation : reservations) {
            if (reservation.getReview() != null) {
                reviews.add(reservation.getReview());
            }
        }
        return reviews;
    }

    public List<Review> findByAccommodation(Long accommodationID) {
        Query query = entityManager.createQuery("SELECT r FROM Review r WHERE r.reservation.accommodation.id = ?1");
        query.setParameter(1, accommodationID);
        return query.getResultList();
    }

    public List<Review> findByGuestUsername(String username) {
        Query query = entityManager.createQuery("SELECT r FROM Review r WHERE r.reservation.guest.username = ?1");
        query.setParameter(1, username);
        return query.getResultList();
    }

    public List<Review> findByHostUsername(String username) {
        Query query = entityManager.createQuery("SELECT r FROM Review r WHERE r.reservation.accommodation.host.username = ?1");
        query.setParameter(1, username);
        return query.getResultList();
    }
}
