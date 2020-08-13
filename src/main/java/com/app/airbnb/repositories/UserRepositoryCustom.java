package com.app.airbnb.repositories;

import com.app.airbnb.model.User;
import org.springframework.web.multipart.MultipartFile;

public interface UserRepositoryCustom {
    User findByUsername(String username);
    User findByEmail(String email);
    byte[] compressBytes(byte[] data);
    byte[] decompressBytes(byte[] data);
    String uploadImage(MultipartFile image);
}
