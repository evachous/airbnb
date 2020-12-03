package com.app.airbnb.repositories;

import com.app.airbnb.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserRepositoryCustom {
    User findByUsername(String username);
    User findByEmail(String email);
    List<User> findAllHosts();
    List<User> findAllGuests();
    String uploadImage(MultipartFile image, String type);
    String uploadImageByURL(String imageURL, String filename, String type);
}
