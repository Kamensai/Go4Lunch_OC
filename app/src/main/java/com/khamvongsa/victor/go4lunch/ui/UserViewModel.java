package com.khamvongsa.victor.go4lunch.ui;

import android.util.Log;

import com.khamvongsa.victor.go4lunch.model.User;
import com.khamvongsa.victor.go4lunch.model.UserStateItem;
import com.khamvongsa.victor.go4lunch.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

/**
 * Created by <Victor Khamvongsa> on <25/07/2022>
 */
public class UserViewModel extends ViewModel {

    private static final String TAG = UserViewModel.class.getSimpleName();

    private final UserRepository mUserRepository;

    public UserViewModel() {
        mUserRepository = UserRepository.getInstance();
    }

    private LiveData<String> mapDataToString(LiveData<String> chosenRestaurantId) {
        return Transformations.map(chosenRestaurantId, restaurantId -> {
            String mChosenRestaurantId = null;
            if (restaurantId != null) {
                mChosenRestaurantId = restaurantId;
            } else {
                Log.e(TAG, "restaurantId is null ");
            }
            return mChosenRestaurantId;
        });
    }

    //Mapping data from remote source to view data, ask to your mentor to know why it is important to do so
    private LiveData<List<UserStateItem>> mapDataToViewState(LiveData<List<User>> users) {
        return Transformations.map(users, user -> {
            List<UserStateItem> userStateItems = new ArrayList<>();
            for (User u : user) {
                userStateItems.add(
                        new UserStateItem(u)
                );
            }
            return userStateItems;
        });
    }

    public LiveData<String> getChosenRestaurantId() {
        return mapDataToString(mUserRepository.getChosenRestaurantIdMutableLiveData());
    }

    public void getUserChosenRestaurant() {
        mUserRepository.getUserChosenRestaurant();
    }

    public LiveData<List<UserStateItem>> getAllUsersSortedByChosenRestaurant() {
        return mapDataToViewState(mUserRepository.getAllUsersSortedByChosenRestaurantMutableLiveData());
    }

}
