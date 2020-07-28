package com.app.airbnb.api;

import com.app.airbnb.model.User;
import com.app.airbnb.model.UserNotFoundException;
import com.app.airbnb.repositories.UserRepository;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
class UserController {

    private final UserRepository userRepository;

    UserController(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/users")
    List<User> all() {

        return userRepository.findAll();
    }

    /* @CrossOrigin(origins = "*")
    @GetMapping("/users/{id}")
    User returnUserByID(@PathVariable Long id) {

        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }*/

    @CrossOrigin(origins = "*")
    @GetMapping("/users/{username}")
    User returnUser(@PathVariable String username) {

        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UserNotFoundException(username);
        }
        if (user.getProfilePicture() != null) {
            user.setProfilePicture(this.userRepository.decompressBytes(user.getProfilePicture()));
        }
        return user;
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping("/user/{id}")
    void deleteUser(@PathVariable Long id) {

        userRepository.deleteById(id);
    }
}
