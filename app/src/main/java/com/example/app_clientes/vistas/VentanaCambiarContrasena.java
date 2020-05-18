package com.example.app_clientes.vistas;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app_clientes.R;

//Clase que contiene toda la logica y conexion con la ventana de cambiar contrasena de la app:
public class VentanaCambiarContrasena extends AppCompatActivity implements View.OnClickListener, TextWatcher {
    //Atributos pertenecientes a la clase:
    private EditText editTextClaveActual, editTextClaveNueva, editTextClaveNuevaRepetida;
    private ImageView btConfirmar, btVolver;
    private TextView txtErrorClaveActual, txtErrorClaveNueva, txtErrorClaveNuevaRepetida;
    //Variables para comprobacion de formatos:
    private boolean pruebaFormatoClaveActual, pruebaFormatoClaveNueva, pruebaFormatoClaveNuevaRepetida;
    //Metodo onCreate encargado de crear la actividad
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambiar_contrasena);
        //Inicializamos variables
        pruebaFormatoClaveActual=false;
        pruebaFormatoClaveNueva=false;
        pruebaFormatoClaveNuevaRepetida=false;
        //Asociamos los elementos XML a los atributos
        editTextClaveActual = findViewById(R.id.editTextClaveOldCambiarContrasena);
        editTextClaveNueva = findViewById(R.id.editTextClaveNew1CambiarContrasena);
        editTextClaveNuevaRepetida = findViewById(R.id.editTextClaveNew2CambiarContrasena);
        btConfirmar = findViewById(R.id.btAceptarCambiosVentanaCambiarContrasena);
        btVolver = findViewById(R.id.IVFlechaAtras);
        txtErrorClaveActual=findViewById(R.id.textViewErrorClaveOldCambiarContrasena);
        txtErrorClaveNueva=findViewById(R.id.textViewErrorNew1CambiarContrasena);
        txtErrorClaveNuevaRepetida=findViewById(R.id.textViewErrorNew2CambiarContrasena);
        //Vinculamos los botones al listener del metodo onclick, que esta implementado en esta clase:
        btConfirmar.setOnClickListener(this);
        btVolver.setOnClickListener(this);
        //Vinculamos los edittext a su listener para el metodo afterTextChanged, que esta implementado en esta clase:
        editTextClaveActual.addTextChangedListener(this);
        editTextClaveNueva.addTextChangedListener(this);
        editTextClaveNuevaRepetida.addTextChangedListener(this);
        //Animaciones
        txtErrorClaveNuevaRepetida.post(new Runnable() {
            @Override
            public void run() {
                //Conjuntos de animators
                AnimatorSet animatorSetEscale = new AnimatorSet();
                //Animacion para el boton confirmar cambios
                ObjectAnimator scaleDownX_btConfirmar = ObjectAnimator.ofFloat(btConfirmar, "scaleX", 0.0f, 0.5f);
                ObjectAnimator scaleDownY_btConfirmar = ObjectAnimator.ofFloat(btConfirmar, "scaleY", 0.0f, 0.5f);
                //Animacion para el edittext clave actual
                ObjectAnimator scaleDownX_TxtClaveActual = ObjectAnimator.ofFloat(editTextClaveActual, "scaleX", 0.0f, 1.0f);
                ObjectAnimator scaleDownY_TxtClaveActual = ObjectAnimator.ofFloat(editTextClaveActual, "scaleY", 0.0f, 1.0f);
                //Animacion para el edittext clave new 1
                ObjectAnimator scaleDownX_TxtClaveNew1 = ObjectAnimator.ofFloat(editTextClaveNueva, "scaleX", 0.0f, 1.0f);
                ObjectAnimator scaleDownY_TxtClaveNew1 = ObjectAnimator.ofFloat(editTextClaveNueva, "scaleY", 0.0f, 1.0f);
                //Animacion para el edittext clave new 2
                ObjectAnimator scaleDownX_TxtClaveNew2 = ObjectAnimator.ofFloat(editTextClaveNuevaRepetida, "scaleX", 0.0f, 1.0f);
                ObjectAnimator scaleDownY_TxtClaveNew2 = ObjectAnimator.ofFloat(editTextClaveNuevaRepetida, "scaleY", 0.0f, 1.0f);
                //Configuramos los animatorsets
                animatorSetEscale.play(scaleDownX_btConfirmar).with(scaleDownY_btConfirmar)
                        .with(scaleDownX_TxtClaveActual).with(scaleDownY_TxtClaveActual)
                        .with(scaleDownX_TxtClaveNew1).with(scaleDownY_TxtClaveNew1)
                        .with(scaleDownX_TxtClaveNew2).with(scaleDownY_TxtClaveNew2);
                animatorSetEscale.setDuration(550);
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
            //Variables booleanas
            boolean claveAct=true, claveNew=true, claveRep=true;
            //Control claveAct que contenga solo caracteres alfanumericos
            for (int i = 0 ; i < editTextClaveActual.getText().toString().length() && claveAct; i++){
                if(editTextClaveActual.getText().toString().toUpperCase().charAt(i) >='A' &&editTextClaveActual.getText().toString().toUpperCase().charAt(i)<='Z'||editTextClaveActual.getText().toString().charAt(i) >='0' &&editTextClaveActual.getText().toString().charAt(i)<='9'){
                }else{
                    claveAct=false;
                    txtErrorClaveActual.setText("Contraseña con caracteres no alfanumericos.");
                    txtErrorClaveActual.setVisibility(View.VISIBLE);
                    editTextClaveActual.setTextColor(getResources().getColor(R.color.colorErrorsitoEditText));
                }
            }
            if (claveAct){
                txtErrorClaveActual.setVisibility(View.GONE);
                txtErrorClaveActual.setText("Error");
                editTextClaveActual.setTextColor(getResources().getColor(R.color.places_ui_default_primary_dark));
            }
            //Control claveNew que contenga solo caracteres alfanumericos y mininimo una letra minuscula, otra mayuscula y un numero y no sea igual a la acutal
            if(editTextClaveActual.getText().toString().equals(editTextClaveNueva.getText().toString())){
                claveNew=false;
                txtErrorClaveNueva.setText("La contraseña nueva es igual a la actual.");
                txtErrorClaveNueva.setVisibility(View.VISIBLE);
                editTextClaveNueva.setTextColor(getResources().getColor(R.color.colorErrorsitoEditText));
            }
            for (int i = 0 ; i < editTextClaveNueva.getText().toString().length() && claveNew; i++){
                if(editTextClaveNueva.getText().toString().toUpperCase().charAt(i) >='A' &&editTextClaveNueva.getText().toString().toUpperCase().charAt(i)<='Z'||editTextClaveNueva.getText().toString().charAt(i) >='0' &&editTextClaveNueva.getText().toString().charAt(i)<='9'){
                }else{
                    claveNew=false;
                    txtErrorClaveNueva.setText("Contraseña con caracteres no alfanumericos.");
                    txtErrorClaveNueva.setVisibility(View.VISIBLE);
                    editTextClaveNueva.setTextColor(getResources().getColor(R.color.colorErrorsitoEditText));
                }
            }
            boolean existMinuscula = false, existMayuscula = false, existNumero = false;
            for (int i = 0 ; i < editTextClaveNueva.getText().toString().length() && (!existMayuscula || !existMinuscula || !existNumero); i++){
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
            if(!existMayuscula&&claveNew){
                claveNew=false;
                txtErrorClaveNueva.setText("Introduzca una letra mayuscula como minimo.");
                txtErrorClaveNueva.setVisibility(View.VISIBLE);
                editTextClaveNueva.setTextColor(getResources().getColor(R.color.colorErrorsitoEditText));
            }else if(!existMinuscula&&claveNew){
                claveNew=false;
                txtErrorClaveNueva.setText("Introduzca una letra minuscula como minimo.");
                txtErrorClaveNueva.setVisibility(View.VISIBLE);
                editTextClaveNueva.setTextColor(getResources().getColor(R.color.colorErrorsitoEditText));
            }else if(!existNumero&&claveNew){
                claveNew=false;
                txtErrorClaveNueva.setText("Introduzca un numero como minimo.");
                txtErrorClaveNueva.setVisibility(View.VISIBLE);
                editTextClaveNueva.setTextColor(getResources().getColor(R.color.colorErrorsitoEditText));
            }
            if (claveNew){
                txtErrorClaveNueva.setVisibility(View.GONE);
                txtErrorClaveNueva.setText("Error");
                editTextClaveNueva.setTextColor(getResources().getColor(R.color.places_ui_default_primary_dark));
            }
            //Control claveRep que sea igual a claveNew
            if(!editTextClaveNueva.getText().toString().equals(editTextClaveNuevaRepetida.getText().toString())){
                claveRep=false;
                txtErrorClaveNuevaRepetida.setText("Las contraseñas no son iguales.");
                txtErrorClaveNuevaRepetida.setVisibility(View.VISIBLE);
                editTextClaveNuevaRepetida.setTextColor(getResources().getColor(R.color.colorErrorsitoEditText));
            }
            if (claveRep){
                txtErrorClaveNuevaRepetida.setVisibility(View.GONE);
                txtErrorClaveNuevaRepetida.setText("Error");
                editTextClaveNuevaRepetida.setTextColor(getResources().getColor(R.color.places_ui_default_primary_dark));
            }/*
            //Si todas las comprobaciones del front son correctas pasamos a lanzar la solicitud al servidor:
            if(claveAct&&claveNew&&claveRep){
                //Creamos objeto Retrofit, para lanzar peticiones y poder recibir respuestas
                Retrofit retrofit = new Retrofit.Builder().baseUrl("http://192.168.0.107:8080/").addConverterFactory(GsonConverterFactory.create()).build();
                //Vinculamos el cliente con la interfaz.
                //En esa interfaz se definen los metodos y los verbos que usan
                //Definimos las peticiones que va a poder hacer segun las implementadas en la interfaz que se indica
                JsonPlaceHolderApi peticiones = retrofit.create(JsonPlaceHolderApi.class);
                //Creamos una peticion para registrar un usuario, que creamos con los valores de los editext:
                Usuario uObject = new Usuario(editTextNombre.getText().toString(),editTextApellidos.getText().toString(),editTextUsuario.getText().toString(),editTextClave.getText().toString());
                Call<Usuario> call = peticiones.registrarUsuario(uObject);
                //Ejecutamos la petición en un hilo en segundo plano, retrofit lo hace por nosotros
                // y esperamos a la respuesta
                call.enqueue(new Callback<Usuario>() {
                    //Gestionamos la respuesta del servidor
                    @Override
                    public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                        //Respuesta del servidor con un error y paramos el flujo del programa, indicando el codigo de error
                        if (!response.isSuccessful()) {
                            //500 si ya existe
                            //400 si da error
                            Toast.makeText(getApplication(),"Code: "+ response.code()
                                    + "\nEs exitoso: "+response.isSuccessful()
                                    + "\nRAW: "+response.raw().headers().names().toString(), Toast.LENGTH_LONG).show();
                            return;
                        }
                        //Instanciamos nuestro objeto Intent explicito, ya que en los parametros ponemos que empieza en esta
                        //clase que sera el contexto y que iniciara la clase que se encarga de la otra actividad en este caso.
                        Intent intentRegistro = new Intent(getApplication(), VentanaLogin.class);
                        //Iniciamos la nueva actividad:
                        startActivity(intentRegistro);
                        finish();
                    }
                    //En caso de que no responda el servidor mostramos mensaje de error
                    @Override
                    public void onFailure(Call<Usuario> call, Throwable t) {
                        Toast.makeText(getApplication(),t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }*/
        }else if(v.equals(btVolver)){
            onBackPressed();
        }
    }
    //Comprobacion de que los campos no esten vacios y tengan un formato correcto antes de poder intentar cambiar la contraseña:
    @Override
    public void afterTextChanged(Editable s) {
        //Boolean prueba de que el estado anterior era true
        boolean anterior=false;
        if(pruebaFormatoClaveActual&&pruebaFormatoClaveNueva&&pruebaFormatoClaveNuevaRepetida){anterior=true;}
        //Si el texto de clave actual ha cambiado:
        if(s==editTextClaveActual.getEditableText()){
            String claveActualAux=s.toString();
            //Si no esta vacio y tiene mas de 5 caracteres y menos de 31 caracteres:
            if(!claveActualAux.equals("")&&claveActualAux.length()>=6&&claveActualAux.length()<=30){
                pruebaFormatoClaveActual=true;
            }else{
                pruebaFormatoClaveActual=false;
            }
        }
        //Si el texto de clave nueva ha cambiado:
        else if (s==editTextClaveNueva.getEditableText()){
            String claveNuevaAux=s.toString();
            //Si no esta vacio y tiene mas de 5 caracteres y menos de 31 caracteres:
            if(!claveNuevaAux.equals("")&&claveNuevaAux.length()>=6&&claveNuevaAux.length()<=30){
                pruebaFormatoClaveNueva=true;
            }else{
                pruebaFormatoClaveNueva=false;
            }
        }
        //Si el texto de clave nueva repetida ha cambiado:
        else if (s==editTextClaveNuevaRepetida.getEditableText()){
            String claveNuevaRepetidaAux=s.toString();
            //Si no esta vacio y tiene mas de 5 caracteres y menos de 31 caracteres:
            if(!claveNuevaRepetidaAux.equals("")&&claveNuevaRepetidaAux.length()>=6&&claveNuevaRepetidaAux.length()<=30){
                pruebaFormatoClaveNuevaRepetida=true;
            }else{
                pruebaFormatoClaveNuevaRepetida=false;
            }
        }
        //Si las tres pruebas de formato son correctas pasamos a liberar el boton de confirmar cambios:
        if (pruebaFormatoClaveActual&&pruebaFormatoClaveNueva&&pruebaFormatoClaveNuevaRepetida&&!anterior){
            //Conjunto de animator
            AnimatorSet animator = new AnimatorSet();
            //Animacion para el boton confirmar cambios
            ObjectAnimator scaleDownX_btConfirmar = ObjectAnimator.ofFloat(btConfirmar, "scaleX", 0.5f, 1.0f);
            ObjectAnimator scaleDownY_btConfirmar = ObjectAnimator.ofFloat(btConfirmar, "scaleY", 0.5f, 1.0f);
            animator.play(scaleDownX_btConfirmar).with(scaleDownY_btConfirmar);
            animator.setDuration(200);
            animator.start();
            //Habilitamos el boton confirmar cambios
            btConfirmar.setEnabled(true);
            btConfirmar.setColorFilter(getResources().getColor(R.color.colorPrimary));
        }else if ((!pruebaFormatoClaveActual||!pruebaFormatoClaveNueva||!pruebaFormatoClaveNuevaRepetida)&&anterior){
            //Conjunto de animator
            AnimatorSet animator = new AnimatorSet();
            //Animacion para el boton confirmar cambios
            ObjectAnimator scaleDownX_btConfirmar = ObjectAnimator.ofFloat(btConfirmar, "scaleX", 1.0f, 0.5f);
            ObjectAnimator scaleDownY_btConfirmar = ObjectAnimator.ofFloat(btConfirmar, "scaleY", 1.0f, 0.5f);
            animator.play(scaleDownX_btConfirmar).with(scaleDownY_btConfirmar);
            animator.setDuration(200);
            animator.start();
            //Deshabilitamos el boton confirmar cambios
            btConfirmar.setEnabled(false);
            btConfirmar.setColorFilter(getResources().getColor(R.color.colorGris));
        }
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}
}
