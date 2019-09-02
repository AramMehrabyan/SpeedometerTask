package com.example.speedometertask.di.app;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.speedometertask.data.model.LocationModel;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.subjects.BehaviorSubject;

@Module
abstract class AppModule {

    @Provides
    @Singleton
    static BehaviorSubject<LocationModel> provideLocationModelSubject() {
        return BehaviorSubject.create();
    }

    @Provides
    @Singleton
    static SharedPreferences provideSharedPreferences(Context context, String prefName) {
        return context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
    }
}
