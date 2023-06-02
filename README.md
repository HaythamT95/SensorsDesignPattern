# SensorsDesignPattern
Android Sensor Display with Design Patterns

## Description
This Android Studio project is developed as part of the Design Pattern course. The purpose of the project is to display readings from five different sensors on an Android mobile phone: Light, Proximity, Pressure, GPS (longitude, latitude), and Compass.

The project incorporates the use of four different design patterns: Singleton, Decorator, Factory, and Facade. Each design pattern serves a specific purpose in the implementation.

### Singleton
The Singleton design pattern is utilized in the SingletonSensorManager class, responsible for managing the sensors. By creating a single instance of the SensorManager, we ensure that the sensors are effectively managed. The instance of SingletonSensorManager is created in the MainActivity.

### Decorator
The Decorator design pattern is employed through the SensorHelper class. This class acts as a helper to obtain services from the phone by receiving the Context as a parameter. Using the Decorator pattern, we wrap the SensorHelper class within the Singleton class, which, in turn, wraps the Context class.

### Factory
The Factory design pattern is implemented using the iSensor interface, which defines a function that receives a SensorManager object and returns an object of type Sensor. Each specific type of sensor, such as LightSensor, MagneticFieldSensor, PressureSensor, ProximitySensor, and AccelerometerSensor, implements the iSensor interface. The SensorFactory class produces all the sensors using the createSensor(int sensorType) function, which returns an object of type iSensor.

### Facade
To enable the software to run in the background, we utilize the Facade design pattern. Multiple classes, including MyBroadcastReceiver, MyForegroundService, and Intent, are involved in running the software in the background. To activate the background service, the foregroundServiceRunning() method needs to be invoked, which in turn requires startForegroundService(serviceIntent). Furthermore, serviceIntent is created using Intent(this, MyForegroundService.class). These interconnected services form a Facade design pattern, as they depend on each other to function properly.

## Installation
To use this project, follow the steps below:

1) Clone the repository to your local machine.
2) Open the project in Android Studio.
3) Build and run the project on an Android device or emulator.

## OR Download the APK file to your device

## Usage
Upon running the application, the main screen will display the readings from the five different sensors: Light, Proximity, Pressure, GPS (longitude, latitude), and Compass. Each sensor reading will be updated in real-time, providing you with the current values.

Feel free to explore the project's code and learn how the various design patterns are implemented. Modify and enhance the application as per your requirements.

