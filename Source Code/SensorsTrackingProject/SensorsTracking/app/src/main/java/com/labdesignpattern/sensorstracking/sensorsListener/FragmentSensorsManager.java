package com.labdesignpattern.sensorstracking.sensorsListener;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.LocationManager;
import androidx.core.app.ActivityCompat;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class FragmentSensorsManager {
    protected SensorManager mSensorManager;
    protected LocationManager mLocatiomManager;
    protected SensorEventGroup<FragmentSensors> sel;

    public FragmentSensorsManager(){
        sel = new SensorEventGroup();
    }

    public void loadSystemServices(Context ctx){
        mSensorManager = (SensorManager) ctx.getSystemService(Context.SENSOR_SERVICE);
        mLocatiomManager = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
    }

    public void removeTracking(){
        for (FragmentSensors self : sel.getSensorsEvent())
            mSensorManager.unregisterListener((SensorEventListenerFragment)self);
        for (FragmentSensors lelf : sel.getLocationEvent())
            mLocatiomManager.removeUpdates((LocationEventListenerFragment)lelf);
    }

    public LocationManager getLocatiomManager(){
        return mLocatiomManager;
    }

    public SensorManager getSensorManager(){
        return mSensorManager;
    }

    public SensorEventGroup<FragmentSensors> getSensorsGroup(){
        return sel;
    }

    public void loadTracking(Hashtable<String, List<Integer>> sensht){
        for (Map.Entry<String, List<Integer>> me: sensht.entrySet()) {
            switch (me.getKey()){
                case "GPS":
                    sel.add(new LocationEventListenerFragment(me.getKey(), me.getValue().get(0), me.getValue().get(1)));
                    break;
                case "PROXIMITY":
                    sel.add(new SensorEventListenerFragment(me.getKey(), mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY), me.getValue().get(0)));
                    break;
                case "LIGHT":
                    sel.add(new SensorEventListenerFragment(me.getKey(), mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT), me.getValue().get(0)));
                    break;
                case "PRESSURE":
                    sel.add(new SensorEventListenerFragment(me.getKey(), mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE), me.getValue().get(0)));
                    break;
                case "COMPASS":
                    sel.add(new CompassSensorEventListenerFragment(me.getKey(), mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), me.getValue().get(0)));
                    break;
            }
        }
    }

    @SuppressLint("MissingPermission")
    public void registerLocationListeners(){
        for (FragmentSensors lelf : sel.getLocationEvent()) {
            mLocatiomManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, (LocationEventListenerFragment)lelf);
            ((LocationEventListenerFragment)lelf).setAvailable(true);
        }
    }

    public void registerListeners(){
        for (FragmentSensors self : sel.getSensorsEvent()) {
            for(Sensor s : ((SensorEventListenerFragment)self).getSensors()){
                mSensorManager.registerListener(((SensorEventListenerFragment)self), s,
                        SensorManager.SENSOR_DELAY_NORMAL);
            }
        }
    }

    public void registerSensorsListeners(Context ctx, String minimunLocationAccess){
        //ACCESS_FINE_LOCATION is the minimum request permission for the position view
        //Background Service need ACCESS_BACKGROUND_LOCATION
        if (ActivityCompat.checkSelfPermission(ctx,
                minimunLocationAccess)
                != PackageManager.PERMISSION_GRANTED) {
        } else {
            registerLocationListeners();
        }
        registerListeners();
    }
}
