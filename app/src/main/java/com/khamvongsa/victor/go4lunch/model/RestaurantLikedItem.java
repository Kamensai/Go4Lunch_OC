package com.khamvongsa.victor.go4lunch.model;

import java.util.Objects;

/**
 * Created by <Victor Khamvongsa> on <09/09/2022>
 */
public class RestaurantLikedItem {

    private String restaurantId;
    private String name;
    private int usersLikingCount;

    public RestaurantLikedItem() {}

    public RestaurantLikedItem(Restaurant restaurant) {
        this.restaurantId = restaurant.getRestaurantId();
        this.name = restaurant.getName();
        this.usersLikingCount = restaurant.getUsersLikingCount();
    }

    // GETTER


    public String getRestaurantId() {
        return restaurantId;
    }

    public String getName() {
        return name;
    }

    public int getUsersLikingCount() {
        return usersLikingCount;
    }

    // SETTER

    public void setName(String name) {
        this.name = name;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public void setUsersLikingCount(int usersLikingCount) {
        this.usersLikingCount = usersLikingCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RestaurantLikedItem)) return false;
        RestaurantLikedItem that = (RestaurantLikedItem) o;

        return restaurantId.equals(that.restaurantId) &&
                name.equals(that.name) &&
                Objects.equals(usersLikingCount, that.usersLikingCount);
    }

    @Override
    public int hashCode() { return Objects.hash(restaurantId, name, usersLikingCount); }
}
