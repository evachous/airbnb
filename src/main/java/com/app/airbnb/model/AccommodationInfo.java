package com.app.airbnb.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class AccommodationInfo {
    @Id @GeneratedValue private Long id;
    private String type;
    private Integer beds;
    private Integer bedrooms;
    private Integer bathrooms;
    private Boolean livingRoom;
    private Integer area;
    private String description;

    @OneToOne(mappedBy = "info")
    @JsonIgnoreProperties("info")
    private Accommodation accommodation;

    public AccommodationInfo() {}
}
