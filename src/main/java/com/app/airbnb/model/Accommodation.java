package com.app.airbnb.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Accommodation {
    @Id
    @GeneratedValue
    @Column(name = "accommodation_id")
    private Long id;

    @OneToOne(cascade=CascadeType.ALL)
    @JsonIgnoreProperties("accommodation")
    private AccommodationInfo info;

    @OneToOne(cascade=CascadeType.ALL)
    @JsonIgnoreProperties("accommodation")
    private AccommodationLocation location;

    @OneToOne(cascade=CascadeType.ALL)
    @JsonIgnoreProperties("accommodation")
    private AccommodationRules rules;

    @ManyToOne(cascade=CascadeType.ALL)
    @JsonIgnoreProperties("accommodations")
    private User host;

    @OneToMany(mappedBy = "accommodation")
    @JsonIgnoreProperties("accommodation")
    private List<Image> images;

    /*@OneToMany(mappedBy = "accommodation")
    @JsonIgnoreProperties("accommodation")
    private List<Reservation> reservations;*/

    //@OneToMany(mappedBy = "accommodation")
    //@JsonIgnoreProperties("accommodation")
    //private List<Chat> chats;

    private Double avgRating;

    public Accommodation() {}

    public Accommodation(AccommodationInfo info, AccommodationLocation location, AccommodationRules rules, User host) {
        this.info = info;
        this.location = location;
        this.rules = rules;
        this.host = host;
        this.avgRating = 0.0;
    }
}
