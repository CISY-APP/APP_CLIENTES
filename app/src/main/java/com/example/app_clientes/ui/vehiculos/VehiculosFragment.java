package com.example.app_clientes.ui.vehiculos;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.app_clientes.adapter.MiAdapterMisVehiculos;
import com.example.app_clientes.item.ItemVehiculo;
import com.example.app_clientes.R;
import com.example.app_clientes.jsonplaceholder.JsonPlaceHolderApi;
import com.example.app_clientes.pojos.Vehiculo;
import com.example.app_clientes.vistas.VentanaAgregarVehiculo;
import com.example.app_clientes.vistas.VentanaLogin;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import petrov.kristiyan.colorpicker.ColorPicker;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.app.Activity.RESULT_OK;
//Clase que contiene toda la logica y conexion con la ventana de registrar vehiculo de la app:
public class VehiculosFragment extends Fragment implements View.OnClickListener, TextWatcher, AdapterView.OnItemSelectedListener, ColorPicker.OnChooseColorListener{
    //Variables para comprobacion de formatos:
    private boolean pruebaFormatoMarca, pruebaFormatoModelo, pruebaCombustible, pruebaColor;
    //Coche sesion
    Vehiculo vSesion ;
    //Atributos de la clase:
    private MiAdapterMisVehiculos miAdapterMisVehiculos;
    private RecyclerView recyclerViewMisVehiculos;
    private ArrayList<ItemVehiculo> misVehiculosList = new ArrayList<>();
    private final ArrayList<String> colores = new ArrayList<>();
    //Elementos del Layout
    private LottieAnimationView me_siento_vasio;
    private TextView textViewMe_siento_vasio;
    private LinearLayout linearLayoutSpinnerCombustible;
    private EditText editTextMatricula, editTextMarca, editTextModelo;
    private TextView txtErrorMatricula, txtErrorMarca, txtErrorModelo;
    private TextView tituloMatricula, tituloMarca, tituloModelo, tituloCombustible, tipoColor;
    private Spinner spinnerTipoCombustible;
    private EditText btSeleccionarColor;
    private CircleImageView imgViewCoche,imgViewColorCoche;
    private ImageView btBorrarVehiculo, btActualizarDatos, imageViewEditar;
    private String colorSeleccionado;
    private StorageReference storageReference;
    private Uri uriImagenEndispositivo;
    private static final int GALERY_INTENT = 1;
    private String ID_USUARIO;
    //Metodo que se ejecuta al crearse la vista
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vehiculo, container, false);
        //Inicializamos variables booleanas de prueba
        pruebaFormatoMarca=false;
        pruebaFormatoModelo=false;
        pruebaCombustible=true;
        pruebaColor=true;
        //Asociamos el id del usuario en sesion a la siguiente variable
        ID_USUARIO = VentanaLogin.usuarioSesion.getIdusuario().toString();
        colorSeleccionado="#07a0c3";
        //Vinculamos los atributos de la clase:
        textViewMe_siento_vasio =view.findViewById(R.id.textViewTituloAnimacionMisVehiculos);
        me_siento_vasio = view.findViewById(R.id.animation_vacio_vehiculos);
        tituloMatricula = view.findViewById(R.id.textViewTituloMatriculaMisVehiculos);
        tituloMarca = view.findViewById(R.id.textViewTituloMarcaMisVehiculos);
        tituloModelo = view.findViewById(R.id.textViewTituloModeloMisVehiculos);
        tituloCombustible = view.findViewById(R.id.textViewTituloTipoCombustibleMisVehiculos);
        tipoColor = view.findViewById(R.id.textViewTituloColorMisVehiculos);
        recyclerViewMisVehiculos = view.findViewById(R.id.RVVehiculosEncontrados); //Vinculamos el recyclerview del xml con el de la clase main
        linearLayoutSpinnerCombustible=view.findViewById(R.id.linearLayoutSpinnerTipoCombustibleMisVehiculos);
        editTextMatricula = view.findViewById(R.id.editTextMatriculaMisVehiculos);editTextMatricula.setKeyListener(null);
        editTextMarca = view.findViewById(R.id.editTextMarcaMisVehiculos);
        editTextModelo = view.findViewById(R.id.editTextModeloMisVehiculos);
        spinnerTipoCombustible = view.findViewById(R.id.spinnerTipoCombustibleMisVehiculos);
        imgViewCoche = view.findViewById(R.id.imageViewCocheMisVehiculos);
        imgViewColorCoche = view.findViewById(R.id.imgLateralSeleccionarColorMisVehiculos);
        imageViewEditar = view.findViewById(R.id.imageViewEditarMisVehiculos);
        btSeleccionarColor = view.findViewById(R.id.btSeleccionarColorCocheMisVehiculos);
        btBorrarVehiculo = view.findViewById(R.id.btBorrarVehiculosMisVehiculos);
        btActualizarDatos = view.findViewById(R.id.btAceptarCambiosMisVehiculos);btActualizarDatos.setOnClickListener(null);
        txtErrorMatricula = view.findViewById(R.id.textViewErrorMatriculaMisVehiculos);
        txtErrorMarca = view.findViewById(R.id.textViewErrorMarcaMisVehiculos);
        txtErrorModelo = view.findViewById(R.id.textViewErrorModeloMisVehiculos);
        //Vinculamos los botones al listener del metodo onclick, que esta implementado en esta clase:
        imgViewCoche.setOnClickListener(this);
        btSeleccionarColor.setOnClickListener(this);
        btBorrarVehiculo.setOnClickListener(this);
        //Vinculamos el spiner al listener correspondiente:
        inicializacionSpinnerCombustible();
        spinnerTipoCombustible.setOnItemSelectedListener(this);
        //Vinculamos los edittext a su listener para el metodo afterTextChanged, que esta implementado en esta clase:
        editTextMatricula.addTextChangedListener(this);
        editTextMarca.addTextChangedListener(this);
        editTextModelo.addTextChangedListener(this);
        //Instancia el objeto de tipo storageReference
        storageReference = FirebaseStorage.getInstance().getReference();
        //RECYCLERRRRRRRR V I E W
        //Agrega los coches al Array de vehiculo y configurar recycler view
        agregarCoches();
        return view;
    }
    //Metodo que carga los datos del coche en la interfaz
    public void cargarDatosCoche(final int pos){
        //Creamos objeto Retrofit, para lanzar peticiones y poder recibir respuestas
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://192.168.0.107:8080/").addConverterFactory(GsonConverterFactory.create()).build();
        //Vinculamos el cliente con la interfaz.
        //En esa interfaz se definen los metodos y los verbos que usan
        //Definimos las peticiones que va a poder hacer segun las implementadas en la interfaz que se indica
        final JsonPlaceHolderApi peticiones = retrofit.create(JsonPlaceHolderApi.class);
        //Creamos una peticion para obtener el vehiculo asociado a la matricula del primer vehiculo en la lista:
        Call<Vehiculo> call = peticiones.getVehiculoByMatricula(misVehiculosList.get(pos).getMatricula());
        //Ejecutamos la petición en un hilo en segundo plano, retrofit lo hace por nosotros
        // y esperamos a la respuesta
        call.enqueue(new Callback<Vehiculo>() {
            //Gestionamos la respuesta del servidor
            @Override
            public void onResponse(Call<Vehiculo> call, Response<Vehiculo> response) {
                //Respuesta del servidor con un error y paramos el flujo del programa, indicando el codigo de error
                if (!response.isSuccessful()) {
                    Toast.makeText(getContext(), "Code: " + response.code(), Toast.LENGTH_LONG).show();
                    return;
                }
                if(misVehiculosList.get(pos).getmImageVehiculo()!=-1){
                    imgViewCoche.setImageResource(misVehiculosList.get(pos).getmImageVehiculo());
                }else{
                    imgViewCoche.setImageResource(R.drawable.coche);
                }
                //Reinicio de variables booleanas
                pruebaFormatoMarca =false;
                pruebaFormatoModelo=false;
                pruebaCombustible=false;
                pruebaColor=false;
                //Lo guardamos en vehiculosesion
                vSesion=response.body();
                editTextMatricula.setText(vSesion.getMatricula());
                editTextMarca.setText(vSesion.getMarca());
                editTextModelo.setText(vSesion.getModelo());
                int posicionSpinner=0;
                if(vSesion.getCombustible().equalsIgnoreCase("Gasolina")){posicionSpinner=0;}
                else if(vSesion.getCombustible().equalsIgnoreCase("Diesel")){posicionSpinner=1;}
                else if(vSesion.getCombustible().equalsIgnoreCase("Híbrido")){posicionSpinner=2;}
                else if(vSesion.getCombustible().equalsIgnoreCase("Electrico")){posicionSpinner=3;}
                spinnerTipoCombustible.setSelection(posicionSpinner);
                imgViewColorCoche.setBackgroundColor(Color.parseColor(vSesion.getColor()));
            }
            //En caso de que no responda el servidor mostramos mensaje de error
            @Override
            public void onFailure(Call<Vehiculo> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    //Inicializa el spinner de vehiculos
    private void inicializacionSpinnerCombustible() {
        // Inicializamos los valores que puede obtener el spinner combustible
        String[] combustibles = new String[]{"Gasolina", "Diesel", "Híbrido", "Electrico"};
        // Inicialimos el adapter y lo asociamos al spinner.
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(),R.layout.color_spinner,combustibles);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.color_spinner);
        spinnerTipoCombustible.setAdapter(spinnerArrayAdapter);
    }
    //Inicializa el ColorPiker del color del vehiculo.
    public void colorPiker() {
        ColorPicker colorPicker = new ColorPicker(getActivity());
        colorPicker.setTitle("¿De que color es?");
        colores.clear();
        //Añadimos los colores a nuestra coleccion
        colores.add("#000000");colores.add("#FFFFFF");colores.add("#616161");colores.add("#C8C8C8");colores.add("#7A0000");colores.add("#E70000");colores.add("#011474");
        colores.add("#20D2F6");colores.add("#01742E");colores.add("#00BA27");colores.add("#8E6D3D");colores.add("#F5C886");colores.add("#F17C00");colores.add("#E7E300");
        colores.add("#FFA3F8");
        //Establecemos los colores , definimos los botones redondos y en 5 columnas, y le asociamos un listener a ese color y lo mostramos
        colorPicker.setColors(colores);
        colorPicker.setColumns(5);
        colorPicker.setRoundColorButton(true);
        colorPicker.setOnChooseColorListener(this);
        colorPicker.show();
    }
    //Abre la galeria de imagenes
    public void abrirGaleria(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,GALERY_INTENT);
    }
    //Rellena el arrayList de vehiculo
    private void agregarCoches() {
        //Creamos objeto Retrofit, para lanzar peticiones y poder recibir respuestas
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://192.168.0.107:8080/").addConverterFactory(GsonConverterFactory.create()).build();
        //Vinculamos el cliente con la interfaz.
        //En esa interfaz se definen los metodos y los verbos que usan
        //Definimos las peticiones que va a poder hacer segun las implementadas en la interfaz que se indica
        final JsonPlaceHolderApi peticiones = retrofit.create(JsonPlaceHolderApi.class);
        //Creamos una peticion para obtener una lista de los vehiculos asociados al cliente:
        Call<List<Vehiculo>> call = peticiones.getListVehiculoById(VentanaLogin.usuarioSesion.getIdusuario());
        //Ejecutamos la petición en un hilo en segundo plano, retrofit lo hace por nosotros
        // y esperamos a la respuesta
        call.enqueue(new Callback<List<Vehiculo>>() {
            //Gestionamos la respuesta del servidor
            @Override
            public void onResponse(Call<List<Vehiculo>> call, Response<List<Vehiculo>> response) {
                //Respuesta del servidor con un error y paramos el flujo del programa, indicando el codigo de error
                if (!response.isSuccessful()) {
                    Toast.makeText(getContext(), "Code: " + response.code(), Toast.LENGTH_LONG).show();
                    return;
                }
                misVehiculosList.clear();
                //Recogemos la lista y la guardamos para el modelo del recycler view
                List<Vehiculo> listObtenida = response.body();
                for (Vehiculo vl : listObtenida) {
                    misVehiculosList.add(new ItemVehiculo(vl.getMarca()+" - "+vl.getModelo(), vl.getMatricula(), R.drawable.seatmio));
                }
                misVehiculosList.add(new ItemVehiculo("Añadir", "botonAñadir", R.drawable.anadir));
                //Asociamos los atributos con los objeto del layout para poder usarlos
                //INSTANCIAMOS Y ASOCIAMOS ELEMENTOS NECESARIOS PARA EL CORRECTO FUNCIONAMIENTO DEL RECYCLERVIEW
                recyclerViewMisVehiculos.setHasFixedSize(true);// RecyclerView sabe de antemano que su tamaño no depende del contenido del adaptador, entonces omitirá la comprobación de si su tamaño debería cambiar cada vez que se agregue o elimine un elemento del adaptador.(mejora el rendimiento)
                recyclerViewMisVehiculos.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                miAdapterMisVehiculos = new MiAdapterMisVehiculos(misVehiculosList);//Instanciamos un objeto de tipo Example_Adapter
                recyclerViewMisVehiculos.setAdapter(miAdapterMisVehiculos);//Vinculamos el adapter al recyclerView
                miAdapterMisVehiculos.setOnClickListener(new MiAdapterMisVehiculos.OnItemClickListener() {
                    @Override
                    public void OnVehiculoClick(int position) {
                        if(position == misVehiculosList.size()-1){
                            VentanaAgregarVehiculo ventanaAgregarVehiculo = new VentanaAgregarVehiculo();
                            Intent VentanaAgregarVehiculo = new Intent(getContext(), ventanaAgregarVehiculo.getClass());
                            //Iniciamos la nueva actividad que devolvera un resultado con el siguiente metodo, pasando nuestro intent creado
                            // y ademas ponemos un codigo para identificar la respuesta.
                            startActivityForResult(VentanaAgregarVehiculo,1997);
                        }else{
                            cargarDatosCoche(position);
                            visibilidadVista(View.VISIBLE);
                        }
                    }
                });
                visibilidadVista(View.GONE);
            }
            //En caso de que no responda el servidor mostramos mensaje de error
            @Override
            public void onFailure(Call<List<Vehiculo>> call, Throwable t) {
                Toast.makeText(getContext(),t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    //Metodo que se encarga de recibir un resultado de la otra actividad lanzada, recibe el codigo de solicitud puesto por nosotros,
    //el codigo del resultado de la operacion especificado en la clase de la segunda actividad, y un Intent con los datos del resultado:
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        //Si el codigo de solicitud y el de respuesta es correcto pasamos a recoger la informacion y a mostrarla en el textView del chat:
        if ((requestCode==1997) && (resultCode==RESULT_OK)){
            agregarCoches();
        }
    }
    //Metodo de la interfaz View.OnClickListener
    @Override
    public void onClick(View v) {
        //Segun el elemento pulsado
        if (v.equals(imgViewCoche)){abrirGaleria();}
        else if (v.equals(btSeleccionarColor)){colorPiker();}
        else if(v.equals(btBorrarVehiculo)){
            //Creamos objeto Retrofit, para lanzar peticiones y poder recibir respuestas
            Retrofit retrofit = new Retrofit.Builder().baseUrl("http://192.168.0.107:8080/").addConverterFactory(GsonConverterFactory.create()).build();
            //Vinculamos el cliente con la interfaz.
            //En esa interfaz se definen los metodos y los verbos que usan
            //Definimos las peticiones que va a poder hacer segun las implementadas en la interfaz que se indica
            JsonPlaceHolderApi peticiones = retrofit.create(JsonPlaceHolderApi.class);
            //Creamos una peticion para borrar un vehiculo:
            Call<Void> call = peticiones.eliminarVehiculoByMatricula(editTextMatricula.getText().toString());
            //Ejecutamos la petición en un hilo en segundo plano, retrofit lo hace por nosotros
            // y esperamos a la respuesta
            call.enqueue(new Callback<Void>() {
                //Gestionamos la respuesta del servidor
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    //Respuesta del servidor con un error y paramos el flujo del programa, indicando el codigo de error
                    if (!response.isSuccessful()) {
                        Toast.makeText(getContext(), "Code: " + response.code(), Toast.LENGTH_LONG).show();
                        return;
                    }
                    //Si el cambio ha sido exitoso volvemos a la actividad anterior
                    Toast.makeText(getContext(), "Borrado realizado con exito.", Toast.LENGTH_LONG).show();
                    agregarCoches();
                    editTextMatricula.setText("");
                    editTextMarca.setText("");
                    editTextModelo.setText("");
                    spinnerTipoCombustible.setSelection(0);
                    imgViewColorCoche.setBackgroundColor(Color.parseColor("#07a0c3"));
                    vSesion=null;
                }
                //En caso de que no responda el servidor mostramos mensaje de error
                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(getContext(),t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
        else if (v.equals(btActualizarDatos)){
            //Variables booleanas
            boolean marca=true, modelo=true, combustible=true, color=true;
            //Antes de hacer la peticion al servidor realizamos las siguientes comprobaciones:
            //Control de marca, de momento ninguno mas:

            //Control de modelo de momento ninguno mas:

            //Control de combustible de momento ninguno mas:

            //Control de color de momento ninguno mas:

            //Si todas las comprobaciones del front son correctas pasamos a lanzar la solicitud al servidor:
            if(marca&&modelo&&color&&combustible){
                //Creamos objeto Retrofit, para lanzar peticiones y poder recibir respuestas
                Retrofit retrofit = new Retrofit.Builder().baseUrl("http://192.168.0.107:8080/").addConverterFactory(GsonConverterFactory.create()).build();
                //Vinculamos el cliente con la interfaz.
                //En esa interfaz se definen los metodos y los verbos que usan
                //Definimos las peticiones que va a poder hacer segun las implementadas en la interfaz que se indica
                JsonPlaceHolderApi peticiones = retrofit.create(JsonPlaceHolderApi.class);
                //Creamos una peticion para actualizar un vehiculo con el valor de los editexts y demas:
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
                infoMap.put("matricula", editTextMatricula.getText().toString());
                infoMap.put("modelo", capitalizaString(modeloAux));
                infoMap.put("marca", capitalizaString(marcaAux));
                infoMap.put("combustible", spinnerTipoCombustible.getSelectedItem().toString());
                infoMap.put("color", colorSeleccionado.toUpperCase());
                Call<Vehiculo> call = peticiones.actualizarVehiculoPorMatricula(infoMap);
                //Ejecutamos la petición en un hilo en segundo plano, retrofit lo hace por nosotros
                // y esperamos a la respuesta
                call.enqueue(new Callback<Vehiculo>() {
                    //Gestionamos la respuesta del servidor
                    @Override
                    public void onResponse(Call<Vehiculo> call, Response<Vehiculo> response) {
                        //Respuesta del servidor con un error y paramos el flujo del programa, indicando el codigo de error
                        if (!response.isSuccessful()) {
                            Toast.makeText(getContext(), "Code: " + response.code(), Toast.LENGTH_LONG).show();
                            return;
                        }
                        vSesion=response.body();
                        agregarCoches();
                        Toast.makeText(getContext(), "Actualizacion realizada con exito.", Toast.LENGTH_LONG).show();
                    }
                    //En caso de que no responda el servidor mostramos mensaje de error
                    @Override
                    public void onFailure(Call<Vehiculo> call, Throwable t) {
                        Toast.makeText(getContext(),t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }
    //Metodos para los editext para cuando cambia su contenido
    //Comprobacion de que los campos no esten vacios y tengan un formato correcto antes de poder intentar iniciar sesion:
    @Override
    public void afterTextChanged(Editable s) {
        if(vSesion!=null) {
            //Boolean prueba de que el estado anterior era true
            boolean anterior = false;
            if (pruebaFormatoMarca || pruebaFormatoModelo || pruebaCombustible || pruebaColor) {
                anterior = true;
            }
            //Si el texto de marca ha cambiado:
            if (s == editTextMarca.getEditableText()) {
                //Limpiamos espacios multiples
                String marca = s.toString().trim();
                StringBuilder aux = new StringBuilder();
                for (int i = 0, contEspacios = 0; i < marca.length(); i++) {
                    if (marca.charAt(i) != ' ') {
                        aux.append(marca.charAt(i));
                        contEspacios = 0;
                    } else {
                        if (contEspacios == 0) {
                            aux.append(marca.charAt(i));
                        }
                        contEspacios++;
                    }
                }
                marca = aux.toString();
                if (!marca.equals("") && marca.length() >= 1 && marca.length() <= 30 && !marca.equalsIgnoreCase(vSesion.getMarca())) {
                    pruebaFormatoMarca = true;
                } else {
                    pruebaFormatoMarca = false;
                }
            }
            //Si el texto de modelo ha cambiado:
            else if (s == editTextModelo.getEditableText()) {
                //Limpiamos espacios multiples
                String modelo = s.toString().trim();
                StringBuilder aux = new StringBuilder();
                for (int i = 0, contEspacios = 0; i < modelo.length(); i++) {
                    if (modelo.charAt(i) != ' ') {
                        aux.append(modelo.charAt(i));
                        contEspacios = 0;
                    } else {
                        if (contEspacios == 0) {
                            aux.append(modelo.charAt(i));
                        }
                        contEspacios++;
                    }
                }
                modelo = aux.toString();
                if (!modelo.equals("") && modelo.length() >= 1 && modelo.length() <= 30 && !modelo.equalsIgnoreCase(vSesion.getModelo())) {
                    pruebaFormatoModelo = true;
                } else {
                    pruebaFormatoModelo = false;
                }
            }
            //Si las 4 pruebas de formato son correctas pasamos a liberar el boton de modificar vehiculo:
            if ((pruebaFormatoMarca || pruebaFormatoModelo || pruebaColor || pruebaCombustible) && !anterior) {
                //Conjunto de animator
                AnimatorSet animator = new AnimatorSet();
                //Animacion para el bt modificar vehiculo
                ObjectAnimator scaleDownX_BtConfirmarRegistro = ObjectAnimator.ofFloat(btActualizarDatos, "scaleX", 0.5f, 1.0f);
                ObjectAnimator scaleDownY_BtConfirmarRegistro = ObjectAnimator.ofFloat(btActualizarDatos, "scaleY", 0.5f, 1.0f);
                animator.play(scaleDownX_BtConfirmarRegistro).with(scaleDownY_BtConfirmarRegistro);
                animator.setDuration(200);
                animator.start();
                //Habilitamos el boton modificar vehiculo:
                btActualizarDatos.setEnabled(true);
                btActualizarDatos.setOnClickListener(this);
                btActualizarDatos.setColorFilter(getResources().getColor(R.color.colorPrimary));
            }
            //Si ninguna prueba es verdadera, es decir ningun campo ha cambiado, y el boton estaba activado pues se desactiva:
            else if (!pruebaFormatoMarca && !pruebaFormatoModelo && !pruebaColor && !pruebaCombustible && anterior) {
                //Conjunto de animator
                AnimatorSet animator = new AnimatorSet();
                //Animacion para el bt agregar vehiculo
                ObjectAnimator scaleDownX_BtConfirmarRegistro = ObjectAnimator.ofFloat(btActualizarDatos, "scaleX", 1.0f, 0.5f);
                ObjectAnimator scaleDownY_BtConfirmarRegistro = ObjectAnimator.ofFloat(btActualizarDatos, "scaleY", 1.0f, 0.5f);
                animator.play(scaleDownX_BtConfirmarRegistro).with(scaleDownY_BtConfirmarRegistro);
                animator.setDuration(200);
                animator.start();
                //Deshabilitamos el boton modificar vehiculo:
                btActualizarDatos.setEnabled(false);
                btActualizarDatos.setOnClickListener(null);
                btActualizarDatos.setColorFilter(getResources().getColor(R.color.colorGris));
            }
        }
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}
    //Metodos para la interfaz de los spinners
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(vSesion!=null) {
            //Boolean prueba de que el estado anterior era true
            boolean anterior = false;
            if (pruebaFormatoMarca || pruebaFormatoModelo || pruebaCombustible || pruebaColor) {
                anterior = true;
            }
            //Si el spinner ha cambiado:
            if (!spinnerTipoCombustible.getSelectedItem().toString().equals("") && !spinnerTipoCombustible.getSelectedItem().toString().equalsIgnoreCase(vSesion.getCombustible())) {
                pruebaCombustible = true;
            } else {
                pruebaCombustible = false;
            }
            //Si las 4 pruebas de formato son correctas pasamos a liberar el boton de modificar vehiculo:
            if ((pruebaFormatoMarca || pruebaFormatoModelo || pruebaColor || pruebaCombustible) && !anterior) {
                //Conjunto de animator
                AnimatorSet animator = new AnimatorSet();
                //Animacion para el bt modificar vehiculo
                ObjectAnimator scaleDownX_BtConfirmarRegistro = ObjectAnimator.ofFloat(btActualizarDatos, "scaleX", 0.5f, 1.0f);
                ObjectAnimator scaleDownY_BtConfirmarRegistro = ObjectAnimator.ofFloat(btActualizarDatos, "scaleY", 0.5f, 1.0f);
                animator.play(scaleDownX_BtConfirmarRegistro).with(scaleDownY_BtConfirmarRegistro);
                animator.setDuration(200);
                animator.start();
                //Habilitamos el boton modificar vehiculo:
                btActualizarDatos.setEnabled(true);
                btActualizarDatos.setOnClickListener(this);
                btActualizarDatos.setColorFilter(getResources().getColor(R.color.colorPrimary));
            }
            //Si ninguna prueba es verdadera, es decir ningun campo ha cambiado, y el boton estaba activado pues se desactiva:
            else if (!pruebaFormatoMarca && !pruebaFormatoModelo && !pruebaColor && !pruebaCombustible && anterior) {
                //Conjunto de animator
                AnimatorSet animator = new AnimatorSet();
                //Animacion para el bt agregar vehiculo
                ObjectAnimator scaleDownX_BtConfirmarRegistro = ObjectAnimator.ofFloat(btActualizarDatos, "scaleX", 1.0f, 0.5f);
                ObjectAnimator scaleDownY_BtConfirmarRegistro = ObjectAnimator.ofFloat(btActualizarDatos, "scaleY", 1.0f, 0.5f);
                animator.play(scaleDownX_BtConfirmarRegistro).with(scaleDownY_BtConfirmarRegistro);
                animator.setDuration(200);
                animator.start();
                //Deshabilitamos el boton modificar vehiculo:
                btActualizarDatos.setEnabled(false);
                btActualizarDatos.setOnClickListener(null);
                btActualizarDatos.setColorFilter(getResources().getColor(R.color.colorGris));
            }
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {}
    //Coge la direccion del dispositivo
    /*@Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALERY_INTENT && resultCode == RESULT_OK) {
            //Coge la Uri del dispositivo
            uriImagenEndispositivo = data.getData();
            //Cambia la imagen desde el dispositivo
            Glide.with(getContext()).load(uriImagenEndispositivo).into(imgViewCoche);
        }
    }*/
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
    //Metodo que vuelve visible o gone los atributos en funcion de lo que se le pase
    public void visibilidadVista(int tipo){
        tituloMatricula .setVisibility(tipo);
        tituloMarca.setVisibility(tipo);
        tituloModelo.setVisibility(tipo);
        tituloCombustible.setVisibility(tipo);
        tipoColor.setVisibility(tipo);
        linearLayoutSpinnerCombustible.setVisibility(tipo);
        editTextMatricula.setVisibility(tipo);
        editTextMarca.setVisibility(tipo);
        editTextModelo.setVisibility(tipo);
        spinnerTipoCombustible.setVisibility(tipo);
        imgViewCoche.setVisibility(tipo);
        imgViewColorCoche.setVisibility(tipo);
        btSeleccionarColor.setVisibility(tipo);
        btBorrarVehiculo.setVisibility(tipo);
        btActualizarDatos.setVisibility(tipo);
        btActualizarDatos.setScaleX(0.5f);
        btActualizarDatos.setScaleY(0.5f);
        btActualizarDatos.setColorFilter(getResources().getColor(R.color.colorGris));
        imageViewEditar.setVisibility(tipo);
        if(tipo==View.VISIBLE){
            me_siento_vasio.setVisibility(View.GONE);
            textViewMe_siento_vasio.setVisibility(View.GONE);
        }else {
            if(misVehiculosList.size()==1){
                me_siento_vasio.setVisibility(View.VISIBLE);
                me_siento_vasio.playAnimation();
                me_siento_vasio.setRepeatCount(ValueAnimator.INFINITE);
                textViewMe_siento_vasio.setVisibility(View.VISIBLE);
            }else{
                me_siento_vasio.setVisibility(View.GONE);
                textViewMe_siento_vasio.setVisibility(View.GONE);
            }
        }
    }
    //Metodos para elegir color
    @Override
    public void onChooseColor(int position, int color) {
        if(vSesion!=null) {
            if (position != -1) {
                //Boolean prueba de que el estado anterior era true
                boolean anterior = false;
                if (pruebaFormatoMarca || pruebaFormatoModelo || pruebaCombustible || pruebaColor) {
                    anterior = true;
                }
                imgViewColorCoche.setBackgroundColor(color);
                colorSeleccionado = colores.get(position);
                //Si el color ha cambiado:
                if (!colorSeleccionado.equals("") && !colorSeleccionado.equalsIgnoreCase(vSesion.getColor())) {
                    pruebaColor = true;
                } else {
                    pruebaColor = false;
                }
                //Si las 4 pruebas de formato son correctas pasamos a liberar el boton de modificar vehiculo:
                if ((pruebaFormatoMarca || pruebaFormatoModelo || pruebaColor || pruebaCombustible) && !anterior) {
                    //Conjunto de animator
                    AnimatorSet animator = new AnimatorSet();
                    //Animacion para el bt modificar vehiculo
                    ObjectAnimator scaleDownX_BtConfirmarRegistro = ObjectAnimator.ofFloat(btActualizarDatos, "scaleX", 0.5f, 1.0f);
                    ObjectAnimator scaleDownY_BtConfirmarRegistro = ObjectAnimator.ofFloat(btActualizarDatos, "scaleY", 0.5f, 1.0f);
                    animator.play(scaleDownX_BtConfirmarRegistro).with(scaleDownY_BtConfirmarRegistro);
                    animator.setDuration(200);
                    animator.start();
                    //Habilitamos el boton modificar vehiculo:
                    btActualizarDatos.setEnabled(true);
                    btActualizarDatos.setOnClickListener(this);
                    btActualizarDatos.setColorFilter(getResources().getColor(R.color.colorPrimary));
                }
                //Si ninguna prueba es verdadera, es decir ningun campo ha cambiado, y el boton estaba activado pues se desactiva:
                else if (!pruebaFormatoMarca && !pruebaFormatoModelo && !pruebaColor && !pruebaCombustible && anterior) {
                    //Conjunto de animator
                    AnimatorSet animator = new AnimatorSet();
                    //Animacion para el bt agregar vehiculo
                    ObjectAnimator scaleDownX_BtConfirmarRegistro = ObjectAnimator.ofFloat(btActualizarDatos, "scaleX", 1.0f, 0.5f);
                    ObjectAnimator scaleDownY_BtConfirmarRegistro = ObjectAnimator.ofFloat(btActualizarDatos, "scaleY", 1.0f, 0.5f);
                    animator.play(scaleDownX_BtConfirmarRegistro).with(scaleDownY_BtConfirmarRegistro);
                    animator.setDuration(200);
                    animator.start();
                    //Deshabilitamos el boton modificar vehiculo:
                    btActualizarDatos.setEnabled(false);
                    btActualizarDatos.setOnClickListener(null);
                    btActualizarDatos.setColorFilter(getResources().getColor(R.color.colorGris));
                }
            }
        }
    }
    @Override
    public void onCancel() {}
}
