package com.example.app_clientes.ui.Home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.example.app_clientes.R;
import com.example.app_clientes.Vistas.VentanaPublicarViaje;

public class HomeFragment extends Fragment {

    private Button BTPublicar;
    private Button BTBuscar;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_home, container, false);

        //Asociamos los atributos de la clase con los item del fragment.
        BTPublicar =  vista.findViewById(R.id.BTPublicar);
        BTBuscar = vista.findViewById(R.id.BTBuscar);

        //Se asigna un setOnClickListener al atributo BTPublicar para que el boton del fragment pueda reaccionar a eventos onClick del usuario
        //Este hace que se cre un nuevo intent en este caso de la VentanaPublicarViaje y lo abre.
        BTPublicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llamadaVentanaPublicarViaje();
            }
        });
        BTBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return vista;
    }

    private void llamadaVentanaPublicarViaje() {
        VentanaPublicarViaje ventanaPublicarViaje = new VentanaPublicarViaje();
        Intent VentanaPublicarViaje = new Intent(getContext(), ventanaPublicarViaje.getClass());
        //VentanaPublicarViaje.putExtra("usuario",ETUsuario.getText().toString());
        //VentanaPublicarViaje.putExtra("control",ETControl.getText().toString());
        startActivity(VentanaPublicarViaje);
    }
}
