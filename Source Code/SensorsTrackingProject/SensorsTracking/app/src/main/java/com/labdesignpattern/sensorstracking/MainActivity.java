package com.labdesignpattern.sensorstracking;


import android.Manifest;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.labdesignpattern.sensorstracking.fragments.AboutFragment;
import com.labdesignpattern.sensorstracking.fragments.DetailsFragment;
import com.labdesignpattern.sensorstracking.sensorsListener.FragmentSensors;
import com.labdesignpattern.sensorstracking.sensorsListener.FragmentSensorsManager;
import com.labdesignpattern.sensorstracking.sensorsListener.LocationEventListenerFragment;
import com.labdesignpattern.sensorstracking.utilities.BackgroundService;
import com.labdesignpattern.sensorstracking.utilities.Logger;
import com.labdesignpattern.sensorstracking.utilities.Notifications;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static Notifications mNotifications;
    private String runningNotID;
    public static FragmentSensorsManager mFragmentSensorsManager;
    public static Logger log;


    public MainActivity(){
        log = Logger.getInstance();
        mFragmentSensorsManager = new FragmentSensorsManager();
        runningNotID = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        log.init(DetailsFragment.instance, R.id.editTextTextMultiLine_log);
        mFragmentSensorsManager.loadSystemServices(getApplicationContext());
        MainActivity.mNotifications = new Notifications(getApplicationContext(), (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE));

        FloatingActionButton fab = findViewById(R.id.exitButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFragmentSensorsManager.removeTracking();
                System.exit(0);
            }
        });

        Hashtable<String, List<Integer>> ht = new Hashtable<>();
        ht.put("GPS", new ArrayList<>(Arrays.asList(R.id.textview_position_lat_data, R.id.textview_position_long_data)));
        ht.put("PROXIMITY", new ArrayList<>(Arrays.asList(R.id.textview_proximity_data)));
        ht.put("LIGHT", new ArrayList<>(Arrays.asList(R.id.textview_light_data)));
        ht.put("PRESSURE", new ArrayList<>(Arrays.asList(R.id.textview_pressure_data)));
        ht.put("COMPASS", new ArrayList<>(Arrays.asList(R.id.textview_compass_data)));
        mFragmentSensorsManager.loadTracking(ht);

        // Here, thisActivity is the current activity
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // Should we show an explanation?
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        }

        mFragmentSensorsManager.registerSensorsListeners(getApplicationContext(),  Manifest.permission.ACCESS_FINE_LOCATION);
    }

    @Override
    protected void onPause() {
        super.onPause();
        log.addNewLineText(getString(R.string.service_run_as_service));
        runningNotID = MainActivity.mNotifications.pushNotification(getIntent(), getString(R.string.service_still_running), null,false);
        mFragmentSensorsManager.removeTracking();
        Intent serviceIntent = new Intent(getApplicationContext(), BackgroundService.class);
        startForegroundService(serviceIntent);
        startService(serviceIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(runningNotID != null){
            log.addNewLineText(getString(R.string.service_stop_service));
            mNotifications.popNotification(runningNotID);
            stopService(new Intent(this, BackgroundService.class));
            mFragmentSensorsManager.registerSensorsListeners(getApplicationContext(),  Manifest.permission.ACCESS_FINE_LOCATION);
            runningNotID = null;
       }
    }

    @Override
    public void finish(){
       // finish();
        this.moveTaskToBack(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment;
        //noinspection SimplifiableIfStatement

        switch (item.getItemId()) {
            case R.id.action_about:
                fragment = AboutFragment.getInstance();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        if (fm.findFragmentById(R.id.nav_host_fragment).getClass() == fragment.getClass())
            return super.onOptionsItemSelected(item);

        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.nav_host_fragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
        return true;
    }

    @Override
    public void onBackPressed() {
        android.app.FragmentManager fm = getFragmentManager();
        if (fm.getBackStackEntryCount() > 0)
            fm.popBackStack();
        else
            super.onBackPressed();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        for (int i = 0; i < permissions.length; i++) {
            MainActivity.log.addText(String.format("%s permission is ",  permissions[i]));
            switch (requestCode) {
                case 1: {
                    // If request is cancelled, the result arrays are empty.
                    if (grantResults.length > 0
                            && grantResults[i] == PackageManager.PERMISSION_GRANTED) {

                        // permission was granted, yay! Do the
                        // contacts-related task you need to do.
                        MainActivity.log.addNewLineText("Approved");
                        switch (permissions[i]){
                            case Manifest.permission.ACCESS_BACKGROUND_LOCATION:
                                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                                    return;
                                break;
                            case Manifest.permission.ACCESS_FINE_LOCATION:
                                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                                    return;
                                break;
                        }
                        for (FragmentSensors lelf : mFragmentSensorsManager.getSensorsGroup().getLocationEvent()) {
                            mFragmentSensorsManager.getLocatiomManager().requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, (LocationEventListenerFragment)lelf);
                            ((LocationEventListenerFragment)lelf).setAvailable(true);
                            MainActivity.log.addNewLineText(String.format("Loading sensor %s......%s",  ((FragmentSensors)lelf).getSensorName(), (lelf.isAvailable())? "Successfully":"Failed"));
                        }
                    } else {
                        // permission denied, boo! Disable the
                        // functionality that depends on this permission.
                        MainActivity.log.addNewLineText("Rejected");
                    }
                }
            }
        }

    }
}
