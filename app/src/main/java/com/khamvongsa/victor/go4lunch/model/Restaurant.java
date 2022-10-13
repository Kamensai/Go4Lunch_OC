package com.khamvongsa.victor.go4lunch.model;

import com.google.firebase.firestore.DocumentReference;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import androidx.annotation.Nullable;

/**
 * Created by <Victor Khamvongsa> on <23/05/2022>
 */
public class Restaurant implements Serializable {

    private String restaurantId;
    private String name;
    private String mail;
    @Nullable
    private String urlPicture;
    private String address;
    private int distance;
    private String website;
    private String phone;
    private String type;
    private int usersEatingCount;
    private int usersLikingCount;
    private Boolean favorite;
    private Boolean openingHours;
    private Boolean openNow;
    private String openUntil;
    private List<DocumentReference> usersEatingList;
    private List<String> usersLikingList;

    public Restaurant() { }

    public Restaurant (String restaurantId, String name, String address, @Nullable String urlPicture, @Nullable Boolean openNow, @Nullable String openUntil,
                       int usersLikingCount, int usersEatingCount, int distance) {
        this.restaurantId = restaurantId;
        this.name = name;
        this.address = address;
        this.urlPicture = urlPicture;
        this.openNow = openNow;
        this.openUntil = openUntil;
        this.usersLikingCount = usersLikingCount;
        this.usersEatingCount = usersEatingCount;
        this.distance = distance;
    }

    public Restaurant (Boolean openNow, String openUntil){
        this.openNow = openNow;
        this.openUntil = openUntil;
    }

    public Restaurant (String restaurantId, String name) {
        this.restaurantId = restaurantId;
        this.name = name;
        this.distance = 0;
        this.type = null;
        this.usersEatingList = null;
        this.usersLikingList = null;
        this.usersEatingCount = 0;
        this.usersLikingCount = 0;
    }

    public Restaurant (String restaurantId, String name, String address) {
        this.restaurantId = restaurantId;
        this.name = name;
        this.address = address;
        this.distance = 0;
        this.type = null;
        this.usersEatingList = null;
        this.usersLikingList = null;
        this.usersEatingCount = 0;
        this.usersLikingCount = 0;
    }
    // TODO : Collection for restaurants liked and restaurants chosen
    public Restaurant (String restaurantId) {
        this.restaurantId = restaurantId;
        this.usersEatingList = null;
        this.usersLikingList = null;
    }

    public Restaurant(String restaurantId, String name, String mail, @Nullable String urlPicture, String address, String website, String phone) {
        this.restaurantId = restaurantId;
        this.name = name;
        this.mail = mail;
        this.urlPicture = urlPicture;
        this.address = address;
        this.website = website;
        this.phone = phone;
        this.favorite = false;
        this.openingHours = false;
        this.usersEatingList = null;
    }

    // --- GETTERS ---
    public String getRestaurantId() { return restaurantId; }

    public String getName() { return name; }

    public String getMail() { return mail; }

    @Nullable
    public String getUrlPicture() { return urlPicture; }

    public String getAddress() { return address; }

    public int getDistance() { return distance; }

    public String getWebsite() { return website; }

    public String getPhone() { return phone; }

    public String getType() { return type; }

    public Boolean getFavorite() { return favorite; }

    public Boolean getOpeningHours() { return openingHours; }

    public Boolean getOpenNow() { return openNow; }

    public String getOpenUntil() { return openUntil; }

    public List<DocumentReference> getUsersEatingList() { return usersEatingList; }

    public int getUsersEatingCount() { return usersEatingCount; }

    public int getUsersLikingCount() { return usersLikingCount; }

    public List<String> getUsersLikingList() { return usersLikingList; }

    // --- SETTERS ---

    public void setRestaurantId(String restaurantId) { this.restaurantId = restaurantId; }

    public void setName(String name) { this.name = name; }

    public void setMail(String mail) { this.mail = mail; }

    public void setUrlPicture(@Nullable String urlPicture) { this.urlPicture = urlPicture; }

    public void setAddress(String address) { this.address = address; }

    public void setDistance(int distance) { this.distance = distance; }

    public void setWebsite(String website) { this.website = website; }

    public void setPhone(String phone) { this.phone = phone; }

    public void setType(String type) { this.type = type; }

    public void setFavorite(Boolean favorite) { this.favorite = favorite; }

    public void setOpeningHours(Boolean openingHours) { this.openingHours = openingHours; }

    public void setOpenNow(Boolean openNow) { this.openNow = openNow; }

    public void setOpenUntil(String openUntil) { this.openUntil = openUntil; }

    public void setUsersEatingList(List<DocumentReference> usersEatingList) { this.usersEatingList = usersEatingList; }

    public void setUsersEatingCount(int usersEatingCount) { this.usersEatingCount = usersEatingCount; }

    public void setUsersLikingList(List<String> usersLikingList) { this.usersLikingList = usersLikingList; }

    public void setUsersLikingCount(int usersLikingCount) { this.usersLikingCount = usersLikingCount; }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Restaurant)) return false;
        Restaurant restaurant = (Restaurant) o;
        return  Objects.equals(restaurantId, restaurant.restaurantId) &&
                Objects.equals(name, restaurant.name) &&
                Objects.equals(address, restaurant.address) &&
                Objects.equals(urlPicture, restaurant.urlPicture) &&
                Objects.equals(openNow, restaurant.openNow) &&
                Objects.equals(openUntil, restaurant.openUntil) &&
                Objects.equals(usersLikingCount, restaurant.usersLikingCount) &&
                Objects.equals(usersEatingCount, restaurant.usersEatingCount) &&
                Objects.equals(distance, restaurant.distance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(restaurantId, name, address, urlPicture, openNow, openUntil, usersLikingCount, usersEatingCount, distance);
    }
}
