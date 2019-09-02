package com.example.speedometertask.ui.speedometer;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.speedometertask.R;
import com.example.speedometertask.data.PrefRepository;
import com.example.speedometertask.databinding.ActivitySpeedometerBinding;
import com.example.speedometertask.service.LocationService;
import com.example.speedometertask.ui.map.MapActivity;
import com.example.speedometertask.util.Constants;
import com.example.speedometertask.util.PermissionUtils;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

import static com.example.speedometertask.util.Constants.permissions;

public class SpeedometerActivity extends DaggerAppCompatActivity {

    @Inject
    ViewModelProvider.Factory providerFactory;

    @Inject
    PrefRepository prefRepository;

    private SpeedometerViewModel viewModel;

    private boolean tracking = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!PermissionUtils.checkSelfPermissionIsGranted(this, permissions)) {
            PermissionUtils.requestPermission(this, permissions,
                    Constants.LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            init();
        }
    }

    public void startService() {
        Intent serviceIntent = new Intent(this, LocationService.class);
        startService(serviceIntent);
    }

    public void stopService() {
        Intent serviceIntent = new Intent(this, LocationService.class);
        stopService(serviceIntent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == Constants.LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                init();
            } else {
                Toast.makeText(this.getApplicationContext(), "Need Permissions", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    private void init() {
        startService();

        ActivitySpeedometerBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_speedometer);
        viewModel = new ViewModelProvider(this, providerFactory).get(SpeedometerViewModel.class);
        binding.setViewmodel(viewModel);
        binding.setLifecycleOwner(this);
        observeObservers();
    }

    private void observeObservers() {
        viewModel.map.observe(this, aBoolean -> {
            if (aBoolean) {
                openMap();
                viewModel.resetMapValue();
            }
        });

        viewModel.clearResults.observe(this, aBoolean -> {
            if (aBoolean) {
                prefRepository.setDistanceInMeter(0);
                prefRepository.setMaxSpeed(0);
            }
        });

        viewModel.speedType.observe(this, s -> {
            prefRepository.setSpeedType(s);
        });
    }

    private void openMap() {
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        tracking = prefRepository.isMapActivated();
    }

    @Override
    protected void onDestroy() {
        if (!tracking) {
            stopService();
        }
        super.onDestroy();
    }
}