package com.khamvongsa.victor.go4lunch.utils;

import com.khamvongsa.victor.go4lunch.BuildConfig;
import com.khamvongsa.victor.go4lunch.model.NearbyRestaurantPOJO;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by <Victor Khamvongsa> on <11/05/2022>
 */
public class MapAPIStream {

    public static Observable<NearbyRestaurantPOJO> streamFetchNearbyRestaurant(String location){
        MapAPIService mapAPIService = MapAPIService.retrofit.create(MapAPIService.class);
        return mapAPIService.getNearby(location, "restaurant", 200, BuildConfig.MAPS_API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }


}
