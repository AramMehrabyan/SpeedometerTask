package com.example.speedometertask.di.activity.map;

import androidx.lifecycle.ViewModel;

import com.example.speedometertask.di.activity.ViewModelKey;
import com.example.speedometertask.ui.map.MapViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public interface MapViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(MapViewModel.class)
    ViewModel bindsMapViewModel(MapViewModel viewModel);
}
