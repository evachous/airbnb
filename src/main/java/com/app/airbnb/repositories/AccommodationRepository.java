package com.app.airbnb.repositories;

import com.app.airbnb.model.Accommodation;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccommodationRepository extends JpaRepository<Accommodation, Long>, AccommodationRepositoryCustom {

}
