package com.khamvongsa.victor.go4lunch.utils;

import android.media.Image;

import com.khamvongsa.victor.go4lunch.BuildConfig;
import com.khamvongsa.victor.go4lunch.model.DetailRestaurantPOJO;
import com.khamvongsa.victor.go4lunch.model.NearbyRestaurantPOJO;

import java.net.URL;

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

    @GET("place/details/json?")
    Observable<DetailRestaurantPOJO>getDetailRestaurant(@Query("place_id") String placeId,
                                                        @Query("fields") String fields,
                                                        @Query("key") String key);

    @GET("place/photo?")
    Observable<String>getPhotoRestaurant(@Query("maxheight") int maxheight,
                                      @Query("photo_reference") String photo_reference,
                                      @Query("key") String key);

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/maps/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();

    public static String getImageUrl(String photoReference){
        final String baseUrl = "https://maps.googleapis.com/maps/api/place/photo";
        final String maxHeight = "270";
        final String maxWidth = "300";
        final String url = baseUrl + "?maxheight="+ maxHeight + "&mawidtht=" + maxWidth + "&photo_reference=" + photoReference + "&key=" + BuildConfig.MAPS_API_KEY;
        String test = "https://maps.googleapis.com/maps/api/place/photo?maxheight=270&photo_reference=AeJbb3e9V0fLXbvqv2rwrLtPUZ3SmGdf2ZnxIryE9oW9iG2BaqQxQ8U4Q2CvI6IPsuT7yg35zVzevYldiHAIN9-kEnNuuLAIC74KS8D0qKECsVEMK5ETDbiRqrUYdddKqO3VsBLHN1Y8WgK0vdoF9D-WHa8zdSwZCGB4Dh4E8hxJF-mYb7hf&key=AIzaSyAGIuVcGrKxR21k8n6CiRP-J438pQYiYMo";
        return url;
    }
}
