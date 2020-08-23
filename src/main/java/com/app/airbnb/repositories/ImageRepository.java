package com.app.airbnb.repositories;

import com.app.airbnb.model.Image;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
