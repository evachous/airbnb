package com.app.airbnb.api;

import com.app.airbnb.model.Image;
import com.app.airbnb.model.User;
import com.app.airbnb.repositories.UserRepository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

@RestController
public class SettingsController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    SettingsController(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {

        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/changeInfo")
    ResponseEntity<String> changeInfo(@RequestParam("user") String jsonUser, @RequestParam("oldUsername") String oldUsername,
                                      @RequestParam("oldEmail") String oldEmail,
                                      @RequestParam(value="profilePicture", required=false) MultipartFile profilePicture) throws IOException {
        try {
            User newUser = new ObjectMapper().readValue(jsonUser, User.class);
            if (!newUser.getUsername().equals(oldUsername) && this.userRepository.findByUsername(newUser.getUsername()) != null) {
                System.out.println("Username exists");
                return ResponseEntity.badRequest().body("Username exists");
            }
            else if (!newUser.getEmail().equals(oldEmail) && this.userRepository.findByEmail(newUser.getEmail()) != null) {
                System.out.println("Email exists");
                return ResponseEntity.badRequest().body("Email exists");
            }
            else {
                User oldUser = this.userRepository.findByUsername(oldUsername);
                oldUser.setFirstName(newUser.getFirstName());
                oldUser.setLastName(newUser.getLastName());
                oldUser.setUsername(newUser.getUsername());
                oldUser.setEmail(newUser.getEmail());
                oldUser.setPhone(newUser.getPhone());
                oldUser.setCity(newUser.getCity());
                oldUser.setCountry(newUser.getCountry());
                oldUser.setIsAdmin(newUser.getIsAdmin());
                oldUser.setIsHost(newUser.getIsHost());
                oldUser.setIsGuest(newUser.getIsGuest());

                if (profilePicture != null) {
                    String path = this.userRepository.uploadImage(profilePicture);
                    oldUser.setProfilePicture(new Image(path));
                }

                this.userRepository.save(oldUser);
                return new ResponseEntity<>(HttpStatus.OK);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/changePassword")
    ResponseEntity<String> changePassword(@RequestParam("username") String username,
                                          @RequestParam("currentPassword") String currentPassword,
                                          @RequestParam("newPassword") String newPassword) {
        User user = this.userRepository.findByUsername(username);
        if (!bCryptPasswordEncoder.matches(currentPassword, user.getPassword())) {
            System.out.println("Current password doesn't match");
            return ResponseEntity.badRequest().body("Current password doesn't match");
        }
        user.setPassword(bCryptPasswordEncoder.encode(newPassword));
        this.userRepository.save(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
