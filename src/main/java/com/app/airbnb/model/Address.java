package com.app.airbnb.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Address {
    @Id @GeneratedValue private Long id;
    private String label;
    private String road;
    private String city;
    private String country;
    private String postcode;
    private String suburb;
    private String number;
    private Double lat;
    private Double lng;

    @OneToOne(mappedBy = "address")
    @JsonIgnoreProperties("address")
    AccommodationLocation location;
}
