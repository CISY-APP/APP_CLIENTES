package com.example.app_clientes.Vistas;

import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.app_clientes.Adapter.miApdapterChat;
import com.example.app_clientes.Pojos.Conversacion;
import com.example.app_clientes.Pojos.Mensaje;
import com.example.app_clientes.R;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class VentanaChatIndividual extends AppCompatActivity {

    private RecyclerView RVMensajesChat;
    private Button BTMenajeEnviarChatIndividual;
    private EditText ETTXTMensajeChatIndividual;
    private CircleImageView IVfotoUsuarioChatIndividual;
    private ImageView IVFlechaAtrasChatIndividual;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference chatReference;
    private StorageReference storageReference;

    private String uriFotoUsuario = "";
    private String uriFotoUsuarioContrario = "";
    private String CHANNEL_ID = "1";
    private miApdapterChat adapterMensajes;
    private String ID_USUARIO;
    private String ID_USUARIO_CONVER;
    private String chatName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_individual);

        ID_USUARIO = cargarCredencialesIdUsuario();
        ID_USUARIO_CONVER = recibirIDUsuarioConver();

        chatName = getChatName(ID_USUARIO, ID_USUARIO_CONVER);

        cargarImagenUsuario();
        cargarMiImagen();

        crearSalaSiEsNecesario(ID_USUARIO, ID_USUARIO_CONVER);


        RVMensajesChat = findViewById(R.id.RVMensajesChat);
        ETTXTMensajeChatIndividual = findViewById(R.id.ETTXTMensajeChatIndividual);
        BTMenajeEnviarChatIndividual = findViewById(R.id.BTMenajeEnviarChatIndividual);
        IVfotoUsuarioChatIndividual = findViewById(R.id.IVfotoUsuarioChatIndividual);
        IVFlechaAtrasChatIndividual = findViewById(R.id.IVFlechaAtrasChatIndividual);
        IVFlechaAtrasChatIndividual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        //Implementacion de firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        chatReference = firebaseDatabase.getReference(chatName); //Sala de chat (nombre)
        chatReference.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Mensaje m = dataSnapshot.getValue(Mensaje.class);
                adapterMensajes.addMensaje(m);
                setScrollBar();

                NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                        .setSmallIcon(R.drawable.enviar)
                        .setContentTitle("Nuevo mensaje de " + ID_USUARIO_CONVER)
                        .setContentText(m.getMensaje())
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(m.getMensaje()))
                        .setPriority(NotificationCompat.PRIORITY_MAX);

                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(1, builder.build());
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

        adapterMensajes = new miApdapterChat(this);
        adapterMensajes.setIDUsuario(ID_USUARIO);
        LinearLayoutManager l = new LinearLayoutManager(this);
        RVMensajesChat.setLayoutManager(l);
        RVMensajesChat.setAdapter(adapterMensajes);
        BTMenajeEnviarChatIndividual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatReference.push().setValue(new Mensaje(ETTXTMensajeChatIndividual.getText().toString(), "Eduardo", ID_USUARIO, getHoraSistema(), uriFotoUsuario));

                DatabaseReference hopperRef = firebaseDatabase.getReference("USUARIOS").child(ID_USUARIO).child(chatName); //Sala de chat (nombre)
                Map<String, Object> hopperUpdates = new HashMap<>();
                hopperUpdates.put("horaUltimoMensaje", getHoraSistema());
                hopperUpdates.put("ultimoMensaje", ETTXTMensajeChatIndividual.getText().toString());
                hopperRef.updateChildren(hopperUpdates);

                DatabaseReference hopperRef1 = firebaseDatabase.getReference("USUARIOS").child(ID_USUARIO_CONVER).child(chatName); //Sala de chat (nombre)
                Map<String, Object> hopperUpdates1 = new HashMap<>();
                hopperUpdates1.put("horaUltimoMensaje", getHoraSistema());
                hopperUpdates1.put("ultimoMensaje", ETTXTMensajeChatIndividual.getText().toString());
                hopperRef1.updateChildren(hopperUpdates);

                ETTXTMensajeChatIndividual.setText("");

            }
        });

    }

    //Metodo para cargar la imagen del usuario
    public void cargarImagenUsuario() {
        //Inatancia el objeto de tipo storageReference
        storageReference = FirebaseStorage.getInstance().getReference();
        //Coje la URL de la imagen de la carpeta que le indiquemos con el nombre que le indiquemos
        storageReference.child("Fotos").child(ID_USUARIO_CONVER).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getApplicationContext()).load(uri).into(IVfotoUsuarioChatIndividual);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

            }
        });
    }
    //Metodo para cargar la imagen del usuario
    public void cargarMiImagen() {
        //Inatancia el objeto de tipo storageReference
        storageReference = FirebaseStorage.getInstance().getReference();
        //Coje la URL de la imagen de la carpeta que le indiquemos con el nombre que le indiquemos
        storageReference.child("Fotos").child(ID_USUARIO).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                uriFotoUsuario = uri.toString();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

            }
        });
    }

    //Metodo utilizado para que el adparter se arrastre hacia abajo con cada nuevo mensaje
    private void setScrollBar() {
        RVMensajesChat.scrollToPosition(adapterMensajes.getItemCount() - 1);
    }

    private String recibirIDUsuarioConver() {
        Bundle datosIN = getIntent().getExtras();
        return datosIN.getString("ID_USUARIO_CONVER");
    }

    //Obtiene la hora del sistema
    private String getHoraSistema() {
        Calendar c1 = Calendar.getInstance();
        String time;
        return time = String.format("%02d:%02d", c1.get(Calendar.HOUR_OF_DAY), c1.get(Calendar.MINUTE));
    }

    private void crearSalaSiEsNecesario(final String ID_USUARIO_1, final String ID_USUARIO_2) {

        DatabaseReference databaseReference1;
        databaseReference1 = firebaseDatabase.getReference(chatName); //Sala de chat (nombre)

        databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    crearSala(ID_USUARIO_1, ID_USUARIO_2);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    private void crearSala(String ID_USUARIO_1, String ID_USUARIO_2) {

        DatabaseReference databaseReference1;
        databaseReference1 = firebaseDatabase.getReference(chatName); //Sala de chat (nombre)

        DatabaseReference databaseReference2;
        databaseReference1.push();

        databaseReference2 = firebaseDatabase.getReference("USUARIOS").child(ID_USUARIO_1).child(chatName); //Sala de chat (nombre)
        databaseReference2.setValue(new Conversacion(databaseReference1.getKey(), ID_USUARIO_2.toString(), "", "", uriFotoUsuarioContrario));
        databaseReference2.push();

        databaseReference2 = firebaseDatabase.getReference("USUARIOS").child(ID_USUARIO_2).child(chatName); //Sala de chat (nombre)
        databaseReference2.setValue(new Conversacion(databaseReference1.getKey(), ID_USUARIO_1.toString(), "", "", uriFotoUsuario));
        databaseReference2.push();
    }


    //ordena los numeros de los salas
    private String getChatName(String user1, String user2) {
        String[] ids = new String[]{user1, user2};
        Arrays.sort(ids);
        return ids[0] + "-" + ids[1];
    }

    private String cargarCredencialesIdUsuario(){
        SharedPreferences credenciales = getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        return credenciales.getString("idUsuario","0");
    }

}

