package com.labdesignpattern.sensorstracking.sensorsListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SensorEventGroup<T extends FragmentSensors> implements SensorEventDAO<T>{
    public List<T> sensorsEvent;
    public List<T> locationEvent;

    public SensorEventGroup(){
        sensorsEvent = new ArrayList<>();
        locationEvent = new ArrayList<>();
    }

    private List<T> union(List<T> l1, List<T> l2){
        Set<T> set = new HashSet<T>();

        set.addAll(l1);
        set.addAll(l2);

        return new ArrayList<T>(set);
    }

    public List<T> unionAvailable(){
        ArrayList<T> l1 = new ArrayList<>();
        ArrayList<T> l2 = new ArrayList<>();
        for(T fs : sensorsEvent) {
            if(fs.isAvailable())
                l1.add(fs);
        }
        for(T ls : locationEvent) {
            if(ls.isAvailable())
                l2.add(ls);
        }
        return union(l1,l2);
    }

    public List<T> getSensorsEvent(){
        return this.sensorsEvent;
    }

    public List<T> getLocationEvent(){
        return this.locationEvent;
    }

    @Override
    public List<T> getAllSensors(){
        return union(sensorsEvent, locationEvent);
    }

    @Override
    public boolean delete(T node) {
        if(node instanceof SensorEventListenerFragment)
            return sensorsEvent.remove(node);
        if(node instanceof LocationEventListenerFragment)
            return locationEvent.remove(node);
        return false;
    }

    @Override
    public boolean add(T node){
        if(node instanceof SensorEventListenerFragment)
            return sensorsEvent.add(node);
        if(node instanceof LocationEventListenerFragment)
            return locationEvent.add(node);
        return false;
    }
}
