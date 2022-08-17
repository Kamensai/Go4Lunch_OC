package com.khamvongsa.victor.go4lunch.ui;

import android.util.Log;

import com.khamvongsa.victor.go4lunch.repositories.UserRepository;

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

    public LiveData<String> getChosenRestaurantId() {
        return mapDataToString(mUserRepository.getChosenRestaurantIdMutableLiveData());
    }

    public void getUserChosenRestaurant() {
        mUserRepository.getUserChosenRestaurant();
    }

}
