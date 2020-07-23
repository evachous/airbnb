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
            /*String encodedPassword1 = bCryptPasswordEncoder.encode("5678");
            log.info("Preloading " + repository.save(new User("katya", encodedPassword1, "Katya", "Zamo", "katya@example.com", "1234567890", null, false)));
            String encodedPassword2 = bCryptPasswordEncoder.encode("1234");
            log.info("Preloading " + repository.save(new User("im_phoebe", encodedPassword2, "Phoebe", "B", "p@example.com", "9876543210", null, false)));
            String encodedPassword3 = bCryptPasswordEncoder.encode("charli");
            log.info("Preloading " + repository.save(new User("charli", encodedPassword3, "charli", "charli", "charli@example.com", "1234567890", null, true)));*/
            // log.info("Encrypted katya " + bCryptPasswordEncoder.encode("5678"));
            // log.info("Encrypted phoebe " + bCryptPasswordEncoder.encode("1234"));
            log.info("Encrypted charli " + bCryptPasswordEncoder.encode("charli"));
        };
    }
}
