package com.labdesignpattern.sensorstracking.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.labdesignpattern.sensorstracking.R;

public class AboutFragment extends Fragment {
    public static AboutFragment instance = null;

    private AboutFragment(){}
    public static AboutFragment getInstance(){
        if(AboutFragment.instance == null)
            AboutFragment.instance = new AboutFragment();
        return  AboutFragment.instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView textView = view.findViewById(R.id.textview_about_submitters);
        textView.setText(getString(R.string.about_submitters));
    }
}
