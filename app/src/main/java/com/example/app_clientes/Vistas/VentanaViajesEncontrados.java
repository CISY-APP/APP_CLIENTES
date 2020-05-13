package com.example.app_clientes.Vistas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_clientes.Adapter.miAdapterViajesEncontrados;
import com.example.app_clientes.Item.ItemViajesEncontrados;
import com.example.app_clientes.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class VentanaViajesEncontrados  extends AppCompatActivity {

    private RecyclerView.LayoutManager layoutManager;
    private miAdapterViajesEncontrados adapterViajesEncontrados;
    private RecyclerView recyclerView;
    private ArrayList<ItemViajesEncontrados> viajesEncontradosList = new ArrayList<>();

    private TextView ETFechaYHora;
    private TextView ETOrigen;
    private TextView ETDestino;
    private ImageView IVFlechaAtras;

    private String ID_USUARIO;

    private static final String ID_USUARIO_CONVER = "2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ventana_viajes_encontrados);

        ID_USUARIO = cargarCredencialesIdUsuario();

        ETOrigen = findViewById(R.id.TVOrigenViajesEncontrados);
        ETDestino = findViewById(R.id.TVDestinoViajesEncontrados);
        ETFechaYHora = findViewById(R.id.TVHoraViajesEncontrados);
        IVFlechaAtras = findViewById(R.id.IVFlechaAtras);
        IVFlechaAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        recibirDatosViaje();

        viajesEncontradosList.add(new ItemViajesEncontrados("https://firebasestorage.googleapis.com/v0/b/appclientes-a0e43.appspot.com/o/Fotos%2F1?alt=media&token=f59bccd3-4c6e-4872-ab50-14b39743685d","1","Javier","Gomez Fernandez", "28", "3",  R.drawable.unaestrella,  "2.22"));
        viajesEncontradosList.add(new ItemViajesEncontrados("https://firebasestorage.googleapis.com/v0/b/appclientes-a0e43.appspot.com/o/Fotos%2F2?alt=media&token=d29a5deb-1476-4ad9-ab7c-fd853a705ac7","2","Eduardo","Gomez Fernandez", "29", "1",  R.drawable.dosestrellas,  "1.90"));


        //Asociamos los atributos con los objeto del layoud para poder usarlos
        //INSTANCIAMOS Y ASOCIAMOS ELEMENTOS NECESARIOS PARA EL CORRECTO FUNCIONAMIENTO DEL RECYCLERVIEW
        recyclerView = findViewById(R.id.RVViajesEncontrados); //Vinculamos el recyclerview del xml con el de la clase main
        recyclerView.setHasFixedSize(true);// RecyclerView sabe de antemano que su tamaño no depende del contenido del adaptador, entonces omitirá la comprobación de si su tamaño debería cambiar cada vez que se agregue o elimine un elemento del adaptador.(mejora el rendimiento)
        layoutManager = new LinearLayoutManager(VentanaViajesEncontrados.this);//Creamos el layoutManager de tipo GridLayaout que vamos a utilizar
        recyclerView.setLayoutManager(layoutManager);//Asociamos al recyclerView el layoutManager que creamos en el paso anterior
        adapterViajesEncontrados = new miAdapterViajesEncontrados(this, viajesEncontradosList);//Instanciamos un objeto de tipo Example_Adapter
        recyclerView.setAdapter(adapterViajesEncontrados);//Vinculamos el adapter al recyclerView
        adapterViajesEncontrados.setOnClickListener(new miAdapterViajesEncontrados.OnItemClickListener() {
            @Override
            public void OnMensajeClick(int position) {
                Intent VentanaChatIndividual = new Intent(getApplicationContext(), VentanaChatIndividual.class);
                VentanaChatIndividual.putExtra("ID_USUARIO_CONVER",ID_USUARIO_CONVER);
                startActivity(VentanaChatIndividual);
            }

            @Override
            public void OnReservaClick(int position) {
                //AQUI DENTRO SE DEBE DE HACER LA LLAMADA CON RETROFIT
                Intent VentanaPublicarViaje = new Intent(getApplicationContext(), VentanaViajeReservado.class);
                //VentanaPublicarViaje.putExtra("usuario",ETUsuario.getText().toString());
                //VentanaPublicarViaje.putExtra("control",ETControl.getText().toString());
                startActivity(VentanaPublicarViaje);
            }
        });
    }

    private void recibirDatosViaje() {
        Bundle datosIN=getIntent().getExtras();
        String origenBundle = datosIN.getString("origen");
        String destinoBundle = datosIN.getString("destino");
        String fechaBundle = datosIN.getString("fecha");
        String horaBundle = datosIN.getString("hora");
        ETOrigen.setText(origenBundle);
        ETDestino.setText(destinoBundle);
        ETFechaYHora.setText(fechaBundle+" a las " +horaBundle);

    }


    //Obtiene la hora del sistema
    private String getHoraSistema() {
        Calendar c1 = Calendar.getInstance();
        String time;
        return time = String.format("%02d:%02d", c1.get(Calendar.HOUR_OF_DAY), c1.get(Calendar.MINUTE));
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
