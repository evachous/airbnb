package com.app.airbnb.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class User {
    @Id @GeneratedValue private Long id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String city;
    private String country;
    private Boolean isAdmin;
    private Boolean isHost;
    private Boolean isGuest;
    private Boolean isApproved;

    @OneToMany(mappedBy = "host")
    @JsonIgnoreProperties("host")
    private List<Accommodation> accommodations;

    @OneToOne(cascade=CascadeType.ALL)
    @JsonIgnoreProperties("user")
    private Image profilePicture;

    private Double rating;

    /*@OneToMany(mappedBy = "guest")
    @JsonIgnoreProperties("guest")
    private List<Reservation> reservations;*/

    @OneToMany(mappedBy = "guest")
    @JsonIgnoreProperties("guest")
    private List<Chat> chats;

    public User() {}

    public User(String username, String password, String firstName, String lastName, String email,
                String phone, String city, String country, Boolean isAdmin, Boolean isHost,
                Boolean isGuest, Boolean isApproved) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.city = city;
        this.country = country;
        this.isAdmin = isAdmin;
        this.isHost = isHost;
        this.isGuest = isGuest;
        this.isApproved = isApproved;
        this.rating = 0.0;
    }
}
