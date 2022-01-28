package com.khamvongsa.victor.go4lunch.ui;

import android.os.Bundle;

import com.khamvongsa.victor.go4lunch.R;
import com.khamvongsa.victor.go4lunch.ui.fragment.SettingsFragment;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by <Victor Khamvongsa> on <26/01/2022>
 */
public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_settings);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings_fragment_content, new SettingsFragment())
                .commit();
    }
}
