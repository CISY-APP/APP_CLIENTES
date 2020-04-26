package com.example.app_clientes.Vistas;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app_clientes.Otros.CalendarioFragment;
import com.example.app_clientes.Otros.HoraFragment;
import com.example.app_clientes.R;

public class VentanaPublicarViaje extends AppCompatActivity {

    //Atributos de la clase
    private EditText ETFecha;
    private EditText ETHora;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ventana_publicar_viaje);

        //Editext al pulsar sobre este muestra un calendario
        ETFecha = findViewById(R.id.ETFecha);
        ETFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        //Editext al pulsar sobre este muestra un reloj
        ETHora = findViewById(R.id.ETHora);
        ETHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });
    }

    //Muestra un cuadro de dialogo con un calendario al pulsar sobre el EditextFecha
    //Este nos permite copturar la fecha para posteriormente hacer un insert en la base de datos con esta
    private void showDatePickerDialog() {
        CalendarioFragment newFragment = CalendarioFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because January is zero
                final String selectedDate = day + " / " + (month+1) + " / " + year;
                ETFecha.setText(selectedDate);
            }
        });

        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    //Muestra un cuadro de dialogo con un reloj al pulsar sobre el EditextFecha
    //Este nos permite copturar la hora para posteriormente hacer un insert en la base de datos con esta
    private void showTimePickerDialog() {
        HoraFragment newFragment = HoraFragment.newInstance(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                // +1 because January is zero
                final String selectedDate = hourOfDay + ":" + minute;
                ETHora.setText(selectedDate);
            }

        });
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

}