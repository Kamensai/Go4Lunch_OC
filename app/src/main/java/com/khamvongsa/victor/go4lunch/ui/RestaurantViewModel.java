package com.khamvongsa.victor.go4lunch.ui;

import android.util.Log;

import com.khamvongsa.victor.go4lunch.model.User;
import com.khamvongsa.victor.go4lunch.model.UserStateItem;
import com.khamvongsa.victor.go4lunch.repositories.RestaurantRepository;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

/**
 * Created by <Victor Khamvongsa> on <19/07/2022>
 */
public class RestaurantViewModel extends ViewModel {

    private static final String TAG = RestaurantViewModel.class.getSimpleName();

    private final RestaurantRepository mRestaurantRepository;

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

    //Mapping data from remote source to view data, ask to your mentor to know why it is important to do so
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

    public LiveData<List<String>> getAllUsersLikeIdList() {
        return mapDataToStringList(mRestaurantRepository.getAllUsersLikingIdListMutableLiveData());
    }

    public void getUsersLikingIdList(String restaurantId) {
        mRestaurantRepository.getUsersLikingListId(restaurantId);
    }

    public LiveData<List<UserStateItem>> getAllUsersEatingList() {
        return mapUserDataToViewState(mRestaurantRepository.getAllUsersEatingListMutableLiveData());
    }

    public void getUsersEatingList(String restaurantId) {
        mRestaurantRepository.getUsersEatingList(restaurantId);
    }


/*
    public LiveData<List<FreelanceStateItem>> getAllFreelances() {
        return mapDataToViewState(repository.getAllFreelances());
    }

    public LiveData<List<FreelanceStateItem>> getAllFreelancesSortedByTJM() {
        return mapDataToViewState(repository.getAllFreelancesSortedByTJM());
    }

     */


}
