package com.app.airbnb.api;

import com.app.airbnb.model.*;
import com.app.airbnb.repositories.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
class SearchController {
    AccommodationRepository accommodationRepository;
    ReservationRepository reservationRepository;
    UserRepository userRepository;
    SearchHistoryRepository searchHistoryRepository;
    AddressRepository addressRepository;

    SearchController(AccommodationRepository accommodationRepository, ReservationRepository reservationRepository,
                     UserRepository userRepository, SearchHistoryRepository searchHistoryRepository,
                     AddressRepository addressRepository) {
        this.accommodationRepository = accommodationRepository;
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.searchHistoryRepository = searchHistoryRepository;
        this.addressRepository = addressRepository;
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
                AccommodationLocation location = accommodation.getLocation();
                AccommodationRules rules = accommodation.getRules();

                double dist = this.accommodationRepository.calcDistance(lat, lng,
                        location.getAddress().getLat(),
                        location.getAddress().getLng());

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

    @CrossOrigin(origins = "*")
    @PostMapping("/addSearchAccommodation")
    ResponseEntity<String> addSearchAccommodation(@RequestParam("username") String username, @RequestParam("id") String id) {
        Accommodation accommodation = this.accommodationRepository.getOne(Long.parseLong(id));
        User guest = this.userRepository.findByUsername(username);
        if (guest != null && guest.getIsGuest()) {
            /*SearchHistory searchHistory = guest.getSearchHistory();
            searchHistory.addAccommodation(accommodation);
            this.accommodationRepository.save(accommodation);
            this.searchHistoryRepository.save(searchHistory);
            this.userRepository.save(guest);*/
        }
        else {
            System.out.println("Not guest");
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/addSearchAddress")
    ResponseEntity<String> addSearchAddress(@RequestParam("username") String username, @RequestParam("lat") String strLat,
                                            @RequestParam("lng") String strLng) {
        Double lat = Double.parseDouble(strLat);
        Double lng = Double.parseDouble(strLng);

        User guest = this.userRepository.findByUsername(username);
        if (guest != null && guest.getIsGuest()) {
            /*SearchHistory searchHistory = guest.getSearchHistory();
            Set<Address> searchAddresses = guest.getSearchHistory().getAddresses();
            Address address = new Address();
            address.setLat(lat);
            address.setLng(lng);
            this.addressRepository.save(address);
            System.out.println(searchHistory.getGuest().getId());

            searchAddresses.add(address);
            searchHistory.setAddresses(searchAddresses);

            Set<SearchHistory> searchHistories = address.getSearchHistories();
            searchHistories.add(guest.getSearchHistory());
            address.setSearchHistories(searchHistories);

            this.searchHistoryRepository.save(searchHistory);
            this.addressRepository.save(address);*/
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
