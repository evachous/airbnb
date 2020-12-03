package com.app.airbnb.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
public class AccommodationInfo {
    @Id @GeneratedValue private Long id;
    private String name;

    private LocalDate startDate;
    private LocalDate endDate;

    private Double minCost;
    private Double costPerPerson;

    private String type;
    private Integer beds;
    private Integer bedrooms;
    private Double bathrooms;
    private Boolean livingRoom;
    private Integer area;
    @Column(length=500)
    private String description;

    private Boolean internet;
    private Boolean ac;
    private Boolean heating;
    private Boolean kitchen;
    private Boolean tv;
    private Boolean parking;
    private Boolean elevator;

    @OneToOne(mappedBy = "info")
    @JsonIgnoreProperties("info")
    private Accommodation accommodation;

    public AccommodationInfo() {}
}
