package com.example.myapplication;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment {
    Calendar cal;
    public TimePickerFragment(Calendar c) {
        this.cal = c;
    }
    public TimePickerFragment(){
        cal = null;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (cal==null) {
            cal = Calendar.getInstance();
        }
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(),(TimePickerDialog.OnTimeSetListener) getActivity(),hour,minute, true);
    }
}
