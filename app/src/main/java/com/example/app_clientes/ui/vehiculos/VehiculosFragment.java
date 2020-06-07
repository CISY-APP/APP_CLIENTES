//Indicamos a que paquete pertenece esta clase:
package com.example.app_clientes.ui.vehiculos;
//Importamos los siguientes paquetes:
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
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
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.example.app_clientes.Biblioteca;
import com.example.app_clientes.adapter.MiAdapterMisVehiculos;
import com.example.app_clientes.item.ItemVehiculo;
import com.example.app_clientes.R;
import com.example.app_clientes.jsonplaceholder.JsonPlaceHolderApi;
import com.example.app_clientes.pojos.Vehiculo;
import com.example.app_clientes.vistas.VentanaAgregarVehiculo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.makeramen.roundedimageview.RoundedImageView;

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
//Clase que contiene toda la logica y conexion con la ventana de mostrar, eliminar, modificar un vehiculo de la app:
public class VehiculosFragment extends Fragment implements View.OnClickListener, TextWatcher, AdapterView.OnItemSelectedListener, ColorPicker.OnChooseColorListener{
    //Variables para comprobacion de formatos:
    private boolean pruebaFormatoMarca, pruebaFormatoModelo, pruebaFormatoCombustible, pruebaFormatoColor, pruebaFormatoFoto;
    //Coche sesion para cuando se pulsa en un elemento del recycler view:
    Vehiculo vSesion ;
    //Atributos XML:
    private LottieAnimationView me_siento_vasio, elige_vehiculo_wey;
    private TextView textViewMe_siento_vasio, textViewElige_vehiculo_wey;
    private LinearLayout linearLayoutSpinnerCombustible;
    private EditText editTextMatricula, editTextMarca, editTextModelo;
    private TextView txtErrorMatricula, txtErrorMarca, txtErrorModelo;
    private TextView tituloMatricula, tituloMarca, tituloModelo, tituloCombustible, tipoColor;
    private Spinner spinnerTipoCombustible;
    private EditText btSeleccionarColor;
    private CircleImageView imgViewColorCoche;
    private RoundedImageView imgViewCoche;
    private ImageView btBorrarVehiculo, btActualizarDatos, imageViewEditar;
    //Atributos de la clase:
    private MiAdapterMisVehiculos miAdapterMisVehiculos;
    private RecyclerView recyclerViewMisVehiculos;
    private ArrayList<ItemVehiculo> misVehiculosList = new ArrayList<>();
    private final ArrayList<String> colores = new ArrayList<>();
    private String colorSeleccionado;
    private StorageReference storageReference;
    private Uri uriImagenEndispositivo=null;
    private static final int GALERY_INTENT = 1;
    private String ID_USUARIO;
    private String uriParaElInsert;
    //Metodo que se ejecuta al crearse la vista:
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Conectamos el xml:
        View view = inflater.inflate(R.layout.fragment_vehiculo, container, false);
        //Inicializamos variables booleanas de prueba:
        pruebaFormatoMarca=false;
        pruebaFormatoModelo=false;
        pruebaFormatoCombustible=true;
        pruebaFormatoColor=true;
        pruebaFormatoFoto=false;
        //Asociamos el id del usuario en sesion a la siguiente variable:
        ID_USUARIO = Biblioteca.usuarioSesion.getIdusuario().toString();
        colorSeleccionado="#07a0c3";
        //Vinculamos los atributos de la clase:
        textViewElige_vehiculo_wey=view.findViewById(R.id.textViewTituloAnimacionEligeMisVehiculos);
        elige_vehiculo_wey=view.findViewById(R.id.animation_elige_algo_wey_vehiculos);
        textViewMe_siento_vasio =view.findViewById(R.id.textViewTituloAnimacionVacioMisVehiculos);
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
        editTextMatricula.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    editTextMatricula.setBackground(getActivity().getDrawable(R.drawable.edittextseleccionadoazul));
                } else {
                    editTextMatricula.setBackground(getActivity().getDrawable(R.drawable.layout_drawable_2));
                }
            }
        });
        editTextMarca.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    editTextMarca.setBackground(getActivity().getDrawable(R.drawable.edittextseleccionadoazul));
                } else {
                    editTextMarca.setBackground(getActivity().getDrawable(R.drawable.layout_drawable_2));
                }
            }
        });
        editTextModelo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    editTextModelo.setBackground(getActivity().getDrawable(R.drawable.edittextseleccionadoazul));
                } else {
                    editTextModelo.setBackground(getActivity().getDrawable(R.drawable.layout_drawable_2));
                }
            }
        });
        //Instancia el objeto de tipo storageReference
        storageReference = FirebaseStorage.getInstance().getReference();
        //RECYCLERRRRRRRR V I E W instanciado en el metodo agregar coches:
        //Agrega los coches al Array de vehiculo y configurar recycler view:
        agregarCoches();
        return view;
    }
    //Metodo que carga los datos del coche en la interfaz:
    public void cargarDatosCoche(final int pos){
        //Creamos objeto Retrofit, para lanzar peticiones y poder recibir respuestas:
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Biblioteca.ip).addConverterFactory(GsonConverterFactory.create()).build();
        //Vinculamos el cliente con la interfaz:
        final JsonPlaceHolderApi peticiones = retrofit.create(JsonPlaceHolderApi.class);
        //Creamos una peticion para obtener el vehiculo asociado a la matricula del primer vehiculo en la lista:
        Call<Vehiculo> call = peticiones.getVehiculoByMatricula(misVehiculosList.get(pos).getMatricula());
        //Ejecutamos la petición en un hilo en segundo plano, retrofit lo hace por nosotros y esperamos a la respuesta:
        call.enqueue(new Callback<Vehiculo>() {
            //Gestionamos la respuesta del servidor:
            @Override
            public void onResponse(Call<Vehiculo> call, Response<Vehiculo> response) {
                //Respuesta del servidor con un error y paramos el flujo del programa, indicando el codigo de error:
                if (!response.isSuccessful()) {
                    Toast.makeText(getContext(), "Codigo de error: " + response.code(), Toast.LENGTH_LONG).show();
                    return;
                }
                //Si la respuesta es correcta pasamos a cargar el vehiculo:
                Glide.with(getActivity()).load(misVehiculosList.get(pos).getmImageVehiculo()).error(R.drawable.coche).into(imgViewCoche);
                //Reinicio de variables booleanas:
                pruebaFormatoMarca =false;
                pruebaFormatoModelo=false;
                pruebaFormatoCombustible =false;
                pruebaFormatoColor =false;
                pruebaFormatoFoto=false;
                uriImagenEndispositivo=null;
                //Lo guardamos en vehiculosesion y cargamos los datos en la interfaz:
                vSesion=response.body();
                editTextMatricula.setText(vSesion.getMatricula());
                editTextMarca.setText(vSesion.getMarca());
                editTextModelo.setText(vSesion.getModelo());
                int posicionSpinner=0;
                if(vSesion.getCombustible().equalsIgnoreCase(getText(R.string.spinner_datos_gasolina_ventanaDatosVehiculo).toString())){posicionSpinner=0;}
                else if(vSesion.getCombustible().equalsIgnoreCase(getText(R.string.spinner_datos_diesel_ventanaDatosVehiculo).toString())){posicionSpinner=1;}
                else if(vSesion.getCombustible().equalsIgnoreCase(getText(R.string.spinner_datos_hibrido_ventanaDatosVehiculo).toString())){posicionSpinner=2;}
                else if(vSesion.getCombustible().equalsIgnoreCase(getText(R.string.spinner_datos_electrico_ventanaDatosVehiculo).toString())){posicionSpinner=3;}
                spinnerTipoCombustible.setSelection(posicionSpinner);
                imgViewColorCoche.setBackgroundColor(Color.parseColor(vSesion.getColor()));
            }
            //En caso de que no responda el servidor mostramos mensaje de error:
            @Override
            public void onFailure(Call<Vehiculo> call, Throwable t) {
                Toast.makeText(getContext(), "El servidor esta caido.", Toast.LENGTH_LONG).show();
            }
        });
    }
    //Inicializa el spinner de vehiculos:
    private void inicializacionSpinnerCombustible() {
        //Inicializamos los valores que puede obtener el spinner combustible:
        String[] combustibles = new String[]{getText(R.string.spinner_datos_gasolina_ventanaDatosVehiculo).toString(), getText(R.string.spinner_datos_diesel_ventanaDatosVehiculo).toString(), getText(R.string.spinner_datos_hibrido_ventanaDatosVehiculo).toString(), getText(R.string.spinner_datos_electrico_ventanaDatosVehiculo).toString()};
        //Inicialimos el adapter y lo asociamos al spinner:
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(),R.layout.color_spinner,combustibles);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.color_spinner);
        spinnerTipoCombustible.setAdapter(spinnerArrayAdapter);
    }
    //Inicializa el ColorPiker del color del vehiculo:
    public void colorPiker() {
        ColorPicker colorPicker = new ColorPicker(getActivity());
        colorPicker.setTitle(getText(R.string.colorPicker_preguntaTitulo_ventanaDatosVehiculo).toString());
        colores.clear();
        //Añadimos los colores a nuestra coleccion:
        colores.add("#000000");colores.add("#FFFFFF");colores.add("#616161");colores.add("#C8C8C8");colores.add("#7A0000");colores.add("#E70000");colores.add("#011474");
        colores.add("#20D2F6");colores.add("#01742E");colores.add("#00BA27");colores.add("#8E6D3D");colores.add("#F5C886");colores.add("#F17C00");colores.add("#E7E300");
        colores.add("#FFA3F8");
        //Establecemos los colores , definimos los botones redondos y en 5 columnas, y le asociamos un listener a ese color y lo mostramos:
        colorPicker.setColors(colores);
        colorPicker.setColumns(5);
        colorPicker.setRoundColorButton(true);
        colorPicker.setOnChooseColorListener(this);
        colorPicker.show();
    }
    //Abre la galeria de imagenes:
    public void abrirGaleria(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,GALERY_INTENT);
    }
    //Rellena el arrayList de vehiculo y con esos datos el recyclerview:
    private void agregarCoches() {
        //Creamos objeto Retrofit, para lanzar peticiones y poder recibir respuestas:
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Biblioteca.ip).addConverterFactory(GsonConverterFactory.create()).build();
        //Vinculamos el cliente con la interfaz:
        final JsonPlaceHolderApi peticiones = retrofit.create(JsonPlaceHolderApi.class);
        //Creamos una peticion para obtener una lista de los vehiculos asociados al cliente:
        Call<List<Vehiculo>> call = peticiones.getListVehiculoById(Biblioteca.usuarioSesion.getIdusuario());
        //Ejecutamos la petición en un hilo en segundo plano, retrofit lo hace por nosotros y esperamos a la respuesta:
        call.enqueue(new Callback<List<Vehiculo>>() {
            //Gestionamos la respuesta del servidor:
            @Override
            public void onResponse(Call<List<Vehiculo>> call, Response<List<Vehiculo>> response) {
                //Respuesta del servidor con un error y paramos el flujo del programa, indicando el codigo de error:
                if (!response.isSuccessful()) {
                    Toast.makeText(getContext(), "Codigo de error: " + response.code(), Toast.LENGTH_LONG).show();
                    return;
                }
                //Si la respuesta del servidor es existosa limpiamos la lista actual y cargamos la nueva y reiniciamos el recyclerview con ella:
                misVehiculosList.clear();
                //Recogemos la lista y la guardamos para el modelo del recycler view
                List<Vehiculo> listObtenida = response.body();
                for (Vehiculo vl : listObtenida) {
                    misVehiculosList.add(new ItemVehiculo(vl.getMarca()+" - "+vl.getModelo(), vl.getMatricula(), vl.getFotovehiculo()));
                }
                misVehiculosList.add(new ItemVehiculo(getText(R.string.bt_añadirVehiculo_ventanaDatosVehiculo).toString(), "botonAñadir", R.drawable.anadir));
                //INSTANCIAMOS Y ASOCIAMOS ELEMENTOS NECESARIOS PARA EL CORRECTO FUNCIONAMIENTO DEL RECYCLERVIEW:
                recyclerViewMisVehiculos.setHasFixedSize(true);// RecyclerView sabe de antemano que su tamaño no depende del contenido del adaptador, entonces omitirá la comprobación de si su tamaño debería cambiar cada vez que se agregue o elimine un elemento del adaptador.(mejora el rendimiento).
                recyclerViewMisVehiculos.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                miAdapterMisVehiculos = new MiAdapterMisVehiculos(getActivity(), misVehiculosList);//Instanciamos un objeto de tipo Example_Adapter.
                recyclerViewMisVehiculos.setAdapter(miAdapterMisVehiculos);//Vinculamos el adapter al recyclerView.
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
                            //Configuramos el animatorSet, como se tienen que reproducir las animaciones, el tiempo que duran, que clase de interpolador utilizan y la lanzamos:
                            animatorSetEscale.play(scaleDownX_ImCoche).with(scaleDownY_ImCoche)
                                    .with(scaleDownX_ImColor).with(scaleDownY_ImColor)
                                    .with(scaleDownX_TxtMatricula).with(scaleDownY_TxtMatricula)
                                    .with(scaleDownX_TxtMarca).with(scaleDownY_TxtMarca)
                                    .with(scaleDownX_TxtModelo).with(scaleDownY_TxtModelo)
                                    .with(scaleDownX_LayoutSpinnerCombustible).with(scaleDownY_LayoutSpinnerCombustible)
                                    .with(scaleDownX_SpinnerCombustible).with(scaleDownY_SpinnerCombustible)
                                    .with(scaleDownX_BtSeleccionarColor).with(scaleDownY_BtSeleccionarColor);
                            animatorSetEscale.setDuration(Biblioteca.tAnimacionesScaleInicial);
                            animatorSetEscale.setInterpolator(new AccelerateDecelerateInterpolator());
                            animatorSetEscale.start();
                            visibilidadVista(View.VISIBLE);
                        }
                    }
                });
                visibilidadVista(View.GONE);
            }
            //En caso de que no responda el servidor mostramos mensaje de error:
            @Override
            public void onFailure(Call<List<Vehiculo>> call, Throwable t) {
                Toast.makeText(getContext(),"El servidor esta caido.", Toast.LENGTH_LONG).show();
            }
        });
    }
    //Metodo que se encarga de recibir un resultado de la otra actividad lanzada, recibe el codigo de solicitud puesto por nosotros,
    //el codigo del resultado de la operacion especificado en la clase de la segunda actividad, y un Intent con los datos del resultado:
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        //Si el codigo de solicitud es 1997 y el de respuesta es correcto pasamos a recoger la informacion y a mostrarla:
        if ((requestCode==1997) && (resultCode==RESULT_OK)){
            agregarCoches();
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALERY_INTENT && resultCode == RESULT_OK) {
            //Coge la Uri del dispositivo
            uriImagenEndispositivo = data.getData();
            //Cambia la imagen desde el dispositivo
            Glide.with(getActivity()).load(uriImagenEndispositivo).error(R.drawable.coche).into(imgViewCoche);
            //Si el vehiculo sesion se ha terminado de cargar:
            if(vSesion!=null) {
                //Boolean para comprobar que estado tiene antes de realizar pruebas:
                boolean anterior = false;
                if (pruebaFormatoMarca || pruebaFormatoModelo || pruebaFormatoCombustible || pruebaFormatoColor || pruebaFormatoFoto) {
                    anterior = true;
                }
                //Depende de si la foto es null o no se obtiene un valor u otro:
                pruebaFormatoFoto=uriImagenEndispositivo!=null;
                //Si alguna de las 5 pruebas de formato son correctas pasamos a liberar el boton de modificar vehiculo:
                if ((pruebaFormatoMarca || pruebaFormatoModelo || pruebaFormatoColor || pruebaFormatoCombustible ||pruebaFormatoFoto) && !anterior) {
                    //Conjunto de animator:
                    AnimatorSet animator = new AnimatorSet();
                    //Animacion para el bt modificar vehiculo:
                    ObjectAnimator scaleDownX_BtConfirmarRegistro = ObjectAnimator.ofFloat(btActualizarDatos, "scaleX", 0.5f, 1.0f);
                    ObjectAnimator scaleDownY_BtConfirmarRegistro = ObjectAnimator.ofFloat(btActualizarDatos, "scaleY", 0.5f, 1.0f);
                    animator.play(scaleDownX_BtConfirmarRegistro).with(scaleDownY_BtConfirmarRegistro);
                    animator.setDuration(Biblioteca.tAnimacionesScaleBotones);
                    animator.start();
                    //Habilitamos el boton modificar vehiculo:
                    btActualizarDatos.setEnabled(true);
                    btActualizarDatos.setOnClickListener(this);
                    btActualizarDatos.setColorFilter(getResources().getColor(R.color.colorPrimary));
                }
                //Si ninguna prueba es verdadera, es decir ningun campo ha cambiado, y el boton estaba activado pues se desactiva:
                else if (!pruebaFormatoMarca && !pruebaFormatoModelo && !pruebaFormatoColor && !pruebaFormatoCombustible && !pruebaFormatoFoto && anterior) {
                    //Conjunto de animator:
                    AnimatorSet animator = new AnimatorSet();
                    //Animacion para el bt agregar vehiculo:
                    ObjectAnimator scaleDownX_BtConfirmarRegistro = ObjectAnimator.ofFloat(btActualizarDatos, "scaleX", 1.0f, 0.5f);
                    ObjectAnimator scaleDownY_BtConfirmarRegistro = ObjectAnimator.ofFloat(btActualizarDatos, "scaleY", 1.0f, 0.5f);
                    animator.play(scaleDownX_BtConfirmarRegistro).with(scaleDownY_BtConfirmarRegistro);
                    animator.setDuration(Biblioteca.tAnimacionesScaleBotones);
                    animator.start();
                    //Deshabilitamos el boton modificar vehiculo:
                    btActualizarDatos.setEnabled(false);
                    btActualizarDatos.setOnClickListener(null);
                    btActualizarDatos.setColorFilter(getResources().getColor(R.color.colorGris));
                }
            }
        }
    }
    //Metodo de la interfaz View.OnClickListener:
    @Override
    public void onClick(View v) {
        //Segun el elemento pulsado
        if (v.equals(imgViewCoche)){abrirGaleria();}
        else if (v.equals(btSeleccionarColor)){colorPiker();}
        else if(v.equals(btBorrarVehiculo)){
            //Creamos objeto Retrofit, para lanzar peticiones y poder recibir respuestas:
            Retrofit retrofit = new Retrofit.Builder().baseUrl(Biblioteca.ip).addConverterFactory(GsonConverterFactory.create()).build();
            //Vinculamos el cliente con la interfaz:
            JsonPlaceHolderApi peticiones = retrofit.create(JsonPlaceHolderApi.class);
            //Creamos una peticion para borrar un vehiculo:
            Call<Void> call = peticiones.eliminarVehiculoByMatricula(editTextMatricula.getText().toString());
            //Ejecutamos la petición en un hilo en segundo plano, retrofit lo hace por nosotros y esperamos a la respuesta
            call.enqueue(new Callback<Void>() {
                //Gestionamos la respuesta del servidor:
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    //Respuesta del servidor con un error y paramos el flujo del programa, indicando el codigo de error:
                    if (!response.isSuccessful()) {
                        Toast.makeText(getContext(), "Codigo de error: " + response.code(), Toast.LENGTH_LONG).show();
                        return;
                    }
                    //Si el cambio ha sido exitoso volvemos a la actividad anterior reiniciando la vista completa y el recyclerview:
                    Toast.makeText(getContext(), getText(R.string.txt_borradoVehiculo_Toast_ventanaDatosVehiculo), Toast.LENGTH_LONG).show();
                    agregarCoches();
                    editTextMatricula.setText("");
                    editTextMarca.setText("");
                    editTextModelo.setText("");
                    Glide.with(getActivity()).load(R.drawable.coche).error(R.drawable.coche).into(imgViewCoche);
                    spinnerTipoCombustible.setSelection(0);
                    imgViewColorCoche.setBackgroundColor(Color.parseColor("#07a0c3"));
                    vSesion=null;
                }
                //En caso de que no responda el servidor mostramos mensaje de error:
                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(getContext(),"El servidor esta caido.", Toast.LENGTH_LONG).show();
                }
            });
        }
        else if (v.equals(btActualizarDatos)){
            //Variables booleanas:
            boolean pbMarca=true, pbModelo=true, pbCombustible=true, pbColor=true;
            //Antes de hacer la peticion al servidor realizamos las siguientes comprobaciones:
            //Control de marca, de momento ninguno mas:

            //Control de modelo de momento ninguno mas:

            //Control de combustible de momento ninguno mas:

            //Control de color de momento ninguno mas:

            //Si todas las comprobaciones del front son correctas pasamos a lanzar la solicitud al servidor:
            if(pbMarca&&pbModelo&&pbColor&&pbCombustible) {
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
                                    //Creamos una peticion para actualizar un vehiculo con el valor de los editexts y demas:
                                    String modeloAux = Biblioteca.quitaEspaciosRepetidosEntrePalabras(editTextModelo.getText().toString());
                                    String marcaAux = Biblioteca.quitaEspaciosRepetidosEntrePalabras(editTextMarca.getText().toString());
                                    Map<String, String> infoMap = new HashMap<String, String>();
                                    infoMap.put("matricula", editTextMatricula.getText().toString());
                                    if (!modeloAux.equals("")) {
                                        infoMap.put("modelo", Biblioteca.capitalizaString(modeloAux));
                                    } else {
                                        infoMap.put("modelo", vSesion.getModelo());
                                    }
                                    if (!marcaAux.equals("")) {
                                        infoMap.put("marca", Biblioteca.capitalizaString(marcaAux));
                                    } else {
                                        infoMap.put("marca", vSesion.getMarca());
                                    }
                                    infoMap.put("combustible", spinnerTipoCombustible.getSelectedItem().toString());
                                    infoMap.put("color", colorSeleccionado.toUpperCase());
                                    infoMap.put("fotovehiculo", uri.toString());
                                    Call<Vehiculo> call = peticiones.actualizarVehiculoPorMatricula(infoMap);
                                    //Ejecutamos la petición en un hilo en segundo plano, retrofit lo hace por nosotros y esperamos a la respuesta:
                                    call.enqueue(new Callback<Vehiculo>() {
                                        //Gestionamos la respuesta del servidor:
                                        @Override
                                        public void onResponse(Call<Vehiculo> call, Response<Vehiculo> response) {
                                            //Respuesta del servidor con un error y paramos el flujo del programa, indicando el codigo de error:
                                            if (!response.isSuccessful()) {
                                                Toast.makeText(getContext(), "Codigo de error: " + response.code(), Toast.LENGTH_LONG).show();
                                                return;
                                            }
                                            //Si la respuesta es satisfactoria, actualizamos el vehiculo sesion, y volvemos a reiniciar interfaz y lista de recycler view:
                                            vSesion = response.body();
                                            agregarCoches();
                                            uriImagenEndispositivo=null;
                                            Toast.makeText(getContext(), getText(R.string.txt_actualizdoVehiculo_Toast_ventanaDatosVehiculo), Toast.LENGTH_LONG).show();
                                        }

                                        //En caso de que no responda el servidor mostramos mensaje de error:
                                        @Override
                                        public void onFailure(Call<Vehiculo> call, Throwable t) {
                                            Toast.makeText(getContext(), "El servidor esta caido.", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    Toast.makeText(getContext(), "No se ha podido realizar el insert en la base de datos", Toast.LENGTH_SHORT).show();
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
                    //Creamos una peticion para actualizar un vehiculo con el valor de los editexts y demas:
                    String modeloAux = Biblioteca.quitaEspaciosRepetidosEntrePalabras(editTextModelo.getText().toString());
                    String marcaAux = Biblioteca.quitaEspaciosRepetidosEntrePalabras(editTextMarca.getText().toString());
                    Map<String, String> infoMap = new HashMap<String, String>();
                    infoMap.put("matricula", editTextMatricula.getText().toString());
                    if (!modeloAux.equals("")) {
                        infoMap.put("modelo", Biblioteca.capitalizaString(modeloAux));
                    } else {
                        infoMap.put("modelo", vSesion.getModelo());
                    }
                    if (!marcaAux.equals("")) {
                        infoMap.put("marca", Biblioteca.capitalizaString(marcaAux));
                    } else {
                        infoMap.put("marca", vSesion.getMarca());
                    }
                    infoMap.put("combustible", spinnerTipoCombustible.getSelectedItem().toString());
                    infoMap.put("color", colorSeleccionado.toUpperCase());
                    infoMap.put("fotovehiculo", "");
                    Call<Vehiculo> call = peticiones.actualizarVehiculoPorMatricula(infoMap);
                    //Ejecutamos la petición en un hilo en segundo plano, retrofit lo hace por nosotros y esperamos a la respuesta:
                    call.enqueue(new Callback<Vehiculo>() {
                        //Gestionamos la respuesta del servidor:
                        @Override
                        public void onResponse(Call<Vehiculo> call, Response<Vehiculo> response) {
                            //Respuesta del servidor con un error y paramos el flujo del programa, indicando el codigo de error:
                            if (!response.isSuccessful()) {
                                Toast.makeText(getContext(), "Codigo de error: " + response.code(), Toast.LENGTH_LONG).show();
                                return;
                            }
                            //Si la respuesta es satisfactoria, actualizamos el vehiculo sesion, y volvemos a reiniciar interfaz y lista de recycler view:
                            vSesion = response.body();
                            agregarCoches();
                            uriImagenEndispositivo=null;
                            Toast.makeText(getContext(), getText(R.string.txt_actualizdoVehiculo_Toast_ventanaDatosVehiculo), Toast.LENGTH_LONG).show();
                        }

                        //En caso de que no responda el servidor mostramos mensaje de error:
                        @Override
                        public void onFailure(Call<Vehiculo> call, Throwable t) {
                            Toast.makeText(getContext(), "El servidor esta caido.", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }

        }
    }
    //Metodo afterTextChanged implementado de la interfaz TextWatcher (cuando cambie el texto se ejecutara):
    @Override
    public void afterTextChanged(Editable s) {
        //Si el vehiculo sesion se ha terminado de cargar:
        if(vSesion!=null) {
            //Boolean para comprobar que estado tiene antes de realizar pruebas:
            boolean anterior = false;
            if (pruebaFormatoMarca || pruebaFormatoModelo || pruebaFormatoCombustible || pruebaFormatoColor || pruebaFormatoFoto) {
                anterior = true;
            }
            //Depende de si la foto es null o no se obtiene un valor u otro:
            pruebaFormatoFoto=uriImagenEndispositivo!=null;
            //Si el texto de marca ha cambiado:
            if (s == editTextMarca.getEditableText()) {
                //Limpiamos espacios multiples y si no es cadena vacia, y no es igual al valor anterior, y esta en el rango de longitud de 1 a 30:
                String marca = Biblioteca.quitaEspaciosRepetidosEntrePalabras(s.toString());
                pruebaFormatoMarca = !marca.equals("") && marca.length() >= 1 && marca.length() <= 30 && !marca.equalsIgnoreCase(vSesion.getMarca());
            }
            //Si el texto de modelo ha cambiado:
            else if (s == editTextModelo.getEditableText()) {
                //Limpiamos espacios multiples y si no es cadena vacia, y no es igual al valor anterior, y esta en el rango de longitud de 1 a 30:
                String modelo = Biblioteca.quitaEspaciosRepetidosEntrePalabras(s.toString());
                pruebaFormatoModelo = !modelo.equals("") && modelo.length() >= 1 && modelo.length() <= 30 && !modelo.equalsIgnoreCase(vSesion.getModelo());
            }
            //Si alguna de las 5 pruebas de formato son correctas pasamos a liberar el boton de modificar vehiculo:
            if ((pruebaFormatoMarca || pruebaFormatoModelo || pruebaFormatoColor || pruebaFormatoCombustible || pruebaFormatoFoto) && !anterior) {
                //Conjunto de animator:
                AnimatorSet animator = new AnimatorSet();
                //Animacion para el bt modificar vehiculo:
                ObjectAnimator scaleDownX_BtConfirmarRegistro = ObjectAnimator.ofFloat(btActualizarDatos, "scaleX", 0.5f, 1.0f);
                ObjectAnimator scaleDownY_BtConfirmarRegistro = ObjectAnimator.ofFloat(btActualizarDatos, "scaleY", 0.5f, 1.0f);
                animator.play(scaleDownX_BtConfirmarRegistro).with(scaleDownY_BtConfirmarRegistro);
                animator.setDuration(Biblioteca.tAnimacionesScaleBotones);
                animator.start();
                //Habilitamos el boton modificar vehiculo:
                btActualizarDatos.setEnabled(true);
                btActualizarDatos.setOnClickListener(this);
                btActualizarDatos.setColorFilter(getResources().getColor(R.color.colorPrimary));
            }
            //Si ninguna prueba es verdadera, es decir ningun campo ha cambiado, y el boton estaba activado pues se desactiva:
            else if (!pruebaFormatoMarca && !pruebaFormatoModelo && !pruebaFormatoColor && !pruebaFormatoCombustible && !pruebaFormatoFoto && anterior) {
                //Conjunto de animator:
                AnimatorSet animator = new AnimatorSet();
                //Animacion para el bt agregar vehiculo:
                ObjectAnimator scaleDownX_BtConfirmarRegistro = ObjectAnimator.ofFloat(btActualizarDatos, "scaleX", 1.0f, 0.5f);
                ObjectAnimator scaleDownY_BtConfirmarRegistro = ObjectAnimator.ofFloat(btActualizarDatos, "scaleY", 1.0f, 0.5f);
                animator.play(scaleDownX_BtConfirmarRegistro).with(scaleDownY_BtConfirmarRegistro);
                animator.setDuration(Biblioteca.tAnimacionesScaleBotones);
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
        //Si el vehiculo sesion se ha terminado de cargar:
        if(vSesion!=null) {
            //Boolean para comprobar que estado tiene antes de realizar pruebas:
            boolean anterior = false;
            if (pruebaFormatoMarca || pruebaFormatoModelo || pruebaFormatoCombustible || pruebaFormatoColor || pruebaFormatoFoto) {
                anterior = true;
            }
            //Depende de si la foto es null o no se obtiene un valor u otro:
            pruebaFormatoFoto=uriImagenEndispositivo!=null;
            //Si el spinner ha cambiado y no tiene valor nulo:
            if (!spinnerTipoCombustible.getSelectedItem().toString().equals("") && !spinnerTipoCombustible.getSelectedItem().toString().equalsIgnoreCase(vSesion.getCombustible())) {
                pruebaFormatoCombustible = true;
            } else {
                pruebaFormatoCombustible = false;
            }
            //Si las 5 pruebas de formato son correctas pasamos a liberar el boton de modificar vehiculo:
            if ((pruebaFormatoMarca || pruebaFormatoModelo || pruebaFormatoColor || pruebaFormatoCombustible || pruebaFormatoFoto) && !anterior) {
                //Conjunto de animator:
                AnimatorSet animator = new AnimatorSet();
                //Animacion para el bt modificar vehiculo:
                ObjectAnimator scaleDownX_BtConfirmarRegistro = ObjectAnimator.ofFloat(btActualizarDatos, "scaleX", 0.5f, 1.0f);
                ObjectAnimator scaleDownY_BtConfirmarRegistro = ObjectAnimator.ofFloat(btActualizarDatos, "scaleY", 0.5f, 1.0f);
                animator.play(scaleDownX_BtConfirmarRegistro).with(scaleDownY_BtConfirmarRegistro);
                animator.setDuration(Biblioteca.tAnimacionesScaleBotones);
                animator.start();
                //Habilitamos el boton modificar vehiculo:
                btActualizarDatos.setEnabled(true);
                btActualizarDatos.setOnClickListener(this);
                btActualizarDatos.setColorFilter(getResources().getColor(R.color.colorPrimary));
            }
            //Si ninguna prueba es verdadera, es decir ningun campo ha cambiado, y el boton estaba activado pues se desactiva:
            else if (!pruebaFormatoMarca && !pruebaFormatoModelo && !pruebaFormatoColor && !pruebaFormatoCombustible && !pruebaFormatoFoto && anterior) {
                //Conjunto de animator:
                AnimatorSet animator = new AnimatorSet();
                //Animacion para el bt agregar vehiculo:
                ObjectAnimator scaleDownX_BtConfirmarRegistro = ObjectAnimator.ofFloat(btActualizarDatos, "scaleX", 1.0f, 0.5f);
                ObjectAnimator scaleDownY_BtConfirmarRegistro = ObjectAnimator.ofFloat(btActualizarDatos, "scaleY", 1.0f, 0.5f);
                animator.play(scaleDownX_BtConfirmarRegistro).with(scaleDownY_BtConfirmarRegistro);
                animator.setDuration(Biblioteca.tAnimacionesScaleBotones);
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
    //Metodo que vuelve visible o gone los atributos en funcion de lo que se le pase:
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
            elige_vehiculo_wey.setVisibility(View.GONE);
            textViewElige_vehiculo_wey.setVisibility(View.GONE);
        }else {
            if(misVehiculosList.size()==1){
                me_siento_vasio.setVisibility(View.VISIBLE);
                me_siento_vasio.playAnimation();
                me_siento_vasio.setRepeatCount(ValueAnimator.INFINITE);
                textViewMe_siento_vasio.setVisibility(View.VISIBLE);
                elige_vehiculo_wey.setVisibility(View.GONE);
                textViewElige_vehiculo_wey.setVisibility(View.GONE);
            }else{
                me_siento_vasio.setVisibility(View.GONE);
                textViewMe_siento_vasio.setVisibility(View.GONE);
                elige_vehiculo_wey.setVisibility(View.VISIBLE);
                elige_vehiculo_wey.playAnimation();
                elige_vehiculo_wey.setRepeatCount(ValueAnimator.INFINITE);
                textViewElige_vehiculo_wey.setVisibility(View.VISIBLE);
            }
        }
    }
    //Metodos para elegir color:
    @Override
    public void onChooseColor(int position, int color) {
        //Si el vehiculo sesion se ha terminado de cargar:
        if(vSesion!=null) {
            //Si es un color valido:
            if (position != -1) {
                //Boolean para comprobar que estado tiene antes de realizar pruebas:
                boolean anterior = false;
                if (pruebaFormatoMarca || pruebaFormatoModelo || pruebaFormatoCombustible || pruebaFormatoColor || pruebaFormatoFoto) {
                    anterior = true;
                }
                //Depende de si la foto es null o no se obtiene un valor u otro:
                pruebaFormatoFoto=uriImagenEndispositivo!=null;
                imgViewColorCoche.setBackgroundColor(color);
                colorSeleccionado = colores.get(position);
                //Si el color ha cambiado y no es nulo:
                if (!colorSeleccionado.equals("") && !colorSeleccionado.equalsIgnoreCase(vSesion.getColor())) {
                    pruebaFormatoColor = true;
                } else {
                    pruebaFormatoColor = false;
                }
                //Si las 5 pruebas de formato son correctas pasamos a liberar el boton de modificar vehiculo:
                if ((pruebaFormatoMarca || pruebaFormatoModelo || pruebaFormatoColor || pruebaFormatoCombustible || pruebaFormatoFoto) && !anterior) {
                    //Conjunto de animator:
                    AnimatorSet animator = new AnimatorSet();
                    //Animacion para el bt modificar vehiculo:
                    ObjectAnimator scaleDownX_BtConfirmarRegistro = ObjectAnimator.ofFloat(btActualizarDatos, "scaleX", 0.5f, 1.0f);
                    ObjectAnimator scaleDownY_BtConfirmarRegistro = ObjectAnimator.ofFloat(btActualizarDatos, "scaleY", 0.5f, 1.0f);
                    animator.play(scaleDownX_BtConfirmarRegistro).with(scaleDownY_BtConfirmarRegistro);
                    animator.setDuration(Biblioteca.tAnimacionesScaleBotones);
                    animator.start();
                    //Habilitamos el boton modificar vehiculo:
                    btActualizarDatos.setEnabled(true);
                    btActualizarDatos.setOnClickListener(this);
                    btActualizarDatos.setColorFilter(getResources().getColor(R.color.colorPrimary));
                }
                //Si ninguna prueba es verdadera, es decir ningun campo ha cambiado, y el boton estaba activado pues se desactiva:
                else if (!pruebaFormatoMarca && !pruebaFormatoModelo && !pruebaFormatoColor && !pruebaFormatoCombustible && !pruebaFormatoFoto && anterior) {
                    //Conjunto de animator:
                    AnimatorSet animator = new AnimatorSet();
                    //Animacion para el bt agregar vehiculo:
                    ObjectAnimator scaleDownX_BtConfirmarRegistro = ObjectAnimator.ofFloat(btActualizarDatos, "scaleX", 1.0f, 0.5f);
                    ObjectAnimator scaleDownY_BtConfirmarRegistro = ObjectAnimator.ofFloat(btActualizarDatos, "scaleY", 1.0f, 0.5f);
                    animator.play(scaleDownX_BtConfirmarRegistro).with(scaleDownY_BtConfirmarRegistro);
                    animator.setDuration(Biblioteca.tAnimacionesScaleBotones);
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
