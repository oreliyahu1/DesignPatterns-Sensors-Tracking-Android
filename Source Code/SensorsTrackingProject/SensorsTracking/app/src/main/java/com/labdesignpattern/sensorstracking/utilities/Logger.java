package com.labdesignpattern.sensorstracking.utilities;

import android.widget.EditText;

import androidx.fragment.app.Fragment;

public class Logger {
    protected Fragment frtext;
    protected int editTextMultilineId;
    protected String buffer;
    private static Logger instance = null;

    public static Logger getInstance(){
        if(Logger.instance == null)
            Logger.instance = new Logger();
        return Logger.instance;
    }

    private Logger(){}

    public void init(Fragment frtext, int editTextMultilineId){
        this.frtext = frtext;
        this.editTextMultilineId = editTextMultilineId;
        this.buffer = "";
    }

    public void setFragment(Fragment frtext){
        this.frtext = frtext;
    }

    public boolean addNewLineText(String text){
        return addText(text + "\n");
    }

    public boolean addText(String text){
        try {
            EditText et = (EditText) frtext.getView().findViewById(editTextMultilineId);
            et.append(text);
            return true;
        }catch (Exception ex){
            buffer += text;
            return false;
        }
    }

    public boolean clearBuffer(){
        try {
            EditText et = (EditText) frtext.getView().findViewById(editTextMultilineId);
            et.append(buffer + "\n");
            buffer = "";
            return true;
        }catch (Exception ex){
            return false;
        }
    }
}
