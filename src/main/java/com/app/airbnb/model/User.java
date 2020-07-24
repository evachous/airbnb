package com.app.airbnb.model;

import lombok.Data;

import javax.persistence.*;
import java.io.File;

@Data
@Entity
public class User {
    private @Id @GeneratedValue Long id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Boolean isAdmin;
    private Boolean isHost;
    private Boolean isGuest;
    private File profilePicture;

    public User() {}

    public User(String username, String password, String firstName, String lastName, String email,
                String phone, Boolean isAdmin, Boolean isHost, Boolean isGuest, File profilePicture) {
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
