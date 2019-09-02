package com.example.speedometertask.data;

import android.content.SharedPreferences;

import com.example.speedometertask.util.Constants;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PrefRepository {

    private SharedPreferences sharedPreferences;

    @Inject
    PrefRepository(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public boolean isMapActivated() {
        return sharedPreferences.getBoolean(Constants.PREF_MAP_ON_OFF, false);
    }

    public void mapOnOff(boolean value) {
        sharedPreferences.edit().putBoolean(Constants.PREF_MAP_ON_OFF, value).apply();
    }

    public long getDistanceInMeter() {
        return sharedPreferences.getLong(Constants.PREF_DISTANCE, 0);
    }

    public void setDistanceInMeter(long value) {
        sharedPreferences.edit().putLong(Constants.PREF_DISTANCE, value).apply();
    }

    public int getMaxSpeed() {
        return sharedPreferences.getInt(Constants.PREF_MAX_SPEED, 0);
    }

    public void setMaxSpeed(int value) {
        sharedPreferences.edit().putInt(Constants.PREF_MAX_SPEED, value).apply();
    }

    public String getSpeedType() {
        return sharedPreferences.getString(Constants.PREF_SPEED_TYPE, "km / h");
    }

    public void setSpeedType(String value) {
        sharedPreferences.edit().putString(Constants.PREF_SPEED_TYPE, value).apply();
    }
}
