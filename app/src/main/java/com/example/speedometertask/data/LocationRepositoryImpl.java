package com.example.speedometertask.data;

import com.example.speedometertask.data.location.LocationManager;
import com.example.speedometertask.di.service.ServiceScope;

import javax.inject.Inject;

@ServiceScope
public class LocationRepositoryImpl implements LocationRepository {

    private LocationManager locationManager;

    @Inject
    LocationRepositoryImpl(LocationManager locationManager) {
        this.locationManager = locationManager;
    }

    @Override
    public void requestLocation() {
        locationManager.requestLocation();
    }
}
