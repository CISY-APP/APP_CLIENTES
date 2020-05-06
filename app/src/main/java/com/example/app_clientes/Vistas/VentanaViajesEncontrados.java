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
    private miAdapterViajesEncontrados adapter;
    private RecyclerView recyclerView;
    private ArrayList<ItemViajesEncontrados> viajesEncontradosList = new ArrayList<>();
    private Viaje viaje;
    private ImageView IMLupa;
    private TextView ETFecha;
    private TextView ETHora;
    private TextView ETOrigen;
    private TextView ETDestino;
    private Button BTBuscarDialog;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ventana_viajes_encontrados);

        IMLupa = findViewById(R.id.IVLupa);
        IMLupa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogBuscarViaje();
            }
        });

        ETOrigen = findViewById(R.id.TVOrigenViajesEncontrados);
        ETDestino = findViewById(R.id.TVDestinoViajesEncontrados);
        ETFecha = findViewById(R.id.TVFechaViajesEncontrados);
        ETHora = findViewById(R.id.TVHoraViajesEncontrados);

        Bundle datosIN=getIntent().getExtras();
        String origenBundle = datosIN.getString("origen");
        String destinoBundle = datosIN.getString("destino");
        String fechaBundle = datosIN.getString("fecha");
        String horaBundle = datosIN.getString("hora");
        ETOrigen.setText(origenBundle);
        ETDestino.setText(destinoBundle);
        ETFecha.setText(fechaBundle);
        ETHora.setText(horaBundle);
        //Toast.makeText(this, origenBundle+"", Toast.LENGTH_SHORT).show();

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
        adapter = new miAdapterViajesEncontrados(viajesEncontradosList);//Instanciamos un objeto de tipo Example_Adapter
        recyclerView.setAdapter(adapter);//Vinculamos el adapter al recyclerView
        adapter.setOnClickListener(new miAdapterViajesEncontrados.OnItemClickListener() {
            @Override
            public void OnMensajeClick(int position) {

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
    //LLamamos al Dialog de buscar viaje
    private void showDialogBuscarViaje() {
        //Creacion del dialog
        //Instanciamos un objeto de tipo  AlertDialog.Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        //Inflamos la vista con el  layoud correspondiente
        View v = inflater.inflate(R.layout.dialog_buscar_viaje,null);
        builder.setView(v); // Asociamos la vista creada a nuestra ventana de dialogo
        //Creamos un objeto de tipo AlertDialog y le asociamos el AlertDialog.Builder creado anteriormente
        final AlertDialog dialog = builder.create();
        ETFecha =  v.findViewById(R.id.ETFechaDialogo);
        ETFecha.setText(getFechaSistema());
        ETHora =  v.findViewById(R.id.ETHoraDialogo);
        ETHora.setText(getHoraSistema());
        BTBuscarDialog = v.findViewById(R.id.BTBuscarDialog);
        ETOrigen = v.findViewById(R.id.ETOrigenDialogo);
        ETDestino = v.findViewById(R.id.ETDestinoDialogo);
        //Volvemos el fondo del layoud transparente
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //Recogemosel boton del layoud  y se lo asiganamos a un atributo para poder trabajar con el y recoger los eventos sobre este
        //Asociamos el resto de elementos del layoud a atributos para poder trabajar con ellos
        //Evento onClickListener del boton agregar de nuestro layoud
        ETFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        ETHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });
        BTBuscarDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //Mostramos la ventana del dialog
        dialog.show();
    }

    //Muestra un cuadro de dialogo con un calendario al pulsar sobre el EditextFecha
    //Este nos permite copturar la fecha para posteriormente hacer un insert en la base de datos con esta
    private void showDatePickerDialog() {
        CalendarioFragment newFragment = CalendarioFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 porque el mes de enero es 0
                final String selectedDate = day + " / " + (month+1) + " / " + year;
                ETFecha.setText(selectedDate);
            }
        });
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    //Muestra un cuadro de dialogo con un reloj al pulsar sobre el EditextFecha
    //Este nos permite copturar la hora para posteriormente hacer un insert en la base de datos con esta
    private void showTimePickerDialog() {
        HoraFragment newFragment = HoraFragment.newInstance(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String minutos ="0";
                if(minute<10){
                    minutos = minutos + minute;
                }else {
                    minutos = ""+minute;
                }
                final String selectedDate = hourOfDay + ":" + minutos;
                ETHora.setText(selectedDate);
            }

        });
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    //Obtiene la fecha del sistema
    private String getFechaSistema() {
        Calendar c1 = Calendar.getInstance();
        String dia = Integer.toString(c1.get(Calendar.DATE));
        String mes = Integer.toString(c1.get(Calendar.MONTH) + 1);
        String annio = Integer.toString(c1.get(Calendar.YEAR));
        return dia+"/"+mes+"/"+annio;
    }

    //Obtiene la hora del sistema
    private String getHoraSistema() {
        Calendar c1 = Calendar.getInstance();
        String time;
        return time = String.format("%02d:%02d", c1.get(Calendar.HOUR_OF_DAY), c1.get(Calendar.MINUTE));
    }

}
