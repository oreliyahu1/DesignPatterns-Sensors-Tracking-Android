package com.labdesignpattern.sensorstracking.sensorsListener;

import java.util.List;

public interface SensorEventDAO<T extends FragmentSensors> {
    boolean add(T node);
    boolean delete(T node);
    List<T> getAllSensors();
}
