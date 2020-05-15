package com.example.app_clientes.vistas;

import android.app.*;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.app_clientes.controlador.Controlador;
import com.example.app_clientes.jsonplaceholder.JsonPlaceHolderApi;
import com.example.app_clientes.otros.CalendarioFragment;
import com.example.app_clientes.otros.HoraFragment;

import com.example.app_clientes.R;


public class VentanaPublicarViaje extends AppCompatActivity {

    //Atributos de la clase
    private ImageView BTPublicar;
    private ImageView IVAtras;
    private EditText ETFecha;
    private EditText ETHora;
    private String ETFechaDate;
    private String ETHoraDate;
    private SeekBar seekBarPrecio;
    private TextView TVPrecio;
    private Spinner spinner_numero_plazas;
    private Spinner SpinnerMatriculaVehiculos;
    private EditText ETOrigenViaje;
    private EditText ETDestinoViaje;

    private  String numPlazas;
    private  String matricula;

    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private Controlador controlador = new Controlador();
    private String ID_USUARIO;

    //Esta clase deberán llegar IDUsuario y todos sus datos, debemos manejar
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ventana_publicar_viaje);

        ID_USUARIO = cargarCredencialesIdUsuario();

        ETOrigenViaje = findViewById(R.id.ETOrigenViaje);
        ETDestinoViaje = findViewById(R.id.ETDestinoViaje);

        //Editext al pulsar sobre este muestra un calendario
        IVAtras = findViewById(R.id.IVFlechaAtrasAgregarVehiculo);
        IVAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

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
                TVPrecio.setText(valores+"");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //SPINNER NUMERO PLAZAS
        SpinnerMatriculaVehiculos = findViewById(R.id.spinnerVehiculo);
        inicializacionSpinnerMatricula();
        SpinnerMatriculaVehiculos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                //Habrá que meterlo en una variable para poder trabajar con ella
                //en este caso el numero de plazas disponibles
                matricula= (String) SpinnerMatriculaVehiculos.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        //SPINNER NUMERO PLAZAS
        spinner_numero_plazas = findViewById(R.id.spinner_numero_plazas);
        inicializacionSpinnerVehiculos();
        spinner_numero_plazas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                //Habrá que meterlo en una variable para poder trabajar con ella
                //en este caso el numero de plazas disponibles
                numPlazas = (String) spinner_numero_plazas.getSelectedItem();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        //Editext al pulsar sobre este muestra un calendario
        BTPublicar = findViewById(R.id.BTPublicar);
        BTPublicar.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                //publicarViaje();
            }
        });

    }

    //Muestra un cuadro de dialogo con un calendario al pulsar sobre el EditextFecha
    //Este nos permite copturar la fecha para posteriormente hacer un insert en la base de datos con esta
    private void showDatePickerDialog() {
        final CalendarioFragment newFragment = CalendarioFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 porque el mes de enero es 0
                final String selectedDate = day + " / " + (month+1) + " / " + year;
                ETFecha.setText(selectedDate);
                ETFechaDate = ETFecha.getText().toString();
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
                ETHoraDate = ETHora.getText().toString();
            }
        });
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    //Este método carga los datos en el Spinner cuando se abre por primera vez la ventana.
    //cargando en el spinner tantos coches como el usuario que se ha logueado tenga.
    private void inicializacionSpinnerMatricula(){
        //Cargar datos de vehiculos del conductor logueado
        String [] vehiculos = new String[] {"Seleccione vehículo", "Matricula 1", "Matricula 2", "Matricula 3", "Matricula 4"};
        //Implemento el adapter con el contexto, layout, arrayVehiculos
        ArrayAdapter<String>  comboAdapter = new ArrayAdapter<>(this,R.layout.color_spinner, vehiculos);
        comboAdapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        //Cargo el spinner con los datos
        SpinnerMatriculaVehiculos.setAdapter(comboAdapter);
    }

    //Inicializa el spinner de vehiculos
    private void inicializacionSpinnerVehiculos() {
        // Initializing a String Array
        String[] numeroPlazas = new String[]{"1", "2", "3", "4", "5", "6", "7", "8"};
        // Initializing an ArrayAdapter.
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,R.layout.color_spinner,numeroPlazas);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.color_spinner);
        spinner_numero_plazas.setAdapter(spinnerArrayAdapter);
    }

    private String cargarCredencialesIdUsuario(){
        SharedPreferences credenciales = getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        return credenciales.getString("idUsuario","0");
    }

    /*@RequiresApi(api = Build.VERSION_CODES.N)
    private  void publicarViaje(){
        //Creamos la conexion
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://192.168.56.1:8080/").addConverterFactory(GsonConverterFactory.create()).build();
        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        BigDecimal bigDecimalCurrency=new BigDecimal(TVPrecio.getText().toString());
        System. out. println("Converted String currency to bigDecimalCurrency: "+bigDecimalCurrency);
        Viaje nuevoViaje = new Viaje(VentanaPrincipal.usuario, controlador.getVehiculo("1"), ETOrigenViaje.getText().toString(), ETDestinoViaje.getText().toString(), null, BigDecimal.valueOf(Double.parseDouble(TVPrecio.getText().toString())), Integer.parseInt(numPlazas), ETFechaDate, ETHoraDate, null, null);
        Call<Viaje> call = jsonPlaceHolderApi.createViaje(nuevoViaje);
        call.enqueue(new Callback<Viaje>() {
            @Override
            public void onResponse(Call<Viaje> call, Response<Viaje> response) {
                if (!response.isSuccessful()) {
                    Log.i("Coder", response.code() + "");
                    Intent VentanaPublicarViaje = new Intent(VentanaPublicarViaje.this, VentanaViajePublicado.class);
                    startActivity(VentanaPublicarViaje);
                }
            }
            @Override
            public void onFailure(Call<Viaje> call, Throwable t) {
                Log.i("Code: " ,  t.getMessage()+"" );
            }
        });
    }*/
}