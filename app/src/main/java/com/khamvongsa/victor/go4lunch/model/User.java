package com.khamvongsa.victor.go4lunch.model;

import java.io.Serializable;

import androidx.annotation.Nullable;

/**
 * Created by <Victor Khamvongsa> on <23/05/2022>
 */
public class User  {

    private String uid;
    private String username;
    private String mail;
    @Nullable
    private String urlPicture;
    private Restaurant chosenRestaurant;

    public User() { }

    public User(String uid, String username, String mail, @Nullable String urlPicture) {
        this.uid = uid;
        this.username = username;
        this.mail = mail;
        this.urlPicture = urlPicture;
        this.chosenRestaurant = null;
    }

    // --- GETTERS ---
    public String getUid() { return uid; }
    public String getUsername() { return username; }
    public String getMail() { return mail; }
    @Nullable
    public String getUrlPicture() { return urlPicture; }
    public Restaurant getChosenRestaurant() { return chosenRestaurant; }

    // --- SETTERS ---
    public void setUid(String uid) { this.uid = uid; }
    public void setUsername(String username) { this.username = username; }
    public void setMail(String mail) { this.mail = mail; }
    public void setUrlPicture(@Nullable String urlPicture) { this.urlPicture = urlPicture; }
    public void setChosenRestaurant(Restaurant chosenRestaurant) { this.chosenRestaurant = chosenRestaurant; }

}

