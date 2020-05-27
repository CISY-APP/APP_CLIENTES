//Indicamos a que paquete pertenece esta clase:
package com.example.app_clientes.vistas;
//Importamos los siguientes paquetes:
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.*;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.app_clientes.Biblioteca;
import com.example.app_clientes.jsonplaceholder.JsonPlaceHolderApi;
import com.example.app_clientes.otros.CalendarioFragment;
import com.example.app_clientes.otros.HoraFragment;
import com.example.app_clientes.R;
import com.example.app_clientes.pojos.Vehiculo;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
//Clase que contiene toda la logica y conexion con la ventana de publicar viaje de la app:
public class VentanaPublicarViaje extends AppCompatActivity implements View.OnClickListener, TextWatcher, AdapterView.OnItemSelectedListener, SeekBar.OnSeekBarChangeListener{
    //Variables para comprobacion de formatos:
    private boolean pruebaFormatoOrigen, pruebaFormatoDestino, pruebaFormatoHora, pruebaFormatoFecha, pruebaFormatoVehiculo, pruebaFormatoNumPlazas, pruebaFormatoPrecio;
    //Atributos XML:
    private Spinner spinnerNumPlazas, spinnerVehiculo;
    private LinearLayout linearLayoutSpinnerVehiculo, linearLayoutSpinnerNumPlazas;
    private EditText editTextOrigen, editTextDestino, editTextHora, editTextFecha;
    private SeekBar seekBarPrecio;
    private ImageView btCrearViaje, btVolver;
    private TextView precioSeekBar;
    private TextView txtErrorOrigen, txtErrorDestino, txtErrorFecha, txtErrorNumPlazas, txtErrorVehiculos, txtErrorPrecio;
    //Atributos de la clase:
    private String numPlazas, matricula;
    private String ID_USUARIO;
    //Metodo que se ejecuta al crearse la vista:
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Conectamos el xml:
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ventana_publicar_viaje);
        //Inicializamos variables booleanas de prueba:
        pruebaFormatoOrigen=false;
        pruebaFormatoDestino=false;
        pruebaFormatoHora=false;
        pruebaFormatoFecha=false;
        pruebaFormatoVehiculo=true;
        pruebaFormatoNumPlazas=true;
        pruebaFormatoPrecio=true;
        //Asociamos el id del usuario en sesion a la siguiente variable:
        ID_USUARIO = Biblioteca.usuarioSesion.getIdusuario().toString();
        //Vinculamos los atributos de la clase:
        spinnerNumPlazas = findViewById(R.id.spinnerNumPlazasPublicarViaje);
        spinnerVehiculo = findViewById(R.id.spinnerVehiculoPublicarViaje);
        linearLayoutSpinnerVehiculo = findViewById(R.id.linearLayoutSpinnerVehiculoPublicarViaje);
        linearLayoutSpinnerNumPlazas = findViewById(R.id.linearLayoutSpinnerNumPlazasPublicarViaje);
        editTextOrigen = findViewById(R.id.editTexOrigenPublicarViaje);
        editTextDestino = findViewById(R.id.editTexDestinoPublicarViaje);
        editTextHora = findViewById(R.id.editTextHoraPublicarViaje);
        editTextFecha = findViewById(R.id.editTextFechaPublicarViaje);
        seekBarPrecio = findViewById(R.id.seekBarPrecioPublicarViaje);
        precioSeekBar = findViewById(R.id.textViewPrecioPublicarViaje);
        btCrearViaje = findViewById(R.id.btAceptarCambiosPublicarViaje);
        btVolver = findViewById(R.id.btFlechaAtrasPublicarViaje);
        txtErrorOrigen = findViewById(R.id.textViewErrorOrigenPublicarViaje);
        txtErrorDestino = findViewById(R.id.textViewErrorDestinoPublicarViaje);
        txtErrorFecha = findViewById(R.id.textViewErrorFechaPublicarViaje);
        txtErrorNumPlazas = findViewById(R.id.textViewErrorNumPlazasPublicarViaje);
        txtErrorVehiculos = findViewById(R.id.textViewErrorVehiculoPublicarViaje);
        txtErrorPrecio = findViewById(R.id.textViewErrorPrecioSeekPublicarViaje);
        //Vinculamos los botones al listener del metodo onclick, que esta implementado en esta clase:
        btVolver.setOnClickListener(this);
        editTextFecha.setOnClickListener(this);
        editTextHora.setOnClickListener(this);
        //Vinculamos los spiners al listener correspondiente:
        inicializacionSpinnerVehiculos();
        inicializacionSpinnerNumPlazas();
        spinnerVehiculo.setOnItemSelectedListener(this);
        spinnerNumPlazas.setOnItemSelectedListener(this);
        //Vinculamos los edittext a su listener para el metodo afterTextChanged, que esta implementado en esta clase:
        editTextOrigen.addTextChangedListener(this);
        editTextDestino.addTextChangedListener(this);
        editTextHora.addTextChangedListener(this);
        editTextFecha.addTextChangedListener(this);
        //Vinculamos el seekbar a su listener:
        seekBarPrecio.setOnSeekBarChangeListener(this);
        //Animaciones tipo scale despues de que todoo se haya realizado:
        txtErrorPrecio.post(new Runnable() {
            @Override
            public void run() {
                //Declaramos un animatorSet para poder luego ejecutar un conjunto de animaciones a la vez:
                AnimatorSet animatorSetEscale = new AnimatorSet();
                //Instanciamos el conjunto de animaciones:
                //Animacion para el spinner de vehiculos:
                ObjectAnimator scaleDownX_spinnerVehiculo = ObjectAnimator.ofFloat(linearLayoutSpinnerVehiculo, "scaleX", 0.0f, 1.0f);
                ObjectAnimator scaleDownY_spinnerVehiculo = ObjectAnimator.ofFloat(linearLayoutSpinnerVehiculo, "scaleY", 0.0f, 1.0f);
                //Animacion para el spinner de numero de plazas:
                ObjectAnimator scaleDownX_spinnerNumPlazas = ObjectAnimator.ofFloat(linearLayoutSpinnerNumPlazas, "scaleX", 0.0f, 1.0f);
                ObjectAnimator scaleDownY_spinnerNumPlazas = ObjectAnimator.ofFloat(linearLayoutSpinnerNumPlazas, "scaleY", 0.0f, 1.0f);
                //Animacion para el edittext Origen:
                ObjectAnimator scaleDownX_TxtOrigen = ObjectAnimator.ofFloat(editTextOrigen, "scaleX", 0.0f, 1.0f);
                ObjectAnimator scaleDownY_TxtOrigen = ObjectAnimator.ofFloat(editTextOrigen, "scaleY", 0.0f, 1.0f);
                //Animacion para el edittext Destino:
                ObjectAnimator scaleDownX_TxtDestino = ObjectAnimator.ofFloat(editTextDestino, "scaleX", 0.0f, 1.0f);
                ObjectAnimator scaleDownY_TxtDestino = ObjectAnimator.ofFloat(editTextDestino, "scaleY", 0.0f, 1.0f);
                //Animacion para el edittext Hora:
                ObjectAnimator scaleDownX_TxtHora = ObjectAnimator.ofFloat(editTextHora, "scaleX", 0.0f, 1.0f);
                ObjectAnimator scaleDownY_TxtHora = ObjectAnimator.ofFloat(editTextHora, "scaleY", 0.0f, 1.0f);
                //Animacion para el edittext Fecha:
                ObjectAnimator scaleDownX_TxtFecha = ObjectAnimator.ofFloat(editTextFecha, "scaleX", 0.0f, 1.0f);
                ObjectAnimator scaleDownY_TxtFecha = ObjectAnimator.ofFloat(editTextFecha, "scaleY", 0.0f, 1.0f);
                //Animacion para el seekbar Precio:
                ObjectAnimator scaleDownX_SeekbarPrecio = ObjectAnimator.ofFloat(seekBarPrecio, "scaleX", 0.0f, 1.0f);
                ObjectAnimator scaleDownY_SeekbarPrecio = ObjectAnimator.ofFloat(seekBarPrecio, "scaleY", 0.0f, 1.0f);
                //Animacion para el boton crear viaje:
                ObjectAnimator scaleDownX_btCrearViaje = ObjectAnimator.ofFloat(btCrearViaje, "scaleX", 0.0f, 0.5f);
                ObjectAnimator scaleDownY_btCrearViaje = ObjectAnimator.ofFloat(btCrearViaje, "scaleY", 0.0f, 0.5f);
                //Configuramos el animatorSet, como se tienen que reproducir las animaciones, el tiempo que duran, que clase de interpolador utilizan y la lanzamos:
                animatorSetEscale.play(scaleDownX_spinnerVehiculo).with(scaleDownY_spinnerVehiculo)
                        .with(scaleDownX_spinnerNumPlazas).with(scaleDownY_spinnerNumPlazas)
                        .with(scaleDownX_TxtOrigen).with(scaleDownY_TxtOrigen)
                        .with(scaleDownX_TxtDestino).with(scaleDownY_TxtDestino)
                        .with(scaleDownX_TxtHora).with(scaleDownY_TxtHora)
                        .with(scaleDownX_TxtFecha).with(scaleDownY_TxtFecha)
                        .with(scaleDownX_SeekbarPrecio).with(scaleDownY_SeekbarPrecio)
                        .with(scaleDownX_btCrearViaje).with(scaleDownY_btCrearViaje);
                animatorSetEscale.setDuration(Biblioteca.tAnimacionesScaleInicial);
                animatorSetEscale.setInterpolator(new AccelerateDecelerateInterpolator());
                animatorSetEscale.start();
            }
        });
    }
    //Este método carga los datos en el Spinner de los vehiculos que tiene el usuario:
    private void inicializacionSpinnerVehiculos(){
        //Creamos objeto Retrofit, para lanzar peticiones y poder recibir respuestas:
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Biblioteca.ip).addConverterFactory(GsonConverterFactory.create()).build();
        //Vinculamos el cliente con la interfaz:
        final JsonPlaceHolderApi peticiones = retrofit.create(JsonPlaceHolderApi.class);
        //Creamos una peticion para obtener la lista de vehiculos asociada al usuario:
        Call <List<Vehiculo>> call = peticiones.getListVehiculoById(Biblioteca.usuarioSesion.getIdusuario());
        //Ejecutamos la petición en un hilo en segundo plano, retrofit lo hace por nosotros y esperamos a la respuesta:
        call.enqueue(new Callback<List<Vehiculo>>() {
            //Gestionamos la respuesta del servidor:
            @Override
            public void onResponse(Call<List<Vehiculo>> call, Response<List<Vehiculo>> response) {
                //Respuesta del servidor con un error y paramos el flujo del programa, indicando el codigo de error:
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Code: " + response.code(), Toast.LENGTH_LONG).show();
                    return;
                }
                //Cargamos los datos de la lista en el spinner:
                List<Vehiculo> listObtenida = response.body();
                //Cargar datos de vehiculos del conductor logueado:
                String [] vehiculos = new String[listObtenida.size()];
                for (int i = 0 ; i < listObtenida.size() ; i++) {
                    vehiculos[i]=listObtenida.get(i).getMatricula()+" | "+listObtenida.get(i).getMarca()+" "+listObtenida.get(i).getModelo();
                }
                //Implemento el adapter con el contexto, layout, arrayVehiculos:
                ArrayAdapter<String>  comboAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.color_spinner, vehiculos);
                comboAdapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
                //Cargo el spinner con los datos:
                spinnerVehiculo.setAdapter(comboAdapter);
            }
            //En caso de que no responda el servidor mostramos mensaje de error:
            @Override
            public void onFailure(Call<List<Vehiculo>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    //Inicializa el spinner de numero de plazas a alquilar en el viaje:
    private void inicializacionSpinnerNumPlazas() {
        //Inicializamos los valores que puede obtener:
        String[] numeroPlazas = new String[]{"1", "2", "3", "4", "5", "6", "7", "8"};
        //Inicializamos el adapter y lo asociamos al spinner:
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,R.layout.color_spinner,numeroPlazas);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.color_spinner);
        spinnerNumPlazas.setAdapter(spinnerArrayAdapter);
    }
    //Muestra un cuadro de dialogo con un calendario al pulsar sobre el EditextFecha:
    private void showDatePickerDialog() {
        CalendarioFragment newFragment = CalendarioFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                final String selectedDate = year + "-" + (month+1) + "-" + day;
                Date hey_dame_la_fecha_como_yo_quiero = java.sql.Date.valueOf(selectedDate);
                editTextFecha.setText(new SimpleDateFormat("dd / MM / yyyy").format(hey_dame_la_fecha_como_yo_quiero)+"");
            }
        });
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
    //Muestra un cuadro de dialogo con un reloj al pulsar sobre el EditextFecha:
    private void showTimePickerDialog() {
        HoraFragment newFragment = HoraFragment.newInstance(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String selectedDate = hourOfDay + ":" + minute;
                editTextHora.setText(selectedDate);
            }
        });
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
    //Metodo de la interfaz View.OnClickListener:
    @Override
    public void onClick(View v) {
        //Segun el elemento pulsado
        if (v.equals(btVolver)){onBackPressed();}
        else if(v.equals(editTextFecha)){showDatePickerDialog();}
        else if(v.equals(editTextHora)){showTimePickerDialog();}
        else if (v.equals(btCrearViaje)){
            //Variables booleanas:
            boolean pbOrigen=true, pbDestino=true, pbVehiculo=true, pbNumPlazas=true, pbHora=true, pbFecha=true, pbPrecio=true;
            //Antes de hacer la peticion al servidor realizamos las siguientes comprobaciones:
            //Control de origen, de momento ninguno mas:

            //Control de destino de momento ninguno mas:

            //Control de vehiculo de momento ninguno mas:

            //Control de numero de plazas de momento ninguno mas:

            //Control de hora y fecha:
            SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String fechaElegida="";
            if(!editTextFecha.getText().toString().equals("")&&!editTextHora.getText().toString().equals("")){
                fechaElegida = Biblioteca.obtieneFechaDataPicker(editTextFecha.getText().toString()) + " "+ editTextHora.getText().toString()+":00";
                try {
                    Date fechaFinal = form.parse(fechaElegida);
                    Date fechaActual = new Date(Calendar.getInstance().getTime().getTime());
                    if (fechaFinal.compareTo(fechaActual) < 0 || fechaFinal.equals(fechaActual)) {
                        pbFecha=false;
                        pbHora=false;
                        txtErrorFecha.setVisibility(View.VISIBLE);
                        txtErrorFecha.setText("Fecha invalida");
                        editTextFecha.setTextColor(getResources().getColor(R.color.colorErrorsitoEditText));
                        editTextHora.setTextColor(getResources().getColor(R.color.colorErrorsitoEditText));
                    }
                } catch (ParseException e) {
                    pbFecha=false;
                    pbHora=false;
                    txtErrorFecha.setVisibility(View.VISIBLE);
                    txtErrorFecha.setText("Fecha invalida");
                    editTextFecha.setTextColor(getResources().getColor(R.color.colorErrorsitoEditText));
                    editTextHora.setTextColor(getResources().getColor(R.color.colorErrorsitoEditText));
                }
            }
            if (pbFecha&&pbHora){
                txtErrorFecha.setVisibility(View.GONE);
                txtErrorFecha.setText("");
                editTextFecha.setTextColor(getResources().getColor(R.color.places_ui_default_primary_dark));
                editTextHora.setTextColor(getResources().getColor(R.color.places_ui_default_primary_dark));
            }
            //Control de precio:
            try {
                if (Double.parseDouble(precioSeekBar.getText().toString().substring(0,precioSeekBar.getText().toString().length()-2))<=0){
                    txtErrorPrecio.setText("Precio no mayor a 0 €");
                    txtErrorPrecio.setVisibility(View.VISIBLE);
                    precioSeekBar.setTextColor(getResources().getColor(R.color.colorErrorsitoEditText));
                    pbPrecio=false;
                }
            }catch (NumberFormatException n){
                txtErrorPrecio.setText("Precio con valor no numerico.");
                txtErrorPrecio.setVisibility(View.VISIBLE);
                precioSeekBar.setTextColor(getResources().getColor(R.color.colorErrorsitoEditText));
                pbPrecio=false;
            }
            if(pbPrecio){
                txtErrorPrecio.setVisibility(View.GONE);
                txtErrorPrecio.setText("");
                precioSeekBar.setTextColor(getResources().getColor(R.color.places_ui_default_primary_dark));
            }/*
            //Si todas las comprobaciones del front son correctas pasamos a lanzar la solicitud al servidor:
            if(pbOrigen&&pbDestino&&pbVehiculo&&pbNumPlazas&&pbHora&&pbFecha&&pbPrecio){
                //Creamos objeto Retrofit, para lanzar peticiones y poder recibir respuestas:
                Retrofit retrofit = new Retrofit.Builder().baseUrl(Biblioteca.ip).addConverterFactory(GsonConverterFactory.create()).build();
                //Vinculamos el cliente con la interfaz:
                JsonPlaceHolderApi peticiones = retrofit.create(JsonPlaceHolderApi.class);
                //Creamos una peticion para actualizar un vehiculo con el valor de los editexts y demas:
                String modeloAux=Biblioteca.quitaEspaciosRepetidosEntrePalabras(editTextModelo.getText().toString());
                String marcaAux=Biblioteca.quitaEspaciosRepetidosEntrePalabras(editTextMarca.getText().toString());
                Map<String, String> infoMap = new HashMap<String, String>();
                infoMap.put("matricula", editTextMatricula.getText().toString());
                if(!modeloAux.equals("")){
                    infoMap.put("modelo", Biblioteca.capitalizaString(modeloAux));
                }else{
                    infoMap.put("modelo", vSesion.getModelo());
                }
                if(!marcaAux.equals("")){
                    infoMap.put("marca", Biblioteca.capitalizaString(marcaAux));
                }else {
                    infoMap.put("marca", vSesion.getMarca());
                }
                infoMap.put("combustible", spinnerTipoCombustible.getSelectedItem().toString());
                infoMap.put("color", colorSeleccionado.toUpperCase());
                Call<Vehiculo> call = peticiones.actualizarVehiculoPorMatricula(infoMap);
                //Ejecutamos la petición en un hilo en segundo plano, retrofit lo hace por nosotros y esperamos a la respuesta:
                call.enqueue(new Callback<Vehiculo>() {
                    //Gestionamos la respuesta del servidor:
                    @Override
                    public void onResponse(Call<Vehiculo> call, Response<Vehiculo> response) {
                        //Respuesta del servidor con un error y paramos el flujo del programa, indicando el codigo de error:
                        if (!response.isSuccessful()) {
                            Toast.makeText(getContext(), "Code: " + response.code(), Toast.LENGTH_LONG).show();
                            return;
                        }
                        //Si la respuesta es satisfactoria, actualizamos el vehiculo sesion, y volvemos a reiniciar interfaz y lista de recycler view:
                        vSesion=response.body();
                        agregarCoches();
                        Toast.makeText(getContext(), getText(R.string.txt_actualizdoVehiculo_Toast_ventanaDatosVehiculo), Toast.LENGTH_LONG).show();
                    }
                    //En caso de que no responda el servidor mostramos mensaje de error:
                    @Override
                    public void onFailure(Call<Vehiculo> call, Throwable t) {
                        Toast.makeText(getContext(),t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }*/
        }
    }
    //Metodo afterTextChanged implementado de la interfaz TextWatcher (cuando cambie el texto se ejecutara):
    @Override
    public void afterTextChanged(Editable s) {
        //Boolean para comprobar que estado tiene antes de realizar pruebas:
        boolean anterior = false;
        if (pruebaFormatoOrigen && pruebaFormatoDestino && pruebaFormatoFecha && pruebaFormatoHora && pruebaFormatoNumPlazas && pruebaFormatoVehiculo && pruebaFormatoPrecio) {
            anterior = true;
        }
        //Si el texto de origen ha cambiado:
        if (s == editTextOrigen.getEditableText()) {
            //Limpiamos espacios multiples y si no es cadena vacia y esta en el rango de longitud de 1 a 30:
            String origen = Biblioteca.quitaEspaciosRepetidosEntrePalabras(s.toString());
            pruebaFormatoOrigen = !origen.equals("") && origen.length() >= 1 && origen.length() <= 30;
        }
        //Si el texto de destino ha cambiado:
        else if (s == editTextDestino.getEditableText()) {
            //Limpiamos espacios multiples y si no es cadena vacia y esta en el rango de longitud de 1 a 30:
            String destino = Biblioteca.quitaEspaciosRepetidosEntrePalabras(s.toString());
            pruebaFormatoDestino = !destino.equals("") && destino.length() >= 1 && destino.length() <= 30;
        }
        //Si el texto de hora ha cambiado:
        else if (s == editTextHora.getEditableText()) {
            //Si la hora no es cadena vacia:
            pruebaFormatoHora = !s.toString().equals("");
        }
        //Si el texto de fecha ha cambiado:
        else if (s == editTextFecha.getEditableText()) {
            //Si la fecha no es cadena vacia:
            pruebaFormatoFecha = !s.toString().equals("");
        }
        //Si las 7 pruebas de formato son correctas pasamos a liberar el boton de publicar viaje:
        if (pruebaFormatoOrigen && pruebaFormatoDestino && pruebaFormatoFecha && pruebaFormatoHora && pruebaFormatoNumPlazas && pruebaFormatoVehiculo && pruebaFormatoPrecio && !anterior) {
            //Conjunto de animator:
            AnimatorSet animator = new AnimatorSet();
            //Animacion para el bt publicar viaje:
            ObjectAnimator scaleDownX_BtPublicarViaje = ObjectAnimator.ofFloat(btCrearViaje, "scaleX", 0.5f, 1.0f);
            ObjectAnimator scaleDownY_BtPublicarViaje = ObjectAnimator.ofFloat(btCrearViaje, "scaleY", 0.5f, 1.0f);
            animator.play(scaleDownX_BtPublicarViaje).with(scaleDownY_BtPublicarViaje);
            animator.setDuration(Biblioteca.tAnimacionesScaleBotones);
            animator.start();
            //Habilitamos el boton publicar viaje:
            btCrearViaje.setEnabled(true);
            btCrearViaje.setOnClickListener(this);
            btCrearViaje.setColorFilter(getResources().getColor(R.color.colorPrimary));
        }
        //Si alguna prueba no es verdadera se desactiva el boton publicar viaje:
        else if ((!pruebaFormatoOrigen || !pruebaFormatoDestino || !pruebaFormatoFecha || !pruebaFormatoHora || !pruebaFormatoNumPlazas || !pruebaFormatoVehiculo || !pruebaFormatoPrecio) && anterior) {
            //Conjunto de animator:
            AnimatorSet animator = new AnimatorSet();
            //Animacion para el bt publicar viaje:
            ObjectAnimator scaleDownX_BtPublicarViaje = ObjectAnimator.ofFloat(btCrearViaje, "scaleX", 1.0f, 0.5f);
            ObjectAnimator scaleDownY_BtPublicarViaje = ObjectAnimator.ofFloat(btCrearViaje, "scaleY", 1.0f, 0.5f);
            animator.play(scaleDownX_BtPublicarViaje).with(scaleDownY_BtPublicarViaje);
            animator.setDuration(Biblioteca.tAnimacionesScaleBotones);
            animator.start();
            //Deshabilitamos el boton publicar viaje:
            btCrearViaje.setEnabled(false);
            btCrearViaje.setOnClickListener(null);
            btCrearViaje.setColorFilter(getResources().getColor(R.color.colorGris));
        }
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}
    //Metodos para la interfaz de los spinners:
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (view.equals(spinnerVehiculo)){
            matricula = spinnerVehiculo.getSelectedItem().toString();
        }else if(view.equals(spinnerNumPlazas)){
            numPlazas = spinnerNumPlazas.getSelectedItem().toString();
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {}
    //Metodos para la interfaz del seekbar:
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        float valores = (float) ((float)progress / 10.0);
        precioSeekBar.setText(valores+" €");
    }
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}