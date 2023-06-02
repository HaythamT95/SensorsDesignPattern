package com.example.sensors;

import android.hardware.SensorManager;
import android.util.Log;

public class SingletonSensorManager {
    private final static SingletonSensorManager _instance = new SingletonSensorManager();
    SensorManager sensorManager;

    private SingletonSensorManager(){
        Log.i("constructor","constructor was called");
    }
    public static SingletonSensorManager get_instance() {
        return _instance;
    }

    public SensorManager getSensorManager(SensorHelper sensorHelper){
        sensorManager=sensorHelper.getSensorManager();
        return sensorManager;
    }
}
