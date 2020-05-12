package com.example.app_clientes.Vistas;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class VentanaChatIndividual extends AppCompatActivity {

    private RecyclerView RVMensajesChat;
    private Button BTMenajeEnviarChatIndividual;
    private EditText ETTXTMensajeChatIndividual;

    private FirebaseDatabase firebaseDatabase =  FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    private String CODIGO_SALA;
    private String uriFotoUsuario = "";

    private miApdapterChat adapterMensajes;
    private static final String ID_USUARIO = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_individual);

        RVMensajesChat = findViewById(R.id.RVMensajesChat);
        ETTXTMensajeChatIndividual = findViewById(R.id.ETTXTMensajeChatIndividual);
        BTMenajeEnviarChatIndividual = findViewById(R.id.BTMenajeEnviarChatIndividual);

        crearSalaSiEsNecesario(ID_USUARIO,"4");

        //Implementacion de firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(recibirNombreSala()); //Sala de chat (nombre)
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

        adapterMensajes = new miApdapterChat(this);
        adapterMensajes.setIDUsuario(ID_USUARIO);
        LinearLayoutManager l= new LinearLayoutManager(this);
        RVMensajesChat.setLayoutManager(l);
        RVMensajesChat.setAdapter(adapterMensajes);
        BTMenajeEnviarChatIndividual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.push().setValue(new Mensaje(ETTXTMensajeChatIndividual.getText().toString(), "Javier", "1",  getHoraSistema(), uriFotoUsuario ));

                DatabaseReference hopperRef = firebaseDatabase.getReference("USUARIOS").child(ID_USUARIO); //Sala de chat (nombre)
                Map<String, Object> hopperUpdates = new HashMap<>();
                hopperUpdates.put("horaUltimoMensaje", getHoraSistema());
                hopperUpdates.put("ultimoMensaje", ETTXTMensajeChatIndividual.getText().toString());
                hopperRef.updateChildren(hopperUpdates);

                DatabaseReference hopperRef1 = firebaseDatabase.getReference("USUARIOS").child("4"); //Sala de chat (nombre)
                Map<String, Object> hopperUpdates1 = new HashMap<>();
                hopperUpdates1.put("horaUltimoMensaje", getHoraSistema());
                hopperUpdates1.put("ultimoMensaje", ETTXTMensajeChatIndividual.getText().toString());
                hopperRef1.updateChildren(hopperUpdates);

                ETTXTMensajeChatIndividual.setText("");


            }
        });

    }

    //Metodo para cargar la imagen del usuario
    public void cargarImagenUsuario(){
        //Inatancia el objeto de tipo storageReference
        storageReference = FirebaseStorage.getInstance().getReference();
        //Coje la URL de la imagen de la carpeta que le indiquemos con el nombre que le indiquemos
        storageReference.child("Fotos").child("Javie_javier@gmail").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //Si la carga es optima la coloca en fotoPerfil
                uriFotoUsuario = uri.toString();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

            }
        });
    }

    //Metodo utilizado para que el adparter se arrastre hacia abajo con cada nuevo mensaje
    private void setScrollBar(){
        RVMensajesChat.scrollToPosition(adapterMensajes.getItemCount()-1);
    }

    private String recibirNombreSala() {
        Bundle datosIN=getIntent().getExtras();
        CODIGO_SALA = datosIN.getString("CODIGO_SALA");
        return CODIGO_SALA;
    }

    //Obtiene la hora del sistema
    private String getHoraSistema() {
        Calendar c1 = Calendar.getInstance();
        String time;
        return time = String.format("%02d:%02d", c1.get(Calendar.HOUR_OF_DAY), c1.get(Calendar.MINUTE));
    }

    private void crearSalaSiEsNecesario(final String ID_USUARIO_1, final String ID_USUARIO_2){

        DatabaseReference databaseReference1;
        databaseReference1 = firebaseDatabase.getReference(getChatName(ID_USUARIO_1,ID_USUARIO_2)); //Sala de chat (nombre)

        databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()==null){
                    crearSala(ID_USUARIO_1, ID_USUARIO_2);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void crearSala(String ID_USUARIO_1, String ID_USUARIO_2){

        DatabaseReference databaseReference1;
        databaseReference1 = firebaseDatabase.getReference(getChatName(ID_USUARIO_1,ID_USUARIO_2)); //Sala de chat (nombre)

        DatabaseReference databaseReference2;
        databaseReference1.push();

        databaseReference2 = firebaseDatabase.getReference("USUARIOS").child(ID_USUARIO_1); //Sala de chat (nombre)
        databaseReference2.setValue(new Conversacion(databaseReference1.getKey(), ID_USUARIO_1.toString(),  "",  "", R.drawable.user));
        databaseReference2.push();

        databaseReference2 = firebaseDatabase.getReference("USUARIOS").child(ID_USUARIO_2); //Sala de chat (nombre)
        databaseReference2.setValue(new Conversacion(databaseReference1.getKey(), ID_USUARIO_2.toString(),  "", "" , R.drawable.user));
        databaseReference2.push();
    }


        //ordena los numeros de los salas
    private String getChatName(String user1, String user2) {
        String[] ids = new String[]{user1, user2};
        Arrays.sort(ids);
        return ids[0] + "-" + ids[1];
    }

}

