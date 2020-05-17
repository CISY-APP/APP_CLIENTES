package com.example.app_clientes.vistas;

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
import com.example.app_clientes.R;
import com.example.app_clientes.jsonplaceholder.JsonPlaceHolderApi;
import com.example.app_clientes.pojos.Usuario;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//Clase que contiene toda la logica y conexion con la ventana de login de la app:
public class VentanaLogin extends AppCompatActivity implements View.OnClickListener, TextWatcher{
    //Atributo estatico que servira para mantener una sesion del cliente logueado
    public static Usuario usuarioSesion;
    //Atributos pertenecientes a la clase
    private EditText editTextUsuario, editTextClave;
    private Button btIniciarSesion, btRegistrar;
    private TextView txtBtClaveOlvidada, txtInformativoRegistro;
    private TextView txtErrorUsuario, txtErrorClave;
    //Variables para comprobacion de formatos:
    private boolean pruebaFormatoUsuario, pruebaFormatoClave;
    //Metodo onCreate encargado de crear la actividad
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ventana_login);
        //Inicializamos variables
        pruebaFormatoUsuario=false;
        pruebaFormatoClave=false;
        //Asociamos los elementos XML a los atributos
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
        //Animaciones
        txtBtClaveOlvidada.post(new Runnable() {
            @Override
            public void run() {
                //Conjuntos de animators
                AnimatorSet animatorSetEscale = new AnimatorSet();
                //Animacion para el boton incio sesion
                ObjectAnimator scaleDownX_inicioSesion = ObjectAnimator.ofFloat(btIniciarSesion, "scaleX", 0.0f, 0.95f);
                ObjectAnimator scaleDownY_inicioSesion = ObjectAnimator.ofFloat(btIniciarSesion, "scaleY", 0.0f, 0.95f);
                //Animacion para el boton registrar
                ObjectAnimator scaleDownX_registro = ObjectAnimator.ofFloat(btRegistrar, "scaleX", 0.0f, 1.0f);
                ObjectAnimator scaleDownY_registro = ObjectAnimator.ofFloat(btRegistrar, "scaleY", 0.0f, 1.0f);
                //Animacion para el edittext Usuario
                ObjectAnimator scaleDownX_TxtUsuario = ObjectAnimator.ofFloat(editTextUsuario, "scaleX", 0.0f, 1.0f);
                ObjectAnimator scaleDownY_TxtUsuario = ObjectAnimator.ofFloat(editTextUsuario, "scaleY", 0.0f, 1.0f);
                //Animacion para el edittext clave
                ObjectAnimator scaleDownX_TxtClave = ObjectAnimator.ofFloat(editTextClave, "scaleX", 0.0f, 1.0f);
                ObjectAnimator scaleDownY_TxtClave = ObjectAnimator.ofFloat(editTextClave, "scaleY", 0.0f, 1.0f);
                //Animacion para el textview clave olvidada
                ObjectAnimator scaleDownX_TxtClaveOlvidada = ObjectAnimator.ofFloat(txtBtClaveOlvidada, "scaleX", 0.0f, 1.0f);
                ObjectAnimator scaleDownY_TxtClaveOlvidada = ObjectAnimator.ofFloat(txtBtClaveOlvidada, "scaleY", 0.0f, 1.0f);
                //Animacion para el boton registrarse
                ObjectAnimator scaleDownX_TxtRegistro = ObjectAnimator.ofFloat(txtInformativoRegistro, "scaleX", 0.0f, 1.0f);
                ObjectAnimator scaleDownY_TxtRegistro = ObjectAnimator.ofFloat(txtInformativoRegistro, "scaleY", 0.0f, 1.0f);
                //Configuramos los animatorsets
                animatorSetEscale.play(scaleDownX_inicioSesion).with(scaleDownY_inicioSesion)
                        .with(scaleDownX_registro).with(scaleDownY_registro)
                        .with(scaleDownX_TxtUsuario).with(scaleDownY_TxtUsuario)
                        .with(scaleDownX_TxtClave).with(scaleDownY_TxtClave)
                        .with(scaleDownX_TxtRegistro).with(scaleDownY_TxtRegistro)
                        .with(scaleDownX_TxtClaveOlvidada).with(scaleDownY_TxtClaveOlvidada);
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
        if(v.equals(btIniciarSesion)){
            //Variables booleanas
            boolean usuario=true, clave=true;
            //Antes de hacer la peticion al servidor realizamos las siguientes comprobaciones:
            //Comprobamos con una expresion regular que sea un email valido:
            Pattern patronEmail = Pattern.compile("([a-z0-9]+(\\.?[a-z0-9])*)+@(([a-z]+)\\.([a-z]+))+");
            Matcher match = patronEmail.matcher(editTextUsuario.getText().toString());
            if (!match.find()) {
                txtErrorUsuario.setText("Email con formato no valido.");
                txtErrorUsuario.setVisibility(View.VISIBLE);
                editTextUsuario.setTextColor(getResources().getColor(R.color.colorErrorsitoEditText));
                usuario=false;
            }else{
                txtErrorUsuario.setVisibility(View.GONE);
                txtErrorUsuario.setText("Error");
                editTextUsuario.setTextColor(getResources().getColor(R.color.places_ui_default_primary_dark));
            }
            //Control clave que contenga solo caracteres alfanumericos
            for (int i = 0 ; i < editTextClave.getText().toString().length() && clave; i++){
                if(editTextClave.getText().toString().toUpperCase().charAt(i) >='A' &&editTextClave.getText().toString().toUpperCase().charAt(i)<='Z'||editTextClave.getText().toString().charAt(i) >='0' &&editTextClave.getText().toString().charAt(i)<='9'){
                }else{
                    clave=false;
                }
            }
            if (clave){
                txtErrorClave.setVisibility(View.GONE);
                txtErrorClave.setText("Error");
                editTextClave.setTextColor(getResources().getColor(R.color.places_ui_default_primary_dark));
            }else{
                txtErrorClave.setText("Clave con caracteres no alfanumericos.");
                txtErrorClave.setVisibility(View.VISIBLE);
                editTextClave.setTextColor(getResources().getColor(R.color.colorErrorsitoEditText));
            }
            if(clave&&usuario){
                //Creamos objeto Retrofit, para lanzar peticiones y poder recibir respuestas
                Retrofit retrofit = new Retrofit.Builder().baseUrl("http://192.168.0.107:8080/").addConverterFactory(GsonConverterFactory.create()).build();
                //Vinculamos el cliente con la interfaz.
                //En esa interfaz se definen los metodos y los verbos que usan
                //Definimos las peticiones que va a poder hacer segun las implementadas en la interfaz que se indica
                JsonPlaceHolderApi peticiones = retrofit.create(JsonPlaceHolderApi.class);
                //Creamos una peticion para buscar un usuario por email y clave
                Call<Usuario> call = peticiones.getUsuarioLogin(editTextUsuario.getText().toString(),editTextClave.getText().toString());
                //Ejecutamos la petición en un hilo en segundo plano, retrofit lo hace por nosotros
                // y esperamos a la respuesta
                call.enqueue(new Callback<Usuario>() {
                    //Gestionamos la respuesta del servidor
                    @Override
                    public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                        //Respuesta del servidor con un error y paramos el flujo del programa, indicando el codigo de error
                        if (!response.isSuccessful()) {
                            //Si la clave no es valida:
                            if(response.code()==403){
                                txtErrorClave.setText("Clave invalida.");
                                txtErrorClave.setVisibility(View.VISIBLE);
                                editTextClave.setTextColor(getResources().getColor(R.color.colorErrorsitoEditText));
                            }else {
                                txtErrorClave.setVisibility(View.GONE);
                                txtErrorClave.setText("Error");
                                editTextClave.setTextColor(getResources().getColor(R.color.places_ui_default_primary_dark));
                            }
                            //Si el usuario no es encontrado:
                            if(response.code()==404){
                                txtErrorUsuario.setText("Usuario no existente.");
                                txtErrorUsuario.setVisibility(View.VISIBLE);
                                editTextUsuario.setTextColor(getResources().getColor(R.color.colorErrorsitoEditText));
                            }else{
                                txtErrorUsuario.setVisibility(View.GONE);
                                txtErrorUsuario.setText("Error");
                                editTextUsuario.setTextColor(getResources().getColor(R.color.places_ui_default_primary_dark));
                            }
                            return;
                        }
                        //El servidor responde con datos y lo almacenamos en nuestra variable estatica
                        Usuario u = response.body();
                        usuarioSesion = u;
                        u.setClave("");
                        //Reiniciamos colores si todoo esta bien:
                        if (txtErrorClave.getVisibility()==View.VISIBLE){
                            txtErrorClave.setVisibility(View.GONE);
                            txtErrorClave.setText("Error");
                            editTextClave.setTextColor(getResources().getColor(R.color.places_ui_default_primary_dark));
                        }
                        if (txtErrorUsuario.getVisibility()==View.VISIBLE){
                            txtErrorUsuario.setVisibility(View.GONE);
                            txtErrorUsuario.setText("Error");
                            editTextUsuario.setTextColor(getResources().getColor(R.color.places_ui_default_primary_dark));
                        }
                        Toast.makeText(getApplication(),"Bienvenido "+ usuarioSesion.getNombre(), Toast.LENGTH_SHORT).show();
                        //Instanciamos nuestro objeto Intent explicito, ya que en los parametros ponemos que empieza en esta
                        //clase que sera el contexto y que iniciara la clase que se encarga de la otra actividad en este caso.
                        Intent intentInicioSesion = new Intent(getApplication(), VentanaPrincipal.class);
                        //Iniciamos la nueva actividad:
                        startActivity(intentInicioSesion);
                        finish();
                    }
                    //En caso de que no responda el servidor mostramos mensaje de error
                    @Override
                    public void onFailure(Call<Usuario> call, Throwable t) {
                        Toast.makeText(getApplication(),t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        }else if(v.equals(btRegistrar)){
            //Instanciamos nuestro objeto Intent explicito, ya que en los parametros ponemos que empieza en esta
            //clase que sera el contexto y que iniciara la clase que se encarga de la otra actividad en este caso.
            Intent intentRegistro = new Intent(this, VentanaRegistro.class);
            //Iniciamos la nueva actividad:
            startActivity(intentRegistro);
        }else if(v.equals(txtBtClaveOlvidada)){
            //Instanciamos nuestro objeto Intent explicito, ya que en los parametros ponemos que empieza en esta
            //clase que sera el contexto y que iniciara la clase que se encarga de la otra actividad en este caso.
            Intent intentClaveOlvidada = new Intent(this, VentanaClaveOlvidada.class);
            //Iniciamos la nueva actividad:
            startActivity(intentClaveOlvidada);
        }
    }

    //Comprobacion de que los campos no esten vacios y tengan un formato correcto antes de poder intentar iniciar sesion:
    @Override
    public void afterTextChanged(Editable s) {
        //Boolean prueba de que el estado anterior era true
        boolean anterior=false;
        if(pruebaFormatoUsuario&&pruebaFormatoClave){anterior=true;}
        //Si el texto de usuario ha cambiado:
        if(s==editTextUsuario.getEditableText()){
            String usuarioAux=s.toString();
            //Si no esta vacio y tiene mas de 4 caracteres y menos de 31 caracteres:
            if(!usuarioAux.equals("")&&usuarioAux.length()>=5&&usuarioAux.length()<=30){
                pruebaFormatoUsuario=true;
            }else{
                pruebaFormatoUsuario=false;
            }
        }
        //Si el texto de clave ha cambiado:
        else if (s==editTextClave.getEditableText()){
            String claveAux=s.toString();
            //Si no esta vacio y tiene mas de 5 caracteres y menos de 31 caracteres:
            if(!claveAux.equals("")&&claveAux.length()>=6&&claveAux.length()<=30){
                pruebaFormatoClave=true;
            }else{
                pruebaFormatoClave=false;
            }
        }
        //Si las dos pruebas de formato son correctas pasamos a liberar el boton de iniciar sesion:
        if (pruebaFormatoUsuario&&pruebaFormatoClave&&!anterior){
            //Conjunto de animator
            AnimatorSet animator = new AnimatorSet();
            //Animacion para el boton incio sesion
            ObjectAnimator scaleDownX_inicioSesion = ObjectAnimator.ofFloat(btIniciarSesion, "scaleX", 0.95f, 1.0f);
            ObjectAnimator scaleDownY_inicioSesion = ObjectAnimator.ofFloat(btIniciarSesion, "scaleY", 0.95f, 1.0f);
            animator.play(scaleDownX_inicioSesion).with(scaleDownY_inicioSesion);
            animator.setDuration(200);
            animator.start();
            //Habilitamos el boton inicio de sesion
            btIniciarSesion.setEnabled(true);
            btIniciarSesion.setTextColor(getResources().getColor(R.color.colorPrimary));
        }else if ((!pruebaFormatoUsuario||!pruebaFormatoClave)&&anterior){
            //Conjunto de animator
            AnimatorSet animator = new AnimatorSet();
            //Animacion para el boton incio sesion
            ObjectAnimator scaleDownX_inicioSesion = ObjectAnimator.ofFloat(btIniciarSesion, "scaleX", 1.0f, 0.95f);
            ObjectAnimator scaleDownY_inicioSesion = ObjectAnimator.ofFloat(btIniciarSesion, "scaleY", 1.0f, 0.95f);
            animator.play(scaleDownX_inicioSesion).with(scaleDownY_inicioSesion);
            animator.setDuration(200);
            animator.start();
            //Deshabilitamos el boton inicio de sesion
            btIniciarSesion.setEnabled(false);
            btIniciarSesion.setTextColor(getResources().getColor(R.color.colorGris));
        }
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}
}
