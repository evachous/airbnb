package com.app.airbnb.repositories;

import com.app.airbnb.model.Chat;

import java.util.List;

public interface ChatRepositoryCustom {
    List<Chat> findByAccommodation(Long accommodationID);
    List<Chat> findByHostUsername(String username);
    List<Chat> findByGuestUsername(String username);
    Chat findByAccommodationAndGuest(Long accommodationID, String guestUsername);
}
