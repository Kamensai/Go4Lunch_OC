package com.khamvongsa.victor.go4lunch.model;

import java.util.Objects;

/**
 * Created by <Victor Khamvongsa> on <13/09/2022>
 */
public class RestaurantEatingItem {
    private String restaurantId;
    private String name;
    private int usersEatingCount;

    public RestaurantEatingItem() {}

    public RestaurantEatingItem(Restaurant restaurant) {
        this.restaurantId = restaurant.getRestaurantId();
        this.name = restaurant.getName();
        this.usersEatingCount = restaurant.getUsersEatingCount();
    }

    // GETTER


    public String getRestaurantId() {
        return restaurantId;
    }

    public String getName() {
        return name;
    }

    public int getUsersEatingCount() {
        return usersEatingCount;
    }

    // SETTER

    public void setName(String name) {
        this.name = name;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public void setUsersEatingCount(int usersEatingCount) {
        this.usersEatingCount = usersEatingCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RestaurantEatingItem)) return false;
        RestaurantEatingItem that = (RestaurantEatingItem) o;

        return restaurantId.equals(that.restaurantId) &&
                name.equals(that.name) &&
                Objects.equals(usersEatingCount, that.usersEatingCount);
    }

    @Override
    public int hashCode() { return Objects.hash(restaurantId, name, usersEatingCount); }
}
