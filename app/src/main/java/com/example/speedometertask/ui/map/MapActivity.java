package com.example.speedometertask.ui.map;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.lifecycle.ViewModelProvider;

import com.example.speedometertask.R;
import com.example.speedometertask.data.model.LocationModel;
import com.example.speedometertask.service.LocationService;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static com.example.speedometertask.util.Constants.COLOR_BLACK_ARGB;
import static com.example.speedometertask.util.Constants.POLYLINE_STROKE_WIDTH_PX;

public class MapActivity extends DaggerAppCompatActivity implements
        OnMapReadyCallback {

    private static final String TAG = "MapActivity+++++";

    @Inject
    ViewModelProvider.Factory providerFactory;

    private MapViewModel viewModel;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private GoogleMap map;

    private LocationModel lastLocation;
    private LocationModel preLocation;
    private boolean isCameraZoomed = false;
    private boolean trackingRoad = false;

    private Button btnTracking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        initMap();
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.dispose();
        super.onDestroy();
    }

    private void initMap() {
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMyLocationEnabled(true);
        init();
    }


    private void init() {
        startService();
        btnTracking = findViewById(R.id.btnTracking);
        viewModel = new ViewModelProvider(this, providerFactory).get(MapViewModel.class);

        btnTracking.setOnClickListener(view -> viewModel.trackingRoadOnOff());

        subscribeObservers();
    }

    private void subscribeObservers() {
        compositeDisposable.add(
                viewModel.getLocationModelSubject()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::updateLocation)
        );

        viewModel.binOnOffText.observe(this, s -> btnTracking.setText(s));

        viewModel.trackingRoad.observe(this, aBoolean -> {
            trackingRoad = aBoolean;
            if (!trackingRoad) {
                map.clear();
            }
        });
    }

    public void startService() {
        Intent serviceIntent = new Intent(this, LocationService.class);
        startService(serviceIntent);
    }

    private void updateLocation(LocationModel location) {
        Log.d(TAG, "   map  Latitude =  " + location.getLatitude() +
                "  Longitude = " + location.getLongitude());
        preLocation = lastLocation;
        lastLocation = location;
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        if (trackingRoad && preLocation != null && lastLocation != null) {
            trackRoad();
        }

        //move map camera
        if (!isCameraZoomed) {
            isCameraZoomed = true;
            map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            map.animateCamera(CameraUpdateFactory.zoomTo(17));
        }
    }

    private void trackRoad() {
        map.addPolyline(new PolylineOptions()
                .add(new LatLng(preLocation.getLatitude(), preLocation.getLongitude()),
                        new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()))
                .width(POLYLINE_STROKE_WIDTH_PX)
                .color(COLOR_BLACK_ARGB)
                .jointType(JointType.ROUND)
                .geodesic(true));
    }
}