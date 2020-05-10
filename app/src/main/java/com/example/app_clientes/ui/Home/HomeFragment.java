package com.example.app_clientes.ui.Home;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;


import com.example.app_clientes.Otros.CalendarioFragment;
import com.example.app_clientes.Otros.HoraFragment;
import com.example.app_clientes.R;
import com.example.app_clientes.Vistas.VentanaBuscarViaje;
import com.example.app_clientes.Vistas.VentanaCambiarContrasena;
import com.example.app_clientes.Vistas.VentanaPublicarViaje;
import com.example.app_clientes.Vistas.VentanaViajesEncontrados;

import java.util.Calendar;

public class HomeFragment extends Fragment {

    private Button BTPublicar;
    private Button BTBuscar;
    private EditText ETFecha;
    private EditText ETHora;
    private EditText ETOrigen;
    private EditText ETDestino;
    private Button BTBuscarDialog;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_home, container, false);

        //Asociamos los atributos de la clase con los item del fragment.
        BTPublicar = vista.findViewById(R.id.BTPublicar);
        BTPublicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llamadaVentanaPublicarViaje();
            }
        });

        BTBuscar = vista.findViewById(R.id.BTBuscar);
        BTBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llamadaVentanaBuscarViaje();
            }
        });
        return vista;
    }

    private void llamadaVentanaBuscarViaje() {
        VentanaBuscarViaje ventanaBuscarViaje = new VentanaBuscarViaje();
        Intent VentanaBuscarViaje = new Intent(getContext(), ventanaBuscarViaje.getClass());
        startActivity(VentanaBuscarViaje);
        //showDialogBuscarViaje();
    }

    //Llamamos al  ventanaPublicarViaje
    private void llamadaVentanaPublicarViaje() {
        VentanaPublicarViaje ventanaPublicarViaje = new VentanaPublicarViaje();
        Intent VentanaPublicarViaje = new Intent(getContext(), ventanaPublicarViaje.getClass());
        //VentanaPublicarViaje.putExtra("usuario",ETUsuario.getText().toString());
        //VentanaPublicarViaje.putExtra("control",ETControl.getText().toString());
        startActivity(VentanaPublicarViaje);
    }

}
