package com.example.speedometertask.di.service;

import com.example.speedometertask.data.LocationRepository;
import com.example.speedometertask.data.LocationRepositoryImpl;

import dagger.Binds;
import dagger.Module;

@Module
public interface ServiceModule {

    @Binds
    LocationRepository bindLocationRepository(LocationRepositoryImpl locationRepository);
}
