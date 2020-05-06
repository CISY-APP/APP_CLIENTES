package com.example.app_clientes.ui.Chat;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_clientes.Adapter.miApdapterChat;
import com.example.app_clientes.Pojos.Mensaje;
import com.example.app_clientes.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatFragment extends Fragment {

    private CircleImageView fotoPerfil;
    private TextView TVNombre;
    private TextView   TVNombreMensajeChat;
    private RecyclerView RVMensajesChat;
    private EditText ETTXTMensaje;
    private Button BTNEnviar ;

    private miApdapterChat adapterMensajes;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        fotoPerfil = view.findViewById(R.id.fotoPerfil);
        TVNombre = view.findViewById(R.id.TVNombreChat);
        RVMensajesChat = view.findViewById(R.id.RVMensajesChat);
        ETTXTMensaje = view.findViewById(R.id.ETTXTMensaje);
        BTNEnviar = view.findViewById(R.id.BTMenajeEnviar);

        //esto debera llegar en un BUNDLE
        TVNombre.setText("Javier");

        //Implementacion de firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("chat"); //Sala de chat (nombre)

        adapterMensajes = new miApdapterChat(getActivity());
        LinearLayoutManager l= new LinearLayoutManager(getContext());
        RVMensajesChat.setLayoutManager(l);
        RVMensajesChat.setAdapter(adapterMensajes);
        BTNEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                databaseReference.push().setValue(new Mensaje(ETTXTMensaje.getText().toString()+"", TVNombre.getText().toString()+"", "" ,getHoraSistema()));
                ETTXTMensaje.setText("");
            }
        });

        //Hce que el adapter este observando los nuevos cambios que en este se produzcan.
        adapterMensajes.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
            }
        });

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Mensaje m = dataSnapshot.getValue(Mensaje.class);
                adapterMensajes.addMensaje(m);
                setScrollBar();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return view;
    }

    //Metodo utilizado para que el adparter se arrastre hacia abajo con cada nuevo mensaje
    private void setScrollBar(){
        RVMensajesChat.scrollToPosition(adapterMensajes.getItemCount()-1);
    }

    //Obtiene la hora del sistema
    private String getHoraSistema() {
        Calendar c1 = Calendar.getInstance();
        String time;
        return time = String.format("%02d:%02d", c1.get(Calendar.HOUR_OF_DAY), c1.get(Calendar.MINUTE));
    }


}
