package com.app.airbnb.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class AccommodationLocation {
    @Id @GeneratedValue private Long id;
    private String address;
    private String neighborhood;
    private String transportation;

    @OneToOne(mappedBy = "location")
    @JsonIgnoreProperties("location")
    private Accommodation accommodation;
}
