package com.khamvongsa.victor.go4lunch.manager;

import com.google.android.gms.tasks.Task;
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
        return restaurantRepository.getAllRestaurantsLiked();
    }

    public void createRestaurant(String restaurantId, String name){
        restaurantRepository.createRestaurantLiked(restaurantId, name);
    }

    public void createChosenRestaurant(String restaurantId, String name, String address){
        restaurantRepository.createReferenceToRestaurantChosen(restaurantId, name, address);
    }

    public void getUsersEatingList(String restaurantId) {
        restaurantRepository.getUsersEatingList(restaurantId);
    }

    public void deleteUserEatingAtOtherRestaurant(String restaurantId){
        restaurantRepository.deleteUserEatingAtOtherRestaurant(restaurantId);
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

    // Remove UsersLikingFromArray
    public Task<Void> removeUsersLiking(String restaurantId) {
        return restaurantRepository.removeUsersLiking(restaurantId);
    }

    // Add UsersLikingCount
    public Task<Void> addUsersLikingCount(String restaurantId) {
        return restaurantRepository.addUsersLikingCount(restaurantId);
    }

    // Decrease UsersLikingCount
    public Task<Void> decreaseUsersLikingCount(String restaurantId) {
        return restaurantRepository.decreaseUsersLikingCount(restaurantId);
    }
}