package com.khamvongsa.victor.go4lunch.ui;

import android.util.Log;

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

    private final MutableLiveData<List<String>> updateUsersLikingListId = new MutableLiveData<>();


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

    public LiveData<List<String>> getAllUsersIdLikeList() {
        return mapDataToStringList(mRestaurantRepository.getAllUsersLikingListId());
    }

    private LiveData<List<String>> mapDataToStringList(LiveData<List<String>> usersId) {
        return Transformations.map(usersId, userId -> {
            ArrayList<String> usersIdList = new ArrayList<>();
            if (userId != null){
                for (String uid : userId) {
                    usersIdList.add(uid);
                }
            } else {
                Log.e(TAG, "List null " );
            }
            return usersIdList;
        });
    }


    public void getUsersLikingListId(String restaurantId) {
        mRestaurantRepository.getUsersLikingListId(restaurantId);
    }
/*
    public LiveData<List<String>> getUsersLikingListIdLiveData() {
        return updateUsersLikingListId ;

    }

     */

    //Mapping data from remote source to view data, ask to your mentor to know why it is important to do so
    /*
    private LiveData<List<FreelanceStateItem>> mapDataToViewState(LiveData<List<Freelance>> freelances) {
        return Transformations.map(freelances, freelance -> {
            List<FreelanceStateItem> freelanceViewStateItems = new ArrayList<>();
            for (Freelance f : freelance) {
                freelanceViewStateItems.add(
                        new FreelanceStateItem(f)
                );
            }
            return freelanceViewStateItems;
        });
    }


    public LiveData<List<FreelanceStateItem>> getAllFreelances() {
        return mapDataToViewState(repository.getAllFreelances());
    }

    public LiveData<List<FreelanceStateItem>> getAllFreelancesSortedByTJM() {
        return mapDataToViewState(repository.getAllFreelancesSortedByTJM());

    }

     */


}
