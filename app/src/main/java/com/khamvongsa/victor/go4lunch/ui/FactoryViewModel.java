package com.khamvongsa.victor.go4lunch.ui;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

/**
 * Created by <Victor Khamvongsa> on <20/07/2022>
 */
public class FactoryViewModel implements ViewModelProvider.Factory {

    private static FactoryViewModel factory;

    public static FactoryViewModel getInstance() {
        if (factory == null) {
            synchronized (FactoryViewModel.class) {
                if (factory == null) {
                    factory = new FactoryViewModel();
                }
            }
        }
        return factory;
    }


    private FactoryViewModel() {
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RestaurantViewModel.class)) {
            return (T) new RestaurantViewModel();
        }
        if (modelClass.isAssignableFrom(UserViewModel.class)) {
            return (T) new UserViewModel();
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
