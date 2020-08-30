package com.app.airbnb.model;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
public class Reservation {
    @Id @GeneratedValue private Long id;

    //@JsonProperty("guest_id")
    //@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    //@JsonIdentityReference(alwaysAsId = true)
    @ManyToOne
    @JoinColumn(name = "user_id")
    //@JsonIgnoreProperties("reservations")
    private User guest;

    @ManyToOne
    @JoinColumn(name = "accommodation_id")
    //@JsonIgnoreProperties("reservations")
    //@JsonIgnore
    private Accommodation accommodation;

    private Integer numPeople;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long days;

    @OneToOne(cascade=CascadeType.ALL)
    @JsonIgnoreProperties("reservation")
    private Review review;

    public Reservation() {}

    public Reservation(User guest, Accommodation accommodation, Integer numPeople, LocalDate startDate,
                       LocalDate endDate, Long days) {
        this.guest = guest;
        this.accommodation = accommodation;
        this.numPeople = numPeople;
        this.startDate = startDate;
        this.endDate = endDate;
        this.days = days;
        this.review = null;
    }
}
