package com.app.airbnb.api;

import com.app.airbnb.model.Accommodation;
import com.app.airbnb.model.AccommodationInfo;
import com.app.airbnb.model.AccommodationRules;
import com.app.airbnb.repositories.AccommodationRepository;
import com.app.airbnb.repositories.ReservationRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@RestController
class SearchController {
    AccommodationRepository accommodationRepository;
    ReservationRepository reservationRepository;

    SearchController(AccommodationRepository accommodationRepository, ReservationRepository reservationRepository) {
        this.accommodationRepository = accommodationRepository;
        this.reservationRepository = reservationRepository;
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/searchAccommodations")
    List<Accommodation> searchAccommodations(@RequestParam("lat") String strLat, @RequestParam("lng") String strLng,
                                             @RequestParam("checkin") String checkin, @RequestParam("checkout") String checkout,
                                             @RequestParam("guests") String strGuests, @RequestParam("type") String type,
                                             @RequestParam("maxCost") String strMaxCost, @RequestParam("internet") String internet,
                                             @RequestParam("ac") String ac, @RequestParam("heating") String heating,
                                             @RequestParam("kitchen") String kitchen, @RequestParam("tv") String tv,
                                             @RequestParam("parking") String parking, @RequestParam("elevator") String elevator) {

        double lat = Double.parseDouble(strLat);
        double lng = Double.parseDouble(strLng);

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("u-M-d");
        LocalDate localCheckin = LocalDate.parse(checkin, dateFormatter);
        LocalDate localCheckout = LocalDate.parse(checkout, dateFormatter);

        int guests = Integer.parseInt(strGuests);
        int maxCost = 0;
        if (!strMaxCost.equals("null"))
            maxCost = Integer.parseInt(strMaxCost);

        List<Accommodation> accommodations = this.accommodationRepository.findAll();
        List<Accommodation> newAccommodations = new ArrayList<>();

        boolean available;

        for (Accommodation accommodation : accommodations) {
            available = this.reservationRepository.checkDateAvailability(accommodation.getId(), localCheckin, localCheckout);

            if (available) {
                AccommodationInfo info = accommodation.getInfo();
                AccommodationRules rules = accommodation.getRules();

                double dist = this.accommodationRepository.calcDistance(lat, lng,
                        accommodation.getLocation().getAddress().getLat(),
                        accommodation.getLocation().getAddress().getLng());

                if (dist < 50.0 && guests <= rules.getMaxPeople() && maxCost < info.getMinCost() &&
                        !(localCheckin.isBefore(info.getStartDate()) || localCheckin.isAfter(info.getEndDate())) &&
                        !(localCheckout.isBefore(info.getStartDate()) || localCheckout.isAfter(info.getEndDate())) &&
                        localCheckin.until(localCheckout, ChronoUnit.DAYS) + 1 >= rules.getMinDays() &&
                        (type.equals("null") || type.equals(info.getType())) && (internet.equals("null") || Boolean.parseBoolean(internet) == info.getInternet()) &&
                        (ac.equals("null") || Boolean.parseBoolean(ac) == info.getAc()) && (heating.equals("null") || Boolean.parseBoolean(heating) == info.getHeating()) &&
                        (kitchen.equals("null") || Boolean.parseBoolean(kitchen) == info.getKitchen()) && (tv.equals("null") || Boolean.parseBoolean(tv) == info.getTv()) &&
                        (parking.equals("null") || Boolean.parseBoolean(parking) == info.getParking()) && (elevator.equals("null") || Boolean.parseBoolean(elevator) == info.getElevator())) {

                    newAccommodations.add(accommodation);
                }
            }
        }
        return newAccommodations;
    }
}
