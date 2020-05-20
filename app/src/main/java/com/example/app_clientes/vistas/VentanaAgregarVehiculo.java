package com.example.app_clientes.vistas;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
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
import com.example.app_clientes.R;
import com.example.app_clientes.jsonplaceholder.JsonPlaceHolderApi;
import com.example.app_clientes.pojos.Usuario;
import com.example.app_clientes.pojos.Vehiculo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private boolean pruebaFormatoMatricula, pruebaFormatoMarca, pruebaFormatoModelo, pruebaCombustible, pruebaColor;
    //Atributos de la clase:
    private LinearLayout linearLayoutSpinnerCombustible;
    private EditText editTextMatricula, editTextMarca, editTextModelo;
    private TextView txtErrorMatricula, txtErrorMarca, txtErrorModelo;
    private Spinner spinnerTipoCombustible;
    private EditText btSeleccionarColor;
    private CircleImageView imgViewCoche,imgViewColorCoche;
    private ImageView btFlechaAtras, btConfirmarRegistro;
    private final ArrayList<String> colores = new ArrayList<>();
    private String colorSeleccionado;
    private String uriFotoCoche;
    private String uriParaElInsert;
    private StorageReference storageReference;
    private Uri uriImagenEndispositivo;
    private String ID_USUARIO;
    private static final int GALERY_INTENT = 1;
    //Metodo onCreate encargado de crear la actividad
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_vehiculo);
        //Inicializamos variables booleanas de prueba
        pruebaFormatoMatricula=false;
        pruebaFormatoMarca=false;
        pruebaFormatoModelo=false;
        pruebaCombustible=true;
        pruebaColor=true;
        //Asociamos el id del usuario en sesion a la siguiente variable
        ID_USUARIO = VentanaLogin.usuarioSesion.getIdusuario().toString();
        colorSeleccionado="#07a0c3";
        //Vinculamos los atributos de la clase:
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
        //Inatancia el objeto de tipo storageReference
        storageReference = FirebaseStorage.getInstance().getReference();
        //Animaciones
        txtErrorModelo.post(new Runnable() {
            @Override
            public void run() {
                //Conjuntos de animators
                AnimatorSet animatorSetEscale = new AnimatorSet();
                //Animacion para el linear layout spinner combustible
                ObjectAnimator scaleDownX_LayoutSpinnerCombustible = ObjectAnimator.ofFloat(linearLayoutSpinnerCombustible, "scaleX", 0.0f, 1.0f);
                ObjectAnimator scaleDownY_LayoutSpinnerCombustible = ObjectAnimator.ofFloat(linearLayoutSpinnerCombustible, "scaleY", 0.0f, 1.0f);
                //Animacion para el im coche
                ObjectAnimator scaleDownX_ImCoche = ObjectAnimator.ofFloat(imgViewCoche, "scaleX", 0.0f, 1.0f);
                ObjectAnimator scaleDownY_ImCoche = ObjectAnimator.ofFloat(imgViewCoche, "scaleY", 0.0f, 1.0f);
                //Animacion para el im color
                ObjectAnimator scaleDownX_ImColor = ObjectAnimator.ofFloat(imgViewColorCoche, "scaleX", 0.0f, 1.0f);
                ObjectAnimator scaleDownY_ImColor = ObjectAnimator.ofFloat(imgViewColorCoche, "scaleY", 0.0f, 1.0f);
                //Animacion para el editext matricula
                ObjectAnimator scaleDownX_TxtMatricula = ObjectAnimator.ofFloat(editTextMatricula, "scaleX", 0.0f, 1.0f);
                ObjectAnimator scaleDownY_TxtMatricula = ObjectAnimator.ofFloat(editTextMatricula, "scaleY", 0.0f, 1.0f);
                //Animacion para el editext marca
                ObjectAnimator scaleDownX_TxtMarca = ObjectAnimator.ofFloat(editTextMarca, "scaleX", 0.0f, 1.0f);
                ObjectAnimator scaleDownY_TxtMarca = ObjectAnimator.ofFloat(editTextMarca, "scaleY", 0.0f, 1.0f);
                //Animacion para el edittext modelo
                ObjectAnimator scaleDownX_TxtModelo = ObjectAnimator.ofFloat(editTextModelo, "scaleX", 0.0f, 1.0f);
                ObjectAnimator scaleDownY_TxtModelo = ObjectAnimator.ofFloat(editTextModelo, "scaleY", 0.0f, 1.0f);
                //Animacion para el spinner tipo combustible
                ObjectAnimator scaleDownX_SpinnerCombustible = ObjectAnimator.ofFloat(spinnerTipoCombustible, "scaleX", 0.0f, 1.0f);
                ObjectAnimator scaleDownY_SpinnerCombustible = ObjectAnimator.ofFloat(spinnerTipoCombustible, "scaleY", 0.0f, 1.0f);
                //Animacion para el bt selecionar color
                ObjectAnimator scaleDownX_BtSeleccionarColor = ObjectAnimator.ofFloat(btSeleccionarColor, "scaleX", 0.0f, 1.0f);
                ObjectAnimator scaleDownY_BtSeleccionarColor = ObjectAnimator.ofFloat(btSeleccionarColor, "scaleY", 0.0f, 1.0f);
                //Animacion para el bt agregar vehiculo
                ObjectAnimator scaleDownX_BtConfirmarRegistro = ObjectAnimator.ofFloat(btConfirmarRegistro, "scaleX", 0.0f, 0.5f);
                ObjectAnimator scaleDownY_BtConfirmarRegistro = ObjectAnimator.ofFloat(btConfirmarRegistro, "scaleY", 0.0f, 0.5f);
                //Configuramos los animatorsets
                animatorSetEscale.play(scaleDownX_ImCoche).with(scaleDownY_ImCoche)
                        .with(scaleDownX_ImColor).with(scaleDownY_ImColor)
                        .with(scaleDownX_TxtMatricula).with(scaleDownY_TxtMatricula)
                        .with(scaleDownX_TxtMarca).with(scaleDownY_TxtMarca)
                        .with(scaleDownX_TxtModelo).with(scaleDownY_TxtModelo)
                        .with(scaleDownX_LayoutSpinnerCombustible).with(scaleDownY_LayoutSpinnerCombustible)
                        .with(scaleDownX_SpinnerCombustible).with(scaleDownY_SpinnerCombustible)
                        .with(scaleDownX_BtSeleccionarColor).with(scaleDownY_BtSeleccionarColor)
                        .with(scaleDownX_BtConfirmarRegistro).with(scaleDownY_BtConfirmarRegistro);
                animatorSetEscale.setDuration(550);
                animatorSetEscale.setInterpolator(new AccelerateDecelerateInterpolator());
                animatorSetEscale.start();
            }
        });
        /*btConfirmarRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uriParaElInsert = Long.toString(System.currentTimeMillis());
                StorageReference filePath = storageReference.child(ID_USUARIO).child(uriParaElInsert);
                //Utiliza la direccion para coger la imagen del dispositivo, sube la imagen a firebase y escucha si se ha realizado de manera adecuada
                filePath.putFile(uriImagenEndispositivo).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //Coje la URL de la imagen de la carpeta que le indiquemos con el nombre que le indiquemos de firebase
                        storageReference.child(ID_USUARIO).child(uriParaElInsert).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                //Meter la URI en un String para posteriormente hacer el update o el insert en la base de datos
                                //INSERTA A LA BASE DE DATOS

                                //Faltan el resto de datos del vehiculo solo es obligatorio
                                // - Numero de plazas
                                // - Matrícula
                                // - Se inserta la uri de la imagen del coche --> uriFotoCoche LINEA 218
                                uri.toString(); //Meterla en la base de datos
                                Toast.makeText(getApplicationContext(), "Vehículo agregado", Toast.LENGTH_SHORT).show();
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
            }
        });*/
    }
    //Inicializa el spinner de vehiculos
    private void inicializacionSpinnerCombustible() {
        // Inicializamos los valores que puede obtener el spinner combustible
        String[] combustibles = new String[]{"Gasolina", "Diesel", "Híbrido", "Electrico"};
        // Inicialimos el adapter y lo asociamos al spinner.
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,R.layout.color_spinner,combustibles);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.color_spinner);
        spinnerTipoCombustible.setAdapter(spinnerArrayAdapter);
    }
    //Inicializa el ColorPiker del color del vehiculo.
    public void colorPiker() {
        ColorPicker colorPicker = new ColorPicker(this);
        colorPicker.setTitle("¿De que color es?");
        colores.clear();
        //Añadimos los colores a nuestra coleccion
        colores.add("#000000");colores.add("#FFFFFF");colores.add("#616161");colores.add("#C8C8C8");colores.add("#7A0000");colores.add("#E70000");colores.add("#011474");
        colores.add("#20D2F6");colores.add("#01742E");colores.add("#00BA27");colores.add("#8E6D3D");colores.add("#F5C886");colores.add("#F17C00");colores.add("#E7E300");
        colores.add("#FFA3F8");
        //Establecemos los colores , definimos los botones redondos y en 5 columnas, y le asociamos un listener a ese color y lo mostramos
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
    //Abre la galaeria de imagenes
    public void abrirGaleria(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,GALERY_INTENT);
    }
    //Coge la direccion de firebase
    public void cargarImagenVehiculo() {
        //Instancia el objeto de tipo storageReference
        storageReference = FirebaseStorage.getInstance().getReference();
        //Coje la URL de la imagen de la carpeta que le indiquemos con el nombre que le indiquemos
        storageReference.child("Fotos"+"EMAILUSUARIO").child(editTextMatricula.getText().toString()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //Si la carga es optima la coloca en IMGUsuarioDatos
                Glide.with(getApplication()).load(uri).into(imgViewCoche);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                //Si la carga no es optima es decir que no existe la direccion proporcinada carga una imagen por defecto
                imgViewCoche.setImageResource(R.drawable.coche);
            }
        });
    }
    //Metodo de la interfaz View.OnClickListener
    @Override
    public void onClick(View v) {
        //Segun el elemento pulsado
        if (v.equals(imgViewCoche)){abrirGaleria();}
        else if (v.equals(btSeleccionarColor)){colorPiker();}
        else if(v.equals(btFlechaAtras)){onBackPressed();}
        else if (v.equals(btConfirmarRegistro)){
            //Variables booleanas
            boolean matricula=true, marca=true, modelo=true, combustible=true, color=true;
            //Antes de hacer la peticion al servidor realizamos las siguientes comprobaciones:
            //Control matricula:
            String auxMatricula=editTextMatricula.getText().toString();
            auxMatricula=auxMatricula.replaceAll("\\s","").toUpperCase();
            if(auxMatricula.length()==7) {
                try {
                    Integer.parseInt(auxMatricula.substring(0, 4));
                    for(int i = 4 ; i < auxMatricula.length() && matricula ; i++ ) {
                        if(auxMatricula.charAt(i)<'A'||auxMatricula.charAt(i)>'Z') {
                            matricula=false;
                        }
                    }
                }catch (NumberFormatException n){
                    matricula=false;
                }
            }else {
                matricula=false;
            }
            if (matricula){
                txtErrorMatricula.setVisibility(View.GONE);
                txtErrorMatricula.setText("Error");
                editTextMatricula.setTextColor(getResources().getColor(R.color.places_ui_default_primary_dark));
            }
            else{
                txtErrorMatricula.setText("Matricula no valida.");
                txtErrorMatricula.setVisibility(View.VISIBLE);
                editTextMatricula.setTextColor(getResources().getColor(R.color.colorErrorsitoEditText));
            }
            //Control de marca, de momento ninguno mas:

            //Control de modelo de momento ninguno mas:

            //Control de combustible de momento ninguno mas:

            //Control de color de momento ninguno mas:

            //Si todas las comprobaciones del front son correctas pasamos a lanzar la solicitud al servidor:
            if(matricula&&marca&&modelo&&color&&combustible){
                //Creamos objeto Retrofit, para lanzar peticiones y poder recibir respuestas
                Retrofit retrofit = new Retrofit.Builder().baseUrl("http://192.168.0.107:8080/").addConverterFactory(GsonConverterFactory.create()).build();
                //Vinculamos el cliente con la interfaz.
                //En esa interfaz se definen los metodos y los verbos que usan
                //Definimos las peticiones que va a poder hacer segun las implementadas en la interfaz que se indica
                JsonPlaceHolderApi peticiones = retrofit.create(JsonPlaceHolderApi.class);
                //Creamos una peticion para registrar un vehiculo con el valor de los editexts y demas:
                String modeloAux=editTextModelo.getText().toString().trim();
                StringBuilder auxModelo= new StringBuilder();
                for(int i=0,contEspacios=0;i<modeloAux.length();i++){
                    if(modeloAux.charAt(i)!=' '){
                        auxModelo.append(modeloAux.charAt(i));
                        contEspacios=0;
                    }else{
                        if(contEspacios==0){
                            auxModelo.append(modeloAux.charAt(i));
                        }
                        contEspacios++;
                    }
                }
                modeloAux= auxModelo.toString();
                String marcaAux=editTextMarca.getText().toString().trim();
                StringBuilder auxMarca= new StringBuilder();
                for(int i=0,contEspacios=0;i<marcaAux.length();i++){
                    if(marcaAux.charAt(i)!=' '){
                        auxMarca.append(marcaAux.charAt(i));
                        contEspacios=0;
                    }else{
                        if(contEspacios==0){
                            auxMarca.append(marcaAux.charAt(i));
                        }
                        contEspacios++;
                    }
                }
                marcaAux= auxMarca.toString();
                Map<String, String> infoMap = new HashMap<String, String>();
                infoMap.put("idUsuario", VentanaLogin.usuarioSesion.getIdusuario().toString());
                infoMap.put("matricula", editTextMatricula.getText().toString().replaceAll("\\s","").toUpperCase());
                infoMap.put("modelo", capitalizaString(modeloAux));
                infoMap.put("marca", capitalizaString(marcaAux));
                infoMap.put("combustible", spinnerTipoCombustible.getSelectedItem().toString());
                infoMap.put("color", colorSeleccionado.toUpperCase());
                Call<Vehiculo> call = peticiones.registrarVehiculo(infoMap);
                //Ejecutamos la petición en un hilo en segundo plano, retrofit lo hace por nosotros
                // y esperamos a la respuesta
                call.enqueue(new Callback<Vehiculo>() {
                    //Gestionamos la respuesta del servidor
                    @Override
                    public void onResponse(Call<Vehiculo> call, Response<Vehiculo> response) {
                        //Respuesta del servidor con un error y paramos el flujo del programa, indicando el codigo de error
                        if (!response.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Code: " + response.code(), Toast.LENGTH_LONG).show();
                            return;
                        }
                        //Si el cambio ha sido exitoso volvemos a la actividad anterior
                        Toast.makeText(getApplicationContext(), "Registro realizado con exito.", Toast.LENGTH_LONG).show();
                        //Refrescar aqui¿? los datos locoooooooo
                        //Con el metodo setResult pasamos el codigo resultado de la operacion, que en este caso es una constante de clase que
                        //significa OK, y el Intent que contiene la informacion del resultado.
                        setResult(RESULT_OK);
                        //Terminamos con la propia actividad con el siguiente metodo.
                        finish();
                    }
                    //En caso de que no responda el servidor mostramos mensaje de error
                    @Override
                    public void onFailure(Call<Vehiculo> call, Throwable t) {
                        Toast.makeText(getApplicationContext(),t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }
    //Metodos para los editext para cuando cambia su contenido
    //Comprobacion de que los campos no esten vacios y tengan un formato correcto antes de poder intentar iniciar sesion:
    @Override
    public void afterTextChanged(Editable s) {
        //Boolean prueba de que el estado anterior era true
        boolean anterior=false;
        if(pruebaFormatoMatricula&&pruebaFormatoMarca&&pruebaFormatoModelo&&pruebaCombustible&&pruebaColor){anterior=true;}
        //Si el texto de matricula ha cambiado:
        if(s==editTextMatricula.getEditableText()){
            String matricula=s.toString().toUpperCase().replaceAll("\\s","");
            if(!matricula.equals("")&&matricula.length()==7){
                pruebaFormatoMatricula=true;
            }else{
                pruebaFormatoMatricula=false;
            }
        }
        //Si el texto de marca ha cambiado:
        else if (s==editTextMarca.getEditableText()){
            //Limpiamos espacios multiples
            String marca=s.toString().trim();
            StringBuilder aux= new StringBuilder();
            for(int i=0,contEspacios=0;i<marca.length();i++){
                if(marca.charAt(i)!=' '){
                    aux.append(marca.charAt(i));
                    contEspacios=0;
                }else{
                    if(contEspacios==0){
                        aux.append(marca.charAt(i));
                    }
                    contEspacios++;
                }
            }
            marca= aux.toString();
            if(!marca.equals("")&&marca.length()>=1&&marca.length()<=30){
                pruebaFormatoMarca=true;
            }else{
                pruebaFormatoMarca=false;
            }
        }
        //Si el texto de modelo ha cambiado:
        else if (s==editTextModelo.getEditableText()){
            //Limpiamos espacios multiples
            String modelo=s.toString().trim();
            StringBuilder aux= new StringBuilder();
            for(int i=0,contEspacios=0;i<modelo.length();i++){
                if(modelo.charAt(i)!=' '){
                    aux.append(modelo.charAt(i));
                    contEspacios=0;
                }else{
                    if(contEspacios==0){
                        aux.append(modelo.charAt(i));
                    }
                    contEspacios++;
                }
            }
            modelo= aux.toString();
            if(!modelo.equals("")&&modelo.length()>=1&&modelo.length()<=30){
                pruebaFormatoModelo=true;
            }else{
                pruebaFormatoModelo=false;
            }
        }
        //Si las 5 pruebas de formato son correctas pasamos a liberar el boton de agregar vehiculo:
        if (pruebaFormatoMatricula&&pruebaFormatoMarca&&pruebaFormatoModelo&&pruebaColor&&pruebaCombustible&&!anterior){
            //Conjunto de animator
            AnimatorSet animator = new AnimatorSet();
            //Animacion para el bt agregar vehiculo
            ObjectAnimator scaleDownX_BtConfirmarRegistro = ObjectAnimator.ofFloat(btConfirmarRegistro, "scaleX", 0.5f, 1.0f);
            ObjectAnimator scaleDownY_BtConfirmarRegistro = ObjectAnimator.ofFloat(btConfirmarRegistro, "scaleY", 0.5f, 1.0f);
            animator.play(scaleDownX_BtConfirmarRegistro).with(scaleDownY_BtConfirmarRegistro);
            animator.setDuration(200);
            animator.start();
            //Habilitamos el boton agregar vehiculo:
            btConfirmarRegistro.setEnabled(true);
            btConfirmarRegistro.setOnClickListener(this);
            btConfirmarRegistro.setColorFilter(getResources().getColor(R.color.colorPrimary));
        }else if ((!pruebaFormatoMatricula||!pruebaFormatoMarca||!pruebaFormatoModelo||!pruebaColor||!pruebaCombustible)&&anterior){
            //Conjunto de animator
            AnimatorSet animator = new AnimatorSet();
            //Animacion para el bt agregar vehiculo
            ObjectAnimator scaleDownX_BtConfirmarRegistro = ObjectAnimator.ofFloat(btConfirmarRegistro, "scaleX", 1.0f, 0.5f);
            ObjectAnimator scaleDownY_BtConfirmarRegistro = ObjectAnimator.ofFloat(btConfirmarRegistro, "scaleY", 1.0f, 0.5f);
            animator.play(scaleDownX_BtConfirmarRegistro).with(scaleDownY_BtConfirmarRegistro);
            animator.setDuration(200);
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
    //Metodos para la interfaz de los spinners
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        spinnerTipoCombustible.getSelectedItem();
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {}
    //Coge la direccion del dispositivo
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALERY_INTENT && resultCode == RESULT_OK) {
            //Coge la Uri del dispositivo
            uriImagenEndispositivo = data.getData();
            //Cambia la imagen desde el dispositivo
            Glide.with(getApplicationContext()).load(uriImagenEndispositivo).into(imgViewCoche);
        }
    }
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
