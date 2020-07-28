package com.app.airbnb.repositories;

import com.app.airbnb.model.User;

public interface UserRepositoryCustom {
    User findByUsername(String username);
    User findByEmail(String email);
    byte[] compressBytes(byte[] data);
    byte[] decompressBytes(byte[] data);
}
