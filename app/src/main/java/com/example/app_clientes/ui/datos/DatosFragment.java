package com.example.app_clientes.ui.datos;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.app_clientes.jsonplaceholder.JsonPlaceHolderApi;
import com.example.app_clientes.otros.CalendarioFragment;
import com.example.app_clientes.R;
import com.example.app_clientes.pojos.Usuario;
import com.example.app_clientes.vistas.VentanaCambiarContrasena;
import com.example.app_clientes.vistas.VentanaLogin;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.app.Activity.RESULT_OK;
//Clase que contiene toda la logica y conexion con la ventana de mi cuenta de la app:
public class DatosFragment extends Fragment implements View.OnClickListener, TextWatcher {
    //Variables para comprobacion de formatos:
    private boolean pruebaFormatoNumero, pruebaFormatoFecha, pruebaFormatoDescripcion;
    //Atributos de la clase:
    private CircleImageView imgUsuario;
    private EditText editTextNombre, editTextApellidos, editTextUsuario, editTextPrefijo, editTextTelefono, editTextFecha, editTextDescripcion;
    private Button btCambiarContrasena, btActualizarDatos, btBorrarCuenta;
    private TextView txtErrorTelefono, txtErrorFecha, txtErrorDescripcion;
    private StorageReference storageReference;
    private String uriFotoUsuario = "";
    private static final int GALERY_INTENT = 1;
    private String ID_USUARIO;
    //Metodo que se ejecuta al crearse la vista
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Inicializamos variables booleanas de prueba
        pruebaFormatoNumero=false;
        pruebaFormatoFecha=false;
        pruebaFormatoDescripcion=false;
        View view = inflater.inflate(R.layout.fragment_datos, container, false);
        //Asociamos el id del usuario en sesion a la siguiente variable
        ID_USUARIO = VentanaLogin.usuarioSesion.getIdusuario().toString();
        //Vinculamos los atributos de la clase:
        editTextNombre = view.findViewById(R.id.editTextNombreDatosPersonales);editTextNombre.setKeyListener(null);
        editTextApellidos = view.findViewById(R.id.editTextApellidosDatosPersonales);editTextApellidos.setKeyListener(null);
        editTextUsuario = view.findViewById(R.id.editTextUsuarioDatosPersonales);editTextUsuario.setKeyListener(null);
        editTextPrefijo = view.findViewById(R.id.editTextPrefijoDatosPersonales);editTextPrefijo.setKeyListener(null);
        editTextTelefono = view.findViewById(R.id.editTextNumeroDatosPersonales);
        editTextFecha = view.findViewById(R.id.editTextFechaDatosPersonales);
        editTextDescripcion = view.findViewById(R.id.editTextDescripcionDatosPersonales);
        imgUsuario = view.findViewById(R.id.imageViewUsuarioDatosPersonales);
        btCambiarContrasena = view.findViewById(R.id.btCambiarContrasenaDatosPersonales);
        btActualizarDatos = view.findViewById(R.id.btGuardarCambiosDatosPersonales);
        btBorrarCuenta = view.findViewById(R.id.btEliminarCuentaDatosPersonales);
        txtErrorTelefono = view.findViewById(R.id.textViewErrorTelefonoDatosPersonales);
        txtErrorFecha = view.findViewById(R.id.textViewErrorFechaDatosPersonales);
        txtErrorDescripcion = view.findViewById(R.id.textViewErrorDescripcionRegistroUsuario);
        //Carga la imagen y datos personales del usuairo al abrir la ventana
        cargarImagenUsuario();
        cargarDatosPersonalesUsuario();
        //Vinculamos los botones al listener del metodo onclick, que esta implementado en esta clase:
        imgUsuario.setOnClickListener(this);
        btActualizarDatos.setOnClickListener(this);
        btCambiarContrasena.setOnClickListener(this);
        btBorrarCuenta.setOnClickListener(this);
        editTextFecha.setOnClickListener(this);
        //Vinculamos los edittext a su listener para el metodo afterTextChanged, que esta implementado en esta clase:
        editTextTelefono.addTextChangedListener(this);
        editTextDescripcion.addTextChangedListener(this);
        editTextFecha.addTextChangedListener(this);
        //Animaciones
        txtErrorDescripcion.post(new Runnable() {
            @Override
            public void run() {
                //Conjuntos de animators
                AnimatorSet animatorSetEscale = new AnimatorSet();
                //Animacion para la imagen del usuario
                ObjectAnimator scaleDownX_ImUsuario = ObjectAnimator.ofFloat(imgUsuario, "scaleX", 0.0f, 1.0f);
                ObjectAnimator scaleDownY_ImUsuario = ObjectAnimator.ofFloat(imgUsuario, "scaleY", 0.0f, 1.0f);
                //Animacion para el editext Nombre
                ObjectAnimator scaleDownX_TxtNombre = ObjectAnimator.ofFloat(editTextNombre, "scaleX", 0.0f, 1.0f);
                ObjectAnimator scaleDownY_TxtNombre = ObjectAnimator.ofFloat(editTextNombre, "scaleY", 0.0f, 1.0f);
                //Animacion para el editext Apellidos
                ObjectAnimator scaleDownX_TxtApellidos = ObjectAnimator.ofFloat(editTextApellidos, "scaleX", 0.0f, 1.0f);
                ObjectAnimator scaleDownY_TxtApellidos = ObjectAnimator.ofFloat(editTextApellidos, "scaleY", 0.0f, 1.0f);
                //Animacion para el edittext Usuario
                ObjectAnimator scaleDownX_TxtUsuario = ObjectAnimator.ofFloat(editTextUsuario, "scaleX", 0.0f, 1.0f);
                ObjectAnimator scaleDownY_TxtUsuario = ObjectAnimator.ofFloat(editTextUsuario, "scaleY", 0.0f, 1.0f);
                //Animacion para el edittext prefijo
                ObjectAnimator scaleDownX_TxtPrefijo = ObjectAnimator.ofFloat(editTextPrefijo, "scaleX", 0.0f, 1.0f);
                ObjectAnimator scaleDownY_TxtPrefijo = ObjectAnimator.ofFloat(editTextPrefijo, "scaleY", 0.0f, 1.0f);
                //Animacion para el edittext numero
                ObjectAnimator scaleDownX_TxtNumero = ObjectAnimator.ofFloat(editTextTelefono, "scaleX", 0.0f, 1.0f);
                ObjectAnimator scaleDownY_TxtNumero = ObjectAnimator.ofFloat(editTextTelefono, "scaleY", 0.0f, 1.0f);
                //Animacion para el edittext fecha
                ObjectAnimator scaleDownX_TxtFecha = ObjectAnimator.ofFloat(editTextFecha, "scaleX", 0.0f, 1.0f);
                ObjectAnimator scaleDownY_TxtFecha = ObjectAnimator.ofFloat(editTextFecha, "scaleY", 0.0f, 1.0f);
                //Animacion para el edittext descripcion
                ObjectAnimator scaleDownX_TxtDescripcion = ObjectAnimator.ofFloat(editTextDescripcion, "scaleX", 0.0f, 1.0f);
                ObjectAnimator scaleDownY_TxtDescripcion = ObjectAnimator.ofFloat(editTextDescripcion, "scaleY", 0.0f, 1.0f);
                //Animacion para el bt actualizar cambios
                ObjectAnimator scaleDownX_BtActualizarCambios = ObjectAnimator.ofFloat(btActualizarDatos, "scaleX", 0.0f, 0.95f);
                ObjectAnimator scaleDownY_BtActualizarCambios = ObjectAnimator.ofFloat(btActualizarDatos, "scaleY", 0.0f, 0.95f);
                //Animacion para el bt cambiar contrasena
                ObjectAnimator scaleDownX_BtCambiarContrasena = ObjectAnimator.ofFloat(btCambiarContrasena, "scaleX", 0.0f, 1.0f);
                ObjectAnimator scaleDownY_BtCambiarContrasena = ObjectAnimator.ofFloat(btCambiarContrasena, "scaleY", 0.0f, 1.0f);
                //Animacion para el bt borrar cuenta
                ObjectAnimator scaleDownX_BtBorrarCuenta = ObjectAnimator.ofFloat(btBorrarCuenta, "scaleX", 0.0f, 1.0f);
                ObjectAnimator scaleDownY_BtBorrarCuenta = ObjectAnimator.ofFloat(btBorrarCuenta, "scaleY", 0.0f, 1.0f);
                //Configuramos los animatorsets
                animatorSetEscale.play(scaleDownX_ImUsuario).with(scaleDownY_ImUsuario)
                        .with(scaleDownX_TxtNombre).with(scaleDownY_TxtNombre)
                        .with(scaleDownX_TxtApellidos).with(scaleDownY_TxtApellidos)
                        .with(scaleDownX_TxtUsuario).with(scaleDownY_TxtUsuario)
                        .with(scaleDownX_TxtPrefijo).with(scaleDownY_TxtPrefijo)
                        .with(scaleDownX_TxtNumero).with(scaleDownY_TxtNumero)
                        .with(scaleDownX_TxtFecha).with(scaleDownY_TxtFecha)
                        .with(scaleDownX_TxtDescripcion).with(scaleDownY_TxtDescripcion)
                        .with(scaleDownX_BtActualizarCambios).with(scaleDownY_BtActualizarCambios)
                        .with(scaleDownX_BtCambiarContrasena).with(scaleDownY_BtCambiarContrasena)
                        .with(scaleDownX_BtBorrarCuenta).with(scaleDownY_BtBorrarCuenta);
                animatorSetEscale.setDuration(550);
                animatorSetEscale.setInterpolator(new AccelerateDecelerateInterpolator());
                animatorSetEscale.start();
            }
        });
        return view;
    }
    //Metodo para cargar la imagen del usuario
    public void cargarImagenUsuario(){
        //Inatancia el objeto de tipo storageReference
        storageReference = FirebaseStorage.getInstance().getReference();
        //Coje la URL de la imagen de la carpeta que le indiquemos con el nombre que le indiquemos
        storageReference.child("Fotos").child(ID_USUARIO).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //Si la carga es optima la coloca en IMGUsuarioDatos
                Glide.with(getActivity()).load(uri).into(imgUsuario);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                //Si la carga no es optima es decir que no existe la direccion proporcinada carga una imagen por defecto
                imgUsuario.setImageResource(R.drawable.user);
            }
        });
    }
    //Metodo que carga los datos personales del usuario
    private void cargarDatosPersonalesUsuario(){
        //Creamos objeto Retrofit, para lanzar peticiones y poder recibir respuestas
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://192.168.0.107:8080/").addConverterFactory(GsonConverterFactory.create()).build();
        //Vinculamos el cliente con la interfaz.
        //En esa interfaz se definen los metodos y los verbos que usan
        //Definimos las peticiones que va a poder hacer segun las implementadas en la interfaz que se indica
        JsonPlaceHolderApi peticiones = retrofit.create(JsonPlaceHolderApi.class);
        //Creamos una peticion para buscar un usuario por id
        Call<Usuario> call = peticiones.getUsuarioById(VentanaLogin.usuarioSesion.getIdusuario());
        //Ejecutamos la petición en un hilo en segundo plano, retrofit lo hace por nosotros
        // y esperamos a la respuesta
        call.enqueue(new Callback<Usuario>() {
            //Gestionamos la respuesta del servidor
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                //Respuesta del servidor con un error y paramos el flujo del programa, indicando el codigo de error
                if (!response.isSuccessful()) {
                    Toast.makeText(getContext(),"Code: "+response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                //Recogemos el usuario
                Usuario u= response.body();
                //Actualizamos la variable de sesion
                VentanaLogin.usuarioSesion=u;
                VentanaLogin.usuarioSesion.setClave("");
                //Cargamos los datos en la vista:
                editTextNombre.setText(u.getNombre());
                editTextApellidos.setText(u.getApellidos());
                editTextUsuario.setText(u.getEmail());
                if(u.getTelefono()!=null){
                    editTextTelefono.setText(u.getTelefono().toString());}
                if(u.getFechanacimiento()!=null){
                    editTextFecha.setText(new SimpleDateFormat("dd / MM / yyyy").format(u.getFechanacimiento()));}
                editTextDescripcion.setText(u.getDescripcion());
            }
            //En caso de que no responda el servidor mostramos mensaje de error
            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Toast.makeText(getContext(),t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    //Metodo que nos abre la galeria
    public void abrirGaleria(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,GALERY_INTENT);
    }
    //Muestra un cuadro de dialogo con un calendario al pulsar sobre el EditextFecha, Este nos permite capturar la fecha
    private void showDatePickerDialog() {
        CalendarioFragment newFragment = CalendarioFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 porque el mes de enero es 0
                final String selectedDate = year + "-" + (month+1) + "-" + day;
                Date hey_dame_la_fecha_como_yo_quiero = Date.valueOf(selectedDate);
                editTextFecha.setText(new SimpleDateFormat("dd / MM / yyyy").format(hey_dame_la_fecha_como_yo_quiero)+"");
            }
        });
        newFragment.show(getChildFragmentManager(), "datePicker");
    }
    //Metodo de la interfaz View.OnClickListener
    @Override
    public void onClick(View v) {
        //Segun el elemento pulsado
        if (v.equals(imgUsuario)){abrirGaleria();}
        else if (v.equals(editTextFecha)){showDatePickerDialog();}
        else if (v.equals(btCambiarContrasena)){
            VentanaCambiarContrasena ventanaCambiarContrasena = new VentanaCambiarContrasena();
            Intent VentanaCambiarContrasena = new Intent(getContext(), ventanaCambiarContrasena.getClass());
            startActivity(VentanaCambiarContrasena);
        }
        else if (v.equals(btBorrarCuenta)){
            //Creamos objeto Retrofit, para lanzar peticiones y poder recibir respuestas
            Retrofit retrofit = new Retrofit.Builder().baseUrl("http://192.168.0.107:8080/").addConverterFactory(GsonConverterFactory.create()).build();
            //Vinculamos el cliente con la interfaz.
            //En esa interfaz se definen los metodos y los verbos que usan
            //Definimos las peticiones que va a poder hacer segun las implementadas en la interfaz que se indica
            JsonPlaceHolderApi peticiones = retrofit.create(JsonPlaceHolderApi.class);
            //Creamos una peticion para dar de baja al usuario por id
            //Creamos un Map para pasarle valores por el cuerpo a la siguiente peticion
            Call<Void> call = peticiones.eliminarUsuarioById(VentanaLogin.usuarioSesion.getIdusuario());
            //Ejecutamos la petición en un hilo en segundo plano, retrofit lo hace por nosotros
            // y esperamos a la respuesta
            call.enqueue(new Callback<Void>() {
                //Gestionamos la respuesta del servidor
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    //Respuesta del servidor con un error y paramos el flujo del programa, indicando el codigo de error
                    if (!response.isSuccessful()) {
                        Toast.makeText(getActivity(),"Code: "+response.code(), Toast.LENGTH_LONG).show();
                        return;
                    }
                    //Si la baja ha sido exitosa
                    VentanaLogin.usuarioSesion=null;
                    Toast.makeText(getActivity(),"Baja realizada con exito.", Toast.LENGTH_LONG).show();
                    //Instanciamos nuestro objeto Intent explicito, ya que en los parametros ponemos que empieza en esta
                    //clase que sera el contexto y que iniciara la clase que se encarga de la otra actividad en este caso.
                    Intent intentInicioSesion = new Intent(getActivity(), VentanaLogin.class);
                    //Iniciamos la nueva actividad:
                    startActivity(intentInicioSesion);
                    getActivity().finish();
                }
                //En caso de que no responda el servidor mostramos mensaje de error
                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(getActivity(),t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
        else if (v.equals(btActualizarDatos)){
            //Variables booleanas
            boolean telefono=true, fecha=true, descripcion=true;
            //Control telefono de que sea no este vacio, sea un numero y tenga 9 caracteres
            if (editTextTelefono.getText().toString().length()==9){
                try {
                    Integer.parseInt(editTextTelefono.getText().toString());
                }catch (NumberFormatException n){
                    telefono=false;
                    txtErrorTelefono.setText("Telefono con caracteres no numericos.");
                    txtErrorTelefono.setVisibility(View.VISIBLE);
                    editTextTelefono.setTextColor(getResources().getColor(R.color.colorErrorsitoEditText));
                }
            }else if(!editTextTelefono.getText().toString().equals("")){
                telefono=false;
                txtErrorTelefono.setText("Telefono necesita 9 digitos.");
                txtErrorTelefono.setVisibility(View.VISIBLE);
                editTextTelefono.setTextColor(getResources().getColor(R.color.colorErrorsitoEditText));
            }
            if(telefono){
                txtErrorTelefono.setVisibility(View.GONE);
                txtErrorTelefono.setText("Error");
                editTextTelefono.setTextColor(getResources().getColor(R.color.places_ui_default_primary_dark));
            }
            //Control descripcion, si no es cadena vacia, quitamos espacios traseros y delanteros
            if (!editTextDescripcion.getText().toString().equals("")){
                editTextDescripcion.setText(editTextDescripcion.getText().toString().trim());
                if(editTextDescripcion.length()>300){
                    descripcion=false;
                    txtErrorDescripcion.setVisibility(View.VISIBLE);
                    txtErrorDescripcion.setText("Excedido limite de 300 caracteres.");
                    editTextDescripcion.setTextColor(getResources().getColor(R.color.places_ui_default_primary_dark));
                }
            }
            if (descripcion){
                txtErrorDescripcion.setVisibility(View.GONE);
                txtErrorDescripcion.setText("Error");
                editTextDescripcion.setTextColor(getResources().getColor(R.color.places_ui_default_primary_dark));
            }
            //Control de fecha validamos que si hay fecha sea menor que hoy, obtenemos los valores por separado con el siguiente algoritmo:
            //Variables necesarias:
            String fechaElegida="";
            if(!editTextFecha.getText().toString().equals("")){
                int diaI=0, mesI=0, anoI=0;
                String aux;
                //Recogemos el valor de la fecha elegida en el DataPicker
                aux = editTextFecha.getText().toString();
                //Bucle for que se encarga de coger los valores numericos de la fecha
                //y guardarlo en variables de tipo Int:
                for (int i=0, j=0;i<aux.length();i++){
                    if(aux.charAt(i)>='0'&&aux.charAt(i)<='9'){
                        fechaElegida+=aux.charAt(i);
                    }
                    else if(aux.charAt(i)=='/'&&j==0){
                        diaI += Integer.parseInt(fechaElegida);
                        j++;
                        fechaElegida="";
                    }
                    else if(aux.charAt(i)=='/'&&j==1){
                        mesI += Integer.parseInt(fechaElegida);
                        j++;
                        fechaElegida="";
                    }
                    if(i==aux.length()-1&&j==2){
                        anoI += Integer.parseInt(fechaElegida);
                        j++;
                        fechaElegida="";
                    }
                }
                fechaElegida=anoI+"-"+mesI+"-"+diaI;
                Date fechaFinal = Date.valueOf(fechaElegida);
                Date fechaActual = new Date(Calendar.getInstance().getTime().getTime());
                if (fechaFinal.compareTo(fechaActual) > 0 || fechaFinal.equals(fechaActual)) {
                    fecha=false;
                    txtErrorFecha.setVisibility(View.VISIBLE);
                    txtErrorFecha.setText("Fecha invalida.");
                    editTextFecha.setTextColor(getResources().getColor(R.color.places_ui_default_primary_dark));
                }
            }
            if (fecha){
                txtErrorFecha.setVisibility(View.GONE);
                txtErrorFecha.setText("Error");
                editTextFecha.setTextColor(getResources().getColor(R.color.places_ui_default_primary_dark));
            }
            //Si todas las comprobaciones del front son correctas pasamos a lanzar la solicitud al servidor:
            if(telefono&&fecha&&descripcion){
                //Creamos objeto Retrofit, para lanzar peticiones y poder recibir respuestas
                Retrofit retrofit = new Retrofit.Builder().baseUrl("http://192.168.0.107:8080/").addConverterFactory(GsonConverterFactory.create()).build();
                //Vinculamos el cliente con la interfaz.
                //En esa interfaz se definen los metodos y los verbos que usan
                //Definimos las peticiones que va a poder hacer segun las implementadas en la interfaz que se indica
                JsonPlaceHolderApi peticiones = retrofit.create(JsonPlaceHolderApi.class);
                //Creamos una peticion para actualizar los datos personales de un usuario, que creamos con los valores de los editext:
                Map<String, String> infoMap = new HashMap<String, String>();
                infoMap.put("idUsuario", VentanaLogin.usuarioSesion.getIdusuario().toString());
                infoMap.put("telefono", editTextTelefono.getText().toString());
                infoMap.put("fechaNac", fechaElegida);
                infoMap.put("descripcion", editTextDescripcion.getText().toString().trim());
                Call<Usuario> call = peticiones.actualizarDatosPersonalesUsuario(infoMap);
                //Ejecutamos la petición en un hilo en segundo plano, retrofit lo hace por nosotros
                // y esperamos a la respuesta
                call.enqueue(new Callback<Usuario>() {
                    //Gestionamos la respuesta del servidor
                    @Override
                    public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                        //Respuesta del servidor con un error y paramos el flujo del programa, indicando el codigo de error
                        if (!response.isSuccessful()) {
                            Toast.makeText(getContext(), "Code: " + response.code(), Toast.LENGTH_LONG).show();
                            return;
                        }
                        //Si el cambio ha sido exitoso volvemos a la actividad anterior
                        Toast.makeText(getContext(), "Datos actualizados con exito.", Toast.LENGTH_LONG).show();
                        cargarDatosPersonalesUsuario();
                    }
                    //En caso de que no responda el servidor mostramos mensaje de error
                    @Override
                    public void onFailure(Call<Usuario> call, Throwable t) {
                        Toast.makeText(getContext(),t.getMessage(), Toast.LENGTH_LONG).show();
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
        if(pruebaFormatoNumero||pruebaFormatoFecha||pruebaFormatoDescripcion){anterior=true;}
        //Si el texto de telefono ha cambiado:
        if(s==editTextTelefono.getEditableText()){
            if(VentanaLogin.usuarioSesion.getTelefono()!=null&&VentanaLogin.usuarioSesion.getTelefono().toString().equals(s.toString())){
                pruebaFormatoNumero=false;
            }else if(!s.toString().equals("")){
                pruebaFormatoNumero=true;
            }else{
                pruebaFormatoNumero=false;
            }
        }
        //Si el texto de fecha ha cambiado:
        else if (s==editTextFecha.getEditableText()){
            if(VentanaLogin.usuarioSesion.getFechanacimiento()!=null&&s.toString().equals(new SimpleDateFormat("dd / MM / yyyy").format(VentanaLogin.usuarioSesion.getFechanacimiento()))){
                pruebaFormatoFecha=false;
            }else if(!s.toString().equals("")){
                pruebaFormatoFecha=true;
            }else{
                pruebaFormatoFecha=false;
            }
        }
        //Si el texto de descripcion ha cambiado:
        else if (s==editTextDescripcion.getEditableText()){
            if(VentanaLogin.usuarioSesion.getDescripcion()!=null&&VentanaLogin.usuarioSesion.getDescripcion().equals(s.toString().trim())){
                pruebaFormatoDescripcion=false;
            }else if (!s.toString().trim().equals("")){
                pruebaFormatoDescripcion=true;
            }else{
                pruebaFormatoDescripcion=false;
            }
        }
        //Si una de las 3 pruebas es verdadera, es decir al menos un campo ha cambiado y no estaba liberado el boton pasamos a liberar el boton de registrar usuario:
        if ((pruebaFormatoNumero||pruebaFormatoFecha||pruebaFormatoDescripcion)&&!anterior){
            //Conjunto de animator
            AnimatorSet animator = new AnimatorSet();
            //Animacion para el bt actualizar cambios
            ObjectAnimator scaleDownX_BtActualizarCambios = ObjectAnimator.ofFloat(btActualizarDatos, "scaleX", 0.95f, 1.0f);
            ObjectAnimator scaleDownY_BtActualizarCambios = ObjectAnimator.ofFloat(btActualizarDatos, "scaleY", 0.95f, 1.0f);
            animator.play(scaleDownX_BtActualizarCambios).with(scaleDownY_BtActualizarCambios);
            animator.setDuration(200);
            animator.start();
            //Habilitamos el boton  actualizar cambios
            btActualizarDatos.setEnabled(true);
            btActualizarDatos.setTextColor(getResources().getColor(R.color.colorVerdesito));
        }
        //Sino ninguna prueba es verdadera, es decir ningun campo ha cambiado, y el boton estaba activado pues se desactiva:
        else if (!pruebaFormatoNumero&&!pruebaFormatoFecha&&!pruebaFormatoDescripcion&&anterior){
            //Conjunto de animator
            AnimatorSet animator = new AnimatorSet();
            //Animacion para el bt actualizar cambios
            ObjectAnimator scaleDownX_BtActualizarCambios = ObjectAnimator.ofFloat(btActualizarDatos, "scaleX", 1.0f, 0.95f);
            ObjectAnimator scaleDownY_BtActualizarCambios = ObjectAnimator.ofFloat(btActualizarDatos, "scaleY", 1.0f, 0.95f);
            animator.play(scaleDownX_BtActualizarCambios).with(scaleDownY_BtActualizarCambios);
            animator.setDuration(200);
            animator.start();
            //Deshabilitamos el boton  actualizar cambios
            btActualizarDatos.setEnabled(false);
            btActualizarDatos.setTextColor(getResources().getColor(R.color.colorGris));
        }
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}
    //Coge la direccion del dispositivo
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALERY_INTENT && resultCode == RESULT_OK) {
            //Coge la Uri del dispositivo
            Uri uri = data.getData();
            //Cambia la imagen desde el dispositivo
            Glide.with(getContext()).load(uri).into(imgUsuario);
            //Crea una direccion para poder subir la imagen a firebase
            StorageReference filePath = storageReference.child("Fotos").child(ID_USUARIO);
            //Utiliza la direccion para coger la imagen del dispositivo, sube la imagen a firebase y escucha si se ha realizado de manera adecuada
            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getContext(), "Imagen cambiada", Toast.LENGTH_SHORT).show();
                    //Coje la URL de la imagen de la carpeta que le indiquemos con el nombre que le indiquemos de firebase
                    storageReference.child("Fotos").child(ID_USUARIO).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            //Meter la URI en un String para posteriormente hacer el update o el insert en la base de datos
                            uriFotoUsuario = uri.toString();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(getContext(), "No se ha podido realizar el insert en la base de datos", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

        }
    }
    //Hacer si nos queda tiempo cambiar pais de origen
    /*private void showRadioButtonDialog() {
        // custom dialog
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_paises);

        List<String> stringList=new ArrayList<>();  // here is list
        for(int i=0;i<2;i++) {
            if (i==0){
                stringList.add("Number Mode");
            }else {
                stringList.add("Character Mode");
            }
        }
        RadioGroup rg = (RadioGroup) dialog.findViewById(R.id.radio_group);
        for(int i=0;i<stringList.size();i++){
            RadioButton rb=new RadioButton(getContext()); // dynamically creating RadioButton and adding to RadioGroup.
            rb.setText(stringList.get(i));
            rg.addView(rb);
        }
        dialog.show();
    }*/
}
