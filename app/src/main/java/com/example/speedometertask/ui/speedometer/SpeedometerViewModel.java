package com.example.speedometertask.ui.speedometer;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.speedometertask.data.model.LocationModel;
import com.example.speedometertask.di.activity.speedometer.SpeedScope;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

@SpeedScope
public class SpeedometerViewModel extends ViewModel {

    private static final String TAG = "SpeedometerViewModel+++";

    private BehaviorSubject<LocationModel> locationModelSubject;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private MutableLiveData<String> _speed = new MutableLiveData<>("0");
    public LiveData<String> speed = _speed;

    private MutableLiveData<String> _speedType = new MutableLiveData<>("km / h");
    public LiveData<String> speedType = _speedType;

    private MutableLiveData<String> _maxSpeed = new MutableLiveData<>("0");
    public LiveData<String> maxSpeed = _maxSpeed;

    private MutableLiveData<String> _allDistance = new MutableLiveData<>("0");
    public LiveData<String> allDistance = _allDistance;

    private MutableLiveData<String> _allDistanceType = new MutableLiveData<>("m");
    public LiveData<String> allDistanceType = _allDistanceType;

    private MutableLiveData<Boolean> _map = new MutableLiveData<>(false);
    public LiveData<Boolean> map = _map;

    private MutableLiveData<Boolean> _clearResults = new MutableLiveData<>(false);
    public LiveData<Boolean> clearResults = _clearResults;

    private MutableLiveData<Boolean> _rbKm = new MutableLiveData<>(true);
    public LiveData<Boolean> rbKm = _rbKm;

    private MutableLiveData<Boolean> _rbMil = new MutableLiveData<>(false);
    public LiveData<Boolean> rbMil = _rbMil;

    @Inject
    SpeedometerViewModel(BehaviorSubject<LocationModel> locationModelSubject) {
        this.locationModelSubject = locationModelSubject;
        startUpdateSpeed();
    }

    private void startUpdateSpeed() {
        compositeDisposable.add(
                locationModelSubject
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::updateData)
        );
    }

    private void updateData(LocationModel locationModel) {
        Log.d(TAG, "  Speedometer Latitude =  " + locationModel.getLatitude() +
                "  Longitude =  " + locationModel.getLongitude());
        Log.d(TAG, "  Speedometer DistanceTowLocation =  " + locationModel.getDistanceTowLocation());

        _speed.setValue(String.valueOf(locationModel.getSpeed()));
        _allDistance.setValue(String.valueOf(locationModel.getAllDistance()));
        _maxSpeed.setValue(String.valueOf(locationModel.getMaxSpeed()));

    }

    public void openMap() {
        _map.setValue(true);
    }

    void resetMapValue() {
        _map.setValue(false);
    }

    public void clearResults() {
        _clearResults.setValue(true);
        _maxSpeed.setValue("0");
        _allDistance.setValue("0");
        _clearResults.setValue(false);
    }

    public void rbKmClick() {
        if (!_rbKm.getValue()) {
            _rbKm.setValue(true);
            _rbMil.setValue(false);
            _allDistanceType.setValue("m");
            _speedType.setValue("km / h");
            clearResults();
        }
    }

    public void rbMilClick() {
        if (!_rbMil.getValue()) {
            _rbMil.setValue(true);
            _rbKm.setValue(false);
            _allDistanceType.setValue("mil");
            _speedType.setValue("mil / h");
            clearResults();
        }
    }

    @Override
    protected void onCleared() {
        compositeDisposable.dispose();
        locationModelSubject = null;
        super.onCleared();
    }
}
