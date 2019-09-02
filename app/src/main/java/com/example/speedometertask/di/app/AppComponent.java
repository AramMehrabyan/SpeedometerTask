package com.example.speedometertask.di.app;

import android.content.Context;

import com.example.speedometertask.SpeedometerApp;
import com.example.speedometertask.di.activity.ActivityBuildersModule;
import com.example.speedometertask.di.service.ServiceBuildersModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Component(
        modules = {
                AndroidSupportInjectionModule.class,
                ServiceBuildersModule.class,
                ActivityBuildersModule.class,
                AppModule.class,
                ViewModelFactoryModule.class
        }
)
@Singleton
public interface AppComponent extends AndroidInjector<SpeedometerApp> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder application(Context applicationContext);

        @BindsInstance
        Builder prefName(String prefName);

        AppComponent build();
    }
}
