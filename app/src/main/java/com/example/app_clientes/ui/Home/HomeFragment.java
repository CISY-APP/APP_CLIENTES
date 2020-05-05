package com.example.app_clientes.ui.Home;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.example.app_clientes.Otros.CalendarioFragment;
import com.example.app_clientes.Otros.HoraFragment;
import com.example.app_clientes.Pojos.Viaje;
import com.example.app_clientes.R;
import com.example.app_clientes.Vistas.VentanaPublicarViaje;
import com.example.app_clientes.Vistas.VentanaViajePublicado;
import com.example.app_clientes.Vistas.VentanaViajesEncontrados;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class HomeFragment extends Fragment {

    private Button BTPublicar;
    private Button BTBuscar;
    private EditText ETFecha;
    private EditText ETHora;
    private EditText ETOrigen;
    private EditText ETDestino;
    private Button BTBuscarDialog;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_home, container, false);

        //Asociamos los atributos de la clase con los item del fragment.
        BTPublicar = vista.findViewById(R.id.BTPublicar);
        BTBuscar = vista.findViewById(R.id.BTBuscar);

        //Se asigna un setOnClickListener al atributo BTPublicar para que el boton del fragment pueda reaccionar a eventos onClick del usuario
        //Este hace que se cre un nuevo intent en este caso de la VentanaPublicarViaje y lo abre.
        BTPublicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llamadaVentanaPublicarViaje();
            }
        });
        BTBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogBuscarViaje();
            }
        });
        return vista;
    }

    //LLamamos al Dialog de buscar viaje
    private void showDialogBuscarViaje() {
        //Creacion del dialog
        //Instanciamos un objeto de tipo  AlertDialog.Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        //Inflamos la vista con el  layoud correspondiente
        View v = inflater.inflate(R.layout.dialog_buscar_viaje, null);
        builder.setView(v); // Asociamos la vista creada a nuestra ventana de dialogo
        //Creamos un objeto de tipo AlertDialog y le asociamos el AlertDialog.Builder creado anteriormente
        final AlertDialog dialog = builder.create();
        ETFecha = v.findViewById(R.id.ETFechaDialogo);
        ETFecha.setText(getFechaSistema());
        ETHora = v.findViewById(R.id.ETHoraDialogo);
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
                //AQUI DENTRO SE DEBE DE HACER LA LLAMADA CON RETROFIT
                VentanaViajesEncontrados ventanaViajesEncontrados = new VentanaViajesEncontrados();
                Intent VentanaViajesEncontrados = new Intent(getContext(), VentanaViajesEncontrados.class);
                VentanaViajesEncontrados.putExtra("origen",ETOrigen.getText()+"");
                VentanaViajesEncontrados.putExtra("destino",ETDestino.getText()+"");
                VentanaViajesEncontrados.putExtra("fecha",ETFecha.getText()+"");
                VentanaViajesEncontrados.putExtra("hora",ETHora.getText()+"");
                startActivity(VentanaViajesEncontrados);
                //CONSULTA CON RETROFIT A LA BASE DE DATOS
                //Deben comprobarse que todos los campos tienes valores
            }
        });
        //Mostramos la ventana del dialog
        dialog.show();
    }

    //Llamamos al  ventanaPublicarViaje
    private void llamadaVentanaPublicarViaje() {
        VentanaPublicarViaje ventanaPublicarViaje = new VentanaPublicarViaje();
        Intent VentanaPublicarViaje = new Intent(getContext(), ventanaPublicarViaje.getClass());
        //VentanaPublicarViaje.putExtra("usuario",ETUsuario.getText().toString());
        //VentanaPublicarViaje.putExtra("control",ETControl.getText().toString());
        startActivity(VentanaPublicarViaje);
    }

    //Muestra un cuadro de dialogo con un calendario al pulsar sobre el EditextFecha
    //Este nos permite copturar la fecha para posteriormente hacer un insert en la base de datos con esta
    private void showDatePickerDialog() {
        CalendarioFragment newFragment = CalendarioFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 porque el mes de enero es 0
                final String selectedDate = day + " / " + (month + 1) + " / " + year;
                ETFecha.setText(selectedDate);
            }
        });
        newFragment.show(getChildFragmentManager(), "datePicker");
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
        newFragment.show(getChildFragmentManager(), "datePicker");
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
