package com.example.app_clientes.Vistas;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.sip.SipSession;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.widget.ImageViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_clientes.Otros.CalendarioFragment;
import com.example.app_clientes.Otros.HoraFragment;
import com.example.app_clientes.R;
import com.example.app_clientes.ui.Home.HomeFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class VentanaPublicarViaje extends AppCompatActivity {

    //Atributos de la clase
    private EditText ETFecha;
    private EditText ETHora;
    private SeekBar seekBarPrecio;
    private TextView TVPrecio;
    private ImageView IVMas;
    private ImageView IVMenos;
    private TextView TVNum_Asientos;
    private int contAsientos=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ventana_publicar_viaje);

        //Cargamos el spinner con los vehiculos que tiene el usuario
        loadSpinner();

        //Editext al pulsar sobre este muestra un calendario
        ETFecha = findViewById(R.id.ETFecha);
        ETFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        //Editext al pulsar sobre este muestra un reloj
        ETHora = findViewById(R.id.ETHora);
        ETHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });

        //Instanciamos para el SeekBar pueda trabajar.
        TVPrecio = findViewById(R.id.TVPrecio);
        seekBarPrecio = findViewById(R.id.seekBarPrecio);
        seekBarPrecio.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float valores = (float) ((float)progress / 10.0);
                TVPrecio.setText(valores+" €");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //Mediante este controzo de codigo se aumento o disminuye el contador de los asientos que el conductor va a publicitar en el anuncion.
        TVNum_Asientos = findViewById(R.id.TVNum_Asientos);
        IVMas = findViewById(R.id.IVMas);
        IVMas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(contAsientos<8){
                    contAsientos++;
                    TVNum_Asientos.setText(contAsientos+"");
                }
                if(contAsientos!=8){
                    ColorStateList csl = AppCompatResources.getColorStateList(VentanaPublicarViaje.this, R.color.colorPrimary);
                    ImageViewCompat.setImageTintList(IVMas, csl);
                }
                else{
                    ColorStateList csl = AppCompatResources.getColorStateList(VentanaPublicarViaje.this, R.color.colorGris);
                    ImageViewCompat.setImageTintList(IVMas, csl);
                }if(contAsientos!=1){
                    ColorStateList csl = AppCompatResources.getColorStateList(VentanaPublicarViaje.this, R.color.colorPrimary);
                    ImageViewCompat.setImageTintList(IVMenos, csl);
                }else{
                    ColorStateList csl = AppCompatResources.getColorStateList(VentanaPublicarViaje.this, R.color.colorGris);
                    ImageViewCompat.setImageTintList(IVMenos, csl);
                }
            }
        });
        IVMenos = findViewById(R.id.IVMenos);
        IVMenos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(contAsientos>1){
                    contAsientos--;
                    TVNum_Asientos.setText(contAsientos+"");
                }
                if(contAsientos!=8){
                    ColorStateList csl = AppCompatResources.getColorStateList(VentanaPublicarViaje.this, R.color.colorPrimary);
                    ImageViewCompat.setImageTintList(IVMas, csl);
                }
                else{
                    ColorStateList csl = AppCompatResources.getColorStateList(VentanaPublicarViaje.this, R.color.colorGris);
                    ImageViewCompat.setImageTintList(IVMas, csl);
                }if(contAsientos!=1){
                    ColorStateList csl = AppCompatResources.getColorStateList(VentanaPublicarViaje.this, R.color.colorPrimary);
                    ImageViewCompat.setImageTintList(IVMenos, csl);
                }else{
                    ColorStateList csl = AppCompatResources.getColorStateList(VentanaPublicarViaje.this, R.color.colorGris);
                    ImageViewCompat.setImageTintList(IVMenos, csl);
                }

            }
        });


    }

    //Muestra un cuadro de dialogo con un calendario al pulsar sobre el EditextFecha
    //Este nos permite copturar la fecha para posteriormente hacer un insert en la base de datos con esta
    private void showDatePickerDialog() {
        CalendarioFragment newFragment = CalendarioFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because January is zero
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
                // +1 because January is zero
                final String selectedDate = hourOfDay + ":" + minute;
                ETHora.setText(selectedDate);
            }

        });
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    //Este método carga los datos en el Spinner cuando se abre por primera vez la ventana.
    //cargando en el spinner tantos coches como el usuario que se ha logueado tenga.
    private void loadSpinner(){
        //================Datos cargados desde Array=====================//
        //Hago referencia al spinner con el id `spinnerVehiculo`
        Spinner SpinnerVehiculos = (Spinner) findViewById(R.id.spinnerVehiculo);
        //Implemento el setOnItemSelectedListener: para realizar acciones cuando se seleccionen los ítems
        SpinnerVehiculos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //AQUI VA LAS ACCIONES CUANDO UN ITEM ES SELECCIONADO
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //Cargar datos de vehiculos del conductor logueado
        String [] vehiculos = new String[] {"Seleccione vehículo", "Matricula 1", "Matricula 2", "Matricula 3", "Matricula 4"};
        //Implemento el adapter con el contexto, layout, arrayVehiculos
        ArrayAdapter<String>  comboAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, vehiculos);
        comboAdapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        //Cargo el spinner con los datos
        SpinnerVehiculos.setAdapter(comboAdapter);
    }
}