package com.khamvongsa.victor.go4lunch.model;

import com.google.firebase.firestore.DocumentReference;

import java.util.List;
import java.util.Objects;

import androidx.annotation.Nullable;

/**
 * Created by <Victor Khamvongsa> on <25/08/2022>
 */
public class RestaurantStateItem {

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
    private String openUntil;
    private int usersEatingCount;
    private int usersLikingCount;
    private Boolean favorite;
    private Boolean openNow;
    private List<DocumentReference> usersEatingList;
    private List<String> usersLikingList;

    public RestaurantStateItem() { }

    public RestaurantStateItem(Restaurant restaurant) {
        this.restaurantId = restaurant.getRestaurantId();
        this.name = restaurant.getName();
        this.address = restaurant.getAddress();
        this.urlPicture = restaurant.getUrlPicture();
        this.openNow = restaurant.getOpenNow();
        this.openUntil = restaurant.getOpenUntil();
        this.usersLikingCount = restaurant.getUsersLikingCount();
        this.usersEatingCount = restaurant.getUsersEatingCount();
        this.distance = restaurant.getDistance();
    }

    public RestaurantStateItem (String restaurantId, String name) {
        this.restaurantId = restaurantId;
        this.name = name;
        this.type = null;
        this.usersEatingList = null;
        this.usersLikingList = null;
        this.usersEatingCount = 0;
        this.usersLikingCount = 0;
    }

    // GETTER
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

    public int getUsersEatingCount() { return usersEatingCount; }

    public int getUsersLikingCount() { return usersLikingCount; }

    public Boolean getFavorite() { return favorite; }

    public Boolean getOpenNow() { return openNow; }

    public String getOpenUntil() { return openUntil; }

    public List<DocumentReference> getUsersEatingList() { return usersEatingList; }

    public List<String> getUsersLikingList() { return usersLikingList; }


    // SETTER


    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setUrlPicture(@Nullable String urlPicture) {
        this.urlPicture = urlPicture;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUsersEatingCount(int usersEatingCount) {
        this.usersEatingCount = usersEatingCount;
    }

    public void setUsersLikingCount(int usersLikingCount) {
        this.usersLikingCount = usersLikingCount;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }

    public void setOpenNow(Boolean openNow) {
        this.openNow = openNow;
    }

    public void setOpenUntil(String openUntil) { this.openUntil = openUntil; }

    public void setUsersEatingList(List<DocumentReference> usersEatingList) {
        this.usersEatingList = usersEatingList;
    }

    public void setUsersLikingList(List<String> usersLikingList) {
        this.usersLikingList = usersLikingList;
    }

    @Override
    public boolean equals(Object o) {
        boolean urlPictureIsTrue = false;
        boolean openNowIsTrue = false;
        boolean openUntilIsTrue = false;
        if (this == o) return true;
        if (!(o instanceof RestaurantStateItem)) return false;
        RestaurantStateItem that = (RestaurantStateItem) o;
        if (that.urlPicture != null){
            urlPictureIsTrue = urlPicture.equals(that.urlPicture);
        }
        if (that.openNow != null){
            openNowIsTrue = openNow.equals(that.openNow);
        }
        if (that.openUntil != null){
            openUntilIsTrue = openUntil.equals(that.openUntil);
        }

        return restaurantId.equals(that.restaurantId) &&
                name.equals(that.name) &&
                address.equals(that.address) &&
                urlPictureIsTrue &&
                openNowIsTrue &&
                openUntilIsTrue &&
                Objects.equals(usersLikingCount, that.usersLikingCount) &&
                Objects.equals(usersEatingCount, that.usersEatingCount) &&
                Objects.equals(distance, that.distance);
    }

    @Override
    public int hashCode() { return Objects.hash(restaurantId, name, address, urlPicture, openNow, openUntil, usersLikingCount, usersEatingCount, distance); }
}
