package com.example.myapplication;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment {
    Calendar cal;
    public DatePickerFragment(Calendar calendar) {
        cal = calendar;
    }
    public DatePickerFragment() {
        cal = null;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (cal==null) {
            cal = Calendar.getInstance();
        }
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(),(DatePickerDialog.OnDateSetListener) getActivity(),year,month, day);
    }
}
