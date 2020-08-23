package com.app.airbnb.repositories;

import com.app.airbnb.model.Chat;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long>, ChatRepositoryCustom {
}
