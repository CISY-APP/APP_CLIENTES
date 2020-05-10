package com.example.app_clientes.Vistas;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_clientes.Adapter.miAdapterViajesEncontrados;
import com.example.app_clientes.Item.ItemViajesEncontrados;
import com.example.app_clientes.Otros.CalendarioFragment;
import com.example.app_clientes.Otros.HoraFragment;
import com.example.app_clientes.Pojos.Viaje;
import com.example.app_clientes.R;

import java.util.ArrayList;
import java.util.Calendar;

public class VentanaViajesEncontrados  extends AppCompatActivity {

    private RecyclerView.LayoutManager layoutManager;
    private miAdapterViajesEncontrados adapterViajesEncontrados;
    private RecyclerView recyclerView;
    private ArrayList<ItemViajesEncontrados> viajesEncontradosList = new ArrayList<>();
    private Viaje viaje;
    private TextView ETFecha;
    private TextView ETHora;
    private TextView ETOrigen;
    private TextView ETDestino;
    private Button BTBuscarDialog;
    private ImageView IVFlechaAtras;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ventana_viajes_encontrados);

        ETOrigen = findViewById(R.id.TVOrigenViajesEncontrados);
        ETDestino = findViewById(R.id.TVDestinoViajesEncontrados);
        ETFecha = findViewById(R.id.TVFechaViajesEncontrados);
        ETHora = findViewById(R.id.TVHoraViajesEncontrados);
        IVFlechaAtras = findViewById(R.id.IVFlechaAtras);
        IVFlechaAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        recibirDatosViaje();

        //Deberemos comprobar que valoracion tiene el usuario para meter una imagen u otra.
        viajesEncontradosList.add(new ItemViajesEncontrados(R.drawable.yo, "Javier","Gomez Fernandez", "28", "3",  R.drawable.unaestrella,  "2.22"));
        viajesEncontradosList.add(new ItemViajesEncontrados(R.drawable.yo, "Javier","Gomez Fernandez", "28", "3",  R.drawable.dosestrellas,  "2.22"));
        viajesEncontradosList.add(new ItemViajesEncontrados(R.drawable.yo, "Javier","Gomez Fernandez", "28", "3",  R.drawable.tresestrellas,  "2.22"));
        viajesEncontradosList.add(new ItemViajesEncontrados(R.drawable.yo, "Javier","Gomez Fernandez", "28", "3",  R.drawable.cuatroestrellas,  "2.22"));
        viajesEncontradosList.add(new ItemViajesEncontrados(R.drawable.yo, "Javier","Gomez Fernandez", "28", "3",  R.drawable.cincoestrellas,  "2.22"));
        viajesEncontradosList.add(new ItemViajesEncontrados(R.drawable.yo, "Javier","Gomez Fernandez", "28", "3",  R.drawable.unaestrella,  "2.22"));

        //Asociamos los atributos con los objeto del layoud para poder usarlos
        //INSTANCIAMOS Y ASOCIAMOS ELEMENTOS NECESARIOS PARA EL CORRECTO FUNCIONAMIENTO DEL RECYCLERVIEW
        recyclerView = findViewById(R.id.RVViajesEncontrados); //Vinculamos el recyclerview del xml con el de la clase main
        recyclerView.setHasFixedSize(true);// RecyclerView sabe de antemano que su tamaño no depende del contenido del adaptador, entonces omitirá la comprobación de si su tamaño debería cambiar cada vez que se agregue o elimine un elemento del adaptador.(mejora el rendimiento)
        layoutManager = new LinearLayoutManager(VentanaViajesEncontrados.this);//Creamos el layoutManager de tipo GridLayaout que vamos a utilizar
        recyclerView.setLayoutManager(layoutManager);//Asociamos al recyclerView el layoutManager que creamos en el paso anterior
        adapterViajesEncontrados = new miAdapterViajesEncontrados(viajesEncontradosList);//Instanciamos un objeto de tipo Example_Adapter
        recyclerView.setAdapter(adapterViajesEncontrados);//Vinculamos el adapter al recyclerView
        adapterViajesEncontrados.setOnClickListener(new miAdapterViajesEncontrados.OnItemClickListener() {
            @Override
            public void OnMensajeClick(int position) {
                //PROGRAMAR CHAT INDIVIDUAL SI NOS DA TIEMPO
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
        ETFecha.setText(fechaBundle);
        ETHora.setText(horaBundle);
    }

}
