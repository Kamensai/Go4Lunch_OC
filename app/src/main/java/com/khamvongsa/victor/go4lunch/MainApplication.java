package com.khamvongsa.victor.go4lunch;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import com.khamvongsa.victor.go4lunch.ui.helper.LocaleHelper;

import java.util.Locale;

import androidx.preference.PreferenceManager;

/**
 * Created by <Victor Khamvongsa> on <29/09/2022>
 */
public class MainApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base,"en"));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Locale locale = new Locale(PreferenceManager.getDefaultSharedPreferences(this).getString("language",""));
        newConfig.locale = locale;
        Locale.setDefault(locale);
        getBaseContext().getResources().updateConfiguration(newConfig,getBaseContext().getResources().getDisplayMetrics());
        super.onConfigurationChanged(newConfig);
    }


    //also handle chnage  language if  device language chnaged
    private void setLanguageFromNewConfig(Configuration newConfig){
        //Preferences.putSaveLocaleLanguage(this,  selectedLocaleLanguage );
        attachBaseContext(LocaleHelper.onAttach(getApplicationContext(),newConfig.locale.getLanguage()));
    }


}
