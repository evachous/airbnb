package com.app.airbnb.model;

import com.app.airbnb.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@Slf4j
class LoadDatabase {

    @Bean
    CommandLineRunner initDatabase(UserRepository repository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        return args -> {
            // String encodedPassword3 = bCryptPasswordEncoder.encode("charli");
            // log.info("Preloading " + repository.save(new User("charli", encodedPassword3, "charli", "charli", "charli@example.com", "1234567890", "london", "uk", true, false, false, null)));
            log.info("Encrypted charli " + bCryptPasswordEncoder.encode("charli"));
        };
    }
}
