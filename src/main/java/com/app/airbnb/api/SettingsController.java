package com.app.airbnb.api;

import com.app.airbnb.model.User;
import com.app.airbnb.model.UserNotFoundException;
import com.app.airbnb.repositories.UserRepository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class SettingsController {

    private final UserRepository userRepository;

    SettingsController(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/settings")
    ResponseEntity<String> changeInfo(@RequestParam("user") String jsonUser, @RequestParam("oldUsername") String oldUsername,
                                      @RequestParam("oldEmail") String oldEmail,
                                      @RequestParam(value="profilePicture", required=false) MultipartFile profilePicture) throws IOException {
        System.out.println("IM HERE");
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
                oldUser.setIsAdmin(newUser.getIsAdmin());
                oldUser.setIsHost(newUser.getIsHost());
                oldUser.setIsGuest(newUser.getIsGuest());

                if (profilePicture == null)
                    System.out.println("no new picture");
                else {
                    oldUser.setProfilePicture(this.userRepository.compressBytes(profilePicture.getBytes()));
                }

                this.userRepository.save(oldUser);
                return new ResponseEntity<>(HttpStatus.OK);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
