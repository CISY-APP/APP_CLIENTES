package com.example.app_clientes.ui.Mensajes;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_clientes.Adapter.miAdapterMensajes;
import com.example.app_clientes.Pojos.Conversacion;
import com.example.app_clientes.Pojos.Mensaje;
import com.example.app_clientes.R;
import com.example.app_clientes.Vistas.VentanaChatIndividual;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

public class MensajesFragment extends Fragment {

    private RecyclerView.LayoutManager layoutManager;
    private miAdapterMensajes miAdapterMensajes;
    private RecyclerView recyclerView;
    private ArrayList<Conversacion> conversacionList = new ArrayList<>();
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference1;
    private DatabaseReference databaseReference2;
    private String idConversacion;

    private static final String ID_USUARIO = "1";

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mensajes, container, false);

        //Implementacion de firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference1 = firebaseDatabase.getReference("USUARIOS").child(ID_USUARIO); //Sala de chat (nombre)
        databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    Conversacion c = dsp.getValue(Conversacion.class);
                    conversacionList.add(c);
                    miAdapterMensajes.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //Asociamos los atributos con los objeto del layoud para poder usarlos
        //INSTANCIAMOS Y ASOCIAMOS ELEMENTOS NECESARIOS PARA EL CORRECTO FUNCIONAMIENTO DEL RECYCLERVIEW
        recyclerView = view.findViewById(R.id.RVConversacionesChat); //Vinculamos el recyclerview del xml con el de la clase main
        recyclerView.setHasFixedSize(
                true);// RecyclerView sabe de antemano que su tamaño no depende del contenido del adaptador, entonces omitirá la comprobación de si su tamaño debería cambiar cada vez que se agregue o elimine un elemento del adaptador.(mejora el rendimiento)
        layoutManager = new LinearLayoutManager(getContext());//Creamos el layoutManager de tipo GridLayaout que vamos a utilizar
        miAdapterMensajes = new miAdapterMensajes(conversacionList);//Instanciamos un objeto de tipo Example_Adapter
        recyclerView.setLayoutManager(layoutManager);//Asociamos al recyclerView el layoutManager que creamos en el paso anterior
        recyclerView.setAdapter(miAdapterMensajes);//Vinculamos el adapter al recyclerView
        miAdapterMensajes.setOnClickListener(new miAdapterMensajes.OnItemClickListener() {
            @Override
            public void OnAbreChatClick(int position) {
                VentanaChatIndividual ventanaChatIndividual = new VentanaChatIndividual();
                Intent VentanaChatIndividual = new Intent(getContext(), ventanaChatIndividual.getClass());
                VentanaChatIndividual.putExtra("CODIGO_SALA", conversacionList.get(position).getIdConversacion());
                startActivity(VentanaChatIndividual);
            }
        });
        return view;
    }

    //Obtiene la hora del sistema
    private String getHoraSistema() {
        Calendar c1 = Calendar.getInstance();
        String time;
        return time = String.format("%02d:%02d", c1.get(Calendar.HOUR_OF_DAY), c1.get(Calendar.MINUTE));
    }
}
