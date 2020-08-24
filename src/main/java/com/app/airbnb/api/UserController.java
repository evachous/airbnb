package com.app.airbnb.api;

import com.app.airbnb.model.User;
import com.app.airbnb.model.UserNotFoundException;
import com.app.airbnb.repositories.UserRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

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

    @CrossOrigin(origins = "*")
    @GetMapping("/users/{username}")
    User returnUser(@PathVariable String username) {

        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UserNotFoundException(username);
        }
        return user;
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/hostCheck/{username}")
    boolean hostCheck(@PathVariable String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UserNotFoundException(username);
        }
        return user.getIsHost();
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/guestCheck/{username}")
    boolean guestCheck(@PathVariable String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UserNotFoundException(username);
        }
        return user.getIsGuest();
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/getUserPicture/{username}", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody byte[] getUserPicture(@PathVariable String username) throws IOException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UserNotFoundException(username);
        }

        if (user.getProfilePicture() != null)
            return Base64.encodeBase64(Files.readAllBytes(Paths.get(user.getProfilePicture().getPath())));
        else return null;
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping("/user/{id}")
    void deleteUser(@PathVariable Long id) {

        userRepository.deleteById(id);
    }
}
