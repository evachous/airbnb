package com.app.airbnb.model;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class SearchHistory {
    @Id
    @GeneratedValue
    private Long id;

    @OneToOne(mappedBy = "searchHistory")
    @JsonIgnoreProperties("searchHistory")
    private User guest;

    @ManyToMany(cascade=CascadeType.ALL)
    @JoinTable(name = "search_accommodations",
            joinColumns = { @JoinColumn(name = "fk_search_id") },
            inverseJoinColumns = { @JoinColumn(name = "fk_accommodation_id") })
    private Set<Accommodation> accommodations;

    @ManyToMany(cascade=CascadeType.ALL)
    @JoinTable(name = "search_addresses",
            joinColumns = { @JoinColumn(name = "fk_search_id") },
            inverseJoinColumns = { @JoinColumn(name = "fk_address_id") })
    private Set<Address> addresses;

    public SearchHistory() {}

    public void addAccommodation(Accommodation accommodation) {
        this.accommodations.add(accommodation);
        accommodation.getSearchHistories().add(this);
    }

    public void addAddress(Address address) {
        this.addresses.add(address);
        address.getSearchHistories().add(this);
    }
}
