package com.khamvongsa.victor.go4lunch.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;

import com.khamvongsa.victor.go4lunch.R;
import com.khamvongsa.victor.go4lunch.ui.MainActivity;
import com.khamvongsa.victor.go4lunch.ui.SettingsActivity;
import com.khamvongsa.victor.go4lunch.ui.helper.LocaleHelper;

import java.util.Locale;
import java.util.Objects;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

/**
 * Created by <Victor Khamvongsa> on <21/01/2022>
 */
public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener{

    public static final String LANGUAGE = "language";


    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences_settings, rootKey);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(LANGUAGE))
        {
            //changeLanguagePref(requireContext(), PreferenceManager.getDefaultSharedPreferences(requireContext()).getString(key, ""));
            /*requireActivity().setContentView(R.layout.fragment_settings);
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings_fragment_content, new SettingsFragment())
                    .commit();

             */
            //Context context = LocaleHelper.setLocale(getContext(), sharedPreferences.getString(key,""));

            //changeLanguagePref(getContext(), PreferenceManager.getDefaultSharedPreferences(requireContext()).getString(key, ""));
            //LocaleHelper.setLocale(getContext(), PreferenceManager.getDefaultSharedPreferences(getContext()).getString(key, ""));
            //requireActivity().recreate(); // necessary here because this Activity is currently running and thus a recreate() in onResume() would be too late
            requireActivity().finish();
            Intent i = new Intent(getContext(), SettingsActivity.class); //change for your case
            startActivity(i);
            requireActivity().overridePendingTransition(0, 0);

        }
    }


    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    private void changeLanguagePref(Context context, String selectedLanguage){
        Resources resources = context.getResources();
        if (!"".equals(selectedLanguage)) {
            if ("English".equals(selectedLanguage)) {
                selectedLanguage = "en";
            } else if ("French".equals(selectedLanguage)) {
                selectedLanguage = "fr";
            }
            Locale locale = new Locale(selectedLanguage);
            Locale.setDefault(locale);
            Configuration config = resources.getConfiguration();
            config.setLocale(locale);
            context.getResources().updateConfiguration(config, resources.getDisplayMetrics());
        }
    }

}
