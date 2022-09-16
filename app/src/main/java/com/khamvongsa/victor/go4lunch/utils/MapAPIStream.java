package com.khamvongsa.victor.go4lunch.utils;

import com.khamvongsa.victor.go4lunch.BuildConfig;
import com.khamvongsa.victor.go4lunch.model.DetailRestaurantPOJO;
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
        return mapAPIService.getNearby(location, "restaurant", 400, BuildConfig.MAPS_API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }

    public static Observable<DetailRestaurantPOJO> streamFetchDetailRestaurant(String placeId){
        MapAPIService mapAPIService = MapAPIService.retrofit.create(MapAPIService.class);
        return mapAPIService.getDetailRestaurant(placeId,"name,formatted_address,formatted_phone_number,website,photo",  BuildConfig.MAPS_API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }

    public static Observable<DetailRestaurantPOJO> streamFetchOpenRestaurant(String placeId){
        MapAPIService mapAPIService = MapAPIService.retrofit.create(MapAPIService.class);
        return mapAPIService.getDetailRestaurant(placeId,"name,formatted_address,geometry,opening_hours,photo",  BuildConfig.MAPS_API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }

    public static Observable<String> streamFetchPhotoRestaurant(String photoReference){
        MapAPIService mapAPIService = MapAPIService.retrofit.create(MapAPIService.class);
        return mapAPIService.getPhotoRestaurant(270, photoReference, BuildConfig.MAPS_API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }

    public static String getImageUrl (String photoReference){
        return MapAPIService.getImageUrl(photoReference);
    }
}
