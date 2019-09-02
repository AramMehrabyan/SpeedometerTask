package com.example.speedometertask.ui.map;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.speedometertask.data.PrefRepository;
import com.example.speedometertask.data.model.LocationModel;
import com.example.speedometertask.di.activity.map.MapScope;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.BehaviorSubject;

@MapScope
public class MapViewModel extends ViewModel {

    private BehaviorSubject<LocationModel> locationModelSubject;
    private PrefRepository prefRepository;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private MutableLiveData<String> _binOnOffText = new MutableLiveData<>("On");
    LiveData<String> binOnOffText = _binOnOffText;

    private MutableLiveData<Boolean> _trackingRoad = new MutableLiveData<>(false);
    LiveData<Boolean> trackingRoad = _trackingRoad;


    @Inject
    MapViewModel(BehaviorSubject<LocationModel> locationModelSubject,
                 PrefRepository prefRepository) {
        this.locationModelSubject = locationModelSubject;
        this.prefRepository = prefRepository;
        init();
    }

    BehaviorSubject<LocationModel> getLocationModelSubject() {
        return locationModelSubject;
    }

    private void init() {
        boolean tracking = prefRepository.isMapActivated();
        if (tracking) {
            _trackingRoad.setValue(true);
            _binOnOffText.setValue("Off");
        }
    }

    void trackingRoadOnOff() {
        if (_trackingRoad.getValue() != null) {
            if (_trackingRoad.getValue()) {
                _trackingRoad.setValue(false);
                _binOnOffText.setValue("On");
                prefRepository.mapOnOff(false);
            } else {
                _trackingRoad.setValue(true);
                _binOnOffText.setValue("Off");
                prefRepository.mapOnOff(true);
            }
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
        locationModelSubject = null;
    }
}