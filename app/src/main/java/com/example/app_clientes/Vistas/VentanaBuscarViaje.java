package com.example.app_clientes.Vistas;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app_clientes.Otros.CalendarioFragment;
import com.example.app_clientes.Otros.HoraFragment;
import com.example.app_clientes.R;

public class VentanaBuscarViaje extends AppCompatActivity {

    private ImageView IVFlechaAtras;
    private ImageView IVAceptar;
    private EditText ETOrigenBuscarViaje;
    private EditText ETDestinoBuscarViaje;
    private EditText ETFechaViajeBuscarViaje;
    private EditText ETHoraViajeBuscarViaje;

    private String ID_USUARIO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_viaje);

        ID_USUARIO = cargarCredencialesIdUsuario();

        ETOrigenBuscarViaje = findViewById(R.id.ETOrigenBuscarViaje);
        ETDestinoBuscarViaje = findViewById(R.id.ETDestinoBuscarViaje);
        ETFechaViajeBuscarViaje = findViewById(R.id.ETFechaViajeBuscarViaje);
        ETFechaViajeBuscarViaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();

            }
        });
        ETHoraViajeBuscarViaje = findViewById(R.id.ETHoraViajeBuscarViaje);
        ETHoraViajeBuscarViaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });
        IVFlechaAtras = findViewById(R.id.IVFlechaAtras);
        IVFlechaAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        IVAceptar = findViewById(R.id.IVAceptarBuscarViaje);
        IVAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Consulta a la base de datos para ver si hay viajes con esas condiciones

                /*VentanaViajeNoEncontrado ventanaViajeNoEncontrado = new VentanaViajeNoEncontrado();
                Intent VentanaViajeNoEncontrado = new Intent(getApplicationContext(), VentanaViajeNoEncontrado.class);
                startActivity(VentanaViajeNoEncontrado);

                VentanaViajeReservado ventanaViajeReservado = new VentanaViajeReservado();
                Intent VentanaViajeReservado = new Intent(getApplicationContext(), VentanaViajeReservado.class);
                startActivity(VentanaViajeReservado);*/
                
                VentanaViajesEncontrados ventanaViajesEncontrados = new VentanaViajesEncontrados();
                Intent VentanaViajesEncontrados = new Intent(getApplicationContext(), VentanaViajesEncontrados.class);
                VentanaViajesEncontrados.putExtra("origen",ETOrigenBuscarViaje.getText().toString());
                VentanaViajesEncontrados.putExtra("destino",ETDestinoBuscarViaje.getText().toString());
                VentanaViajesEncontrados.putExtra("fecha",ETFechaViajeBuscarViaje.getText().toString());
                VentanaViajesEncontrados.putExtra("hora",ETHoraViajeBuscarViaje.getText().toString());
                startActivity(VentanaViajesEncontrados);
            }
        });
    }
    //Muestra un cuadro de dialogo con un calendario al pulsar sobre el EditextFecha
    //Este nos permite copturar la fecha para posteriormente hacer un insert en la base de datos con esta
    private void showDatePickerDialog() {
        CalendarioFragment newFragment = CalendarioFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 porque el mes de enero es 0
                final String selectedDate = day + " / " + (month + 1) + " / " + year;
                ETFechaViajeBuscarViaje.setText(selectedDate);
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
                String minutos ="0";
                if(minute<10){
                    minutos = minutos + minute;
                }else {
                    minutos = ""+minute;
                }
                final String selectedDate = hourOfDay + ":" + minutos;
                ETHoraViajeBuscarViaje.setText(selectedDate);
            }

        });
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private String cargarCredencialesIdUsuario(){
        SharedPreferences credenciales = getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        return credenciales.getString("idUsuario","0");
    }
}

