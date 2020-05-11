package com.example.app_clientes.ui.Chat;

import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;

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
    private String uriFotoUsuario = "";

    private PendingIntent pendingIntent;
    private final static String CHANNEL_ID = "NOTIFICACION";
    private final static int NOTIFICACION = 0;
    private static final int GALERY_INTENT = 1;
    private static final String EMAIL_USUARIO = "EMAIL DE EL USUARIO 1";

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);


        cargarImagenUsuario();

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

        //esto debera llegar en un BUNDLE
        TVNombreChat.setText("Javier");

        //Implementacion de firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("chat"); //Sala de chat (nombre)

        adapterMensajes = new miApdapterChat(getActivity());
        adapterMensajes.setEmailUsuario(EMAIL_USUARIO);
        LinearLayoutManager l= new LinearLayoutManager(getContext());
        RVMensajesChat.setLayoutManager(l);
        RVMensajesChat.setAdapter(adapterMensajes);
        BTNEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.push().setValue(new Mensaje(ETTXTMensaje.getText().toString()+"", TVNombreChat.getText().toString()+"", EMAIL_USUARIO ,getHoraSistema(), uriFotoUsuario ));
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
    //Coge la direccion del dispositivo
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALERY_INTENT && resultCode == RESULT_OK) {
            //Coge la Uri del dispositivo
            Uri uri = data.getData();
            //Cambia la imagen desde el dispositivo
            Glide.with(getContext()).load(uri).into(fotoPerfil);
            //Crea una direccion para poder subir la imagen a firebase
            StorageReference filePath = storageReference.child("Fotos").child(EMAIL_USUARIO);
            //Utiliza la direccion para coger la imagen del dispositivo, sube la imagen a firebase y escucha si se ha realizado de manera adecuada
            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getContext(), "Imagen cambiada", Toast.LENGTH_SHORT).show();
                    //Coje la URL de la imagen de la carpeta que le indiquemos con el nombre que le indiquemos de firebase
                    storageReference.child("Fotos").child(EMAIL_USUARIO).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            //Meter la URI en un String para posteriormente hacer el update o el insert en la base de datos
                            uriFotoUsuario = uri.toString();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(getContext(), "No se ha podido realizar el insert en la base de datos", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

        }
    }

    //Metodo para cargar la imagen del usuario
    public void cargarImagenUsuario(){
        //Inatancia el objeto de tipo storageReference
        storageReference = FirebaseStorage.getInstance().getReference();
        //Coje la URL de la imagen de la carpeta que le indiquemos con el nombre que le indiquemos
        storageReference.child("Fotos").child(EMAIL_USUARIO).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //Si la carga es optima la coloca en fotoPerfil
                uriFotoUsuario = uri.toString();
                Glide.with(getActivity()).load(uri).into(fotoPerfil);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                //Si la carga no es optima es decir que no existe la direccion proporcinada carga una imagen por defecto
                fotoPerfil.setImageResource(R.drawable.user);
            }
        });
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
