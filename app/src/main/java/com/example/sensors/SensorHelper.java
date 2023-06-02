package com.example.sensors;

import static android.content.Context.SENSOR_SERVICE;

import android.content.Context;
import android.hardware.SensorManager;

public class SensorHelper  {
    private SensorManager sensorManager;

    public SensorHelper (Context context) {
        // Get the SensorManager using the context
        sensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
    }
    public SensorManager getSensorManager(){
        return sensorManager;
    }
}
