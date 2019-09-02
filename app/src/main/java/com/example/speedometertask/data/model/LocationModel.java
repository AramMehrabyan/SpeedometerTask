package com.example.speedometertask.data.model;

public class LocationModel {

    private double latitude;
    private double longitude;
    private float distanceTowLocation;
    private long allDistance;
    private int speed;
    private int maxSpeed;

    public LocationModel(double latitude, double longitude,
                         float distanceTowLocation, long allDistance, int speed, int maxSpeed) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.distanceTowLocation = distanceTowLocation;
        this.allDistance = allDistance;
        this.speed = speed;
        this.maxSpeed = maxSpeed;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public float getDistanceTowLocation() {
        return distanceTowLocation;
    }

    public long getAllDistance() {
        return allDistance;
    }

    public int getSpeed() {
        return speed;
    }

    public int getMaxSpeed() {
        return maxSpeed;
    }
}
