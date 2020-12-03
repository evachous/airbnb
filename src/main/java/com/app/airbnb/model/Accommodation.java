package com.app.airbnb.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @ManyToOne
    @JsonIgnoreProperties("accommodations")
    private User host;

    @OneToMany(mappedBy = "accommodation")
    @JsonIgnoreProperties("accommodation")
    private List<Image> images;

    @ManyToMany(mappedBy = "accommodations")
    @JsonIgnore
    private Set<SearchHistory> searchHistories = new HashSet<>();

    private Integer numRatings;
    private Double avgRating;
    private Long datasetId;

    public Accommodation() {}

    public Accommodation(AccommodationInfo info, AccommodationLocation location, AccommodationRules rules, User host) {
        this.info = info;
        this.location = location;
        this.rules = rules;
        this.host = host;
        this.numRatings = 0;
        this.avgRating = 0.0;
        this.datasetId = (long)-1;
        //this.searchHistories = new HashSet<>();
    }

    public void addSearch(SearchHistory searchHistory) {
        this.searchHistories.add(searchHistory);
        //searchHistory.getAccommodations().add(this);
    }
}
