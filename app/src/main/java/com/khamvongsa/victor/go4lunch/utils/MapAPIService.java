package com.khamvongsa.victor.go4lunch.utils;

import com.khamvongsa.victor.go4lunch.model.NearbyRestaurantPOJO;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by <Victor Khamvongsa> on <11/05/2022>
 */
public interface MapAPIService {
    @GET("place/nearbysearch/json?")
    Observable<NearbyRestaurantPOJO>getNearby(@Query("location") String location,
                                              @Query("type") String type,
                                              @Query("radius") int radius,
                                              @Query("key") String key);

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/maps/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();


}
