package com.app.airbnb.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class User {
    @Id @GeneratedValue @Column(name="id") private Long id;
    @Column(name="username") private String username;
    @Column(name="password") private String password;
    @Column(name="firstName") private String firstName;
    @Column(name="lastName") private String lastName;
    @Column(name="email") private String email;
    @Column(name="phone") private String phone;
    @Column(name="city") private String city;
    @Column(name="country") private String country;
    @Column(name="isAdmin") private Boolean isAdmin;
    @Column(name="isHost") private Boolean isHost;
    @Column(name="isGuest") private Boolean isGuest;
    @Column(name="isApproved") private Boolean isApproved;
    @Column(name="profilePicture", length=1000000) private byte[] profilePicture;

    @OneToOne(cascade=CascadeType.ALL)
    @JsonIgnoreProperties("host")
    private Accommodation[] accommodations;

    public User() {}

    public User(String username, String password, String firstName, String lastName, String email,
                String phone, String city, String country, Boolean isAdmin, Boolean isHost,
                Boolean isGuest, Boolean isApproved, byte[] profilePicture) {
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
        this.profilePicture = profilePicture;

        if (!isHost) {
            this.accommodations = null;
        }
        else {
            this.accommodations = new Accommodation[100];
            for (int i = 0; i < this.accommodations.length; i++) {
                this.accommodations[i] = null;
            }
        }
    }
}
