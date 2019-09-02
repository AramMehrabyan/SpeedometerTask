package com.example.speedometertask.di.service;

import com.example.speedometertask.service.LocationService;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public interface ServiceBuildersModule {

    @ContributesAndroidInjector(modules = {
            ServiceModule.class
    })
    @ServiceScope
    LocationService contributeLocationService();
}
