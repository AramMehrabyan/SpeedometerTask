package com.example.speedometertask;

import com.example.speedometertask.di.app.DaggerAppComponent;

import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;

public class SpeedometerApp extends DaggerApplication {

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerAppComponent.builder().application(this).prefName("map_pref").build();
    }
}
