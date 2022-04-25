package com.khamvongsa.victor.go4lunch.ui.helper;

import android.content.Context;
import android.content.Intent;

import com.khamvongsa.victor.go4lunch.ui.MainActivity;
import com.khamvongsa.victor.go4lunch.ui.RestaurantActivity;
import com.khamvongsa.victor.go4lunch.ui.SettingsActivity;
import com.khamvongsa.victor.go4lunch.ui.SignInActivity;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by <Victor Khamvongsa> on <09/03/2022>
 */
public class NavigationHelper extends AppCompatActivity {

    public static void launchNewActivity(Context context, Class mActivityToLaunch) {
        Intent i = new Intent(context, mActivityToLaunch);
        context.startActivity(i);
    }

}
