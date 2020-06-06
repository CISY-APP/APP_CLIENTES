//Indicamos a que paquete pertenece esta clase:
package com.example.app_clientes.vistas;
//Importamos los siguientes paquetes:
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.app_clientes.Biblioteca;
import com.example.app_clientes.R;
import com.example.app_clientes.jsonplaceholder.JsonPlaceHolderApi;
import com.example.app_clientes.pojos.Vehiculo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import de.hdodenhof.circleimageview.CircleImageView;
import petrov.kristiyan.colorpicker.ColorPicker;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
//Clase que contiene toda la logica y conexion con la ventana de registrar vehiculo de la app:
public class VentanaAgregarVehiculo extends AppCompatActivity implements View.OnClickListener, TextWatcher, AdapterView.OnItemSelectedListener {
    //Variables para comprobacion de formatos:
    private boolean pruebaFormatoMatricula, pruebaFormatoMarca, pruebaFormatoModelo, pruebaFormatoCombustible, pruebaFormatoColor;
    //Atributos XML:
    private LinearLayout linearLayoutSpinnerCombustible;
    private EditText editTextMatricula, editTextMarca, editTextModelo;
    private TextView txtErrorMatricula, txtErrorMarca, txtErrorModelo;
    private Spinner spinnerTipoCombustible;
    private EditText btSeleccionarColor;
    private CircleImageView imgViewColorCoche;
    private RoundedImageView imgViewCoche;
    private ImageView btFlechaAtras, btConfirmarRegistro;
    //Atributos de la clase:
    private final ArrayList<String> colores = new ArrayList<>();
    private String colorSeleccionado;
    private String uriFotoCoche;
    private String uriParaElInsert;
    private StorageReference storageReference;
    private Uri uriImagenEndispositivo;
    private String ID_USUARIO;
    private static final int GALERY_INTENT = 1;
    //Metodo onCreate encargado de crear la actividad:
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Llamamos al superconstructor y conectamos el XML del login:
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_vehiculo);
        //Inicializamos variables booleanas de prueba:
        pruebaFormatoMatricula=false;
        pruebaFormatoMarca=false;
        pruebaFormatoModelo=false;
        pruebaFormatoCombustible =true;
        pruebaFormatoColor =true;
        //Asociamos el id del usuario en sesion a la siguiente variable
        ID_USUARIO = Biblioteca.usuarioSesion.getIdusuario().toString();
        colorSeleccionado="#07a0c3";
        //Asociamos los elementos XML a los atributos:
        linearLayoutSpinnerCombustible=findViewById(R.id.linearLayoutSpinnerTipoCombustibleAgregarVehiculo);
        editTextMatricula = findViewById(R.id.editTextMatriculaAgregarVehiculo);
        editTextMarca = findViewById(R.id.editTextMarcaAgregarVehiculo);
        editTextModelo = findViewById(R.id.editTextModeloAgregarVehiculo);
        spinnerTipoCombustible = findViewById(R.id.spinnerTipoCombustibleAgregarVehiculo);
        imgViewCoche = findViewById(R.id.imageViewCocheAgregarVehiculo);
        imgViewColorCoche = findViewById(R.id.imgLateralSeleccionarColorAgregarVehiculo);
        btSeleccionarColor = findViewById(R.id.btSeleccionarColorCocheAgregarVehiculo);
        btFlechaAtras = findViewById(R.id.btFlechaAtrasAgregarVehiculo);
        btConfirmarRegistro = findViewById(R.id.btAceptarCambiosVentanaAgregarVehiculo);btConfirmarRegistro.setOnClickListener(null);
        txtErrorMatricula = findViewById(R.id.textViewErrorMatriculaAgregarVehiculo);
        txtErrorMarca = findViewById(R.id.textViewErrorMarcaAgregarVehiculo);
        txtErrorModelo = findViewById(R.id.textViewErrorModeloAgregarVehiculo);
        //Vinculamos los botones al listener del metodo onclick, que esta implementado en esta clase:
        imgViewCoche.setOnClickListener(this);
        btSeleccionarColor.setOnClickListener(this);
        btFlechaAtras.setOnClickListener(this);
        //Vinculamos el spiner al listener correspondiente:
        inicializacionSpinnerCombustible();
        spinnerTipoCombustible.setOnItemSelectedListener(this);
        //Vinculamos los edittext a su listener para el metodo afterTextChanged, que esta implementado en esta clase:
        editTextMatricula.addTextChangedListener(this);
        editTextMarca.addTextChangedListener(this);
        editTextModelo.addTextChangedListener(this);
        //Instancia el objeto de tipo storageReference:
        storageReference = FirebaseStorage.getInstance().getReference();
        //Animaciones tipo scale despues de que todoo se haya realizado:
        txtErrorModelo.post(new Runnable() {
            @Override
            public void run() {
                //Declaramos un animatorSet para poder luego ejecutar un conjunto de animaciones a la vez:
                AnimatorSet animatorSetEscale = new AnimatorSet();
                //Animacion para el linear layout spinner combustible:
                ObjectAnimator scaleDownX_LayoutSpinnerCombustible = ObjectAnimator.ofFloat(linearLayoutSpinnerCombustible, "scaleX", 0.0f, 1.0f);
                ObjectAnimator scaleDownY_LayoutSpinnerCombustible = ObjectAnimator.ofFloat(linearLayoutSpinnerCombustible, "scaleY", 0.0f, 1.0f);
                //Animacion para el im coche:
                ObjectAnimator scaleDownX_ImCoche = ObjectAnimator.ofFloat(imgViewCoche, "scaleX", 0.0f, 1.0f);
                ObjectAnimator scaleDownY_ImCoche = ObjectAnimator.ofFloat(imgViewCoche, "scaleY", 0.0f, 1.0f);
                //Animacion para el im color:
                ObjectAnimator scaleDownX_ImColor = ObjectAnimator.ofFloat(imgViewColorCoche, "scaleX", 0.0f, 1.0f);
                ObjectAnimator scaleDownY_ImColor = ObjectAnimator.ofFloat(imgViewColorCoche, "scaleY", 0.0f, 1.0f);
                //Animacion para el editext matricula:
                ObjectAnimator scaleDownX_TxtMatricula = ObjectAnimator.ofFloat(editTextMatricula, "scaleX", 0.0f, 1.0f);
                ObjectAnimator scaleDownY_TxtMatricula = ObjectAnimator.ofFloat(editTextMatricula, "scaleY", 0.0f, 1.0f);
                //Animacion para el editext marca:
                ObjectAnimator scaleDownX_TxtMarca = ObjectAnimator.ofFloat(editTextMarca, "scaleX", 0.0f, 1.0f);
                ObjectAnimator scaleDownY_TxtMarca = ObjectAnimator.ofFloat(editTextMarca, "scaleY", 0.0f, 1.0f);
                //Animacion para el edittext modelo:
                ObjectAnimator scaleDownX_TxtModelo = ObjectAnimator.ofFloat(editTextModelo, "scaleX", 0.0f, 1.0f);
                ObjectAnimator scaleDownY_TxtModelo = ObjectAnimator.ofFloat(editTextModelo, "scaleY", 0.0f, 1.0f);
                //Animacion para el spinner tipo combustible:
                ObjectAnimator scaleDownX_SpinnerCombustible = ObjectAnimator.ofFloat(spinnerTipoCombustible, "scaleX", 0.0f, 1.0f);
                ObjectAnimator scaleDownY_SpinnerCombustible = ObjectAnimator.ofFloat(spinnerTipoCombustible, "scaleY", 0.0f, 1.0f);
                //Animacion para el bt selecionar color:
                ObjectAnimator scaleDownX_BtSeleccionarColor = ObjectAnimator.ofFloat(btSeleccionarColor, "scaleX", 0.0f, 1.0f);
                ObjectAnimator scaleDownY_BtSeleccionarColor = ObjectAnimator.ofFloat(btSeleccionarColor, "scaleY", 0.0f, 1.0f);
                //Animacion para el bt agregar vehiculo:
                ObjectAnimator scaleDownX_BtConfirmarRegistro = ObjectAnimator.ofFloat(btConfirmarRegistro, "scaleX", 0.0f, 0.5f);
                ObjectAnimator scaleDownY_BtConfirmarRegistro = ObjectAnimator.ofFloat(btConfirmarRegistro, "scaleY", 0.0f, 0.5f);
                //Configuramos el animatorSet, como se tienen que reproducir las animaciones, el tiempo que duran, que clase de interpolador utilizan y la lanzamos:
                animatorSetEscale.play(scaleDownX_ImCoche).with(scaleDownY_ImCoche)
                        .with(scaleDownX_ImColor).with(scaleDownY_ImColor)
                        .with(scaleDownX_TxtMatricula).with(scaleDownY_TxtMatricula)
                        .with(scaleDownX_TxtMarca).with(scaleDownY_TxtMarca)
                        .with(scaleDownX_TxtModelo).with(scaleDownY_TxtModelo)
                        .with(scaleDownX_LayoutSpinnerCombustible).with(scaleDownY_LayoutSpinnerCombustible)
                        .with(scaleDownX_SpinnerCombustible).with(scaleDownY_SpinnerCombustible)
                        .with(scaleDownX_BtSeleccionarColor).with(scaleDownY_BtSeleccionarColor)
                        .with(scaleDownX_BtConfirmarRegistro).with(scaleDownY_BtConfirmarRegistro);
                animatorSetEscale.setDuration(Biblioteca.tAnimacionesScaleInicial);
                animatorSetEscale.setInterpolator(new AccelerateDecelerateInterpolator());
                animatorSetEscale.start();
            }
        });
        cargaImagen();
        //Recibidor de broadcast para cerrar sesion:
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent intent) {
                String action = intent.getAction();
                if (action.equals("cierre_de_sesion")) {
                    finish();
                }
            }
        };
        registerReceiver(broadcastReceiver, new IntentFilter("cierre_de_sesion"));
    }
    //Metodo que carga imagen por defecto en el imageview redondeado:
    private void cargaImagen(){
        Glide.with(getApplicationContext()).load((Drawable) null).error(R.drawable.coche).into(imgViewCoche);
    }
    //Inicializa el spinner de vehiculos:
    private void inicializacionSpinnerCombustible() {
        //Inicializamos los valores que puede obtener el spinner combustible:
        String[] combustibles = new String[]{getText(R.string.spinner_datos_gasolina_ventanaAgregarVehiculo).toString(), getText(R.string.spinner_datos_diesel_ventanaAgregarVehiculo).toString(), getText(R.string.spinner_datos_hibrido_ventanaAgregarVehiculo).toString(), getText(R.string.spinner_datos_electrico_ventanaAgregarVehiculo).toString()};
        //Inicialimos el adapter y lo asociamos al spinner:
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,R.layout.color_spinner,combustibles);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.color_spinner);
        spinnerTipoCombustible.setAdapter(spinnerArrayAdapter);
    }
    //Inicializa el ColorPiker del color del vehiculo:
    public void colorPiker() {
        ColorPicker colorPicker = new ColorPicker(this);
        colorPicker.setTitle(getText(R.string.colorPicker_preguntaTitulo_ventanaAgregarVehiculo).toString());
        colores.clear();
        //Añadimos los colores a nuestra coleccion:
        colores.add("#000000");colores.add("#FFFFFF");colores.add("#616161");colores.add("#C8C8C8");colores.add("#7A0000");colores.add("#E70000");colores.add("#011474");
        colores.add("#20D2F6");colores.add("#01742E");colores.add("#00BA27");colores.add("#8E6D3D");colores.add("#F5C886");colores.add("#F17C00");colores.add("#E7E300");
        colores.add("#FFA3F8");
        //Establecemos los colores , definimos los botones redondos y en 5 columnas, y le asociamos un listener a ese color y lo mostramos:
        colorPicker.setColors(colores).setColumns(5).setRoundColorButton(true).setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
            @Override
            public void onChooseColor(int position, int color) {
                if(position!=-1) {
                    imgViewColorCoche.setBackgroundColor(color);
                    colorSeleccionado = colores.get(position);
                }
            }
            @Override
            public void onCancel() {}
        }).show();
    }
    //Abre la galeria de imagenes:
    public void abrirGaleria(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,GALERY_INTENT);
    }
    //Metodo de la interfaz View.OnClickListener:
    @Override
    public void onClick(View v) {
        //Segun el elemento pulsado:
        if (v.equals(imgViewCoche)){abrirGaleria();}
        else if (v.equals(btSeleccionarColor)){colorPiker();}
        else if(v.equals(btFlechaAtras)){onBackPressed();}
        else if (v.equals(btConfirmarRegistro)){
            //Variables booleanas para controlar el valor de los campos a rellenar:
            boolean pbMatricula=true, pbMarca=true, pbModelo=true, pbCombustible=true, pbColor=true;
            //Antes de hacer la peticion al servidor realizamos las siguientes comprobaciones:
            //Control matricula:
            String auxMatricula=editTextMatricula.getText().toString();
            auxMatricula=auxMatricula.replaceAll("\\s","").toUpperCase();
            //Si de longitus es 7 y es numerica hasta el 4 caracter y despues son letras:
            if(auxMatricula.length()==7) {
                try {
                    Integer.parseInt(auxMatricula.substring(0, 4));
                    for(int i = 4 ; i < auxMatricula.length() && pbMatricula ; i++ ) {
                        if(auxMatricula.charAt(i)<'A'||auxMatricula.charAt(i)>'Z') {
                            pbMatricula=false;
                        }
                    }
                }catch (NumberFormatException n){
                    pbMatricula=false;
                }
            }else {
                pbMatricula=false;
            }
            if (pbMatricula){
                txtErrorMatricula.setVisibility(View.GONE);
                txtErrorMatricula.setText("");
                editTextMatricula.setTextColor(getResources().getColor(R.color.places_ui_default_primary_dark));
            }
            else{
                txtErrorMatricula.setText(getText(R.string.txt_errorMatricula_Formato_ventanaAgregarVehiculo));
                txtErrorMatricula.setVisibility(View.VISIBLE);
                editTextMatricula.setTextColor(getResources().getColor(R.color.colorErrorsitoEditText));
            }
            //Control de marca, de momento ninguno mas:

            //Control de modelo de momento ninguno mas:

            //Control de combustible de momento ninguno mas:

            //Control de color de momento ninguno mas:

            //Si todas las comprobaciones del front son correctas pasamos a lanzar la solicitud al servidor:
            if(pbMatricula&&pbMarca&&pbModelo&&pbColor&&pbCombustible){
                //Si la uri no esta vacia realizamos lo siguiente:
                if(uriImagenEndispositivo!=null) {
                    uriParaElInsert = Long.toString(System.currentTimeMillis());
                    StorageReference filePath = storageReference.child(Biblioteca.usuarioSesion.getIdusuario().toString()).child(uriParaElInsert);
                    //Utiliza la direccion para coger la imagen del dispositivo, sube la imagen a firebase y escucha si se ha realizado de manera adecuada
                    filePath.putFile(uriImagenEndispositivo).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //Coje la URL de la imagen de la carpeta que le indiquemos con el nombre que le indiquemos de firebase
                            storageReference.child(Biblioteca.usuarioSesion.getIdusuario().toString()).child(uriParaElInsert).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    //Creamos objeto Retrofit, para lanzar peticiones y poder recibir respuestas:
                                    Retrofit retrofit = new Retrofit.Builder().baseUrl(Biblioteca.ip).addConverterFactory(GsonConverterFactory.create()).build();
                                    //Vinculamos el cliente con la interfaz:
                                    JsonPlaceHolderApi peticiones = retrofit.create(JsonPlaceHolderApi.class);
                                    //Creamos una peticion para registrar un vehiculo con el valor de los editexts y demas:
                                    String modeloAux = Biblioteca.quitaEspaciosRepetidosEntrePalabras(editTextModelo.getText().toString());
                                    String marcaAux = Biblioteca.quitaEspaciosRepetidosEntrePalabras(editTextMarca.getText().toString());
                                    Map<String, String> infoMap = new HashMap<>();
                                    infoMap.put("idUsuario", Biblioteca.usuarioSesion.getIdusuario().toString());
                                    infoMap.put("matricula", editTextMatricula.getText().toString().replaceAll("\\s", "").toUpperCase());
                                    infoMap.put("modelo", Biblioteca.capitalizaString(modeloAux));
                                    infoMap.put("marca", Biblioteca.capitalizaString(marcaAux));
                                    infoMap.put("combustible", spinnerTipoCombustible.getSelectedItem().toString());
                                    infoMap.put("color", colorSeleccionado.toUpperCase());
                                    infoMap.put("fotovehiculo", uri.toString());
                                    Call<Vehiculo> call = peticiones.registrarVehiculo(infoMap);
                                    //Ejecutamos la petición en un hilo en segundo plano, retrofit lo hace por nosotros y esperamos a la respuesta:
                                    call.enqueue(new Callback<Vehiculo>() {
                                        //Gestionamos la respuesta del servidor:
                                        @Override
                                        public void onResponse(Call<Vehiculo> call, Response<Vehiculo> response) {
                                            //Respuesta del servidor con un error y paramos el flujo del programa, indicando el codigo de error:
                                            if (!response.isSuccessful()) {
                                                Toast.makeText(getApplicationContext(), "Code: " + response.code(), Toast.LENGTH_LONG).show();
                                                return;
                                            }
                                            //Si el cambio ha sido exitoso volvemos a la actividad anterior:
                                            Toast.makeText(getApplicationContext(), getText(R.string.txt_agregadoVehiculo_Toast_ventanaAgregarVehiculo), Toast.LENGTH_LONG).show();
                                            //Con el metodo setResult pasamos el codigo resultado de la operacion, que en este caso es una constante de clase que
                                            //significa OK, y el Intent que contiene la informacion del resultado, para que en la actividad anterior se recarguen
                                            //los datos del recyclerview de la lista de coches:
                                            setResult(RESULT_OK);
                                            //Terminamos con la propia actividad con el siguiente metodo:
                                            finish();
                                        }

                                        //En caso de que no responda el servidor mostramos mensaje de error:
                                        @Override
                                        public void onFailure(Call<Vehiculo> call, Throwable t) {
                                            Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    Toast.makeText(getApplicationContext(), "No se ha podido realizar el insert en la base de datos", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {

                        }
                    });
                }else{
                    //Creamos objeto Retrofit, para lanzar peticiones y poder recibir respuestas:
                    Retrofit retrofit = new Retrofit.Builder().baseUrl(Biblioteca.ip).addConverterFactory(GsonConverterFactory.create()).build();
                    //Vinculamos el cliente con la interfaz:
                    JsonPlaceHolderApi peticiones = retrofit.create(JsonPlaceHolderApi.class);
                    //Creamos una peticion para registrar un vehiculo con el valor de los editexts y demas:
                    String modeloAux = Biblioteca.quitaEspaciosRepetidosEntrePalabras(editTextModelo.getText().toString());
                    String marcaAux = Biblioteca.quitaEspaciosRepetidosEntrePalabras(editTextMarca.getText().toString());
                    Map<String, String> infoMap = new HashMap<>();
                    infoMap.put("idUsuario", Biblioteca.usuarioSesion.getIdusuario().toString());
                    infoMap.put("matricula", editTextMatricula.getText().toString().replaceAll("\\s", "").toUpperCase());
                    infoMap.put("modelo", Biblioteca.capitalizaString(modeloAux));
                    infoMap.put("marca", Biblioteca.capitalizaString(marcaAux));
                    infoMap.put("combustible", spinnerTipoCombustible.getSelectedItem().toString());
                    infoMap.put("color", colorSeleccionado.toUpperCase());
                    infoMap.put("fotovehiculo", "");
                    Call<Vehiculo> call = peticiones.registrarVehiculo(infoMap);
                    //Ejecutamos la petición en un hilo en segundo plano, retrofit lo hace por nosotros y esperamos a la respuesta:
                    call.enqueue(new Callback<Vehiculo>() {
                        //Gestionamos la respuesta del servidor:
                        @Override
                        public void onResponse(Call<Vehiculo> call, Response<Vehiculo> response) {
                            //Respuesta del servidor con un error y paramos el flujo del programa, indicando el codigo de error:
                            if (!response.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Code: " + response.code(), Toast.LENGTH_LONG).show();
                                return;
                            }
                            //Si el cambio ha sido exitoso volvemos a la actividad anterior:
                            Toast.makeText(getApplicationContext(), getText(R.string.txt_agregadoVehiculo_Toast_ventanaAgregarVehiculo), Toast.LENGTH_LONG).show();
                            //Con el metodo setResult pasamos el codigo resultado de la operacion, que en este caso es una constante de clase que
                            //significa OK, y el Intent que contiene la informacion del resultado, para que en la actividad anterior se recarguen
                            //los datos del recyclerview de la lista de coches:
                            setResult(RESULT_OK);
                            //Terminamos con la propia actividad con el siguiente metodo:
                            finish();
                        }

                        //En caso de que no responda el servidor mostramos mensaje de error:
                        @Override
                        public void onFailure(Call<Vehiculo> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }
    }
    //Metodo afterTextChanged implementado de la interfaz TextWatcher (cuando cambie el texto se ejecutara):
    @Override
    public void afterTextChanged(Editable s) {
        //Boolean para comprobar que estado tiene antes de realizar pruebas:
        boolean anterior=false;
        if(pruebaFormatoMatricula&&pruebaFormatoMarca&&pruebaFormatoModelo&& pruebaFormatoCombustible && pruebaFormatoColor){anterior=true;}
        //Si el texto de matricula ha cambiado:
        if(s==editTextMatricula.getEditableText()){
            //Limpiamos de espacios y si la matricula no es cadena vacia y tiene longitud de 7 caracteres es valida de formato:
            String matricula=s.toString().toUpperCase().replaceAll("\\s","");
            pruebaFormatoMatricula= !matricula.equals("") && matricula.length() == 7;
        }
        //Si el texto de marca ha cambiado:
        else if (s==editTextMarca.getEditableText()){
            //Limpiamos espacios multiples y si es distinto de cadena vacia y esta entre 1 a los 30 caracteres es valido de formato:
            String marca=Biblioteca.quitaEspaciosRepetidosEntrePalabras(s.toString());
            pruebaFormatoMarca= !marca.equals("") && marca.length() >= 1 && marca.length() <= 30;
        }
        //Si el texto de modelo ha cambiado:
        else if (s==editTextModelo.getEditableText()){
            //Limpiamos espacios multiples y si es distinto de cadena vacia y esta entre 1 a los 30 caracteres es valido de formato:
            String modelo=Biblioteca.quitaEspaciosRepetidosEntrePalabras(s.toString());
            pruebaFormatoModelo= !modelo.equals("") && modelo.length() >= 1 && modelo.length() <= 30;
        }
        //Si las 5 pruebas de formato son correctas pasamos a habilitar el boton de agregar vehiculo:
        if (pruebaFormatoMatricula&&pruebaFormatoMarca&&pruebaFormatoModelo&& pruebaFormatoColor && pruebaFormatoCombustible &&!anterior){
            //Conjunto de animator:
            AnimatorSet animator = new AnimatorSet();
            //Animacion para el bt agregar vehiculo:
            ObjectAnimator scaleDownX_BtConfirmarRegistro = ObjectAnimator.ofFloat(btConfirmarRegistro, "scaleX", 0.5f, 1.0f);
            ObjectAnimator scaleDownY_BtConfirmarRegistro = ObjectAnimator.ofFloat(btConfirmarRegistro, "scaleY", 0.5f, 1.0f);
            animator.play(scaleDownX_BtConfirmarRegistro).with(scaleDownY_BtConfirmarRegistro);
            animator.setDuration(Biblioteca.tAnimacionesScaleBotones);
            animator.start();
            //Habilitamos el boton agregar vehiculo:
            btConfirmarRegistro.setEnabled(true);
            btConfirmarRegistro.setOnClickListener(this);
            btConfirmarRegistro.setColorFilter(getResources().getColor(R.color.colorPrimary));
        }else if ((!pruebaFormatoMatricula||!pruebaFormatoMarca||!pruebaFormatoModelo||!pruebaFormatoColor ||!pruebaFormatoCombustible)&&anterior){
            //Conjunto de animator:
            AnimatorSet animator = new AnimatorSet();
            //Animacion para el bt agregar vehiculo:
            ObjectAnimator scaleDownX_BtConfirmarRegistro = ObjectAnimator.ofFloat(btConfirmarRegistro, "scaleX", 1.0f, 0.5f);
            ObjectAnimator scaleDownY_BtConfirmarRegistro = ObjectAnimator.ofFloat(btConfirmarRegistro, "scaleY", 1.0f, 0.5f);
            animator.play(scaleDownX_BtConfirmarRegistro).with(scaleDownY_BtConfirmarRegistro);
            animator.setDuration(Biblioteca.tAnimacionesScaleBotones);
            animator.start();
            //Deshabilitamos el boton agregar vehiculo:
            btConfirmarRegistro.setEnabled(false);
            btConfirmarRegistro.setOnClickListener(null);
            btConfirmarRegistro.setColorFilter(getResources().getColor(R.color.colorGris));
        }
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}
    //Metodos de la interfaz de los spinners:
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        spinnerTipoCombustible.getSelectedItem();
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {}
    //Coge la direccion del dispositivo:
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALERY_INTENT && resultCode == RESULT_OK) {
            //Coge la Uri del dispositivo
            uriImagenEndispositivo = data.getData();
            //Cambia la imagen desde el dispositivo
            Glide.with(getApplicationContext()).load(uriImagenEndispositivo).error(R.drawable.coche).into(imgViewCoche);
        }
    }
}
