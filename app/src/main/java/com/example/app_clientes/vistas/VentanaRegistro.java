//Indicamos a que paquete pertenece esta clase:
package com.example.app_clientes.vistas;
//Importamos los siguientes paquetes:
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.app_clientes.Biblioteca;
import com.example.app_clientes.R;
import com.example.app_clientes.jsonplaceholder.JsonPlaceHolderApi;
import com.example.app_clientes.pojos.Usuario;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
//Clase que contiene toda la logica y conexion con la ventana de registro usuario de la app:
public class VentanaRegistro extends AppCompatActivity implements View.OnClickListener, TextWatcher {
    //Atributos XML:
    private EditText editTextUsuario, editTextClave, editTextNombre, editTextApellidos;
    private Button btRegistrar;
    private ImageView btVolver;
    private TextView txtErrorUsuario, txtErrorClave, txtErrorNombre, txtErrorApellidos;
    //Variables para comprobacion de formatos:
    private boolean pruebaFormatoUsuario, pruebaFormatoClave, pruebaFormatoNombre, pruebaFormatoApellidos;
    //Metodo onCreate encargado de crear la actividad:
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //Llamamos al superconstructor y conectamos el XML del login:
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ventana_registro);
        //Inicializamos variables de formato:
        pruebaFormatoUsuario=false;
        pruebaFormatoClave=false;
        pruebaFormatoNombre=false;
        pruebaFormatoApellidos=false;
        //Asociamos los elementos XML a los atributos:
        editTextUsuario=findViewById(R.id.editTextUsuarioRegistroUsuario);
        editTextClave=findViewById(R.id.editTexClaveRegistroUsuario);
        editTextNombre=findViewById(R.id.editTextNombreRegistroUsuario);
        editTextApellidos=findViewById(R.id.editTextApellidosRegistroUsuario);
        btRegistrar=findViewById(R.id.buttonRegistrarRegistroUsuario);
        btVolver=findViewById(R.id.btFlechaAtrasRegistrarUsuario);
        txtErrorUsuario=findViewById(R.id.textViewErrorUsuarioRegistroUsuario);
        txtErrorClave=findViewById(R.id.textViewErrorClaveRegistroUsuario);
        txtErrorNombre=findViewById(R.id.textViewErrorNombreRegistroUsuario);
        txtErrorApellidos=findViewById(R.id.textViewErrorApellidosRegistroUsuario);
        //Vinculamos los botones al listener del metodo onclick, que esta implementado en esta clase:
        btRegistrar.setOnClickListener(this);
        btVolver.setOnClickListener(this);
        //Vinculamos los edittext a su listener para el metodo afterTextChanged, que esta implementado en esta clase:
        editTextUsuario.addTextChangedListener(this);
        editTextClave.addTextChangedListener(this);
        editTextNombre.addTextChangedListener(this);
        editTextApellidos.addTextChangedListener(this);
        //Animaciones tipo scale despues de que todoo se haya realizado:
        txtErrorApellidos.post(new Runnable() {
            @Override
            public void run() {
                //Declaramos un animatorSet para poder luego ejecutar un conjunto de animaciones a la vez:
                AnimatorSet animatorSetEscale = new AnimatorSet();
                //Instanciamos el conjunto de animaciones:
                //Animacion para el boton registrar (Este boton nace mas pequeño ya que segun el formato de los edittext se activara o no):
                ObjectAnimator scaleDownX_registro = ObjectAnimator.ofFloat(btRegistrar, "scaleX", 0.0f, 0.95f);
                ObjectAnimator scaleDownY_registro = ObjectAnimator.ofFloat(btRegistrar, "scaleY", 0.0f, 0.95f);
                //Animacion para el edittext Usuario:
                ObjectAnimator scaleDownX_TxtUsuario = ObjectAnimator.ofFloat(editTextUsuario, "scaleX", 0.0f, 1.0f);
                ObjectAnimator scaleDownY_TxtUsuario = ObjectAnimator.ofFloat(editTextUsuario, "scaleY", 0.0f, 1.0f);
                //Animacion para el edittext clave:
                ObjectAnimator scaleDownX_TxtClave = ObjectAnimator.ofFloat(editTextClave, "scaleX", 0.0f, 1.0f);
                ObjectAnimator scaleDownY_TxtClave = ObjectAnimator.ofFloat(editTextClave, "scaleY", 0.0f, 1.0f);
                //Animacion para el edittext nombre:
                ObjectAnimator scaleDownX_TxtNombre = ObjectAnimator.ofFloat(editTextNombre, "scaleX", 0.0f, 1.0f);
                ObjectAnimator scaleDownY_TxtNombre = ObjectAnimator.ofFloat(editTextNombre, "scaleY", 0.0f, 1.0f);
                //Animacion para el edittext apellidos:
                ObjectAnimator scaleDownX_TxtApellidos = ObjectAnimator.ofFloat(editTextApellidos, "scaleX", 0.0f, 1.0f);
                ObjectAnimator scaleDownY_TxtApellidos = ObjectAnimator.ofFloat(editTextApellidos, "scaleY", 0.0f, 1.0f);
                //Configuramos el animatorSet, como se tienen que reproducir las animaciones, el tiempo que duran, que clase de interpolador utilizan y la lanzamos:
                animatorSetEscale.play(scaleDownX_registro).with(scaleDownY_registro)
                        .with(scaleDownX_TxtUsuario).with(scaleDownY_TxtUsuario)
                        .with(scaleDownX_TxtClave).with(scaleDownY_TxtClave)
                        .with(scaleDownX_TxtApellidos).with(scaleDownY_TxtApellidos)
                        .with(scaleDownX_TxtNombre).with(scaleDownY_TxtNombre);
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
        if(v.equals(btRegistrar)){
            //Variables booleanas para controlar el valor que contienen los editexts:
            boolean pbUsuario=true, pbClave=true, pbNombre=true, pbApellidos=true;
            //Antes de hacer la peticion al servidor realizamos las siguientes comprobaciones:
            //Comprobamos que sea un email valido, y dependiendo del resultado imprimimos mensaje de error o no:
            if (!Biblioteca.compruebaEmailValido(editTextUsuario.getText().toString())) {
                txtErrorUsuario.setText(getText(R.string.txt_emailError_Formato_ventanaRegistro));
                txtErrorUsuario.setVisibility(View.VISIBLE);
                editTextUsuario.setTextColor(getResources().getColor(R.color.colorErrorsitoEditText));
                pbUsuario=false;
            }else{
                txtErrorUsuario.setVisibility(View.GONE);
                txtErrorUsuario.setText("");
                editTextUsuario.setTextColor(getResources().getColor(R.color.places_ui_default_primary_dark));
            }
            //Control clave que contenga solo caracteres alfanumericos(sin acentos ni demas variantes), y dependiendo del mensaje imprimimos mensaje de error o no:
            if(!Biblioteca.compruebaSiCadenaContieneCaracteresAlfanumericos(editTextClave.getText().toString())){
                pbClave=false;
                txtErrorClave.setText(getText(R.string.txt_claveError_Formato_ventanaRegistro));
                txtErrorClave.setVisibility(View.VISIBLE);
                editTextClave.setTextColor(getResources().getColor(R.color.colorErrorsitoEditText));
            }
            boolean pbExistMinuscula = false, pbExistMayuscula = false, pbExistNumero = false;
            for (int i = 0 ; i < editTextClave.getText().toString().length() &&pbClave && (!pbExistMayuscula || !pbExistMinuscula || !pbExistNumero); i++){
                if(editTextClave.getText().toString().charAt(i) >='A' &&editTextClave.getText().toString().charAt(i)<='Z'){
                    pbExistMayuscula=true;
                }
                else if (editTextClave.getText().toString().charAt(i) >='a' &&editTextClave.getText().toString().charAt(i)<='z'){
                    pbExistMinuscula=true;
                }
                else if(editTextClave.getText().toString().charAt(i) >='0' &&editTextClave.getText().toString().charAt(i)<='9'){
                    pbExistNumero=true;
                }
            }
            if(!pbExistMayuscula&&pbClave){
                pbClave=false;
                txtErrorClave.setText(getText(R.string.txt_claveErrorMayuscula_Formato_ventanaRegistro));
                txtErrorClave.setVisibility(View.VISIBLE);
                editTextClave.setTextColor(getResources().getColor(R.color.colorErrorsitoEditText));
            }else if(!pbExistMinuscula&&pbClave){
                pbClave=false;
                txtErrorClave.setText(getText(R.string.txt_claveErrorMinuscula_Formato_ventanaRegistro));
                txtErrorClave.setVisibility(View.VISIBLE);
                editTextClave.setTextColor(getResources().getColor(R.color.colorErrorsitoEditText));
            }else if(!pbExistNumero&&pbClave){
                pbClave=false;
                txtErrorClave.setText(getText(R.string.txt_claveErrorNumero_Formato_ventanaRegistro));
                txtErrorClave.setVisibility(View.VISIBLE);
                editTextClave.setTextColor(getResources().getColor(R.color.colorErrorsitoEditText));
            }
            if (pbClave){
                txtErrorClave.setVisibility(View.GONE);
                txtErrorClave.setText("");
                editTextClave.setTextColor(getResources().getColor(R.color.places_ui_default_primary_dark));
            }
            //Control de nombre para ver si han metido algun caracter no letra ni espacio, si se aceptan caracteres con acentos del español:
            editTextNombre.setText(Biblioteca.quitaEspaciosRepetidosEntrePalabras(editTextNombre.getText().toString()));
            if (Biblioteca.compruebaSiCadenaContieneCaracteresValidosConAcentos(editTextNombre.getText().toString())){
                txtErrorNombre.setVisibility(View.GONE);
                txtErrorNombre.setText("");
                editTextNombre.setTextColor(getResources().getColor(R.color.places_ui_default_primary_dark));
            }else{
                txtErrorNombre.setText(getText(R.string.txt_nombreError_Formato_ventanaRegistro));
                txtErrorNombre.setVisibility(View.VISIBLE);
                editTextNombre.setTextColor(getResources().getColor(R.color.colorErrorsitoEditText));
                pbNombre=false;
            }
            //Control de apellidos para ver si han metido algun caracter no letra ni espacio, si se aceptan caracteres con acentos del español:
            editTextApellidos.setText(Biblioteca.quitaEspaciosRepetidosEntrePalabras(editTextApellidos.getText().toString()));
            if (Biblioteca.compruebaSiCadenaContieneCaracteresValidosConAcentos(editTextApellidos.getText().toString())){
                txtErrorApellidos.setVisibility(View.GONE);
                txtErrorApellidos.setText("");
                editTextApellidos.setTextColor(getResources().getColor(R.color.places_ui_default_primary_dark));
            }else{
                txtErrorApellidos.setText(getText(R.string.txt_apellidosError_Formato_ventanaRegistro));
                txtErrorApellidos.setVisibility(View.VISIBLE);
                editTextApellidos.setTextColor(getResources().getColor(R.color.colorErrorsitoEditText));
                pbApellidos=false;
            }
            //Si todas las comprobaciones del front son correctas pasamos a lanzar la solicitud al servidor:
            if(pbClave&&pbUsuario&&pbNombre&&pbApellidos){
                //Creamos objeto Retrofit, para lanzar peticiones y poder recibir respuestas:
                Retrofit retrofit = new Retrofit.Builder().baseUrl(Biblioteca.ip).addConverterFactory(GsonConverterFactory.create()).build();
                //Vinculamos el cliente con la interfaz:
                JsonPlaceHolderApi peticiones = retrofit.create(JsonPlaceHolderApi.class);
                //Creamos una peticion para registrar un usuario, que creamos con los valores de los editext:
                Usuario uObject = new Usuario(Biblioteca.capitalizaString(editTextNombre.getText().toString()),Biblioteca.capitalizaString(editTextApellidos.getText().toString()),editTextUsuario.getText().toString().toLowerCase(),editTextClave.getText().toString());
                Call<Usuario> call = peticiones.registrarUsuario(uObject);
                //Ejecutamos la petición en un hilo en segundo plano, retrofit lo hace por nosotros y esperamos a la respuesta:
                call.enqueue(new Callback<Usuario>() {
                    //Gestionamos la respuesta del servidor:
                    @Override
                    public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                        //Respuesta del servidor con un error y paramos el flujo del programa, indicando el codigo de error:
                        if (!response.isSuccessful()) {
                            //500 si ya existe
                            //400 si da error
                            Toast.makeText(getApplication(),"Code: "+ response.code(), Toast.LENGTH_LONG).show();
                            return;
                        }
                        //Mostramos un Toast de registro realizado con exito:
                        Toast.makeText(getApplication(),getText(R.string.txt_registro_Toast_ventanaRegistro), Toast.LENGTH_SHORT).show();
                        //Instanciamos nuestro objeto Intent explicito para pasar a la ventana del login:
                        Intent intentRegistro = new Intent(getApplication(), VentanaLogin.class);
                        //Iniciamos la nueva actividad y finalizamos la otra:
                        startActivity(intentRegistro);
                        finish();
                    }
                    //En caso de que no responda el servidor mostramos mensaje de error:
                    @Override
                    public void onFailure(Call<Usuario> call, Throwable t) {
                        Toast.makeText(getApplication(),t.getMessage(), Toast.LENGTH_LONG).show();
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
        if(pruebaFormatoUsuario&&pruebaFormatoClave&&pruebaFormatoNombre&&pruebaFormatoApellidos){anterior=true;}
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
        //Si el texto de nombre ha cambiado:
        else if (s==editTextNombre.getEditableText()){
            //Quitamos espacios al nombre pero dejamos uno entre palabras:
            String nombre=Biblioteca.quitaEspaciosRepetidosEntrePalabras(s.toString());
            //Si no esta vacio y tiene mas de 0 caracter y menos de 21:
            pruebaFormatoNombre= !nombre.equals("") && nombre.length() >= 1 && nombre.length() <= 20;
        }
        //Si el texto de apellidos ha cambiado:
        else if (s==editTextApellidos.getEditableText()){
            //Quitamos espacios a los apellidos pero dejamos uno entre palabras:
            String apellidos=Biblioteca.quitaEspaciosRepetidosEntrePalabras(s.toString());
            //Si no esta vacio y tiene mas de 0 caracter y menos de 21:
            pruebaFormatoApellidos= !apellidos.equals("") && apellidos.length() >= 1 && apellidos.length() <= 20;
        }
        //Si las cuatro pruebas de formato son correctas pasamos a habilitar el boton de registrar usuario:
        if (pruebaFormatoUsuario&&pruebaFormatoClave&&pruebaFormatoNombre&&pruebaFormatoApellidos&&!anterior){
            //Conjunto de animator:
            AnimatorSet animator = new AnimatorSet();
            //Animacion para el boton registrar usuario:
            ObjectAnimator scaleDownX_RegistrarUsuario = ObjectAnimator.ofFloat(btRegistrar, "scaleX", 0.85f, 1.0f);
            ObjectAnimator scaleDownY_RegistrarUsuario = ObjectAnimator.ofFloat(btRegistrar, "scaleY", 0.85f, 1.0f);
            animator.play(scaleDownX_RegistrarUsuario).with(scaleDownY_RegistrarUsuario);
            animator.setDuration(Biblioteca.tAnimacionesScaleBotones);
            animator.start();
            //Habilitamos el boton registrar usuario:
            btRegistrar.setEnabled(true);
            btRegistrar.setTextColor(getResources().getColor(R.color.colorPrimary));
        }else if ((!pruebaFormatoUsuario||!pruebaFormatoClave||!pruebaFormatoNombre||!pruebaFormatoApellidos)&&anterior){
            //Conjunto de animator:
            AnimatorSet animator = new AnimatorSet();
            //Animacion para el boton registrar usuario:
            ObjectAnimator scaleDownX_RegistrarUsuario = ObjectAnimator.ofFloat(btRegistrar, "scaleX", 1.0f, 0.85f);
            ObjectAnimator scaleDownY_RegistrarUsuario = ObjectAnimator.ofFloat(btRegistrar, "scaleY", 1.0f, 0.85f);
            animator.play(scaleDownX_RegistrarUsuario).with(scaleDownY_RegistrarUsuario);
            animator.setDuration(Biblioteca.tAnimacionesScaleBotones);
            animator.start();
            //Deshabilitamos el boton registrar usuario:
            btRegistrar.setEnabled(false);
            btRegistrar.setTextColor(getResources().getColor(R.color.colorGris));
        }
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}
}
