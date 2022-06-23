package com.khamvongsa.victor.go4lunch.manager;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.Query;
import com.khamvongsa.victor.go4lunch.repositories.RestaurantRepository;

/**
 * Created by <Victor Khamvongsa> on <30/05/2022>
 */
public class RestaurantManager {

    private static volatile RestaurantManager instance;
    private RestaurantRepository restaurantRepository;

    private RestaurantManager() {
        restaurantRepository = RestaurantRepository.getInstance();
    }

    public static RestaurantManager getInstance() {
        RestaurantManager result = instance;
        if (result != null) {
            return result;
        }
        synchronized(RestaurantManager.class) {
            if (instance == null) {
                instance = new RestaurantManager();
            }
            return instance;
        }
    }

    public Query getAllRestaurants(){
        return restaurantRepository.getAllRestaurants();
    }

    public void createRestaurant(String restaurantId, String name){
        restaurantRepository.createRestaurant(restaurantId, name);
    }

    public void getUsersEatingList(String restaurantId) {
        restaurantRepository.getUsersEatingList(restaurantId);
    }

    // Add UsersEatingToArray
    public Task<Void> addUsersEating(String restaurantId) {
        return restaurantRepository.addUsersEating(restaurantId);
    }

    // Remove UsersEatingFromArray
    public Task<Void> removeUsersEating(String restaurantId) {
        return restaurantRepository.removeUsersEating(restaurantId);
    }

    // Add UsersEatingCount
    public Task<Void> addUsersEatingCount(String restaurantId) {
        return restaurantRepository.addUsersEatingCount(restaurantId);
    }

    // Decrease UsersEatingCount
    public Task<Void> decreaseUsersEatingCount(String restaurantId) {
        return restaurantRepository.decreaseUsersEatingCount(restaurantId);
    }

    // Add UsersLiking
    public Task<Void> addUsersLiking(String restaurantId) {
        return restaurantRepository.addUsersLiking(restaurantId);
    }

    // Add UsersEatingCount
    public Task<Void> addUsersLikingCount(String restaurantId) {
        return restaurantRepository.addUsersLikingCount(restaurantId);
    }

    // Decrease UsersEatingCount
    public Task<Void> decreaseUsersLikingCount(String restaurantId) {
        return restaurantRepository.decreaseUsersLikingCount(restaurantId);
    }
}