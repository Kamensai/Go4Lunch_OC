package com.khamvongsa.victor.go4lunch.model;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by <Victor Khamvongsa> on <21/09/2022>
 */
public class RestaurantLocation {

    private String restaurantId;
    private String name;
    private LatLng latLng;

    public RestaurantLocation() { }

    public RestaurantLocation (String restaurantId, String name, LatLng latLng){
        this.restaurantId = restaurantId;
        this.name = name;
        this.latLng = latLng;
    }

    // GETTER
    public String getRestaurantId() { return restaurantId; }

    public String getName() { return name; }

    public LatLng getLatLng() { return latLng; }

    // SETTER

    public void setRestaurantId(String restaurantId) { this.restaurantId = restaurantId; }

    public void setName(String name) { this.name = name; }

    public void setLatLng(LatLng latLng) { this.latLng = latLng; }
}
