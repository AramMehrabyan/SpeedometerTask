package com.example.speedometertask.binding;

import androidx.databinding.BindingAdapter;

import de.nitri.gauge.Gauge;

public class BindingAdapters {

    @BindingAdapter("app:valueSpeed")
    public static void updateSpeedometerValue(Gauge gauge, String valueSpeed) {
        gauge.moveToValue(Float.valueOf(valueSpeed));
    }
}
