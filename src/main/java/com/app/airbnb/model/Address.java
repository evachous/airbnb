package com.app.airbnb.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class Address {
    @Id
    @GeneratedValue
    private Long id;

    private String label;
    private String number;
    private String road;
    private String suburb;
    private String city;
    private String state;
    private String postcode;
    private String country;
    private Double lat;
    private Double lng;

    @OneToOne(mappedBy = "address")
    @JsonIgnoreProperties("address")
    AccommodationLocation location;

    @ManyToMany(mappedBy = "addresses")
    @JsonIgnore
    private Set<SearchHistory> searchHistories = new HashSet<>();

    public Address() {}

    public Address(Double lat, Double lng) {
        this.lat = lat;
        this.lng = lng;
        this.searchHistories = new HashSet<>();
    }
}
