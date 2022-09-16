package com.khamvongsa.victor.go4lunch.model;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by <Victor Khamvongsa> on <28/07/2022>
 */
public class UserStateItem {

    private final String uid;
    private final String username;
    private final String mail;
    @Nullable
    private final String urlPicture;
    private final String chosenRestaurantId;
    private final String chosenRestaurantName;

    public UserStateItem(User user) {
        this.uid = user.getUid();
        this.username = user.getUsername();
        this.mail = user.getMail();
        this.urlPicture = user.getUrlPicture();
        this.chosenRestaurantId = user.getChosenRestaurantId();
        this.chosenRestaurantName = user.getChosenRestaurantName();

    }

    public UserStateItem(@NonNull String uid, String username, String mail, @Nullable String urlPicture, String chosenRestaurantId, String chosenRestaurantName) {
        this.uid = uid;
        this.username = username;
        this.mail = mail;
        this.urlPicture = urlPicture;
        this.chosenRestaurantId = chosenRestaurantId;
        this.chosenRestaurantName = chosenRestaurantName;
    }

    // --- GETTERS ---
    public String getUid() { return uid; }
    public String getUsername() { return username; }
    public String getMail() { return mail; }
    @Nullable
    public String getUrlPicture() { return urlPicture; }
    public String getChosenRestaurantId() { return chosenRestaurantId; }
    public String getChosenRestaurantName() { return chosenRestaurantName; }
/*
    // --- SETTERS ---
    public void setUid(String uid) { this.uid = uid; }
    public void setUsername(String username) { this.username = username; }
    public void setMail(String mail) { this.mail = mail; }
    public void setUrlPicture(@Nullable String urlPicture) { this.urlPicture = urlPicture; }
    public void setChosenRestaurant(Restaurant chosenRestaurant) { this.chosenRestaurant = chosenRestaurant; }

     */

    @Override
    public boolean equals(Object o) {
        boolean urlPictureIsTrue = false;
        boolean chosenRestaurantIdTrue = false;
        boolean chosenRestaurantNameTrue = false;
        if (this == o) return true;
        if (!(o instanceof UserStateItem)) return false;
        UserStateItem that = (UserStateItem) o;
        if (that.urlPicture != null){
            if (urlPicture != null){
                urlPictureIsTrue = urlPicture.equals(that.urlPicture);
            }
        }
        if (that.chosenRestaurantId != null ){
            if (chosenRestaurantId != null){
                chosenRestaurantIdTrue = chosenRestaurantId.equals(that.chosenRestaurantId);
            }
        }
        if (that.chosenRestaurantName != null){
            if (chosenRestaurantName != null) {
                chosenRestaurantNameTrue = chosenRestaurantName.equals(that.chosenRestaurantName);
            }
        }
        return uid.equals(that.uid) &&
                username.equals(that.username) &&
                mail.equals(that.mail) &&
                urlPictureIsTrue &&
                chosenRestaurantIdTrue &&
                chosenRestaurantNameTrue;
    }

    @Override
    public int hashCode() { return Objects.hash(uid, username, mail, urlPicture, chosenRestaurantId, chosenRestaurantName); }
}
