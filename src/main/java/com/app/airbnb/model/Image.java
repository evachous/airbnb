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

    public Image() {}

    public Image(String path) {
        this.path = path;
    }
}
