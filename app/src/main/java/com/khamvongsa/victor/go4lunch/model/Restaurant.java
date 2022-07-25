package com.khamvongsa.victor.go4lunch.model;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

/**
 * Created by <Victor Khamvongsa> on <23/05/2022>
 */
public class Restaurant {

    private String restaurantId;
    private String name;
    private String mail;
    @Nullable
    private String urlPicture;
    private String address;
    private String distance;
    private String website;
    private String phone;
    private String type;
    private int usersEatingCount;
    private Number usersLikingCount;
    private Boolean favorite;
    private Boolean openingHours;
    private List<String> usersEatingList;
    private List<String> usersLikingList;

    public Restaurant() { }

    public Restaurant (String restaurantId, String name) {
        this.restaurantId = restaurantId;
        this.name = name;
        this.type = null;
        this.usersEatingList = null;
        this.usersLikingList = null;
        this.usersEatingCount = 0;
        this.usersLikingCount = 0;
    }
    // TODO : Collection for restaurants liked and restaurants chosen
    public Restaurant (String restaurantId, String name, String type) {
        this.restaurantId = restaurantId;
        this.name = name;
        this.type = type;
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
        this.distance = null;
        this.usersEatingList = null;

    }

    // --- GETTERS ---
    public String getRestaurantId() { return restaurantId; }

    public String getName() { return name; }

    public String getMail() { return mail; }

    @Nullable
    public String getUrlPicture() { return urlPicture; }

    public String getAddress() { return address; }

    public String getDistance() { return distance; }

    public String getWebsite() { return website; }

    public String getPhone() { return phone; }

    public String getType() { return type; }

    public Boolean getFavorite() { return favorite; }

    public Boolean getOpeningHours() { return openingHours; }

    public List<String> getUsersEatingList() { return usersEatingList; }

    public int getUsersEatingCount() { return usersEatingCount; }

    public Number getUsersLikingCount() { return usersLikingCount; }

    public List<String> getUsersLikingList() { return usersLikingList; }

    // --- SETTERS ---

    public void setRestaurantId(String restaurantId) { this.restaurantId = restaurantId; }

    public void setName(String name) { this.name = name; }

    public void setMail(String mail) { this.mail = mail; }

    public void setUrlPicture(@Nullable String urlPicture) { this.urlPicture = urlPicture; }

    public void setAddress(String address) { this.address = address; }

    public void setDistance(String distance) { this.distance = distance; }

    public void setWebsite(String website) { this.website = website; }

    public void setPhone(String phone) { this.phone = phone; }

    public void setType(String type) { this.type = type; }

    public void setFavorite(Boolean favorite) { this.favorite = favorite; }

    public void setOpeningHours(Boolean openingHours) { this.openingHours = openingHours; }

    public void setUsersEatingList(List<String> usersEatingList) { this.usersEatingList = usersEatingList; }

    public void setUsersEatingCount(int usersEatingCount) { this.usersEatingCount = usersEatingCount; }

    public void setUsersLikingList(List<String> usersLikingList) { this.usersLikingList = usersLikingList; }

    public void setUsersLikingCount(Number usersLikingCount) { this.usersLikingCount = usersLikingCount; }
}
