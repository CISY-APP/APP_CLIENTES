//Indicamos a que paquete pertenece esta clase:
package com.example.app_clientes.ui.cerrar;
//Importamos los siguientes paquetes:
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.app_clientes.Biblioteca;
import com.example.app_clientes.R;
import com.example.app_clientes.vistas.VentanaLogin;

import java.util.Objects;

//Clase que contiene toda la logica y conexion con la ventana de cerrar sesion:
public class CerrarSesionFragment extends Fragment {
    //Metodo encargado de crear la vista:
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cerrarsesion, container, false);
        //Vamos a cerrar la sesion:
        //Cerramos todas las actividades mandando esta señal que estan preparadas para filtrar, no funciona bien si android cerro esa actividad por falta de memoria:
        Intent intent = new Intent("cierre_de_sesion");
        requireActivity().sendBroadcast(intent);
        //Ponemos en estado null el objeto de sesion:
        Biblioteca.usuarioSesion=null;
        //Lanzamos un intent a login:
        VentanaLogin ventanaLogin = new VentanaLogin();
        Intent VentanaLogin = new Intent(getContext(), ventanaLogin.getClass());
        startActivity(VentanaLogin);
        //Cerramos actividades con el siguiente metodo:
        requireActivity().finishAffinity();
        return view;
    }
}
