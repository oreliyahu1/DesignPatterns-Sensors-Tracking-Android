package com.labdesignpattern.sensorstracking.sensorsListener;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;

import com.labdesignpattern.sensorstracking.fragments.DetailsFragment;

public class CompassSensorEventListenerFragment extends SensorEventListenerFragment {
    protected float[] mGravity;
    protected float[] mGeomagnetic;
    public CompassSensorEventListenerFragment(String sname, Sensor sensor1,Sensor sensor2, int textview_firstfragment_id0, int... textview_firstfragment_id) {
        super(sname, sensor1,sensor2, textview_firstfragment_id0, textview_firstfragment_id);
        mGravity = mGeomagnetic = null;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (DetailsFragment.instance == null) return;
        int i = 0;

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            mGravity = event.values;

        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            mGeomagnetic = event.values;

        if (mGravity != null && mGeomagnetic != null) {
            float R[] = new float[9];
            float I[] = new float[9];

            if (SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic)) {

                // orientation contains azimut, pitch and roll
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);

                for(int textview_id: super.textview_firstfragment_ids)
                    DetailsFragment.instance.setText(textview_id, (int)(Math.toDegrees(orientation[0]) + 360) % 360 + " degree");

            }
        }

    }
}
