package com.app.airbnb.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Review {
    @Id @GeneratedValue private Long id;

    @OneToOne(mappedBy = "review")
    @JsonIgnoreProperties("review")
    private Reservation reservation;

    private String text;
    private Double rating;
}
