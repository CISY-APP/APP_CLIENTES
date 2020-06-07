//Indicamos a que paquete pertenece esta clase:
package com.example.app_clientes.vistas;
//Importamos los siguientes paquetes:
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.example.app_clientes.Biblioteca;
import com.example.app_clientes.R;
import com.example.app_clientes.jsonplaceholder.JsonPlaceHolderApi;
import com.example.app_clientes.pojos.Usuario;
import java.util.HashMap;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
//Clase que contiene toda la logica y conexion con la ventana de cambiar contrasena de la app:
public class VentanaCambiarContrasena extends AppCompatActivity implements View.OnClickListener, TextWatcher {
    //Atributos XML:
    private EditText editTextClaveActual, editTextClaveNueva, editTextClaveNuevaRepetida;
    private ImageView btConfirmar, btVolver;
    private TextView txtErrorClaveActual, txtErrorClaveNueva, txtErrorClaveNuevaRepetida;
    //Variables para comprobacion de formatos:
    private boolean pruebaFormatoClaveActual, pruebaFormatoClaveNueva, pruebaFormatoClaveNuevaRepetida;
    //Metodo onCreate encargado de crear la actividad:
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Llamamos al superconstructor y conectamos el XML del login:
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambiar_contrasena);
        //Inicializamos variables de formato:
        pruebaFormatoClaveActual=false;
        pruebaFormatoClaveNueva=false;
        pruebaFormatoClaveNuevaRepetida=false;
        //Asociamos los elementos XML a los atributos:
        editTextClaveActual = findViewById(R.id.editTextClaveOldCambiarContrasena);
        editTextClaveNueva = findViewById(R.id.editTextClaveNew1CambiarContrasena);
        editTextClaveNuevaRepetida = findViewById(R.id.editTextClaveNew2CambiarContrasena);
        btConfirmar = findViewById(R.id.btAceptarCambiosVentanaCambiarContrasena);btConfirmar.setOnClickListener(null);
        btVolver = findViewById(R.id.btFlechaAtrasCambiarContrasena);
        txtErrorClaveActual=findViewById(R.id.textViewErrorClaveOldCambiarContrasena);
        txtErrorClaveNueva=findViewById(R.id.textViewErrorNew1CambiarContrasena);
        txtErrorClaveNuevaRepetida=findViewById(R.id.textViewErrorNew2CambiarContrasena);
        //Vinculamos los botones al listener del metodo onclick, que esta implementado en esta clase:
        btVolver.setOnClickListener(this);
        //Vinculamos los edittext a su listener para el metodo afterTextChanged, que esta implementado en esta clase:
        editTextClaveActual.addTextChangedListener(this);
        editTextClaveNueva.addTextChangedListener(this);
        editTextClaveNuevaRepetida.addTextChangedListener(this);
        //Establecemos distinta imagen de fondo segun el foco de los editText's:
        editTextClaveActual.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    editTextClaveActual.setBackground(getDrawable(R.drawable.edittextseleccionadoazul));
                } else {
                    editTextClaveActual.setBackground(getDrawable(R.drawable.layout_drawable_2));
                }
            }
        });//Establecemos distinta imagen de fondo segun el foco de los editText's:
        editTextClaveNueva.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    editTextClaveNueva.setBackground(getDrawable(R.drawable.edittextseleccionadoazul));
                } else {
                    editTextClaveNueva.setBackground(getDrawable(R.drawable.layout_drawable_2));
                }
            }
        });
        //Establecemos distinta imagen de fondo segun el foco de los editText's:
        editTextClaveNuevaRepetida.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    editTextClaveNuevaRepetida.setBackground(getDrawable(R.drawable.edittextseleccionadoazul));
                } else {
                    editTextClaveNuevaRepetida.setBackground(getDrawable(R.drawable.layout_drawable_2));
                }
            }
        });
        //Animaciones tipo scale despues de que todoo se haya realizado:
        txtErrorClaveNuevaRepetida.post(new Runnable() {
            @Override
            public void run() {
                //Declaramos un animatorSet para poder luego ejecutar un conjunto de animaciones a la vez:
                AnimatorSet animatorSetEscale = new AnimatorSet();
                //Animacion para el boton confirmar cambios (Este boton nace mas pequeño ya que segun el formato de los edittext se activara o no):
                ObjectAnimator scaleDownX_btConfirmar = ObjectAnimator.ofFloat(btConfirmar, "scaleX", 0.0f, 0.5f);
                ObjectAnimator scaleDownY_btConfirmar = ObjectAnimator.ofFloat(btConfirmar, "scaleY", 0.0f, 0.5f);
                //Animacion para el edittext clave actual:
                ObjectAnimator scaleDownX_TxtClaveActual = ObjectAnimator.ofFloat(editTextClaveActual, "scaleX", 0.0f, 1.0f);
                ObjectAnimator scaleDownY_TxtClaveActual = ObjectAnimator.ofFloat(editTextClaveActual, "scaleY", 0.0f, 1.0f);
                //Animacion para el edittext clave new 1:
                ObjectAnimator scaleDownX_TxtClaveNew1 = ObjectAnimator.ofFloat(editTextClaveNueva, "scaleX", 0.0f, 1.0f);
                ObjectAnimator scaleDownY_TxtClaveNew1 = ObjectAnimator.ofFloat(editTextClaveNueva, "scaleY", 0.0f, 1.0f);
                //Animacion para el edittext clave new 2:
                ObjectAnimator scaleDownX_TxtClaveNew2 = ObjectAnimator.ofFloat(editTextClaveNuevaRepetida, "scaleX", 0.0f, 1.0f);
                ObjectAnimator scaleDownY_TxtClaveNew2 = ObjectAnimator.ofFloat(editTextClaveNuevaRepetida, "scaleY", 0.0f, 1.0f);
                //Configuramos el animatorSet, como se tienen que reproducir las animaciones, el tiempo que duran, que clase de interpolador utilizan y la lanzamos:
                animatorSetEscale.play(scaleDownX_btConfirmar).with(scaleDownY_btConfirmar)
                        .with(scaleDownX_TxtClaveActual).with(scaleDownY_TxtClaveActual)
                        .with(scaleDownX_TxtClaveNew1).with(scaleDownY_TxtClaveNew1)
                        .with(scaleDownX_TxtClaveNew2).with(scaleDownY_TxtClaveNew2);
                animatorSetEscale.setDuration(Biblioteca.tAnimacionesScaleInicial);
                animatorSetEscale.setInterpolator(new AccelerateDecelerateInterpolator());
                animatorSetEscale.start();
            }
        });
    }
    //Metodo onClick implementado de la interfaz View.OnClickListener.
    @Override
    public void onClick(View v) {
        //Segun el boton clickado hacemos lo siguiente:
        if(v.equals(btConfirmar)){
            //Variables booleanas para controlar el valor que contienen los editexts:
            boolean pbClaveAct=true, pbClaveNew=true, pbClaveRep=true;
            //Antes de hacer la peticion al servidor realizamos las siguientes comprobaciones:
            //Control claveAct que contenga solo caracteres alfanumericos:
            if (Biblioteca.compruebaSiCadenaContieneCaracteresAlfanumericos(editTextClaveActual.getText().toString())){
                txtErrorClaveActual.setVisibility(View.GONE);
                txtErrorClaveActual.setText("");
                editTextClaveActual.setTextColor(getResources().getColor(R.color.places_ui_default_primary_dark));
            }else{
                pbClaveAct=false;
                txtErrorClaveActual.setText(getText(R.string.txt_claveActualError_Formato_ventanaCambiarContrasena));
                txtErrorClaveActual.setVisibility(View.VISIBLE);
                editTextClaveActual.setTextColor(getResources().getColor(R.color.colorErrorsitoEditText));
            }
            //Control claveNew que contenga solo caracteres alfanumericos y mininimo una letra minuscula, otra mayuscula y un numero y no sea igual a la actual:
            if(editTextClaveActual.getText().toString().equals(editTextClaveNueva.getText().toString())){
                pbClaveNew=false;
                txtErrorClaveNueva.setText(getText(R.string.txt_claveNewErrorIgualActual_ventanaCambiarContrasena));
                txtErrorClaveNueva.setVisibility(View.VISIBLE);
                editTextClaveNueva.setTextColor(getResources().getColor(R.color.colorErrorsitoEditText));
            }
            if (!Biblioteca.compruebaSiCadenaContieneCaracteresAlfanumericos(editTextClaveNueva.getText().toString())){
                pbClaveNew=false;
                txtErrorClaveNueva.setText(getText(R.string.txt_claveNewError_Formato_ventanaCambiarContrasena));
                txtErrorClaveNueva.setVisibility(View.VISIBLE);
                editTextClaveNueva.setTextColor(getResources().getColor(R.color.colorErrorsitoEditText));
            }
            boolean existMinuscula = false, existMayuscula = false, existNumero = false;
            for (int i = 0 ; i < editTextClaveNueva.getText().toString().length() && pbClaveNew && (!existMayuscula || !existMinuscula || !existNumero); i++){
                if(editTextClaveNueva.getText().toString().charAt(i) >='A' &&editTextClaveNueva.getText().toString().charAt(i)<='Z'){
                    existMayuscula=true;
                }
                else if (editTextClaveNueva.getText().toString().charAt(i) >='a' &&editTextClaveNueva.getText().toString().charAt(i)<='z'){
                    existMinuscula=true;
                }
                else if(editTextClaveNueva.getText().toString().charAt(i) >='0' &&editTextClaveNueva.getText().toString().charAt(i)<='9'){
                    existNumero=true;
                }
            }
            if(!existMayuscula&&pbClaveNew){
                pbClaveNew=false;
                txtErrorClaveNueva.setText(getText(R.string.txt_claveNewErrorMayuscula_Formato_ventanaCambiarContrasena));
                txtErrorClaveNueva.setVisibility(View.VISIBLE);
                editTextClaveNueva.setTextColor(getResources().getColor(R.color.colorErrorsitoEditText));
            }else if(!existMinuscula&&pbClaveNew){
                pbClaveNew=false;
                txtErrorClaveNueva.setText(R.string.txt_claveNewErrorMinuscula_Formato_ventanaCambiarContrasena);
                txtErrorClaveNueva.setVisibility(View.VISIBLE);
                editTextClaveNueva.setTextColor(getResources().getColor(R.color.colorErrorsitoEditText));
            }else if(!existNumero&&pbClaveNew){
                pbClaveNew=false;
                txtErrorClaveNueva.setText(R.string.txt_claveNewErrorNumero_Formato_ventanaCambiarContrasena);
                txtErrorClaveNueva.setVisibility(View.VISIBLE);
                editTextClaveNueva.setTextColor(getResources().getColor(R.color.colorErrorsitoEditText));
            }
            if (pbClaveNew){
                txtErrorClaveNueva.setVisibility(View.GONE);
                txtErrorClaveNueva.setText("");
                editTextClaveNueva.setTextColor(getResources().getColor(R.color.places_ui_default_primary_dark));
            }
            //Control claveRep que sea igual a claveNew:
            if(!editTextClaveNueva.getText().toString().equals(editTextClaveNuevaRepetida.getText().toString())){
                pbClaveRep=false;
                txtErrorClaveNuevaRepetida.setText(getText(R.string.txt_claveRepErrorNoIgual_ventanaCambiarContrasena));
                txtErrorClaveNuevaRepetida.setVisibility(View.VISIBLE);
                editTextClaveNuevaRepetida.setTextColor(getResources().getColor(R.color.colorErrorsitoEditText));
            }
            if (pbClaveRep){
                txtErrorClaveNuevaRepetida.setVisibility(View.GONE);
                txtErrorClaveNuevaRepetida.setText("");
                editTextClaveNuevaRepetida.setTextColor(getResources().getColor(R.color.places_ui_default_primary_dark));
            }
            //Si todas las comprobaciones del front son correctas pasamos a lanzar la solicitud al servidor:
            if(pbClaveAct&&pbClaveNew&&pbClaveRep){
                //Creamos objeto Retrofit, para lanzar peticiones y poder recibir respuestas:
                Retrofit retrofit = new Retrofit.Builder().baseUrl(Biblioteca.ip).addConverterFactory(GsonConverterFactory.create()).build();
                //Vinculamos el cliente con la interfaz:
                JsonPlaceHolderApi peticiones = retrofit.create(JsonPlaceHolderApi.class);
                //Creamos una peticion para cambiar la contrasena de un usuario por su idusuario:
                Map<String, String> infoMap = new HashMap<String, String>();
                infoMap.put("idUsuario", Biblioteca.usuarioSesion.getIdusuario().toString());
                infoMap.put("claveActual", editTextClaveActual.getText().toString());
                infoMap.put("nuevaClave", editTextClaveNueva.getText().toString());
                Call<Usuario> call = peticiones.actualizarClaveUsuario(infoMap);
                //Ejecutamos la petición en un hilo en segundo plano, retrofit lo hace por nosotros y esperamos a la respuesta:
                call.enqueue(new Callback<Usuario>() {
                    //Gestionamos la respuesta del servidor:
                    @Override
                    public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                        //Respuesta del servidor con un error y paramos el flujo del programa, indicando el codigo de error:
                        if (!response.isSuccessful()) {
                            //Si la clave no es valida:
                            if(response.code()==403){
                                txtErrorClaveActual.setText(getText(R.string.txt_claveActual_Servidor_ventanaCambiarContrasena));
                                txtErrorClaveActual.setVisibility(View.VISIBLE);
                                editTextClaveActual.setTextColor(getResources().getColor(R.color.colorErrorsitoEditText));
                            }else {
                                txtErrorClaveActual.setVisibility(View.GONE);
                                txtErrorClaveActual.setText("");
                                editTextClaveActual.setTextColor(getResources().getColor(R.color.places_ui_default_primary_dark));
                            }
                            return;
                        }
                        //Reiniciamos colores si todoo esta bien:
                        if (txtErrorClaveActual.getVisibility()==View.VISIBLE){
                            txtErrorClaveActual.setVisibility(View.GONE);
                            txtErrorClaveActual.setText("");
                            editTextClaveActual.setTextColor(getResources().getColor(R.color.places_ui_default_primary_dark));
                        }
                        //Si el cambio ha sido exitoso volvemos a la actividad anterior:
                        Toast.makeText(getApplication(),getText(R.string.txt_cambio_Toast_ventanaCambiarContrasena), Toast.LENGTH_LONG).show();
                        onBackPressed();
                    }
                    //En caso de que no responda el servidor mostramos mensaje de error:
                    @Override
                    public void onFailure(Call<Usuario> call, Throwable t) {
                        Toast.makeText(getApplication(),"El servidor esta caido.", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }else if(v.equals(btVolver)){
            onBackPressed();
        }
    }
    //Metodo afterTextChanged implementado de la interfaz TextWatcher (cuando cambie el texto se ejecutara):
    @Override
    public void afterTextChanged(Editable s) {
        //Boolean para comprobar que estado tiene antes de realizar pruebas:
        boolean anterior=false;
        if(pruebaFormatoClaveActual&&pruebaFormatoClaveNueva&&pruebaFormatoClaveNuevaRepetida){anterior=true;}
        //Si el texto de clave actual ha cambiado:
        if(s==editTextClaveActual.getEditableText()){
            String claveActualAux=s.toString();
            //Si no esta vacio y tiene mas de 5 caracteres y menos de 31 caracteres:
            pruebaFormatoClaveActual= !claveActualAux.equals("") && claveActualAux.length() >= 6 && claveActualAux.length() <= 30;
        }
        //Si el texto de clave nueva ha cambiado:
        else if (s==editTextClaveNueva.getEditableText()){
            String claveNuevaAux=s.toString();
            //Si no esta vacio y tiene mas de 5 caracteres y menos de 31 caracteres:
            pruebaFormatoClaveNueva= !claveNuevaAux.equals("") && claveNuevaAux.length() >= 6 && claveNuevaAux.length() <= 30;
        }
        //Si el texto de clave nueva repetida ha cambiado:
        else if (s==editTextClaveNuevaRepetida.getEditableText()){
            String claveNuevaRepetidaAux=s.toString();
            //Si no esta vacio y tiene mas de 5 caracteres y menos de 31 caracteres:
            pruebaFormatoClaveNuevaRepetida= !claveNuevaRepetidaAux.equals("") && claveNuevaRepetidaAux.length() >= 6 && claveNuevaRepetidaAux.length() <= 30;
        }
        //Si las tres pruebas de formato son correctas pasamos a liberar el boton de confirmar cambios:
        if (pruebaFormatoClaveActual&&pruebaFormatoClaveNueva&&pruebaFormatoClaveNuevaRepetida&&!anterior){
            //Conjunto de animator:
            AnimatorSet animator = new AnimatorSet();
            //Animacion para el boton confirmar cambios:
            ObjectAnimator scaleDownX_btConfirmar = ObjectAnimator.ofFloat(btConfirmar, "scaleX", 0.5f, 1.0f);
            ObjectAnimator scaleDownY_btConfirmar = ObjectAnimator.ofFloat(btConfirmar, "scaleY", 0.5f, 1.0f);
            animator.play(scaleDownX_btConfirmar).with(scaleDownY_btConfirmar);
            animator.setDuration(Biblioteca.tAnimacionesScaleBotones);
            animator.start();
            //Habilitamos el boton confirmar cambios:
            btConfirmar.setEnabled(true);
            btConfirmar.setOnClickListener(this);
            btConfirmar.setColorFilter(getResources().getColor(R.color.colorPrimary));
        }else if ((!pruebaFormatoClaveActual||!pruebaFormatoClaveNueva||!pruebaFormatoClaveNuevaRepetida)&&anterior){
            //Conjunto de animator:
            AnimatorSet animator = new AnimatorSet();
            //Animacion para el boton confirmar cambios:
            ObjectAnimator scaleDownX_btConfirmar = ObjectAnimator.ofFloat(btConfirmar, "scaleX", 1.0f, 0.5f);
            ObjectAnimator scaleDownY_btConfirmar = ObjectAnimator.ofFloat(btConfirmar, "scaleY", 1.0f, 0.5f);
            animator.play(scaleDownX_btConfirmar).with(scaleDownY_btConfirmar);
            animator.setDuration(Biblioteca.tAnimacionesScaleBotones);
            animator.start();
            //Deshabilitamos el boton confirmar cambios:
            btConfirmar.setEnabled(false);
            btConfirmar.setOnClickListener(null);
            btConfirmar.setColorFilter(getResources().getColor(R.color.colorGris));
        }
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}
}
