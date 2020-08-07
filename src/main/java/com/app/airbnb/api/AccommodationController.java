package com.app.airbnb.api;

import com.app.airbnb.model.*;
import com.app.airbnb.repositories.AccommodationRepository;
import com.app.airbnb.repositories.UserRepository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
class AccommodationController {
    AccommodationRepository accommodationRepository;
    UserRepository userRepository;

    AccommodationController(AccommodationRepository accommodationRepository,
                            UserRepository userRepository) {
        this.accommodationRepository = accommodationRepository;
        this.userRepository = userRepository;
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/addAccommodation")
    ResponseEntity<String> addAccommodation(@RequestParam("info") String jsonInfo, @RequestParam("location") String jsonLocation,
                                                 @RequestParam("rules") String jsonRules, @RequestParam("user") String jsonUser) {
        try {
            User user = new ObjectMapper().readValue(jsonUser, User.class);
            AccommodationInfo info = new ObjectMapper().readValue(jsonInfo, AccommodationInfo.class);
            AccommodationLocation location = new ObjectMapper().readValue(jsonLocation, AccommodationLocation.class);
            AccommodationRules rules = new ObjectMapper().readValue(jsonRules, AccommodationRules.class);
            Accommodation accommodation = new Accommodation(info, location, rules, user);

            this.accommodationRepository.save(accommodation);   //TODO: error

            Accommodation[] accommodations = user.getAccommodations();
            int i = 0;
            while (accommodations[i] != null) i++;
            accommodations[i] = accommodation;
            user.setAccommodations(accommodations);
            this.userRepository.save(user);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
