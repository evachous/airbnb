package com.app.airbnb.api;

import com.app.airbnb.model.User;
import com.app.airbnb.repositories.UserRepository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.Deflater;

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
    ResponseEntity<String> signup(@RequestParam("user") String jsonUser,
                                  @RequestParam("profilePicture") MultipartFile profilePicture) throws IOException {
        try {
            User newUser = new ObjectMapper().readValue(jsonUser, User.class);
            if (this.userRepository.findByUsername(newUser.getUsername()) != null) {
                System.out.println("Username exists");
                return ResponseEntity.badRequest().body("Username exists");
            } else if (this.userRepository.findByEmail(newUser.getEmail()) != null) {
                System.out.println("Email exists");
                return ResponseEntity.badRequest().body("Email exists");
            } else {
                newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
                newUser.setProfilePicture(compressBytes(profilePicture.getBytes()));
                userRepository.save(newUser);
                System.out.println("New User added");
                return new ResponseEntity<>(HttpStatus.OK);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] compressBytes(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        try {
            outputStream.close();
        } catch (IOException ioe) {
            System.out.println("Error compressing");
        }
        System.out.println("Compressed Image Byte Size - " + outputStream.toByteArray().length);
        return outputStream.toByteArray();
    }
}
