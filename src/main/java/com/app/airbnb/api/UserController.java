package com.app.airbnb.api;

import com.app.airbnb.model.User;
import com.app.airbnb.model.UserNotFoundException;
import com.app.airbnb.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

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
    @GetMapping("/users/{id}")
    User returnUserByID(@PathVariable Long id) {

        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/users/{username}")
    User returnUser(@PathVariable String username) {

        return userRepository.findByUsername(username);
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
}
