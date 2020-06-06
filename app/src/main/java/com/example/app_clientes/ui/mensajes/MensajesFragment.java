package com.example.app_clientes.ui.mensajes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_clientes.Biblioteca;
import com.example.app_clientes.adapter.MiAdapterMensajes;
import com.example.app_clientes.pojos.Conversacion;
import com.example.app_clientes.R;
import com.example.app_clientes.vistas.VentanaChatIndividual;

import com.example.app_clientes.vistas.VentanaLogin;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class MensajesFragment extends Fragment{

    private RecyclerView.LayoutManager layoutManager;
    private MiAdapterMensajes miAdapterMensajes;
    private RecyclerView recyclerView;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference1;
    private final ArrayList<Conversacion> conversacionArrayList = new ArrayList<>();

    private String ID_USUARIO;

    private ValueEventListener listener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            ArrayList<Conversacion> conversacionArrayList = new ArrayList<>();
            for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                Conversacion c = dsp.getValue(Conversacion.class);
                conversacionArrayList.add(c);
                miAdapterMensajes.setConversaciones(conversacionArrayList);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mensajes, container, false);

        ID_USUARIO = Biblioteca.usuarioSesion.getIdusuario().toString();

        //Asociamos los atributos con los objeto del layoud para poder usarlos
        //INSTANCIAMOS Y ASOCIAMOS ELEMENTOS NECESARIOS PARA EL CORRECTO FUNCIONAMIENTO DEL RECYCLERVIEW
        recyclerView = view.findViewById(R.id.RVConversacionesChat); //Vinculamos el recyclerview del xml con el de la clase main
        recyclerView.setHasFixedSize(
                true);// RecyclerView sabe de antemano que su tamaño no depende del contenido del adaptador, entonces omitirá la comprobación de si su tamaño debería cambiar cada vez que se agregue o elimine un elemento del adaptador.(mejora el rendimiento)
        layoutManager = new LinearLayoutManager(getContext());//Creamos el layoutManager de tipo GridLayaout que vamos a utilizar
        miAdapterMensajes = new MiAdapterMensajes(getActivity(), conversacionArrayList);
        recyclerView.setLayoutManager(layoutManager);//Asociamos al recyclerView el layoutManager que creamos en el paso anterior
        recyclerView.setAdapter(miAdapterMensajes);//Vinculamos el adapter al recyclerView
        miAdapterMensajes.setOnClickListener(new MiAdapterMensajes.OnItemClickListener() {
            @Override
            public void OnAbreChatClick(int position) {
                Intent VentanaChatIndividual = new Intent(getContext(), VentanaChatIndividual.class);
                VentanaChatIndividual.putExtra("ID_USUARIO", Biblioteca.usuarioSesion.getIdusuario());
                VentanaChatIndividual.putExtra("ID_USUARIO_CONVER", miAdapterMensajes.getConversacion(position).getId_usuario());
                VentanaChatIndividual.putExtra("NOMBRE_USUARIO_CONVER", miAdapterMensajes.getConversacion(position).getNombreUsuario());
                VentanaChatIndividual.putExtra("FOTO_USUARIO_CONVER", miAdapterMensajes.getConversacion(position).getFotoUsuarioContrario());
                startActivity(VentanaChatIndividual);
            }
        });

        //Recibidor de broadcast para cerrar sesion:
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent intent) {
                String action = intent.getAction();
                if (action.equals("cierre_de_sesion")) {
                    requireActivity().finish();
                }
            }
        };
        requireActivity().registerReceiver(broadcastReceiver, new IntentFilter("cierre_de_sesion"));
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //Implementacion de firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference1 = firebaseDatabase.getReference("USUARIOS").child(ID_USUARIO); //Sala de chat (nombre)
        databaseReference1.addValueEventListener(listener);
    }

    //Obtiene la hora del sistema
    private String getHoraSistema() {
        Calendar c1 = Calendar.getInstance();
        String time;
        return time = String.format("%02d:%02d", c1.get(Calendar.HOUR_OF_DAY), c1.get(Calendar.MINUTE));
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        databaseReference1.removeEventListener(listener);
    }

}