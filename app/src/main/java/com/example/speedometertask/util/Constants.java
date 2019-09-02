package com.example.speedometertask.util;

import android.Manifest;

public class Constants {

    public static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    public static String[] permissions = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
    };

    static final String CHANNEL_ID = "LocationNotificationChanel";
    public static final int NOTIFICATION_ID = 101;

    public static final int COLOR_BLACK_ARGB = 0xff000000;
    public static final int POLYLINE_STROKE_WIDTH_PX = 12;

    public final static long UPDATE_INTERVAL = 5000;  /* 5 secs */
    public final static long FASTEST_INTERVAL = 5000; /* 5 secs */

    public static final String PREF_MAP_ON_OFF = "pref_map_on_off";
    public static final String PREF_DISTANCE = "pref_distance";
    public static final String PREF_MAX_SPEED = "pref_max_speed";
    public static final String PREF_SPEED_TYPE = "pref_speed_type";

    public static String STOPFOREGROUND_ACTION = "com.example.speedometertask.stopforeground";

}
