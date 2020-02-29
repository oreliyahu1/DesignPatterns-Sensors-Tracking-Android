package com.labdesignpattern.sensorstracking.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.labdesignpattern.sensorstracking.MainActivity;
import com.labdesignpattern.sensorstracking.R;
import com.labdesignpattern.sensorstracking.sensorsListener.FragmentSensors;

public class DetailsFragment extends Fragment {
    public static DetailsFragment instance = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DetailsFragment.instance = this;
        MainActivity.log.setFragment(this);
        MainActivity.log.clearBuffer();

        for(FragmentSensors fs : MainActivity.mFragmentSensorsManager.getSensorsGroup().getAllSensors()) {
            MainActivity.log.addNewLineText(String.format("Loading sensor %s......%s",  ((FragmentSensors)fs).getSensorName(), (fs.isAvailable())? "Successfully":"Failed"));
            for (int textid : fs.getTextview_detailsfragment_ids()) {
                if (!fs.isAvailable())
                    setTextWcolor(textid, R.string.main_sensor_exception, R.color.colorSensorException);
            }
        }
    }

    public boolean setText(int textViewId, int textId){
        TextView data = (TextView) this.getView().findViewById(textViewId);
        if(data == null) return false;
        data.setText(getText(textId));
        return true;
    }

    public boolean setText(int textViewId, String text){
        TextView data = (TextView) this.getView().findViewById(textViewId);
        if(data == null) return false;
        data.setText(text);
        return true;
    }

    public boolean setTextWcolor(int textViewId, int textId, int colorId){
        if(setText(textViewId, textId)) {
            TextView data = (TextView) this.getView().findViewById(textViewId);
            data.setTextColor(ContextCompat.getColor(getContext(), colorId));
            return true;
        }
        return false;
    }

    public boolean setTextWcolor(int textViewId, String text, int colorId){
        if(setText(textViewId, text)) {
            TextView data = (TextView) this.getView().findViewById(textViewId);
            data.setTextColor(ContextCompat.getColor(getContext(), colorId));
            return true;
        }
        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        DetailsFragment.instance = null;
        MainActivity.log.setFragment(null);
    }
}
