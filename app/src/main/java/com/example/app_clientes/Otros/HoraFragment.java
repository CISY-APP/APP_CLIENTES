package com.example.app_clientes.Otros;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class HoraFragment extends DialogFragment {

    private TimePickerDialog.OnTimeSetListener listener;

    public static HoraFragment newInstance(TimePickerDialog.OnTimeSetListener listener) {
        HoraFragment fragment = new HoraFragment();
        fragment.setListener(listener);
        return fragment;
    }

    private void setListener(TimePickerDialog.OnTimeSetListener listener) {
        this.listener = listener;
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR);
        int minute = c.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(), listener, hour, minute, true);
    }

}