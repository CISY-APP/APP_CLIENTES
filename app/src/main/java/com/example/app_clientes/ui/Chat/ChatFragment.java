package com.example.app_clientes.ui.Chat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.app_clientes.Adapter.miApdapterChat;
import com.example.app_clientes.Pojos.Mensaje;
import com.example.app_clientes.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class ChatFragment extends Fragment {

    private TextView   TVNombreChat;
    private RecyclerView RVMensajesChat;
    private EditText ETTXTMensaje;
    private Button BTNEnviar ;
    private CircleImageView fotoPerfil;

    private miApdapterChat adapterMensajes;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    private static final int GALERY_INTENT = 1;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        storageReference = FirebaseStorage.getInstance().getReference();

        TVNombreChat = view.findViewById(R.id.TVNombreChatUsuario);
        RVMensajesChat = view.findViewById(R.id.RVMensajesChat);
        ETTXTMensaje = view.findViewById(R.id.ETTXTMensaje);
        BTNEnviar = view.findViewById(R.id.BTMenajeEnviar);
        fotoPerfil = view.findViewById(R.id.fotoPerfil);
        fotoPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,GALERY_INTENT);
            }
        });

        storageReference.child("Fotos").child("javdeiiier@gmail.com").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getActivity()).load(uri).into(fotoPerfil);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

        //esto debera llegar en un BUNDLE
        TVNombreChat.setText("Javier");

        //Implementacion de firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("chat"); //Sala de chat (nombre)


        adapterMensajes = new miApdapterChat(getActivity());
        adapterMensajes.setEmailUsuario("javdeiiier@gmail.com");

        LinearLayoutManager l= new LinearLayoutManager(getContext());
        RVMensajesChat.setLayoutManager(l);
        RVMensajesChat.setAdapter(adapterMensajes);
        BTNEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                databaseReference.push().setValue(new Mensaje(ETTXTMensaje.getText().toString()+"", TVNombreChat.getText().toString()+"", "javdeiiier@gmail.com" ,getHoraSistema()));
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALERY_INTENT &&  resultCode == RESULT_OK) {
            if (requestCode == GALERY_INTENT && resultCode == RESULT_OK) {

                Uri uri = data.getData();
                Glide.with(requireContext()).load(uri).into(fotoPerfil);
                StorageReference filePath = storageReference.child("Fotos").child("javdeiiier@gmail.com");
                Toast.makeText(getActivity(), "Imagen cambiada", Toast.LENGTH_SHORT).show();
                filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    }
                });
            }
        }
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
