package com.example.speedometertask.di.activity;

import com.example.speedometertask.di.activity.map.MapScope;
import com.example.speedometertask.di.activity.map.MapViewModelsModule;
import com.example.speedometertask.di.activity.speedometer.SpeedScope;
import com.example.speedometertask.di.activity.speedometer.SpeedViewModelsModule;
import com.example.speedometertask.ui.map.MapActivity;
import com.example.speedometertask.ui.speedometer.SpeedometerActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public interface ActivityBuildersModule {

    @ContributesAndroidInjector(modules = {
            SpeedViewModelsModule.class
    })
    @SpeedScope
    SpeedometerActivity contributeSpeedometerActivity();

    @ContributesAndroidInjector(modules = {
            MapViewModelsModule.class
    })
    @MapScope
    MapActivity contributeMapActivity();
}
