package com.app.airbnb.api;

import com.app.airbnb.model.Accommodation;
import com.app.airbnb.model.Reservation;
import com.app.airbnb.model.Review;
import com.app.airbnb.model.User;
import com.app.airbnb.repositories.AccommodationRepository;
import com.app.airbnb.repositories.ReservationRepository;
import com.app.airbnb.repositories.ReviewRepository;
import com.app.airbnb.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@RestController
class ReservationController {

    ReservationRepository reservationRepository;
    ReviewRepository reviewRepository;
    AccommodationRepository accommodationRepository;
    UserRepository userRepository;

    ReservationController(ReservationRepository reservationRepository, ReviewRepository reviewRepository,
                          AccommodationRepository accommodationRepository, UserRepository userRepository) {
        this.reservationRepository = reservationRepository;
        this.reviewRepository = reviewRepository;
        this.accommodationRepository = accommodationRepository;
        this.userRepository = userRepository;
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/makeReservation")
    ResponseEntity<String> makeReservation(@RequestParam("guestUsername") String guestUsername, @RequestParam("accommodationID") String accommodationID,
                                           @RequestParam("numPeople") String numPeople, @RequestParam("startDate") String startDate,
                                           @RequestParam("endDate") String endDate) {
        User guest = this.userRepository.findByUsername(guestUsername);
        Accommodation accommodation = this.accommodationRepository.getOne(Long.parseLong(accommodationID));

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("u-M-d");
        LocalDate localStartDate = LocalDate.parse(startDate, dateFormatter);
        LocalDate localEndDate = LocalDate.parse(endDate, dateFormatter);

        Long days = localStartDate.until(localEndDate, ChronoUnit.DAYS) + 1;

        Reservation reservation = new Reservation(guest, accommodation, Integer.parseInt(numPeople), localStartDate, localEndDate, days);

        this.reservationRepository.save(reservation);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/addReview")
    ResponseEntity<String> addReview(@RequestParam("reservationID") String reservationID,
                                     @RequestParam("review") String jsonReview) {
        try {
            Reservation reservation = this.reservationRepository.getOne(Long.parseLong(reservationID));
            Review review = new ObjectMapper().readValue(jsonReview, Review.class);

            Accommodation accommodation = reservation.getAccommodation();
            List<Reservation> accommodationReservations = this.reservationRepository.findByAccommodation(accommodation.getId());
            List<Review> accommodationReviews = this.reviewRepository.findByReservations(accommodationReservations);

            User host = accommodation.getHost();
            List<Reservation> hostReservations = this.reservationRepository.findByHostUsername(host.getUsername());
            List<Review> hostReviews = this.reviewRepository.findByReservations(hostReservations);

            if (reservation.getReview() == null) {
                reservation.setReview(review);

                accommodation.setNumRatings(accommodation.getNumRatings() + 1);
                double total = review.getRating();
                for (Review accommodationReview : accommodationReviews) {
                    total = total + accommodationReview.getRating();
                }
                accommodation.setAvgRating(total / accommodation.getNumRatings());

                host.setNumRatings(host.getNumRatings() + 1);
                total = review.getRating();
                for (Review hostReview : hostReviews) {
                    total = total + hostReview.getRating();
                }
                host.setAvgRating(total / host.getNumRatings());
            }
            else {
                reservation.getReview().setText(review.getText());
                reservation.getReview().setRating(review.getRating());

                double total = review.getRating();
                for (Review accommodationReview : accommodationReviews) {
                    if (!reservation.getId().equals(accommodationReview.getReservation().getId()))
                        total = total + accommodationReview.getRating();
                }
                accommodation.setAvgRating(total / accommodation.getNumRatings());

                total = review.getRating();
                for (Review hostReview : hostReviews) {
                    if (!reservation.getId().equals(hostReview.getReservation().getId()))
                        total = total + hostReview.getRating();
                }
                host.setAvgRating(total / host.getNumRatings());
            }

            this.reservationRepository.save(reservation);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/checkDateAvailability/{id}/{checkin}/{checkout}")
    boolean checkDateAvailability(@PathVariable Long id, @PathVariable String checkin,
                                  @PathVariable String checkout) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("u-M-d");
        LocalDate localCheckin = LocalDate.parse(checkin, dateFormatter);
        LocalDate localCheckout = LocalDate.parse(checkout, dateFormatter);

        return this.reservationRepository.checkDateAvailability(id, localCheckin, localCheckout);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/allReservations")
    List<Reservation> allReservations() {

        return reservationRepository.findAll();
    }

    @CrossOrigin(origins = "*")
    @GetMapping("getAccommodationReservations/{id}")
    List<Reservation> returnAccommodationReservations(@PathVariable Long id) {
        return this.reservationRepository.findByAccommodation(id);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("getGuestReservations/{username}")
    List<Reservation> returnGuestReservations(@PathVariable String username) {
        return this.reservationRepository.findByGuestUsername(username);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/allReviews")
    List<Review> allReviews() {

        return this.reviewRepository.findAll();
    }

    @CrossOrigin(origins = "*")
    @GetMapping("getAccommodationReviews/{id}")
    List<Review> returnAccommodationReviews(@PathVariable Long id) {
        List<Reservation> reservations = this.reservationRepository.findByAccommodation(id);
        return this.reviewRepository.findByReservations(reservations);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("getGuestReviews/{username}")
    List<Review> returnGuestReviews(@PathVariable String username) {
        List<Reservation> reservations = this.reservationRepository.findByGuestUsername(username);
        return this.reviewRepository.findByReservations(reservations);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("getHostReviews/{username}")
    List<Review> returnHostReviews(@PathVariable String username) {
        List<Reservation> reservations = this.reservationRepository.findByHostUsername(username);
        return this.reviewRepository.findByReservations(reservations);
    }
}
