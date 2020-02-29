package com.labdesignpattern.sensorstracking.sensorsListener;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.labdesignpattern.sensorstracking.fragments.DetailsFragment;
import com.labdesignpattern.sensorstracking.MainActivity;
import com.labdesignpattern.sensorstracking.R;

import java.util.ArrayList;
import java.util.List;

public class LocationEventListenerFragment implements LocationListener, FragmentSensors {
    protected List<Integer> textview_firstfragment_ids;
    protected boolean available;
    String sname;
    public LocationEventListenerFragment(String sname, int textview_firstfragment_id0, int...textview_firstfragment_id){
        textview_firstfragment_ids = new ArrayList<Integer>();
        textview_firstfragment_ids.add(textview_firstfragment_id0);
        for(int id : textview_firstfragment_id)
            textview_firstfragment_ids.add(id);
        this.available = false;
        this.sname= sname;
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        if (DetailsFragment.instance == null) return;

        String[] arr = new String[2];
        arr[0] = String.format("%.4f%s",Math.abs(location.getLatitude()), (location.getLatitude() < 0) ? "S" : "N");
        arr[1] = String.format("%.4f%s",Math.abs(location.getLongitude()), (location.getLongitude() < 0) ? "W" : "E");

        int i = 0;
        for(int textview_id: textview_firstfragment_ids)
            DetailsFragment.instance.setTextWcolor(textview_id, String.valueOf(arr[i++]), android.R.color.darker_gray);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
        if(DetailsFragment.instance == null) return;
        MainActivity.log.addNewLineText(String.format("%s sensor is on",  this.getSensorName()));

        for(int textview_id: textview_firstfragment_ids)
            DetailsFragment.instance.setTextWcolor(textview_id, R.string.main_loading_data, android.R.color.darker_gray);
    }

    @Override
    public void onProviderDisabled(String provider) {
        if(DetailsFragment.instance == null) return;

        MainActivity.log.addNewLineText(String.format("%s sensor is off",  this.getSensorName()));

        for (int textview_id : textview_firstfragment_ids)
            DetailsFragment.instance.setTextWcolor(textview_id, R.string.main_gps_off, android.R.color.darker_gray);
    }

    @Override
    public List<Integer> getTextview_detailsfragment_ids() {
        return textview_firstfragment_ids;
    }

    public void setAvailable(boolean available){
        this.available = available;
        if(available){
            if(DetailsFragment.instance != null){
                for(int textview_id: textview_firstfragment_ids)
                    DetailsFragment.instance.setTextWcolor(textview_id, R.string.main_loading_data, android.R.color.darker_gray);
            }
        }
    }

    @Override
    public boolean isAvailable() {
        return available;
    }

    @Override
    public String getSensorName() {
        return sname;
    }
}
