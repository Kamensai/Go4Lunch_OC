package com.khamvongsa.victor.go4lunch.ui;

import android.os.Bundle;

import com.khamvongsa.victor.go4lunch.R;
import com.khamvongsa.victor.go4lunch.ui.fragment.DetailsRestaurantFragment;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by <Victor Khamvongsa> on <09/12/2021>
 */
public class RestaurantActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_restaurant_details);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.layout_fragment_restaurant_details, new DetailsRestaurantFragment())
                .commit();
    }
}