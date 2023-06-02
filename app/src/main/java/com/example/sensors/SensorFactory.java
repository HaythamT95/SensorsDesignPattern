package com.example.sensors;

import android.hardware.Sensor;

public class SensorFactory {

    public iSensor createSensor(int sensorType){
        if(sensorType == Sensor.TYPE_LIGHT)
            return new LightSensor();
        if(sensorType == Sensor.TYPE_PROXIMITY)
            return new ProximitySensor();
        if(sensorType == Sensor.TYPE_PRESSURE)
            return new PressureSensor();
        if(sensorType == Sensor.TYPE_MAGNETIC_FIELD)
            return new MagneticFieldSensor();
        if(sensorType == Sensor.TYPE_ACCELEROMETER)
            return new AccelerometerSensor();
        return null;
    }
}
