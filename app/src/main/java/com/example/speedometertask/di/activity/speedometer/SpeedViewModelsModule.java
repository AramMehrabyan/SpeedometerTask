package com.example.speedometertask.di.activity.speedometer;

import androidx.lifecycle.ViewModel;

import com.example.speedometertask.di.activity.ViewModelKey;
import com.example.speedometertask.ui.speedometer.SpeedometerViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public interface SpeedViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(SpeedometerViewModel.class)
    ViewModel bindsSpeedometerViewModel(SpeedometerViewModel viewModel);
}
