package com.khamvongsa.victor.go4lunch.model;

import java.util.List;

/**
 * Created by <Victor Khamvongsa> on <05/10/2022>
 */
public class RestaurantNotification {

    private String name;
    private String address;
    private List<String> usersEatingList;

    public RestaurantNotification() { }

    public RestaurantNotification (String name, String address, List<String> usersEatingList){
        this.name = name;
        this.address = address;
        this.usersEatingList = usersEatingList;
    }

    // GETTER
    public String getName() { return name; }

    public String getAddress() {
        return address;
    }

    public List<String> getUsersEatingList() {
        return usersEatingList;
    }

    // SETTER
    public void setName(String name) { this.name = name; }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setUsersEatingList(List<String> usersEatingList) {
        this.usersEatingList = usersEatingList;
    }
}


