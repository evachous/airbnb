package com.app.airbnb.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class AccommodationLocation {
    @Id @GeneratedValue private Long id;
    private String transportation;

    @OneToOne(cascade=CascadeType.ALL)
    @JsonIgnoreProperties("location")
    private Address address;

    @OneToOne(mappedBy = "location")
    @JsonIgnoreProperties("location")
    private Accommodation accommodation;

    public AccommodationLocation() {}
}
