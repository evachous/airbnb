package com.app.airbnb.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
public class Reservation {
    @Id @GeneratedValue private Long id;

    @ManyToOne(cascade=CascadeType.ALL)
    // @JoinColumn(name = "guestReservations", insertable = false, updatable = false)
    @JsonIgnoreProperties("reservations")
    private User guest;

    @ManyToOne(cascade=CascadeType.ALL)
    // @JoinColumn(name = "accommodationReservations", insertable = false, updatable = false)
    @JsonIgnoreProperties("reservations")
    private Accommodation accommodation;

    private String review;
    private Double rating;
    private Integer days;
    private Integer people;
    private LocalDate startDate;
    private LocalDate endDate;

    public Reservation() {}
}
