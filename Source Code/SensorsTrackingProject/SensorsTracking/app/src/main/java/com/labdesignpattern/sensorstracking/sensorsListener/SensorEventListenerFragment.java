package com.labdesignpattern.sensorstracking.sensorsListener;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import com.labdesignpattern.sensorstracking.fragments.DetailsFragment;

import java.util.ArrayList;
import java.util.List;

public class SensorEventListenerFragment implements SensorEventListener, FragmentSensors {
    protected List<Integer> textview_firstfragment_ids;
    protected List<Sensor> sensors;
    private String sname;
    public SensorEventListenerFragment(String sname, Sensor sensor1, int textview_firstfragment_id0, int...textview_firstfragment_id){
        textview_firstfragment_ids = new ArrayList<Integer>();
        sensors = new ArrayList<Sensor>();
        textview_firstfragment_ids.add(textview_firstfragment_id0);
        for(int id : textview_firstfragment_id)
            textview_firstfragment_ids.add(id);
        this.sensors.add(sensor1);
        this.sname = sname;
    }

    public SensorEventListenerFragment(String sname, Sensor sensor1, Sensor sensor2, int textview_firstfragment_id0, int...textview_firstfragment_id){
        this(sname,sensor1,textview_firstfragment_id0,textview_firstfragment_id);
        this.sensors.add(sensor2);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (DetailsFragment.instance == null) return;

        int i = 0;
        for(int textview_id: textview_firstfragment_ids)
            DetailsFragment.instance.setText(textview_id, String.valueOf(event.values[i++]));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy){
    }

    public List<Sensor> getSensors(){
        return this.sensors;
    }

    @Override
    public List<Integer> getTextview_detailsfragment_ids() {
        return textview_firstfragment_ids;
    }

    @Override
    public boolean isAvailable() {
        for(Sensor s : sensors) {
            if (s == null)
                return false;
        }
        return  true;
    }

    @Override
    public String getSensorName() {
        return sname;
    }

}
