//Indicamos a que paquete pertenece esta clase:
package com.example.app_clientes.vistas;
//Importamos los siguientes paquetes:
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.example.app_clientes.Biblioteca;
import com.example.app_clientes.jsonplaceholder.JsonPlaceHolderApi;
import com.example.app_clientes.otros.CalendarioFragment;
import com.example.app_clientes.otros.HoraFragment;
import com.example.app_clientes.R;
import com.example.app_clientes.pojos.Vehiculo;
import com.example.app_clientes.pojos.Viaje;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
//Clase que contiene toda la logica y conexion con la ventana de buscar viaje de la app:
public class VentanaBuscarViaje extends AppCompatActivity implements View.OnClickListener, TextWatcher, SeekBar.OnSeekBarChangeListener{
    //Variables para comprobacion de formatos:
    private boolean pruebaFormatoLocalidadOrigen, pruebaFormatoLugarOrigen, pruebaFormatoLocalidadDestino, pruebaFormatoLugarDestino, pruebaFormatoHora, pruebaFormatoFecha, pruebaFormatoPrecio;
    //Atributos XML:
    private EditText editTextLocalidadOrigen, editTextLugarOrigen, editTextLocalidadDestino, editTextLugarDestino, editTextHora, editTextFecha;
    private SeekBar seekBarPrecio;
    private ImageView btBuscarViaje, btVolver;
    private TextView precioSeekBar;
    private TextView txtErrorLocalidadOrigen, txtErrorLugarOrigen, txtErrorLocalidadDestino, txtErrorLugarDestino, txtErrorFecha, txtErrorPrecio;
    //Atributos de la clase:
    private String ID_USUARIO;
    //Metodo que se ejecuta al crearse la vista:
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Conectamos el xml:
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_viaje);
        //Inicializamos variables booleanas de prueba:
        pruebaFormatoLocalidadOrigen=false;
        pruebaFormatoLugarOrigen=false;
        pruebaFormatoLocalidadDestino=false;
        pruebaFormatoLugarDestino=false;
        pruebaFormatoHora=false;
        pruebaFormatoFecha=false;
        pruebaFormatoPrecio=true;
        //Asociamos el id del usuario en sesion a la siguiente variable:
        ID_USUARIO = Biblioteca.usuarioSesion.getIdusuario().toString();
        //Vinculamos los atributos de la clase:
        editTextLocalidadOrigen = findViewById(R.id.editTexLocalidadOrigenBuscarViaje);
        editTextLugarOrigen = findViewById(R.id.editTexLugarOrigenBuscarViaje);
        editTextLocalidadDestino = findViewById(R.id.editTexLocalidadDestinoBuscarViaje);
        editTextLugarDestino = findViewById(R.id.editTexLugarDestinoBuscarViaje);
        editTextHora = findViewById(R.id.editTextHoraBuscarViaje);
        editTextFecha = findViewById(R.id.editTextFechaBuscarViaje);
        seekBarPrecio = findViewById(R.id.seekBarPrecioBuscarViaje);
        precioSeekBar = findViewById(R.id.textViewPrecioBuscarViaje);
        btBuscarViaje = findViewById(R.id.btAceptarBuscarViaje);
        btVolver = findViewById(R.id.btFlechaAtrasBuscarViaje);
        txtErrorLocalidadOrigen = findViewById(R.id.textViewErrorLocalidadOrigenBuscarViaje);
        txtErrorLugarOrigen = findViewById(R.id.textViewErrorLugarOrigenBuscarViaje);
        txtErrorLocalidadDestino = findViewById(R.id.textViewErrorLocalidadDestinoBuscarViaje);
        txtErrorLugarDestino = findViewById(R.id.textViewErrorLugarDestinoBuscarViaje);
        txtErrorFecha = findViewById(R.id.textViewErrorFechaBuscarViaje);
        txtErrorPrecio = findViewById(R.id.textViewErrorPrecioSeekBuscarViaje);
        //Vinculamos los botones al listener del metodo onclick, que esta implementado en esta clase:
        btVolver.setOnClickListener(this);
        editTextFecha.setOnClickListener(this);
        editTextHora.setOnClickListener(this);
        //Vinculamos los edittext a su listener para el metodo afterTextChanged, que esta implementado en esta clase:
        editTextLocalidadOrigen.addTextChangedListener(this);
        editTextLugarOrigen.addTextChangedListener(this);
        editTextLocalidadDestino.addTextChangedListener(this);
        editTextLugarDestino.addTextChangedListener(this);
        editTextHora.addTextChangedListener(this);
        editTextFecha.addTextChangedListener(this);
        //Establecemos distinta imagen de fondo segun el foco de los editText's:
        editTextLocalidadOrigen.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    editTextLocalidadOrigen.setBackground(getDrawable(R.drawable.edittextseleccionadoazul));
                } else {
                    editTextLocalidadOrigen.setBackground(getDrawable(R.drawable.layout_drawable_2));
                }
            }
        });
        //Establecemos distinta imagen de fondo segun el foco de los editText's:
        editTextLugarOrigen.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    editTextLugarOrigen.setBackground(getDrawable(R.drawable.edittextseleccionadoazul));
                } else {
                    editTextLugarOrigen.setBackground(getDrawable(R.drawable.layout_drawable_2));
                }
            }
        });
        //Establecemos distinta imagen de fondo segun el foco de los editText's:
        editTextLocalidadDestino.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    editTextLocalidadDestino.setBackground(getDrawable(R.drawable.edittextseleccionadoazul));
                } else {
                    editTextLocalidadDestino.setBackground(getDrawable(R.drawable.layout_drawable_2));
                }
            }
        });
        //Establecemos distinta imagen de fondo segun el foco de los editText's:
        editTextLugarDestino.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    editTextLugarDestino.setBackground(getDrawable(R.drawable.edittextseleccionadoazul));
                } else {
                    editTextLugarDestino.setBackground(getDrawable(R.drawable.layout_drawable_2));
                }
            }
        });
        //Establecemos distinta imagen de fondo segun el foco de los editText's:
        editTextHora.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    editTextHora.setBackground(getDrawable(R.drawable.edittextseleccionadoazul));
                } else {
                    editTextHora.setBackground(getDrawable(R.drawable.layout_drawable_2));
                }
            }
        });
        //Establecemos distinta imagen de fondo segun el foco de los editText's:
        editTextFecha.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    editTextFecha.setBackground(getDrawable(R.drawable.edittextseleccionadoazul));
                } else {
                    editTextFecha.setBackground(getDrawable(R.drawable.layout_drawable_2));
                }
            }
        });
        //Vinculamos el seekbar a su listener:
        seekBarPrecio.setOnSeekBarChangeListener(this);
        //Animaciones tipo scale despues de que todoo se haya realizado:
        txtErrorPrecio.post(new Runnable() {
            @Override
            public void run() {
                //Declaramos un animatorSet para poder luego ejecutar un conjunto de animaciones a la vez:
                AnimatorSet animatorSetEscale = new AnimatorSet();
                //Instanciamos el conjunto de animaciones:
                //Animacion para el edittext Localidad Origen:
                ObjectAnimator scaleDownX_TxtLocalidadOrigen = ObjectAnimator.ofFloat(editTextLocalidadOrigen, "scaleX", 0.0f, 1.0f);
                ObjectAnimator scaleDownY_TxtLocalidadOrigen = ObjectAnimator.ofFloat(editTextLocalidadOrigen, "scaleY", 0.0f, 1.0f);
                //Animacion para el edittext Lugar Origen:
                ObjectAnimator scaleDownX_TxtLugarOrigen = ObjectAnimator.ofFloat(editTextLugarOrigen, "scaleX", 0.0f, 1.0f);
                ObjectAnimator scaleDownY_TxtLugarOrigen = ObjectAnimator.ofFloat(editTextLugarOrigen, "scaleY", 0.0f, 1.0f);
                //Animacion para el edittext Localidad Destino:
                ObjectAnimator scaleDownX_TxtLocalidadDestino = ObjectAnimator.ofFloat(editTextLocalidadDestino, "scaleX", 0.0f, 1.0f);
                ObjectAnimator scaleDownY_TxtLocalidadDestino = ObjectAnimator.ofFloat(editTextLocalidadDestino, "scaleY", 0.0f, 1.0f);
                //Animacion para el edittext Lugar Destino:
                ObjectAnimator scaleDownX_TxtLugarDestino = ObjectAnimator.ofFloat(editTextLugarDestino, "scaleX", 0.0f, 1.0f);
                ObjectAnimator scaleDownY_TxtLugarDestino = ObjectAnimator.ofFloat(editTextLugarDestino, "scaleY", 0.0f, 1.0f);
                //Animacion para el edittext Hora:
                ObjectAnimator scaleDownX_TxtHora = ObjectAnimator.ofFloat(editTextHora, "scaleX", 0.0f, 1.0f);
                ObjectAnimator scaleDownY_TxtHora = ObjectAnimator.ofFloat(editTextHora, "scaleY", 0.0f, 1.0f);
                //Animacion para el edittext Fecha:
                ObjectAnimator scaleDownX_TxtFecha = ObjectAnimator.ofFloat(editTextFecha, "scaleX", 0.0f, 1.0f);
                ObjectAnimator scaleDownY_TxtFecha = ObjectAnimator.ofFloat(editTextFecha, "scaleY", 0.0f, 1.0f);
                //Animacion para el seekbar Precio:
                ObjectAnimator scaleDownX_SeekbarPrecio = ObjectAnimator.ofFloat(seekBarPrecio, "scaleX", 0.0f, 1.0f);
                ObjectAnimator scaleDownY_SeekbarPrecio = ObjectAnimator.ofFloat(seekBarPrecio, "scaleY", 0.0f, 1.0f);
                //Animacion para el boton buscar viaje:
                ObjectAnimator scaleDownX_btCrearViaje = ObjectAnimator.ofFloat(btBuscarViaje, "scaleX", 0.0f, 0.5f);
                ObjectAnimator scaleDownY_btCrearViaje = ObjectAnimator.ofFloat(btBuscarViaje, "scaleY", 0.0f, 0.5f);
                //Configuramos el animatorSet, como se tienen que reproducir las animaciones, el tiempo que duran, que clase de interpolador utilizan y la lanzamos:
                animatorSetEscale.play(scaleDownX_TxtLocalidadOrigen).with(scaleDownY_TxtLocalidadOrigen)
                        .with(scaleDownX_TxtLugarOrigen).with(scaleDownY_TxtLugarOrigen)
                        .with(scaleDownX_TxtLocalidadDestino).with(scaleDownY_TxtLocalidadDestino)
                        .with(scaleDownX_TxtLugarDestino).with(scaleDownY_TxtLugarDestino)
                        .with(scaleDownX_TxtHora).with(scaleDownY_TxtHora)
                        .with(scaleDownX_TxtFecha).with(scaleDownY_TxtFecha)
                        .with(scaleDownX_SeekbarPrecio).with(scaleDownY_SeekbarPrecio)
                        .with(scaleDownX_btCrearViaje).with(scaleDownY_btCrearViaje);
                animatorSetEscale.setDuration(Biblioteca.tAnimacionesScaleInicial);
                animatorSetEscale.setInterpolator(new AccelerateDecelerateInterpolator());
                animatorSetEscale.start();
            }
        });
        //Recibidor de broadcast para que cuando 'c' se cierre se cierra 'a' tambien, y solo quede home, tambien para cerrar sesion:
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent intent) {
                String action = intent.getAction();
                if (action.equals("matar_buscarViaje_actividad")) {
                    finish();
                }else if (action.equals("cierre_de_sesion")){
                    finish();
                }
            }
        };
        registerReceiver(broadcastReceiver, new IntentFilter("matar_buscarViaje_actividad"));
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
        else if (v.equals(btBuscarViaje)){
            //Variables booleanas:
            boolean pbLocalidadOrigen=true, pbLugarOrigen=true, pbLocalidadDestino=true, pbLugarDestino=true, pbHora=true, pbFecha=true, pbPrecio=true;
            //Antes de hacer la peticion al servidor realizamos las siguientes comprobaciones:
            //Control de localidad origen, de momento ninguno mas:

            //Control de lugar origen, de momento ninguno mas:

            //Control de localidad destino, de momento ninguno mas:

            //Control de lugar destino de momento ninguno mas:

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
                        txtErrorFecha.setText(getText(R.string.txt_errorFecha_Menor_ventanaBuscarViaje));
                        editTextFecha.setTextColor(getResources().getColor(R.color.colorErrorsitoEditText));
                        editTextHora.setTextColor(getResources().getColor(R.color.colorErrorsitoEditText));
                    }
                } catch (ParseException e) {
                    pbFecha=false;
                    pbHora=false;
                    txtErrorFecha.setVisibility(View.VISIBLE);
                    txtErrorFecha.setText(getText(R.string.txt_errorfecha_Formato_ventanaBuscarViaje));
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
                    txtErrorPrecio.setText(getText(R.string.txt_errorPrecio_Menor_ventanaBuscarViaje));
                    txtErrorPrecio.setVisibility(View.VISIBLE);
                    precioSeekBar.setTextColor(getResources().getColor(R.color.colorErrorsitoEditText));
                    pbPrecio=false;
                }
            }catch (NumberFormatException n){
                txtErrorPrecio.setText(getText(R.string.txt_errorPrecio_Formato_ventanaBuscarViaje));
                txtErrorPrecio.setVisibility(View.VISIBLE);
                precioSeekBar.setTextColor(getResources().getColor(R.color.colorErrorsitoEditText));
                pbPrecio=false;
            }
            if(pbPrecio){
                txtErrorPrecio.setVisibility(View.GONE);
                txtErrorPrecio.setText("");
                precioSeekBar.setTextColor(getResources().getColor(R.color.places_ui_default_primary_dark));
            }
            //Si todas las comprobaciones del front son correctas pasamos a lanzar la solicitud al servidor:
            if(pbLocalidadOrigen&&pbLugarOrigen&&pbLocalidadDestino&&pbLugarDestino&&pbHora&&pbFecha&&pbPrecio){
                //Obtenemos las variables antes haciendolas un pequeño tratamiento para pasarselas a la siguiente actividad a traves del intent:
                String localidadOrigen=Biblioteca.capitalizaString(Biblioteca.quitaEspaciosRepetidosEntrePalabras(editTextLocalidadOrigen.getText().toString()));
                String lugarOrigen=Biblioteca.capitalizaString(Biblioteca.quitaEspaciosRepetidosEntrePalabras(editTextLugarOrigen.getText().toString()));
                String localidadDestino=Biblioteca.capitalizaString(Biblioteca.quitaEspaciosRepetidosEntrePalabras(editTextLocalidadDestino.getText().toString()));
                String lugarDestino=Biblioteca.capitalizaString(Biblioteca.quitaEspaciosRepetidosEntrePalabras(editTextLugarDestino.getText().toString()));
                String precio=precioSeekBar.getText().toString().substring(0,precioSeekBar.getText().toString().length()-2);
                //Creamos el intent explicito y le añadimos la informacion:
                Intent ventanaViajesEncontrados = new Intent(getApplicationContext(), VentanaViajesEncontrados.class);
                ventanaViajesEncontrados.putExtra("localidadOrigen",localidadOrigen);
                ventanaViajesEncontrados.putExtra("lugarOrigen",lugarOrigen);
                ventanaViajesEncontrados.putExtra("localidadDestino",localidadDestino);
                ventanaViajesEncontrados.putExtra("lugarDestino",lugarDestino);
                ventanaViajesEncontrados.putExtra("fechaElegida",fechaElegida);
                ventanaViajesEncontrados.putExtra("precio",precio);
                startActivity(ventanaViajesEncontrados);
            }
        }
    }
    //Metodo afterTextChanged implementado de la interfaz TextWatcher (cuando cambie el texto se ejecutara):
    @Override
    public void afterTextChanged(Editable s) {
        //Boolean para comprobar que estado tiene antes de realizar pruebas:
        boolean anterior = false;
        if (pruebaFormatoLocalidadOrigen && pruebaFormatoLugarOrigen && pruebaFormatoLocalidadDestino && pruebaFormatoLugarDestino && pruebaFormatoFecha && pruebaFormatoHora && pruebaFormatoPrecio) {
            anterior = true;
        }
        //Si el texto localidad de origen ha cambiado:
        if (s == editTextLocalidadOrigen.getEditableText()) {
            //Limpiamos espacios multiples y si no es cadena vacia y esta en el rango de longitud de 1 a 30:
            String localidadOrigen = Biblioteca.quitaEspaciosRepetidosEntrePalabras(s.toString());
            pruebaFormatoLocalidadOrigen = !localidadOrigen.equals("") && localidadOrigen.length() >= 1 && localidadOrigen.length() <= 30;
        }
        //Si el texto lugar de origen ha cambiado:
        else if (s == editTextLugarOrigen.getEditableText()) {
            //Limpiamos espacios multiples y si no es cadena vacia y esta en el rango de longitud de 1 a 30:
            String lugarOrigen = Biblioteca.quitaEspaciosRepetidosEntrePalabras(s.toString());
            pruebaFormatoLugarOrigen = !lugarOrigen.equals("") && lugarOrigen.length() >= 1 && lugarOrigen.length() <= 30;
        }
        //Si el texto localidad de destino ha cambiado:
        else if (s == editTextLocalidadDestino.getEditableText()) {
            //Limpiamos espacios multiples y si no es cadena vacia y esta en el rango de longitud de 1 a 30:
            String localidadDestino = Biblioteca.quitaEspaciosRepetidosEntrePalabras(s.toString());
            pruebaFormatoLocalidadDestino = !localidadDestino.equals("") && localidadDestino.length() >= 1 && localidadDestino.length() <= 30;
        }
        //Si el texto de lugar de destino ha cambiado:
        else if (s == editTextLugarDestino.getEditableText()) {
            //Limpiamos espacios multiples y si no es cadena vacia y esta en el rango de longitud de 1 a 30:
            String lugarDestino = Biblioteca.quitaEspaciosRepetidosEntrePalabras(s.toString());
            pruebaFormatoLugarDestino = !lugarDestino.equals("") && lugarDestino.length() >= 1 && lugarDestino.length() <= 30;
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
        if (pruebaFormatoLocalidadOrigen && pruebaFormatoLugarOrigen && pruebaFormatoLocalidadDestino && pruebaFormatoLugarDestino && pruebaFormatoFecha && pruebaFormatoHora && pruebaFormatoPrecio && !anterior) {
            //Conjunto de animator:
            AnimatorSet animator = new AnimatorSet();
            //Animacion para el bt buscar viaje:
            ObjectAnimator scaleDownX_BtPublicarViaje = ObjectAnimator.ofFloat(btBuscarViaje, "scaleX", 0.5f, 1.0f);
            ObjectAnimator scaleDownY_BtPublicarViaje = ObjectAnimator.ofFloat(btBuscarViaje, "scaleY", 0.5f, 1.0f);
            animator.play(scaleDownX_BtPublicarViaje).with(scaleDownY_BtPublicarViaje);
            animator.setDuration(Biblioteca.tAnimacionesScaleBotones);
            animator.start();
            //Habilitamos el boton buscar viaje:
            btBuscarViaje.setEnabled(true);
            btBuscarViaje.setOnClickListener(this);
            btBuscarViaje.setColorFilter(getResources().getColor(R.color.colorPrimary));
        }
        //Si alguna prueba no es verdadera se desactiva el boton publicar viaje:
        else if ((!pruebaFormatoLocalidadOrigen || !pruebaFormatoLugarOrigen || !pruebaFormatoLocalidadDestino || !pruebaFormatoLugarDestino || !pruebaFormatoFecha || !pruebaFormatoHora || !pruebaFormatoPrecio) && anterior) {
            //Conjunto de animator:
            AnimatorSet animator = new AnimatorSet();
            //Animacion para el bt buscar viaje:
            ObjectAnimator scaleDownX_BtPublicarViaje = ObjectAnimator.ofFloat(btBuscarViaje, "scaleX", 1.0f, 0.5f);
            ObjectAnimator scaleDownY_BtPublicarViaje = ObjectAnimator.ofFloat(btBuscarViaje, "scaleY", 1.0f, 0.5f);
            animator.play(scaleDownX_BtPublicarViaje).with(scaleDownY_BtPublicarViaje);
            animator.setDuration(Biblioteca.tAnimacionesScaleBotones);
            animator.start();
            //Deshabilitamos el boton buscar viaje:
            btBuscarViaje.setEnabled(false);
            btBuscarViaje.setOnClickListener(null);
            btBuscarViaje.setColorFilter(getResources().getColor(R.color.colorGris));
        }
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}
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

