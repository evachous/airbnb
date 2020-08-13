package com.app.airbnb.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Image {
    @Id @GeneratedValue private Long id;
    private String path;

    @OneToOne(mappedBy = "profilePicture")
    @JsonIgnoreProperties("profilePicture")
    private User user;

    @ManyToOne
    @JoinColumn(name = "accommodationImages", insertable = false, updatable = false)
    @JsonIgnoreProperties("images")
    private Accommodation accommodation;

    public Image() {}

    public Image(String path) {
        this.path = path;
    }
}
