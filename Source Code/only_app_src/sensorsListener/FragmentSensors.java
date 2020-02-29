package com.labdesignpattern.sensorstracking.sensorsListener;
import java.util.List;

public interface FragmentSensors {
    List<Integer> getTextview_detailsfragment_ids();
    boolean isAvailable();
    String getSensorName();
}
