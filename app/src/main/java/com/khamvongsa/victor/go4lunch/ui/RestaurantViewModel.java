package com.khamvongsa.victor.go4lunch.ui;

import android.util.Log;

import com.khamvongsa.victor.go4lunch.model.Restaurant;
import com.khamvongsa.victor.go4lunch.model.RestaurantEatingItem;
import com.khamvongsa.victor.go4lunch.model.RestaurantLikedItem;
import com.khamvongsa.victor.go4lunch.model.RestaurantNotification;
import com.khamvongsa.victor.go4lunch.model.RestaurantStateItem;
import com.khamvongsa.victor.go4lunch.model.User;
import com.khamvongsa.victor.go4lunch.model.UserStateItem;
import com.khamvongsa.victor.go4lunch.repositories.RestaurantRepository;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

/**
 * Created by <Victor Khamvongsa> on <19/07/2022>
 */
public class RestaurantViewModel extends ViewModel {

    private static final String TAG = RestaurantViewModel.class.getSimpleName();

    private final RestaurantRepository mRestaurantRepository;

    private final MutableLiveData<List<Restaurant>> mRestaurantList = new MutableLiveData<>();

    public RestaurantViewModel() {
        mRestaurantRepository = RestaurantRepository.getInstance();
    }

/*
    public RestaurantViewModel() {
        mRestaurantRepository = RestaurantRepository.getInstance();

        mRestaurantRepository.getAllUsersLikingListId().observeForever(new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> usersIdList) {
                updateUsersLikingListId.postValue(usersIdList);
            }
        });
    }

 */

    public void setRestaurantList(List<Restaurant> restaurantList) {
        mRestaurantList.setValue(restaurantList);
    }

    public LiveData<List<RestaurantStateItem>> getAllRestaurant() {
        return mapRestaurantDataToViewState(mRestaurantList);
    }

    //Mapping data from remote source to view data, ask to your mentor to know why it is important to do so
    private LiveData<List<RestaurantStateItem>> mapRestaurantDataToViewState(LiveData<List<Restaurant>> restaurants) {
        return Transformations.map(restaurants, restaurant -> {
            List<RestaurantStateItem> restaurantStateItems = new ArrayList<>();
            if (restaurant != null){
                for (Restaurant r : restaurant) {
                    restaurantStateItems.add(
                            new RestaurantStateItem(r)
                    );
                }
                Log.e(TAG, restaurantStateItems.toString() );
            }else {
                Log.e(TAG, "RestaurantList null ! " );
            }
            return restaurantStateItems;
        });
    }

    private LiveData<List<UserStateItem>> mapUserDataToViewState(LiveData<List<User>> users) {
        return Transformations.map(users, user -> {
            List<UserStateItem> userViewStateItems = new ArrayList<>();
            if (user != null){
                for (User u : user) {
                    userViewStateItems.add(
                            new UserStateItem(u)
                    );
                }
            }else {
                Log.e(TAG, "UserEatingList null " );
            }
            return userViewStateItems;
        });
    }

    private LiveData<List<RestaurantLikedItem>> mapRestaurantLikedDataToViewState(LiveData<List<Restaurant>> restaurants) {
        return Transformations.map(restaurants, restaurant -> {
            List<RestaurantLikedItem> restaurantViewStateItems = new ArrayList<>();
            if (restaurant != null){
                for (Restaurant r : restaurant) {
                    restaurantViewStateItems.add(
                            new RestaurantLikedItem(r)
                    );
                }
            }else {
                Log.e(TAG, "UserEatingList null " );
            }
            return restaurantViewStateItems;
        });
    }

    private LiveData<List<RestaurantEatingItem>> mapRestaurantEatingDataToViewState(LiveData<List<Restaurant>> restaurants) {
        return Transformations.map(restaurants, restaurant -> {
            List<RestaurantEatingItem> restaurantViewEatingItems = new ArrayList<>();
            if (restaurant != null){
                for (Restaurant r : restaurant) {
                    restaurantViewEatingItems.add(
                            new RestaurantEatingItem(r)
                    );
                }
            }else {
                Log.e(TAG, "UserEatingList null " );
            }
            return restaurantViewEatingItems;
        });
    }

    private LiveData<List<String>> mapDataToStringList(LiveData<List<String>> usersId) {
        return Transformations.map(usersId, userId -> {
            ArrayList<String> usersIdList = new ArrayList<>();
            if (userId != null){
                for (String uid : userId) {
                    usersIdList.add(uid);
                }
            } else {
                Log.e(TAG, "UserLikingList null " );
            }
            return usersIdList;
        });
    }

    // Getting Lists
    public LiveData<List<String>> getAllUsersLikeIdList() {
        return mapDataToStringList(mRestaurantRepository.getAllUsersLikingIdListMutableLiveData());
    }

    public void getUsersLikingIdList(String restaurantId) {
        mRestaurantRepository.getUsersLikingListId(restaurantId);
    }

    public LiveData<List<UserStateItem>> getAllUsersEatingList() {
        return mapUserDataToViewState(mRestaurantRepository.getAllUsersEatingListMutableLiveData());
    }

    public LiveData<RestaurantNotification> getRestaurantNotification() {
        return mRestaurantRepository.getRestaurantNotificationMutableLiveData();
    }

    public void getRestaurantInfoNotification(String restaurantId) {
        mRestaurantRepository.getInfoNotification(restaurantId);
    }

    public void getUsersEatingList(String restaurantId) {
        mRestaurantRepository.getUsersEatingList(restaurantId);
    }

    public LiveData<List<RestaurantLikedItem>> getAllRestaurantLikedList() {
        return mapRestaurantLikedDataToViewState(mRestaurantRepository.getAllRestaurantLikedListMutableLiveData());
    }

    public void getRestaurantsLikedList() {
       mRestaurantRepository.getRestaurantsLikedList();
    }

    public LiveData<List<RestaurantEatingItem>> getAllRestaurantEatingList() {
        return mapRestaurantEatingDataToViewState(mRestaurantRepository.getAllRestaurantEatingListMutableLiveData());
    }

    public void getRestaurantsEatingList() {
        mRestaurantRepository.getRestaurantsEatingList();
    }

}
