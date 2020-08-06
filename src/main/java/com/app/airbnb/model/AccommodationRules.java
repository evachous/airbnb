package com.app.airbnb.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class AccommodationRules {
    @Id private Long id;
    private Boolean smoking;
    private Boolean pets;
    private Boolean events;
    private Integer minDays;

    @OneToOne(mappedBy = "rules")
    @JsonIgnoreProperties("rules")
    private Accommodation accommodation;
}
