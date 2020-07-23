package com.app.airbnb.api;

import com.app.airbnb.AirbnbApplication;
import com.app.airbnb.model.User;
import com.app.airbnb.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

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
    ResponseEntity<String> signup(@RequestBody User newUser) {
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
            userRepository.save(newUser);
            System.out.println("New User added");
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }
}
