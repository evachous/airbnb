package com.app.airbnb.model;

import lombok.Data;

import javax.persistence.*;
import java.io.File;

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
    @Column(name="isAdmin") private Boolean isAdmin;
    @Column(name="isHost") private Boolean isHost;
    @Column(name="isGuest") private Boolean isGuest;
    // private File profilePicture;
    // private String profilePicture;
    @Column(name="profilePicture", length=1000) private byte[] profilePicture;

    public User() {}

    public User(String username, String password, String firstName, String lastName, String email,
                String phone, Boolean isAdmin, Boolean isHost, Boolean isGuest, byte[] profilePicture) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.isAdmin = isAdmin;
        this.isHost = isHost;
        this.isGuest = isGuest;
        this.profilePicture = profilePicture;
    }
}
