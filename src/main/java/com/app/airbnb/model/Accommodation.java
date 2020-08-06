package com.app.airbnb.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Accommodation {
    @Id @GeneratedValue private Long id;
    private String description;

    @OneToOne(cascade=CascadeType.ALL)
    @JsonIgnoreProperties("accommodation")
    private AccommodationInfo info;

    @OneToOne(cascade=CascadeType.ALL)
    @JsonIgnoreProperties("accommodation")
    private AccommodationLocation location;

    @OneToOne(cascade=CascadeType.ALL)
    @JsonIgnoreProperties("accommodation")
    private AccommodationRules rules;

    @OneToOne(mappedBy = "accommodations")
    @JsonIgnoreProperties("accommodations")
    private User host;

    public Accommodation() {}

    public Accommodation(String description) {
        this.description = description;
    }
}
