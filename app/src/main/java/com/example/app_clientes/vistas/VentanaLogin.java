//Indicamos a que paquete pertenece esta clase:
package com.example.app_clientes.vistas;
//Importamos los siguientes paquetes:
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.app_clientes.Biblioteca;
import com.example.app_clientes.R;
import com.example.app_clientes.jsonplaceholder.JsonPlaceHolderApi;
import com.example.app_clientes.pojos.Usuario;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
//Clase que contiene toda la logica y conexion con la ventana de login de la app:
public class VentanaLogin extends AppCompatActivity implements View.OnClickListener, TextWatcher{
    //Atributos XML:
    private EditText editTextUsuario, editTextClave;
    private Button btIniciarSesion, btRegistrar;
    private TextView txtBtClaveOlvidada, txtInformativoRegistro;
    private TextView txtErrorUsuario, txtErrorClave;
    //Variables para comprobacion de formatos:
    private boolean pruebaFormatoUsuario, pruebaFormatoClave;
    //Metodo onCreate encargado de crear la actividad:
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //Llamamos al superconstructor y conectamos el XML del login:
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ventana_login);
        //Inicializamos variables de formato:
        pruebaFormatoUsuario=false;
        pruebaFormatoClave=false;
        //Asociamos los elementos XML a los atributos:
        editTextUsuario=findViewById(R.id.editTextUsuarioInicioSesion);
        editTextClave=findViewById(R.id.editTextClaveInicioSesion);
        btIniciarSesion=findViewById(R.id.buttonLoginInicioSesion);
        btRegistrar=findViewById(R.id.buttonRegistrarInicioSesion);
        txtBtClaveOlvidada=findViewById(R.id.textViewOlvidasteTuClaveInicioSesion);
        txtInformativoRegistro=findViewById(R.id.textViewNoCuentaInicioSesion);
        txtErrorUsuario=findViewById(R.id.textViewErrorUsuarioInicioSesion);
        txtErrorClave=findViewById(R.id.textViewErrorClaveInicioSesion);
        txtBtClaveOlvidada=findViewById(R.id.textViewOlvidasteTuClaveInicioSesion);
        //Vinculamos los botones al listener del metodo onclick, que esta implementado en esta clase:
        btIniciarSesion.setOnClickListener(this);
        btRegistrar.setOnClickListener(this);
        //Vinculamos los edittext a su listener para el metodo afterTextChanged, que esta implementado en esta clase:
        editTextUsuario.addTextChangedListener(this);
        editTextClave.addTextChangedListener(this);
        //Animaciones tipo scale despues de que todoo se haya realizado:
        txtBtClaveOlvidada.post(new Runnable() {
            @Override
            public void run() {
                //Declaramos un animatorSet para poder luego ejecutar un conjunto de animaciones a la vez:
                AnimatorSet animatorSetEscale = new AnimatorSet();
                //Instanciamos el conjunto de animaciones:
                //Animacion para el boton incio sesion (Este boton nace mas pequeño ya que segun el formato de los edittext se activara o no):
                ObjectAnimator scaleDownX_inicioSesion = ObjectAnimator.ofFloat(btIniciarSesion, "scaleX", 0.0f, 0.85f);
                ObjectAnimator scaleDownY_inicioSesion = ObjectAnimator.ofFloat(btIniciarSesion, "scaleY", 0.0f, 0.85f);
                //Animacion para el boton registrar:
                ObjectAnimator scaleDownX_registro = ObjectAnimator.ofFloat(btRegistrar, "scaleX", 0.0f, 1.0f);
                ObjectAnimator scaleDownY_registro = ObjectAnimator.ofFloat(btRegistrar, "scaleY", 0.0f, 1.0f);
                //Animacion para el edittext Usuario:
                ObjectAnimator scaleDownX_TxtUsuario = ObjectAnimator.ofFloat(editTextUsuario, "scaleX", 0.0f, 1.0f);
                ObjectAnimator scaleDownY_TxtUsuario = ObjectAnimator.ofFloat(editTextUsuario, "scaleY", 0.0f, 1.0f);
                //Animacion para el edittext clave:
                ObjectAnimator scaleDownX_TxtClave = ObjectAnimator.ofFloat(editTextClave, "scaleX", 0.0f, 1.0f);
                ObjectAnimator scaleDownY_TxtClave = ObjectAnimator.ofFloat(editTextClave, "scaleY", 0.0f, 1.0f);
                //Animacion para el textview clave olvidada:
                ObjectAnimator scaleDownX_TxtClaveOlvidada = ObjectAnimator.ofFloat(txtBtClaveOlvidada, "scaleX", 0.0f, 1.0f);
                ObjectAnimator scaleDownY_TxtClaveOlvidada = ObjectAnimator.ofFloat(txtBtClaveOlvidada, "scaleY", 0.0f, 1.0f);
                //Animacion para el boton registrarse:
                ObjectAnimator scaleDownX_TxtRegistro = ObjectAnimator.ofFloat(txtInformativoRegistro, "scaleX", 0.0f, 1.0f);
                ObjectAnimator scaleDownY_TxtRegistro = ObjectAnimator.ofFloat(txtInformativoRegistro, "scaleY", 0.0f, 1.0f);
                //Configuramos el animatorSet, como se tienen que reproducir las animaciones, el tiempo que duran, que clase de interpolador utilizan y la lanzamos:
                animatorSetEscale.play(scaleDownX_inicioSesion).with(scaleDownY_inicioSesion)
                        .with(scaleDownX_registro).with(scaleDownY_registro)
                        .with(scaleDownX_TxtUsuario).with(scaleDownY_TxtUsuario)
                        .with(scaleDownX_TxtClave).with(scaleDownY_TxtClave)
                        .with(scaleDownX_TxtRegistro).with(scaleDownY_TxtRegistro)
                        .with(scaleDownX_TxtClaveOlvidada).with(scaleDownY_TxtClaveOlvidada);
                animatorSetEscale.setDuration(Biblioteca.tAnimacionesScaleInicial);
                animatorSetEscale.setInterpolator(new AccelerateDecelerateInterpolator());
                animatorSetEscale.start();
            }
        });
    }
    //Metodo onClick implementado de la interfaz View.OnClickListener:
    @Override
    public void onClick(View v) {
        //Segun el boton clickado hacemos lo siguiente:
        if(v.equals(btIniciarSesion)){
            //Variables booleanas para controlar el valor que contienen los editexts:
            boolean pbUsuario=true, pbClave=true;
            //Antes de hacer la peticion al servidor realizamos las siguientes comprobaciones:
            //Comprobamos que sea un email valido, y dependiendo del resultado imprimimos mensaje de error o no:
            if (!Biblioteca.compruebaEmailValido(editTextUsuario.getText().toString())) {
                txtErrorUsuario.setText(getText(R.string.txt_emailError_Formato_ventanaLogin));
                txtErrorUsuario.setVisibility(View.VISIBLE);
                editTextUsuario.setTextColor(getResources().getColor(R.color.colorErrorsitoEditText));
                pbUsuario=false;
            }else{
                txtErrorUsuario.setVisibility(View.GONE);
                txtErrorUsuario.setText("");
                editTextUsuario.setTextColor(getResources().getColor(R.color.places_ui_default_primary_dark));
            }
            //Control clave que contenga solo caracteres alfanumericos(sin acentos ni demas variantes), y dependiendo del mensaje imprimimos mensaje de error o no:
            if (Biblioteca.compruebaSiCadenaContieneCaracteresAlfanumericos(editTextClave.getText().toString())){
                txtErrorClave.setVisibility(View.GONE);
                txtErrorClave.setText("");
                editTextClave.setTextColor(getResources().getColor(R.color.places_ui_default_primary_dark));
            }else{
                txtErrorClave.setText(getText(R.string.txt_claveError_Formato_ventanaLogin));
                txtErrorClave.setVisibility(View.VISIBLE);
                editTextClave.setTextColor(getResources().getColor(R.color.colorErrorsitoEditText));
                pbClave=false;
            }
            //Si las dos pruebas son correctas pasamos a hacer la solicitud al servidor:
            if(pbClave&&pbUsuario){
                //Creamos objeto Retrofit, para lanzar peticiones y poder recibir respuestas:
                Retrofit retrofit = new Retrofit.Builder().baseUrl(Biblioteca.ip).addConverterFactory(GsonConverterFactory.create()).build();
                //Vinculamos el cliente con la interfaz:
                JsonPlaceHolderApi peticiones = retrofit.create(JsonPlaceHolderApi.class);
                //Creamos una peticion para buscar un usuario por email y clave:
                Call<Usuario> call = peticiones.getUsuarioLogin(editTextUsuario.getText().toString().toLowerCase(),editTextClave.getText().toString());
                //Ejecutamos la petición en un hilo en segundo plano, retrofit lo hace por nosotros y esperamos a la respuesta:
                call.enqueue(new Callback<Usuario>() {
                    //Gestionamos la respuesta del servidor:
                    @Override
                    public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                        //Respuesta del servidor con un error y paramos el flujo del programa, indicando el codigo de error:
                        if (!response.isSuccessful()) {
                            //Si la clave no es valida:
                            if(response.code()==403){
                                txtErrorClave.setText(getText(R.string.txt_emailError_Servidor_ventanaLogin));
                                txtErrorClave.setVisibility(View.VISIBLE);
                                editTextClave.setTextColor(getResources().getColor(R.color.colorErrorsitoEditText));
                            }else {
                                txtErrorClave.setVisibility(View.GONE);
                                txtErrorClave.setText("");
                                editTextClave.setTextColor(getResources().getColor(R.color.places_ui_default_primary_dark));
                            }
                            //Si el usuario no es encontrado:
                            if(response.code()==404){
                                txtErrorUsuario.setText(getText(R.string.txt_claveError_Servidor_ventanaLogin));
                                txtErrorUsuario.setVisibility(View.VISIBLE);
                                editTextUsuario.setTextColor(getResources().getColor(R.color.colorErrorsitoEditText));
                            }else{
                                txtErrorUsuario.setVisibility(View.GONE);
                                txtErrorUsuario.setText("");
                                editTextUsuario.setTextColor(getResources().getColor(R.color.places_ui_default_primary_dark));
                            }
                            return;
                        }
                        //El servidor responde con datos y lo almacenamos en nuestra variable estatica:
                        Biblioteca.usuarioSesion = response.body();
                        Biblioteca.usuarioSesion.setClave("");              //Vaciamos la clave por seguridad.
                        //Reiniciamos colores si todoo esta bien:
                        if (txtErrorClave.getVisibility()==View.VISIBLE){
                            txtErrorClave.setVisibility(View.GONE);
                            txtErrorClave.setText("");
                            editTextClave.setTextColor(getResources().getColor(R.color.places_ui_default_primary_dark));
                        }
                        if (txtErrorUsuario.getVisibility()==View.VISIBLE){
                            txtErrorUsuario.setVisibility(View.GONE);
                            txtErrorUsuario.setText("");
                            editTextUsuario.setTextColor(getResources().getColor(R.color.places_ui_default_primary_dark));
                        }
                        //Mostramos un Toast de bienvenida al usuario con su nombre:
                        Toast.makeText(getApplication(),getText(R.string.txt_bienvenida_Toast_ventanaLogin) +" "+ Biblioteca.usuarioSesion.getNombre(), Toast.LENGTH_SHORT).show();
                        //Instanciamos nuestro objeto Intent explicito para pasar a la ventana Principal:
                        Intent intentInicioSesion = new Intent(getApplication(), VentanaPrincipal.class);
                        //Iniciamos la nueva actividad y finalizamos esta:
                        startActivity(intentInicioSesion);
                        finish();
                    }
                    //En caso de que no responda el servidor mostramos mensaje de error:
                    @Override
                    public void onFailure(Call<Usuario> call, Throwable t) {
                        Toast.makeText(getApplication(),t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        }else if(v.equals(btRegistrar)){
            //Instanciamos nuestro objeto Intent explicito para pasar a la ventana Registro:
            Intent intentRegistro = new Intent(this, VentanaRegistro.class);
            //Iniciamos la nueva actividad:
            startActivity(intentRegistro);
        }else if(v.equals(txtBtClaveOlvidada)){
            //Instanciamos nuestro objeto Intent explicito para pasar a la ventana ClaveOlvidada.
            Intent intentClaveOlvidada = new Intent(this, VentanaClaveOlvidada.class);
            //Iniciamos la nueva actividad:
            startActivity(intentClaveOlvidada);
        }
    }
    //Metodo afterTextChanged implementado de la interfaz TextWatcher (cuando cambie el texto se ejecutara):
    @Override
    public void afterTextChanged(Editable s) {
        //Boolean para comprobar que estado tiene antes de realizar pruebas:
        boolean anterior=false;
        if(pruebaFormatoUsuario&&pruebaFormatoClave){anterior=true;}
        //Si el texto de usuario ha cambiado:
        if(s==editTextUsuario.getEditableText()){
            String usuarioAux=s.toString();
            //Si no esta vacio y tiene mas de 4 caracteres y menos de 31 caracteres:
            pruebaFormatoUsuario= !usuarioAux.equals("") && usuarioAux.length() >= 5 && usuarioAux.length() <= 30;
        }
        //Si el texto de clave ha cambiado:
        else if (s==editTextClave.getEditableText()){
            String claveAux=s.toString();
            //Si no esta vacio y tiene mas de 5 caracteres y menos de 31 caracteres:
            pruebaFormatoClave= !claveAux.equals("") && claveAux.length() >= 6 && claveAux.length() <= 30;
        }
        //Si las dos pruebas de formato son correctas pasamos a habilitar el boton de iniciar sesion:
        if (pruebaFormatoUsuario&&pruebaFormatoClave&&!anterior){
            //Conjunto de animator:
            AnimatorSet animator = new AnimatorSet();
            //Animacion para el boton incio sesion:
            ObjectAnimator scaleDownX_inicioSesion = ObjectAnimator.ofFloat(btIniciarSesion, "scaleX", 0.85f, 1.0f);
            ObjectAnimator scaleDownY_inicioSesion = ObjectAnimator.ofFloat(btIniciarSesion, "scaleY", 0.85f, 1.0f);
            animator.play(scaleDownX_inicioSesion).with(scaleDownY_inicioSesion);
            animator.setDuration(Biblioteca.tAnimacionesScaleBotones);
            animator.start();
            //Habilitamos el boton inicio de sesion:
            btIniciarSesion.setEnabled(true);
            btIniciarSesion.setTextColor(getResources().getColor(R.color.colorPrimary));
        }else if ((!pruebaFormatoUsuario||!pruebaFormatoClave)&&anterior){
            //Conjunto de animator:
            AnimatorSet animator = new AnimatorSet();
            //Animacion para el boton incio sesion:
            ObjectAnimator scaleDownX_inicioSesion = ObjectAnimator.ofFloat(btIniciarSesion, "scaleX", 1.0f, 0.85f);
            ObjectAnimator scaleDownY_inicioSesion = ObjectAnimator.ofFloat(btIniciarSesion, "scaleY", 1.0f, 0.85f);
            animator.play(scaleDownX_inicioSesion).with(scaleDownY_inicioSesion);
            animator.setDuration(Biblioteca.tAnimacionesScaleBotones);
            animator.start();
            //Deshabilitamos el boton inicio de sesion:
            btIniciarSesion.setEnabled(false);
            btIniciarSesion.setTextColor(getResources().getColor(R.color.colorGris));
        }
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}
}
