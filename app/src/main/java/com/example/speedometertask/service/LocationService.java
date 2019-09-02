package com.example.speedometertask.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.example.speedometertask.data.LocationRepository;
import com.example.speedometertask.data.model.LocationModel;
import com.example.speedometertask.ui.map.MapActivity;
import com.example.speedometertask.util.Constants;
import com.example.speedometertask.util.LocationServiceNotificationUtil;

import javax.inject.Inject;

import dagger.android.DaggerService;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

public class LocationService extends DaggerService {

    @Inject
    LocationRepository locationRepository;

    @Inject
    LocationServiceNotificationUtil notificationUtil;

    @Inject
    BehaviorSubject<LocationModel> modelSubject;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private NotificationManager notificationManager;

    private String speed = "0";
    private String maxSpeed = "0";
    private String distance = "0";

    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        locationRepository.requestLocation();
        compositeDisposable.add(
                modelSubject
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::updateData)
        );
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        notificationUtil.createNotificationChannel(this);

        Notification notification = notificationUtil.createNotification(this, createPendingIntent(), getStopPendingIntent(),
                speed, maxSpeed, distance);
        startForeground(Constants.NOTIFICATION_ID, notification);
        return START_NOT_STICKY;
    }

    private void updateNotification(String speed, String maxSpeed, String distance) {
        Notification notification = notificationUtil.createNotification(this, createPendingIntent(), getStopPendingIntent(),
                speed, maxSpeed, distance);
        notificationManager.notify(Constants.NOTIFICATION_ID, notification);
    }

    private PendingIntent createPendingIntent() {
        Intent notificationIntent = new Intent(this, MapActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MapActivity.class);
        stackBuilder.addNextIntent(notificationIntent);

        return stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void updateData(LocationModel locationModel) {
        speed = String.valueOf(locationModel.getSpeed());
        maxSpeed = String.valueOf(locationModel.getMaxSpeed());
        distance = String.valueOf(locationModel.getAllDistance());
        updateNotification(speed, maxSpeed, distance);
    }

    public PendingIntent getStopPendingIntent() {
        BroadcastReceiver closeReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                stopSelf();
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        };
        IntentFilter intFilter = new IntentFilter(Constants.STOPFOREGROUND_ACTION);
        registerReceiver(closeReceiver, intFilter);
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setAction(Constants.STOPFOREGROUND_ACTION);
        return PendingIntent.getBroadcast(this, 0, intent, 0);

    }

    @Override
    public void onDestroy() {
        compositeDisposable.dispose();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
