package com.app.airbnb.api;

import com.app.airbnb.model.*;
import com.app.airbnb.repositories.AccommodationRepository;
import com.app.airbnb.repositories.UserRepository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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
                                            @RequestParam("rules") String jsonRules, @RequestParam("username") String username,
                                            @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate,
                                            @RequestParam("images") MultipartFile[] images) {
        try {
            User user = this.userRepository.findByUsername(username);
            AccommodationInfo info = new ObjectMapper().readValue(jsonInfo, AccommodationInfo.class);

            // get correct date format
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("u-M-d");
            LocalDate localStartDate = LocalDate.parse(startDate, dateFormatter);
            LocalDate localEndDate = LocalDate.parse(endDate, dateFormatter);
            info.setStartDate(localStartDate);
            info.setEndDate(localEndDate);

            AccommodationLocation location = new ObjectMapper().readValue(jsonLocation, AccommodationLocation.class);
            AccommodationRules rules = new ObjectMapper().readValue(jsonRules, AccommodationRules.class);
            Accommodation accommodation = new Accommodation(info, location, rules, user);

            // upload accommodation images
            if (images != null) {
                List<Image> accommodationImages = new ArrayList<>();
                for (MultipartFile image : images) {
                    String path = this.userRepository.uploadImage(image);
                    Image newImage = new Image(path);
                    accommodationImages.add(newImage);
                }
                accommodation.setImages(accommodationImages);
            }
            this.accommodationRepository.save(accommodation);

            // add accommodation to host
            List<Accommodation> accommodations = user.getAccommodations();
            accommodations.add(accommodation);
            user.setAccommodations(accommodations);
            this.userRepository.save(user);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
