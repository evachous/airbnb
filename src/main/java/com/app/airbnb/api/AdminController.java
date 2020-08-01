package com.app.airbnb.api;

import com.app.airbnb.model.User;
import com.app.airbnb.model.UserNotFoundException;
import com.app.airbnb.repositories.UserRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AdminController {

    private final UserRepository userRepository;

    AdminController(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/adminCheck/{username}")
    boolean adminCheck(@PathVariable String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UserNotFoundException(username);
        }

        return user.getIsAdmin();
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/approveHost")
    ResponseEntity<String> approveHost(@RequestBody String username) {
        User user = this.userRepository.findByUsername(username);
        user.setIsApproved(true);
        this.userRepository.save(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
