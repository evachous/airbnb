package com.app.airbnb.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Accommodation {
    @Id @GeneratedValue private Long id;

    @OneToOne(cascade=CascadeType.ALL)
    @JsonIgnoreProperties("accommodation")
    private AccommodationInfo info;

    @OneToOne(cascade=CascadeType.ALL)
    @JsonIgnoreProperties("accommodation")
    private AccommodationLocation location;

    @OneToOne(cascade=CascadeType.ALL)
    @JsonIgnoreProperties("accommodation")
    private AccommodationRules rules;

    @ManyToOne
    @JoinColumn(name = "host", insertable = false, updatable = false)
    @JsonIgnoreProperties("accommodations")
    private User host;

    @OneToMany(cascade=CascadeType.ALL)
    @JoinColumn(name = "accommodationImages")
    @JsonIgnoreProperties("accommodationImages")
    private List<Image> images;

    public Accommodation() {}

    public Accommodation(AccommodationInfo info, AccommodationLocation location, AccommodationRules rules, User host) {
        this.info = info;
        this.location = location;
        this.rules = rules;
        this.host = host;
    }
}