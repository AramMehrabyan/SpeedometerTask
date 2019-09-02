package com.example.speedometertask.domain;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SpeedCalculator {

    private long startTime;

    @Inject
    SpeedCalculator() {
        startTime = System.currentTimeMillis();
    }

    public int calculateSpeed(float distance) {

        long newTime = System.currentTimeMillis();
        long intervalTime = newTime - startTime;
        startTime = newTime;
        float intervalByHour = ((float) intervalTime) / (1000 * 60 * 60);
        return (int) (distance / intervalByHour);
    }
}
