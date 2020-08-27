package com.app.airbnb.api;

import com.app.airbnb.model.Accommodation;
import com.app.airbnb.model.Reservation;
import com.app.airbnb.model.Review;
import com.app.airbnb.model.User;
import com.app.airbnb.repositories.AccommodationRepository;
import com.app.airbnb.repositories.ReservationRepository;
import com.app.airbnb.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RestController
class ReservationController {

    ReservationRepository reservationRepository;
    AccommodationRepository accommodationRepository;
    UserRepository userRepository;

    ReservationController(ReservationRepository reservationRepository, AccommodationRepository accommodationRepository,
                          UserRepository userRepository) {
        this.reservationRepository = reservationRepository;
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

            reservation.setReview(review);
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
    @GetMapping("getAccommodationReservations/{id}")
    List<Reservation> returnAccommodationReservations(@PathVariable Long id) {
        return this.reservationRepository.findByAccommodation(id);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("getGuestReservations/{username}")
    List<Reservation> returnGuestReservations(@PathVariable String username) {
        return this.reservationRepository.findByGuestUsername(username);
    }
}
