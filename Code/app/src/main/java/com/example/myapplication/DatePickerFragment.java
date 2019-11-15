package com.example.myapplication;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

/**
 * This class creates the fragment that holds the DatePicker
 */
public class DatePickerFragment extends DialogFragment {
    Calendar cal;
    // Multiple constructors so that I can use in Add and Edit

    /**
     * Creates the DatePicker from an existing calendar object
     * @param calendar
     */
    public DatePickerFragment(Calendar calendar) {
        cal = calendar;
    }

    /**
     * Create the DatePicker with the current time
     */
    public DatePickerFragment() {
        cal = Calendar.getInstance();
    }

    /**
     * Constructs the Dialog with the DatePicker in it
     * @param savedInstanceState this is the values saved in previous instances of the class
     * @return returns a dialog with the DatePicker in it
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(),(DatePickerDialog.OnDateSetListener) getActivity(),year,month, day);
    }
}
