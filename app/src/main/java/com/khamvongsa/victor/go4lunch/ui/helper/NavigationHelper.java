package com.khamvongsa.victor.go4lunch.ui.helper;

import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by <Victor Khamvongsa> on <09/03/2022>
 */
public class NavigationHelper extends AppCompatActivity {

    public static final String KEY_PLACE_ID = "placeId";

    public static void launchNewActivity(Context context, Class mActivityToLaunch) {
        Intent i = new Intent(context, mActivityToLaunch);
        context.startActivity(i);
    }

    public static void launchRestaurantActivity(Context context, Class mActivityToLaunch, String placeId) {
        Intent i = new Intent(context, mActivityToLaunch);
        i.putExtra(KEY_PLACE_ID, placeId);
        context.startActivity(i);
    }

}
