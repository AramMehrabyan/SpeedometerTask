package com.example.speedometertask.data.location;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.example.speedometertask.data.PrefRepository;
import com.example.speedometertask.data.model.LocationModel;
import com.example.speedometertask.domain.SpeedCalculator;
import com.example.speedometertask.util.Constants;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.subjects.BehaviorSubject;

import static com.example.speedometertask.util.Constants.FASTEST_INTERVAL;
import static com.example.speedometertask.util.Constants.UPDATE_INTERVAL;

@Singleton
public class LocationManager {

    private static final String TAG = "LocationManager+++++++";

    private BehaviorSubject<LocationModel> locationModelSubject;
    private PrefRepository prefRepository;
    private SpeedCalculator speedCalculator;
    private Context context;
    private Location currentLocation;
    private Location prevLocation;
    private int distanceType = 1000;

    @Inject
    LocationManager(BehaviorSubject<LocationModel> locationModelSubject,
                    SpeedCalculator speedCalculator, Context context, PrefRepository prefRepository) {
        this.locationModelSubject = locationModelSubject;
        this.speedCalculator = speedCalculator;
        this.context = context;
        this.prefRepository = prefRepository;
    }

    private void startLocationUpdates() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        SettingsClient settingsClient = LocationServices.getSettingsClient(context);
        settingsClient.checkLocationSettings(locationSettingsRequest);

        LocationServices.getFusedLocationProviderClient(context)
                .requestLocationUpdates(mLocationRequest, new LocationCallback() {
                            @Override
                            public void onLocationResult(LocationResult locationResult) {
                                onLocationChanged(locationResult.getLastLocation());
                            }
                        },
                        Looper.myLooper());
    }

    private void onLocationChanged(Location location) {
        // GPS may be turned off
        if (location == null) {
            return;
        }

        if (currentLocation == null) {
            currentLocation = location;
        } else {
            prevLocation = new Location(currentLocation);
            currentLocation = location;

            Log.d(TAG, "  Prev  Latitude =  " + prevLocation.getLatitude() + "  Longitude =  " + prevLocation.getLongitude());
            Log.d(TAG, "  Current  Latitude =  " + currentLocation.getLatitude() + "  Longitude =  " + currentLocation.getLongitude());
        }

        long distanceInMeter = prefRepository.getDistanceInMeter();
        int maxSpeed = prefRepository.getMaxSpeed();
        String speedType = prefRepository.getSpeedType();
        if (speedType.equals("km / h")) {
            distanceType = 1000;
        }
        else {
            distanceType = 1600;
        }

        float distanceTwoLocationByType = 0;
        if (prevLocation != null) {
            float distanceTwoLocation = prevLocation.distanceTo(currentLocation);
            if (distanceTwoLocation <= 1) {
                distanceTwoLocation = 0;
            }
            distanceTwoLocationByType = distanceTwoLocation / distanceType;
            Log.d(TAG, "  distanceTwoLocation =  " + distanceTwoLocationByType);
        }

        int speed = speedCalculator.calculateSpeed(distanceTwoLocationByType);
        if (speed > maxSpeed) {
            maxSpeed = speed;
            prefRepository.setMaxSpeed(maxSpeed);
        }
        distanceInMeter += distanceTwoLocationByType * distanceType;
        prefRepository.setDistanceInMeter(distanceInMeter);
        Log.d(TAG, "  distanceInMeter =  " + distanceInMeter);

        sendDataLocation(location.getLatitude(), location.getLongitude(), distanceTwoLocationByType, distanceInMeter, speed, maxSpeed);
    }

    private void sendDataLocation(double latitude, double longitude, float destanceTowLocation, long allDistance, int speed, int maxSpeed) {
        LocationModel locationModel = new LocationModel(
                latitude, longitude,
                destanceTowLocation, allDistance, speed, maxSpeed);
        locationModelSubject.onNext(locationModel);
    }

    public void requestLocation() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            startLocationUpdates();
        }
    }
}
