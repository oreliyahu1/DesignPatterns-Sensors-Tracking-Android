package com.labdesignpattern.sensorstracking.utilities;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.labdesignpattern.sensorstracking.R;
import com.labdesignpattern.sensorstracking.sensorsListener.FragmentSensorsManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

public class BackgroundService extends Service {
    protected FragmentSensorsManager mFragmentSensorsManager;

    public BackgroundService(){
        mFragmentSensorsManager = new FragmentSensorsManager();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
       throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mFragmentSensorsManager.loadSystemServices(getApplicationContext());
        Hashtable<String, List<Integer>> ht = new Hashtable<>();
        ht.put("GPS", new ArrayList<>(Arrays.asList(R.id.textview_position_lat_data, R.id.textview_position_long_data)));
        ht.put("PROXIMITY", new ArrayList<>(Arrays.asList(R.id.textview_proximity_data)));
        ht.put("LIGHT", new ArrayList<>(Arrays.asList(R.id.textview_light_data)));
        ht.put("PRESSURE", new ArrayList<>(Arrays.asList(R.id.textview_pressure_data)));
        ht.put("COMPASS", new ArrayList<>(Arrays.asList(R.id.textview_compass_data)));
        mFragmentSensorsManager.loadTracking(ht);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        mFragmentSensorsManager.registerSensorsListeners(this,  Manifest.permission.ACCESS_BACKGROUND_LOCATION);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mFragmentSensorsManager.removeTracking();
    }
}
