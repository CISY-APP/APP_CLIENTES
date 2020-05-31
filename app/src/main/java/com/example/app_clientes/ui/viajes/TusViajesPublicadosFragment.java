package com.example.app_clientes.ui.viajes;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.app_clientes.Biblioteca;
import com.example.app_clientes.R;
import com.example.app_clientes.adapter.MiAdapterTusViajesDisfrutados;
import com.example.app_clientes.adapter.MiAdapterTusViajesPublicados;
import com.example.app_clientes.pojos.Usuario;
import com.example.app_clientes.pojos.Vehiculo;
import com.example.app_clientes.pojos.Viaje;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TusViajesPublicadosFragment extends Fragment {

    private RecyclerView recyclerViewTusViajes;
    private MiAdapterTusViajesPublicados miAdapterTusViajes;
    private ArrayList<Viaje> misViajesList = new ArrayList<>();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tusviajespublicados, container, false);

        recyclerViewTusViajes = view.findViewById(R.id.RVViajesPublicados);

        cargarViajesViajesDisfrutados();

        //Asociamos los atributos con los objeto del layout para poder usarlos
        //INSTANCIAMOS Y ASOCIAMOS ELEMENTOS NECESARIOS PARA EL CORRECTO FUNCIONAMIENTO DEL RECYCLERVIEW
        recyclerViewTusViajes.setHasFixedSize(true);// RecyclerView sabe de antemano que su tamaño no depende del contenido del adaptador, entonces omitirá la comprobación de si su tamaño debería cambiar cada vez que se agregue o elimine un elemento del adaptador.(mejora el rendimiento)
        recyclerViewTusViajes.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        miAdapterTusViajes = new MiAdapterTusViajesPublicados(getContext(), misViajesList);//Instanciamos un objeto de tipo Example_Adapter
        recyclerViewTusViajes.setAdapter(miAdapterTusViajes);//Vinculamos el adapter al recyclerView
        miAdapterTusViajes.setOnClickListener(new MiAdapterTusViajesPublicados.OnItemClickListener() {
            @Override
            public void OnEliminarViajePublicadoClick(int position) {
                //ELIMINAR EL VIAJE QUE AUN NO SE HA REALIZADO
            }
        });

        return view;
    }

    //CARGA LOS DATOS DE LOS VIAJES QUE EL USUARIO A DISFRUTADO VIAJES
    private void cargarViajesViajesDisfrutados() {
        java.util.Date utilDate = new java.util.Date(2021-1900,1,21); //fecha actual
        long lnMilisegundos = utilDate.getTime();
        java.sql.Date sqlDate = new java.sql.Date(lnMilisegundos);
        java.sql.Time sqlTime = new java.sql.Time(lnMilisegundos);

        java.util.Date utilDate1 = new java.util.Date(2021-1900,1,21); //fecha actual
        long lnMilisegundos1 = utilDate.getTime();
        java.sql.Date sqlDate1 = new java.sql.Date(lnMilisegundos);
        java.sql.Time sqlTime1 = new java.sql.Time(lnMilisegundos);
        java.sql.Timestamp sqlTimestamp1 = new java.sql.Timestamp(lnMilisegundos);

        java.util.Date utilDate2 = new java.util.Date(2019-1900,1,21); //fecha actual
        long lnMilisegundos2 = utilDate.getTime();
        java.sql.Date sqlDate2 = new java.sql.Date(lnMilisegundos);
        java.sql.Time sqlTime2 = new java.sql.Time(lnMilisegundos);
        java.sql.Timestamp sqlTimestamp2 = new java.sql.Timestamp(lnMilisegundos);
        misViajesList.add(new Viaje(Biblioteca.usuarioSesion, new Vehiculo(Biblioteca.usuarioSesion, "1234aaa", utilDate2), "Getafe", "Getafe",
                "Madrid", "Madrid", 3, utilDate2));
        misViajesList.add(new Viaje(Biblioteca.usuarioSesion, new Vehiculo(Biblioteca.usuarioSesion, "1234aaa", utilDate1), "Getafe", "Getafe",
                "Madrid", "Madrid", 3, utilDate1));
        misViajesList.add(new Viaje(Biblioteca.usuarioSesion, new Vehiculo(Biblioteca.usuarioSesion, "1234aaa", utilDate), "Getafe", "Getafe",
                "Madrid", "Madrid", 3, utilDate));
    }
}