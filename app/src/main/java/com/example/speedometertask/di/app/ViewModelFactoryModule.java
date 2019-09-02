package com.example.speedometertask.di.app;

import androidx.lifecycle.ViewModelProvider;

import com.example.speedometertask.viewmodels.ViewModelProviderFactory;

import dagger.Binds;
import dagger.Module;

@Module
public interface ViewModelFactoryModule {

    @Binds
    ViewModelProvider.Factory bindViewModelFactory(ViewModelProviderFactory viewModelFactory);
}
