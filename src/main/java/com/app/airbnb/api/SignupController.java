package com.app.airbnb.api;

import com.app.airbnb.model.*;
import com.app.airbnb.repositories.UserRepository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
class SignupController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    SignupController(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {

        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/signup")
    ResponseEntity<String> signup(@RequestParam("user") String jsonUser,
                                  @RequestParam("profilePicture") MultipartFile profilePicture) throws IOException {
        try {
            User newUser = new ObjectMapper().readValue(jsonUser, User.class);
            if (this.userRepository.findByUsername(newUser.getUsername()) != null) {
                System.out.println("Username exists");
                return ResponseEntity.badRequest().body("Username exists");
            }
            else if (this.userRepository.findByEmail(newUser.getEmail()) != null) {
                System.out.println("Email exists");
                return ResponseEntity.badRequest().body("Email exists");
            }
            else {
                newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));

                String path = this.userRepository.uploadImage(profilePicture, "profile");
                newUser.setProfilePicture(new Image(path, null));

                newUser.setNumRatings(0);
                newUser.setAvgRating(0.0);

                newUser.setSearchHistory(new SearchHistory());

                this.userRepository.save(newUser);
                return new ResponseEntity<>(HttpStatus.OK);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
