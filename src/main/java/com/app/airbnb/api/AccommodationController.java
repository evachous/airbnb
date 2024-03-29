package com.app.airbnb.api;

import com.app.airbnb.model.*;
import com.app.airbnb.repositories.AccommodationRepository;
import com.app.airbnb.repositories.ImageRepository;
import com.app.airbnb.repositories.UserRepository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
class AccommodationController {
    AccommodationRepository accommodationRepository;
    UserRepository userRepository;
    ImageRepository imageRepository;

    AccommodationController(AccommodationRepository accommodationRepository, UserRepository userRepository,
                            ImageRepository imageRepository) {
        this.accommodationRepository = accommodationRepository;
        this.userRepository = userRepository;
        this.imageRepository = imageRepository;
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
                for (MultipartFile image : images) {
                    String path = this.userRepository.uploadImage(image, "accommodation");
                    Image newImage = new Image(path, accommodation);
                    this.imageRepository.save(newImage);
                }
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

    @CrossOrigin(origins = "*")
    @GetMapping("/allAccommodations")
    List<Accommodation> allAccommodations() {

        return accommodationRepository.findAll();
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/accommodations/{id}")
    Accommodation returnAccommodation(@PathVariable Long id) {
        return this.accommodationRepository.findById(id)
                .orElseThrow(() -> new AccommodationNotFoundException(id));
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/getAccommodationImage/{id}/{index}", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody byte[] getAccommodationImage(@PathVariable Long id, @PathVariable Integer index) throws IOException {
        Accommodation accommodation = this.accommodationRepository.getOne(id);
        return Base64.encodeBase64(Files.readAllBytes(Paths.get(accommodation.getImages().get(index).getPath())));
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping("/deleteAccommodationImage/{id}/{index}")
    ResponseEntity<String> deleteAccommodationImage(@PathVariable Long id, @PathVariable Integer index) {
        Accommodation accommodation = this.accommodationRepository.getOne(id);
        Image image = accommodation.getImages().get(index);

        image.setUser(null);
        image.setAccommodation(null);

        this.imageRepository.delete(image);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/changeAccommodation")
    ResponseEntity<String> changeAccommodation(@RequestParam("info") String jsonInfo, @RequestParam("location") String jsonLocation,
                                               @RequestParam("rules") String jsonRules, @RequestParam("id") String id,
                                               @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate,
                                               @RequestParam("images") MultipartFile[] images) {
        try {
            Accommodation accommodation = this.accommodationRepository.getOne(Long.parseLong(id));
            AccommodationInfo info = new ObjectMapper().readValue(jsonInfo, AccommodationInfo.class);
            AccommodationLocation location = new ObjectMapper().readValue(jsonLocation, AccommodationLocation.class);
            AccommodationRules rules = new ObjectMapper().readValue(jsonRules, AccommodationRules.class);

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("u-M-d");
            LocalDate localStartDate = LocalDate.parse(startDate, dateFormatter);
            LocalDate localEndDate = LocalDate.parse(endDate, dateFormatter);
            info.setStartDate(localStartDate);
            info.setEndDate(localEndDate);

            if (images != null) {
                List<Image> accommodationImages = accommodation.getImages();
                for (MultipartFile image : images) {
                    String path = this.userRepository.uploadImage(image, "accommodation");
                    Image newImage = new Image(path, accommodation);
                    this.imageRepository.save(newImage);
                    accommodationImages.add(newImage);
                }
                accommodation.setImages(accommodationImages);
            }

            this.accommodationRepository.replaceAccommodationInfo(accommodation, info);
            this.accommodationRepository.replaceAccommodationLocation(accommodation, location);
            this.accommodationRepository.replaceAccommodationRules(accommodation, rules);

            this.accommodationRepository.save(accommodation);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
