package com.example.app_clientes.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import com.example.app_clientes.R;
import com.example.app_clientes.vistas.VentanaBuscarViaje;
import com.example.app_clientes.vistas.VentanaPublicarViaje;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomeFragment extends Fragment {

    private Button BTPublicar;
    private Button BTBuscar;
    private EditText ETFecha;
    private EditText ETHora;
    private EditText ETOrigen;
    private EditText ETDestino;
    private Button BTBuscarDialog;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    public HomeFragment() {
    }

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
