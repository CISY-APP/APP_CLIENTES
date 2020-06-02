package com.example.app_clientes.ui.cerrar;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.app_clientes.R;
import com.example.app_clientes.vistas.VentanaChatIndividual;
import com.example.app_clientes.vistas.VentanaLogin;

public class CerrarSesionFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cerrarsesion, container, false);
        VentanaLogin ventanaLogin = new VentanaLogin();
        Intent VentanaLogin = new Intent(getContext(), ventanaLogin.getClass());
        startActivity(VentanaLogin);
        return view;
    }
}
