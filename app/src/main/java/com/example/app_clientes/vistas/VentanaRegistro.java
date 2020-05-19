package com.example.app_clientes.vistas;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

//Clase que contiene toda la logica y conexion con la ventana de registro usuario de la app:
public class VentanaRegistro extends AppCompatActivity implements View.OnClickListener, TextWatcher {
    //Atributos pertenecientes a la clase:
    private EditText editTextUsuario, editTextClave, editTextNombre, editTextApellidos;
    private Button btRegistrar;
    private TextView txtErrorUsuario, txtErrorClave, txtErrorNombre, txtErrorApellidos;
    //Variables para comprobacion de formatos:
    private boolean pruebaFormatoUsuario, pruebaFormatoClave, pruebaFormatoNombre, pruebaFormatoApellidos;
    //Metodo onCreate encargado de crear la actividad
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ventana_registro);
        //Inicializamos variables
        pruebaFormatoUsuario=false;
        pruebaFormatoClave=false;
        pruebaFormatoNombre=false;
        pruebaFormatoApellidos=false;
        //Asociamos los elementos XML a los atributos
        editTextUsuario=findViewById(R.id.editTextUsuarioRegistroUsuario);
        editTextClave=findViewById(R.id.editTexClaveRegistroUsuario);
        editTextNombre=findViewById(R.id.editTextNombreRegistroUsuario);
        editTextApellidos=findViewById(R.id.editTextApellidosRegistroUsuario);
        btRegistrar=findViewById(R.id.buttonRegistrarRegistroUsuario);
        txtErrorUsuario=findViewById(R.id.textViewErrorUsuarioRegistroUsuario);
        txtErrorClave=findViewById(R.id.textViewErrorClaveRegistroUsuario);
        txtErrorNombre=findViewById(R.id.textViewErrorNombreRegistroUsuario);
        txtErrorApellidos=findViewById(R.id.textViewErrorApellidosRegistroUsuario);
        //Vinculamos los botones al listener del metodo onclick, que esta implementado en esta clase:
        btRegistrar.setOnClickListener(this);
        //Vinculamos los edittext a su listener para el metodo afterTextChanged, que esta implementado en esta clase:
        editTextUsuario.addTextChangedListener(this);
        editTextClave.addTextChangedListener(this);
        editTextNombre.addTextChangedListener(this);
        editTextApellidos.addTextChangedListener(this);
        //Animaciones
        txtErrorApellidos.post(new Runnable() {
            @Override
            public void run() {
                //Conjuntos de animators
                AnimatorSet animatorSetEscale = new AnimatorSet();
                //Animacion para el boton registrar
                ObjectAnimator scaleDownX_registro = ObjectAnimator.ofFloat(btRegistrar, "scaleX", 0.0f, 0.95f);
                ObjectAnimator scaleDownY_registro = ObjectAnimator.ofFloat(btRegistrar, "scaleY", 0.0f, 0.95f);
                //Animacion para el edittext Usuario
                ObjectAnimator scaleDownX_TxtUsuario = ObjectAnimator.ofFloat(editTextUsuario, "scaleX", 0.0f, 1.0f);
                ObjectAnimator scaleDownY_TxtUsuario = ObjectAnimator.ofFloat(editTextUsuario, "scaleY", 0.0f, 1.0f);
                //Animacion para el edittext clave
                ObjectAnimator scaleDownX_TxtClave = ObjectAnimator.ofFloat(editTextClave, "scaleX", 0.0f, 1.0f);
                ObjectAnimator scaleDownY_TxtClave = ObjectAnimator.ofFloat(editTextClave, "scaleY", 0.0f, 1.0f);
                //Animacion para el edittext nombre
                ObjectAnimator scaleDownX_TxtNombre = ObjectAnimator.ofFloat(editTextNombre, "scaleX", 0.0f, 1.0f);
                ObjectAnimator scaleDownY_TxtNombre = ObjectAnimator.ofFloat(editTextNombre, "scaleY", 0.0f, 1.0f);
                //Animacion para el edittext apellidos
                ObjectAnimator scaleDownX_TxtApellidos = ObjectAnimator.ofFloat(editTextApellidos, "scaleX", 0.0f, 1.0f);
                ObjectAnimator scaleDownY_TxtApellidos = ObjectAnimator.ofFloat(editTextApellidos, "scaleY", 0.0f, 1.0f);
                //Configuramos los animatorsets
                animatorSetEscale.play(scaleDownX_registro).with(scaleDownY_registro)
                        .with(scaleDownX_TxtUsuario).with(scaleDownY_TxtUsuario)
                        .with(scaleDownX_TxtClave).with(scaleDownY_TxtClave)
                        .with(scaleDownX_TxtApellidos).with(scaleDownY_TxtApellidos)
                        .with(scaleDownX_TxtNombre).with(scaleDownY_TxtNombre);
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
        if(v.equals(btRegistrar)){
            //Variables booleanas
            boolean usuario=true, clave=true, nombre=true, apellidos=true;
            //Antes de hacer la peticion al servidor realizamos las siguientes comprobaciones:
            //Comprobamos con una expresion regular que sea un email valido:
            Pattern patronEmail = Pattern.compile("([a-z0-9]+(\\.?[a-z0-9])*)+@(([a-z]+)\\.([a-z]+))+");
            Matcher match = patronEmail.matcher(editTextUsuario.getText().toString().toLowerCase());
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
                    txtErrorClave.setText("Clave con caracteres no alfanumericos.");
                    txtErrorClave.setVisibility(View.VISIBLE);
                    editTextClave.setTextColor(getResources().getColor(R.color.colorErrorsitoEditText));
                }
            }
            boolean existMinuscula = false, existMayuscula = false, existNumero = false;
            for (int i = 0 ; i < editTextClave.getText().toString().length() && (!existMayuscula || !existMinuscula || !existNumero); i++){
                if(editTextClave.getText().toString().charAt(i) >='A' &&editTextClave.getText().toString().charAt(i)<='Z'){
                    existMayuscula=true;
                }
                else if (editTextClave.getText().toString().charAt(i) >='a' &&editTextClave.getText().toString().charAt(i)<='z'){
                    existMinuscula=true;
                }
                else if(editTextClave.getText().toString().charAt(i) >='0' &&editTextClave.getText().toString().charAt(i)<='9'){
                    existNumero=true;
                }
            }
            if(!existMayuscula&&clave){
                clave=false;
                txtErrorClave.setText("Introduzca una letra mayuscula como minimo.");
                txtErrorClave.setVisibility(View.VISIBLE);
                editTextClave.setTextColor(getResources().getColor(R.color.colorErrorsitoEditText));
            }else if(!existMinuscula&&clave){
                clave=false;
                txtErrorClave.setText("Introduzca una letra minuscula como minimo.");
                txtErrorClave.setVisibility(View.VISIBLE);
                editTextClave.setTextColor(getResources().getColor(R.color.colorErrorsitoEditText));
            }else if(!existNumero&&clave){
                clave=false;
                txtErrorClave.setText("Introduzca un numero como minimo.");
                txtErrorClave.setVisibility(View.VISIBLE);
                editTextClave.setTextColor(getResources().getColor(R.color.colorErrorsitoEditText));
            }
            if (clave){
                txtErrorClave.setVisibility(View.GONE);
                txtErrorClave.setText("Error");
                editTextClave.setTextColor(getResources().getColor(R.color.places_ui_default_primary_dark));
            }
            //Control de nombre para ver si han metido algun caracter no letra ni espacio:
            //Quitamos espacios al nombre pero dejamos uno entre palabras:
            StringBuilder aux= new StringBuilder();
            for(int i=0,contEspacios=0;i<editTextNombre.getText().length();i++){
                if(editTextNombre.getText().charAt(i)!=' '){
                    aux.append(editTextNombre.getText().charAt(i));
                    contEspacios=0;
                }else{
                    if(contEspacios==0){
                        aux.append(editTextNombre.getText().charAt(i));
                    }
                    contEspacios++;
                }
            }
            editTextNombre.setText(aux.toString().trim());
            for (int i = 0 ; i < editTextNombre.getText().toString().length() && nombre; i++){
                if(editTextNombre.getText().toString().toUpperCase().charAt(i) >='A' &&editTextNombre.getText().toString().toUpperCase().charAt(i)<='Z'||editTextNombre.getText().toString().charAt(i)==' '){
                }else{
                    nombre=false;
                }
            }
            if (nombre){
                txtErrorNombre.setVisibility(View.GONE);
                txtErrorNombre.setText("Error");
                editTextNombre.setTextColor(getResources().getColor(R.color.places_ui_default_primary_dark));
            }else{
                txtErrorNombre.setText("Nombre no valido.");
                txtErrorNombre.setVisibility(View.VISIBLE);
                editTextNombre.setTextColor(getResources().getColor(R.color.colorErrorsitoEditText));
            }
            //Control de apellidos para ver si han metido algun caracter no alfanumerico ni espacio:
            //Quitamos espacios al nombre pero dejamos uno entre palabras:
            aux= new StringBuilder();
            for(int i=0,contEspacios=0;i<editTextApellidos.getText().length();i++){
                if(editTextApellidos.getText().charAt(i)!=' '){
                    aux.append(editTextApellidos.getText().charAt(i));
                    contEspacios=0;
                }else{
                    if(contEspacios==0){
                        aux.append(editTextApellidos.getText().charAt(i));
                    }
                    contEspacios++;
                }
            }
            editTextApellidos.setText(aux.toString().trim());
            for (int i = 0 ; i < editTextApellidos.getText().toString().length() && apellidos; i++){
                if(editTextApellidos.getText().toString().toUpperCase().charAt(i) >='A' &&editTextApellidos.getText().toString().toUpperCase().charAt(i)<='Z'||editTextApellidos.getText().toString().charAt(i)==' '){
                }else{
                    apellidos=false;
                }
            }
            if (apellidos){
                txtErrorApellidos.setVisibility(View.GONE);
                txtErrorApellidos.setText("Error");
                editTextApellidos.setTextColor(getResources().getColor(R.color.places_ui_default_primary_dark));
            }else{
                txtErrorApellidos.setText("Apellidos no validos.");
                txtErrorApellidos.setVisibility(View.VISIBLE);
                editTextApellidos.setTextColor(getResources().getColor(R.color.colorErrorsitoEditText));
            }
            //Si todas las comprobaciones del front son correctas pasamos a lanzar la solicitud al servidor:
            if(clave&&usuario&&nombre&&apellidos){
                //Creamos objeto Retrofit, para lanzar peticiones y poder recibir respuestas
                Retrofit retrofit = new Retrofit.Builder().baseUrl("http://192.168.0.107:8080/").addConverterFactory(GsonConverterFactory.create()).build();
                //Vinculamos el cliente con la interfaz.
                //En esa interfaz se definen los metodos y los verbos que usan
                //Definimos las peticiones que va a poder hacer segun las implementadas en la interfaz que se indica
                JsonPlaceHolderApi peticiones = retrofit.create(JsonPlaceHolderApi.class);
                //Creamos una peticion para registrar un usuario, que creamos con los valores de los editext:
                Usuario uObject = new Usuario(capitalizaString(editTextNombre.getText().toString()),capitalizaString(editTextApellidos.getText().toString()),editTextUsuario.getText().toString().toLowerCase(),editTextClave.getText().toString());
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
            }
        }
    }
    //Comprobacion de que los campos no esten vacios y tengan un formato correcto antes de poder intentar iniciar sesion:
    @Override
    public void afterTextChanged(Editable s) {
        //Boolean prueba de que el estado anterior era true
        boolean anterior=false;
        if(pruebaFormatoUsuario&&pruebaFormatoClave&&pruebaFormatoNombre&&pruebaFormatoApellidos){anterior=true;}
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
        //Si el texto de nombre ha cambiado:
        else if (s==editTextNombre.getEditableText()){
            //Quitamos espacios al nombre pero dejamos uno entre palabras:
            String nombre=s.toString().trim();
            StringBuilder aux= new StringBuilder();
            for(int i=0,contEspacios=0;i<nombre.length();i++){
                if(nombre.charAt(i)!=' '){
                    aux.append(nombre.charAt(i));
                    contEspacios=0;
                }else{
                    if(contEspacios==0){
                        aux.append(nombre.charAt(i));
                    }
                    contEspacios++;
                }
            }
            nombre= aux.toString();
            //Si no esta vacio y tiene mas de 0 caracter:
            if(!nombre.equals("")&&nombre.length()>=1&&nombre.length()<=20){
                pruebaFormatoNombre=true;
            }else{
                pruebaFormatoNombre=false;
            }
        }
        //Si el texto de apellidos ha cambiado:
        else if (s==editTextApellidos.getEditableText()){
            //Quitamos espacios a los apellidos pero dejamos uno entre palabras:
            String apellidos=s.toString().trim();
            StringBuilder aux= new StringBuilder();
            for(int i=0,contEspacios=0;i<apellidos.length();i++){
                if(apellidos.charAt(i)!=' '){
                    aux.append(apellidos.charAt(i));
                    contEspacios=0;
                }else{
                    if(contEspacios==0){
                        aux.append(apellidos.charAt(i));
                    }
                    contEspacios++;
                }
            }
            apellidos= aux.toString();
            //Si no esta vacio y tiene mas de 1 caracter:
            if(!apellidos.equals("")&&apellidos.length()>=1&&apellidos.length()<=20){
                pruebaFormatoApellidos=true;
            }else{
                pruebaFormatoApellidos=false;
            }
        }
        //Si las cuatro pruebas de formato son correctas pasamos a liberar el boton de registrar usuario:
        if (pruebaFormatoUsuario&&pruebaFormatoClave&&pruebaFormatoNombre&&pruebaFormatoApellidos&&!anterior){
            //Conjunto de animator
            AnimatorSet animator = new AnimatorSet();
            //Animacion para el boton registrar usuario
            ObjectAnimator scaleDownX_RegistrarUsuario = ObjectAnimator.ofFloat(btRegistrar, "scaleX", 0.95f, 1.0f);
            ObjectAnimator scaleDownY_RegistrarUsuario = ObjectAnimator.ofFloat(btRegistrar, "scaleY", 0.95f, 1.0f);
            animator.play(scaleDownX_RegistrarUsuario).with(scaleDownY_RegistrarUsuario);
            animator.setDuration(200);
            animator.start();
            //Habilitamos el boton registrar usuario
            btRegistrar.setEnabled(true);
            btRegistrar.setTextColor(getResources().getColor(R.color.colorPrimary));
        }else if ((!pruebaFormatoUsuario||!pruebaFormatoClave||!pruebaFormatoNombre||!pruebaFormatoApellidos)&&anterior){
            //Conjunto de animator
            AnimatorSet animator = new AnimatorSet();
            //Animacion para el boton registrar usuario
            ObjectAnimator scaleDownX_RegistrarUsuario = ObjectAnimator.ofFloat(btRegistrar, "scaleX", 1.0f, 0.95f);
            ObjectAnimator scaleDownY_RegistrarUsuario = ObjectAnimator.ofFloat(btRegistrar, "scaleY", 1.0f, 0.95f);
            animator.play(scaleDownX_RegistrarUsuario).with(scaleDownY_RegistrarUsuario);
            animator.setDuration(200);
            animator.start();
            //Deshabilitamos el boton registrar usuario
            btRegistrar.setEnabled(false);
            btRegistrar.setTextColor(getResources().getColor(R.color.colorGris));
        }
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}
    //EXTRACCION DE METODOS
    public String capitalizaString(String txtOrigen){
        //Dividimos el string en las palabras segun los caracteres vacios o en blanco
        String []palabras = txtOrigen.split("\\s+");
        StringBuilder txtFinal = new StringBuilder();
        //Bucle para conseguir la primera letra en mayuscula
        for(String palabra : palabras){
            txtFinal.append(palabra.substring(0,1).toUpperCase().concat( palabra.substring(1,palabra.length()).toLowerCase()).concat(" "));
        }
        //Antes de devolverlo realizamos un trim por si ha metido espacios a la derecha de la ultima palabra
        return txtFinal.toString().trim();
    }
}
