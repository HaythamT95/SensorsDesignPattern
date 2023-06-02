package com.example.sensors;

import android.hardware.Sensor;
import android.hardware.SensorManager;

public class AccelerometerSensor implements iSensor{
    @Override
    public Sensor getSensor(SensorManager sensorManager) {
        return sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }
}
