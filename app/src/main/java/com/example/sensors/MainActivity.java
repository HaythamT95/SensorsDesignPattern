package com.example.sensors;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_LOCATION_PERMISSION = 1;

    private SensorManager sensorManager;
    private Sensor lightSensor;
    private Sensor proximitySensor;
    private Sensor pressureSensor;
    private Sensor accelerometerSensor;
    private Sensor magneticFieldSensor;
    private LocationManager locationManager;
    private ImageView imageView;
    private float[] floatGravity = new float[3];
    private float[] floatGeoMagnetic = new float[3];
    private float[] floatOrientation = new float[3];
    private float[] floatRotationMatrix = new float[9];

    public void closeActivity(View view) {
        // Stop any background services
        stopService(new Intent(this, MyForegroundService.class));
        // Close the activity
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageView);
        //Create SingletonSensorManager instance
        //Singleton Design Pattern
        SingletonSensorManager singleton = SingletonSensorManager.get_instance();

        // Create a new SensorHelper and pass the context
        // Get references to the sensor manager and the individual sensors
        // Decorator Design Pattern
        sensorManager = singleton.getSensorManager(new SensorHelper(this));

        //Create SensorFactory
        SensorFactory sensorFactory = new SensorFactory();

        //Factory Design Pattern
        iSensor light = sensorFactory.createSensor(Sensor.TYPE_LIGHT);
        iSensor proximity = sensorFactory.createSensor(Sensor.TYPE_PROXIMITY);
        iSensor pressure = sensorFactory.createSensor(Sensor.TYPE_PRESSURE);
        iSensor magneticField = sensorFactory.createSensor(Sensor.TYPE_MAGNETIC_FIELD);
        iSensor accelerometer = sensorFactory.createSensor(Sensor.TYPE_ACCELEROMETER);

        //Call each "Product" in our case Sensor
        lightSensor = light.getSensor(sensorManager);
        proximitySensor = proximity.getSensor(sensorManager);
        pressureSensor = pressure.getSensor(sensorManager);
        accelerometerSensor = accelerometer.getSensor(sensorManager);
        magneticFieldSensor = magneticField.getSensor(sensorManager);

        // Get a reference to the location manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //Facade Design Pattern
        Intent serviceIntent = new Intent(this, MyForegroundService.class);
        startForegroundService(serviceIntent);
        foregroundServiceRunning();
    }
    public boolean foregroundServiceRunning() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for(ActivityManager.RunningServiceInfo service : activityManager.getRunningServices(Integer.MAX_VALUE)){
            if(MyForegroundService.class.getName().equals(service.service.getClassName()))
                return true;
        }
        return false;
    }

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            // Update the GPS text view with the new location
            TextView gpsTextView = findViewById(R.id.text_gps);
            gpsTextView.setText(String.format("GPS: Latitude= %.2f, Longitude= %.2f", location.getLatitude(), location.getLongitude()));
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // Do nothing
        }

        @Override
        public void onProviderEnabled(String provider) {
            // Do nothing
        }

        @Override
        public void onProviderDisabled(String provider) {
            // Do nothing
        }
    };

    SensorEventListener sensorEventListenerAccelerometer = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            floatGravity = event.values;
            SensorManager.getRotationMatrix(floatRotationMatrix, null, floatGravity, floatGeoMagnetic);
            SensorManager.getOrientation(floatRotationMatrix, floatOrientation);

            TextView compassTextView = findViewById(R.id.text_compass);
            int compass=(int) (-floatOrientation[0]*180/3.14159);
            if(compass<0)
                compass+=360;
            compassTextView.setText(String.format("Compass: %d degrees",(compass)));
            imageView.setRotation((float) (-floatOrientation[0]*180/3.14159));
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    SensorEventListener sensorEventListenerMagneticField = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            floatGeoMagnetic = event.values;

            SensorManager.getRotationMatrix(floatRotationMatrix, null, floatGravity, floatGeoMagnetic);
            SensorManager.getOrientation(floatRotationMatrix, floatOrientation);
            //imageView.setRotation((float) (-floatOrientation[0]*180/3.14159));
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    private SensorEventListener lightSensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            // Update the light text view with the sensor value
            TextView lightTextView = findViewById(R.id.text_light);
            lightTextView.setText(String.format("Light: %.0flx", event.values[0]));
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // Do nothing
        }
    };

    private SensorEventListener proximitySensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            // Update the proximity text view with the sensor value
            TextView proximityTextView = findViewById(R.id.text_proximity);
            proximityTextView.setText(String.format("Proximity: %.0f cm", event.values[0]));
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // Do nothing
        }
    };
    private SensorEventListener pressureSensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            // Update the pressure text view with the sensor value
            TextView pressureTextView = findViewById(R.id.text_pressure);
            pressureTextView.setText(String.format("Pressure: %.2f hPa", event.values[0]));
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // Do nothing
        }
    };

    protected void onResume() {
        super.onResume();

        // Check if the ACCESS_FINE_LOCATION permission has been granted
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // If not, request it
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        } else {
            // If the permission has already been granted, start receiving location updates
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }

        // Register sensor event listeners
        sensorManager.registerListener(lightSensorEventListener, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(proximitySensorEventListener, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(pressureSensorEventListener, pressureSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(sensorEventListenerAccelerometer, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(sensorEventListenerMagneticField, magneticFieldSensor, SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    protected void onPause() {
        super.onPause();

        // Unregister sensor event listeners
        sensorManager.unregisterListener(lightSensorEventListener);
        sensorManager.unregisterListener(proximitySensorEventListener);
        sensorManager.unregisterListener(pressureSensorEventListener);
        sensorManager.unregisterListener(sensorEventListenerAccelerometer);
        sensorManager.unregisterListener(sensorEventListenerMagneticField);
    }


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // If the permission has been granted, start receiving location updates
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            } else {
                // If the permission has not been granted, display a message
                Toast.makeText(this, "Location permission is required to get GPS location", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

