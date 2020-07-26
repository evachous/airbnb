package com.app.airbnb.api;

import com.app.airbnb.model.User;
import com.app.airbnb.model.UserNotFoundException;
import com.app.airbnb.repositories.UserRepository;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

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
            user.setProfilePicture(decompressBytes(user.getProfilePicture()));
        }
        return user;
    }


    /*@CrossOrigin(origins = "*")
    @PutMapping("/users/{id}")
    User replaceUser(@RequestBody User newUser, @PathVariable Long id) {

        return repository.findById(id)
                .map(user -> {
                    user.setFirstName(newUser.getFirstName());
                    user.setLastName(newUser.getLastName());
                    user.setId(newUser.getId());
                    return repository.save(user);
                })
                .orElseGet(() -> {
                    newUser.setId(id);
                    return repository.save(newUser);
                });
    }*/

    @CrossOrigin(origins = "*")
    @DeleteMapping("/user/{id}")
    void deleteUser(@PathVariable Long id) {

        userRepository.deleteById(id);
    }

    public static byte[] decompressBytes(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
        } catch (IOException | DataFormatException ioe) {
            System.out.println("Error decompressing");
        }
        return outputStream.toByteArray();
    }
}
