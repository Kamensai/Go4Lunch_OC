package com.khamvongsa.victor.go4lunch.model;

import java.io.Serializable;
import java.util.Objects;

import androidx.annotation.Nullable;

/**
 * Created by <Victor Khamvongsa> on <23/05/2022>
 */
public class User implements Serializable {

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return  Objects.equals(uid, user.uid) &&
                Objects.equals(username, user.username) &&
                Objects.equals(mail, user.mail) &&
                Objects.equals(urlPicture, user.urlPicture) &&
                Objects.equals(chosenRestaurant, user.chosenRestaurant);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uid, username, mail, urlPicture, chosenRestaurant);
    }
}

