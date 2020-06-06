//Indicamos a que paquete pertenece esta clase:
package com.example.app_clientes.vistas;
//Importamos los siguientes paquetes:
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.example.app_clientes.Biblioteca;
import com.example.app_clientes.adapter.MiAdapterViajesEncontrados;
import com.example.app_clientes.item.ItemViajesEncontrados;
import com.example.app_clientes.R;
import com.example.app_clientes.jsonplaceholder.JsonPlaceHolderApi;
import com.example.app_clientes.pojos.Reserva;
import com.example.app_clientes.pojos.Viaje;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
//Clase que contiene toda la logica y conexion con la ventana de viajes encontrados de la app:
public class VentanaViajesEncontrados  extends AppCompatActivity {
    //Atributos de la clase:
    private MiAdapterViajesEncontrados miAdapterViajesEncontrados;
    private RecyclerView recyclerViewViajesEncontrados;
    private ArrayList<ItemViajesEncontrados> viajesEncontradosList = new ArrayList<>();
    private String ID_USUARIO;
    private String localidadOrigen, lugarOrigen, localidadDestino, lugarDestino, precio, fechaElegida;
    private List<Viaje> listViajes = new ArrayList<Viaje>();
    //Atributos XML:
    private LottieAnimationView person_tristesito;
    private TextView textViewPersonTristesito;
    private ImageView btVolver;
    private SwipeRefreshLayout swipeRefreshLayout;
    //Metodo que se ejecuta al crearse la vista:
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Conectamos el xml:
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ventana_viajes_encontrados);
        //Asociamos el id del usuario en sesion a la siguiente variable:
        ID_USUARIO =  Biblioteca.usuarioSesion.getIdusuario().toString();
        //Vinculamos los atributos de la clase:
        person_tristesito =findViewById(R.id.animation_vacio_viajes_encontrados);
        textViewPersonTristesito =findViewById(R.id.textViewTituloAnimacionVacioViajesEncontrados);
        btVolver = findViewById(R.id.btFlechaAtrasViajesEncontrados);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayoutViajesEncontrados);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.colorBlanco);
        recyclerViewViajesEncontrados = findViewById(R.id.RVViajesEncontrados);
        //Obtenemos los datos enviados desde la ventana anterior, para asi poder hacer la consulta al servidor:
        recibirDatosViaje();
        //Obtenemos la lista con los viajes filtrados gracias a los atributos puestos:
        obtenerYcargarListaViajes();
        btVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //Le damos oyente al swipeRefreshLayout:
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //Recargamos los datos y el recyclerview
                obtenerYcargarListaViajes();
                //Cancelamos la animacion
                swipeRefreshLayout.setRefreshing(false);
            }
        });
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
    //Metodo que recibe los datos de la ventana buscar viaje, y los guarda en los atributos de esta ventana para poder trabajar con ellos:
    private void recibirDatosViaje() {
        Bundle datosRecibidos = getIntent().getExtras();
        localidadOrigen = datosRecibidos.getString("localidadOrigen");
        lugarOrigen = datosRecibidos.getString("lugarOrigen");
        localidadDestino = datosRecibidos.getString("localidadDestino");
        lugarDestino = datosRecibidos.getString("lugarDestino");
        fechaElegida = datosRecibidos.getString("fechaElegida");
        precio = datosRecibidos.getString("precio");
    }
    //Metodo que recibe una lista de viajes a traves de una consulta al servidor con los datos cargados de la ventana de buscar viaje:
    private void obtenerYcargarListaViajes(){
        //Creamos objeto Retrofit, para lanzar peticiones y poder recibir respuestas:
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Biblioteca.ip).addConverterFactory(GsonConverterFactory.create()).build();
        //Vinculamos el cliente con la interfaz:
        JsonPlaceHolderApi peticiones = retrofit.create(JsonPlaceHolderApi.class);
        //Creamos una peticion para obtener una lista de viajes filtrados con los datos pasados por el body:
        Map<String, String> infoMap = new HashMap<String, String>();
        infoMap.put("idUsuario",Biblioteca.usuarioSesion.getIdusuario().toString());
        infoMap.put("localidadOrigen", localidadOrigen);
        infoMap.put("lugarSalida", lugarOrigen);
        infoMap.put("localidadDestino", localidadDestino);
        infoMap.put("lugarLlegada", lugarDestino);
        infoMap.put("fechaSalida", fechaElegida);
        infoMap.put("precioMax", precio);
        Call<List<Viaje>> call = peticiones.getListViajesFiltrados(infoMap);
        //Ejecutamos la petición en un hilo en segundo plano, retrofit lo hace por nosotros y esperamos a la respuesta:
        call.enqueue(new Callback<List<Viaje>>() {
            //Gestionamos la respuesta del servidor:
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<List<Viaje>> call, Response<List<Viaje>> response) {
                //Respuesta del servidor con un error y paramos el flujo del programa, indicando el codigo de error:
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Codigo de error: " + response.code(), Toast.LENGTH_LONG).show();
                    return;
                }
                //Si la respuesta del servidor es existosa obtenemos la lista, eso si, antes limpiamos la lista actual:
                listViajes.clear();
                viajesEncontradosList.clear();
                //Recogemos la lista y la guardamos para el modelo del recycler view:
                listViajes = response.body();
                if (listViajes!=null&&!listViajes.isEmpty()) {
                    //Bucle for para ir rellenando la lista de items de viajes, con la lista de viajes obtenida:
                    for (Viaje vl : listViajes) {
                        //Comprobamos que ese viaje sea mayor que la fecha actual, ya que entre que se ha enviado la peticion y demas puede haber cambiado su estado:
                        Date fechaSalida = vl.getFechasalida();
                        Date fechaActual = new Date(Calendar.getInstance().getTime().getTime());
                        if (fechaSalida.compareTo(fechaActual) > 0) {
                            //Si la foto de usuario esta null, le damos estado de cadena vacia:
                            if (vl.getUsuario().getFotousuario() == null) {
                                vl.getUsuario().setFotousuario("");
                            }
                            //Calculamos los años de la persona utilizando el siguiente metodo, siempre y cuando no este a null la fecha de nacimiento:
                            String aniosRv = "0";
                            if (vl.getUsuario().getFechanacimiento() == null) {
                                aniosRv = "";
                            } else {
                                aniosRv = Biblioteca.obtieneEdad(vl.getUsuario().getFechanacimiento());
                            }
                            //Si el telefono es null, le damos valor de cadena vacia y sino cogemos el telefono:
                            String telefonoRv = "";
                            if (vl.getUsuario().getTelefono() == null) {
                                telefonoRv = "";
                            } else {
                                telefonoRv = vl.getUsuario().getTelefono() + "";
                            }
                            //Si la foto del vehiculo esta null, le damos el valor de cadena vacia:
                            if (vl.getVehiculo().getFotovehiculo() == null) {
                                vl.getVehiculo().setFotovehiculo("");
                            }
                            //Obtenemos la fecha de salida del viaje en el formato que nos interesa, y le sumamos dos horas para contrarestar el problema de que en el emulador se cogen dos horas menos:
                            String fechaSalidaRv = Biblioteca.obtieneHoraViajesEncontrados(vl.getFechasalida());
                            //Añadimos items de viajes a la lista requerida para el recycler view:
                            viajesEncontradosList.add(
                                    new ItemViajesEncontrados(vl.getUsuario().getIdusuario().toString(), vl.getUsuario().getFotousuario(),
                                            vl.getUsuario().getNombre() + " " + vl.getUsuario().getApellidos(), aniosRv + " años",
                                            telefonoRv, vl.getIdviaje().toString(), vl.getLocalidadOrigen() + " - " + vl.getLugarSalida(),
                                            vl.getLocalidadDestino() + " - " + vl.getLugarLlegada(), fechaSalidaRv, vl.getPrecio() + "€",
                                            vl.getVehiculo().getIdvehiculo().toString(), vl.getVehiculo().getFotovehiculo(), vl.getVehiculo().getMatricula(),
                                            vl.getVehiculo().getMarca() + " - " + vl.getVehiculo().getModelo(), vl.getVehiculo().getColor(),
                                            vl.getVehiculo().getCombustible(),vl.getFechasalida()));
                        }
                    }
                    //ACTUALIZAMOS VISION DE ELEMENTOS SI LA LISTA DE VIAJES ENCONTRADOS NO ESTA VACIA:
                    if (viajesEncontradosList!=null&&!viajesEncontradosList.isEmpty()) {
                        //Reiniciamos vision de componentes:
                        recyclerViewViajesEncontrados.setVisibility(View.VISIBLE);
                        person_tristesito.setVisibility(View.GONE);
                        textViewPersonTristesito.setVisibility(View.GONE);
                        //INSTANCIAMOS Y ASOCIAMOS ELEMENTOS NECESARIOS PARA EL CORRECTO FUNCIONAMIENTO DEL RECYCLERVIEW:
                        recyclerViewViajesEncontrados.setHasFixedSize(true);// RecyclerView sabe de antemano que su tamaño no depende del contenido del adaptador, entonces omitirá la comprobación de si su tamaño debería cambiar cada vez que se agregue o elimine un elemento del adaptador.(mejora el rendimiento)
                        recyclerViewViajesEncontrados.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));//Asociamos al recyclerView el layoutManager que creamos en el paso anterior
                        miAdapterViajesEncontrados = new MiAdapterViajesEncontrados(getApplicationContext(), viajesEncontradosList);//Instanciamos un objeto de tipo Example_Adapter
                        recyclerViewViajesEncontrados.setAdapter(miAdapterViajesEncontrados);//Vinculamos el adapter al recyclerView
                        miAdapterViajesEncontrados.setOnClickListener(new MiAdapterViajesEncontrados.OnItemClickListener() {
                            @Override
                            public void OnMensajeClick(int position) {
                                Intent VentanaChatIndividual = new Intent(getApplicationContext(), VentanaChatIndividual.class);
                                VentanaChatIndividual.putExtra("ID_USUARIO_CONVER", viajesEncontradosList.get(position).getCod_usuario());
                                VentanaChatIndividual.putExtra("NOMBRE_USUARIO_CONVER", viajesEncontradosList.get(position).getNombreApellidos());
                                VentanaChatIndividual.putExtra("FOTO_USUARIO_CONVER", viajesEncontradosList.get(position).getUriImagenUsuario());
                                startActivity(VentanaChatIndividual);
                            }

                            @Override
                            public void OnReservaClick(int position) {
                                //Comprobamos que ese viaje sea mayor que la fecha actual, ya que entre que se ha enviado la peticion y demas puede haber cambiado su estado:
                                Date fechaSalida = viajesEncontradosList.get(position).getFechaFormato();
                                Date fechaActual = new Date(Calendar.getInstance().getTime().getTime());
                                if (fechaSalida.compareTo(fechaActual) > 0) {
                                    //Creamos objeto Retrofit, para lanzar peticiones y poder recibir respuestas:
                                    Retrofit retrofit = new Retrofit.Builder().baseUrl(Biblioteca.ip).addConverterFactory(GsonConverterFactory.create()).build();
                                    //Vinculamos el cliente con la interfaz:
                                    JsonPlaceHolderApi peticiones = retrofit.create(JsonPlaceHolderApi.class);
                                    //Creamos una peticion para registrar una reserva por el body:
                                    Map<String, String> infoMap = new HashMap<String, String>();
                                    infoMap.put("idUsuario", Biblioteca.usuarioSesion.getIdusuario().toString());
                                    infoMap.put("idViaje", viajesEncontradosList.get(position).getCod_viaje());
                                    Call<Reserva> call = peticiones.registraReserva(infoMap);
                                    //Ejecutamos la petición en un hilo en segundo plano, retrofit lo hace por nosotros y esperamos a la respuesta:
                                    call.enqueue(new Callback<Reserva>() {
                                        //Gestionamos la respuesta del servidor:
                                        @Override
                                        public void onResponse(Call<Reserva> call, Response<Reserva> response) {
                                            //Respuesta del servidor con un error y paramos el flujo del programa, indicando el codigo de error:
                                            if (!response.isSuccessful()) {
                                                if(response.code()==404){
                                                    Toast.makeText(getApplicationContext(), "Viaje ya no disponible.", Toast.LENGTH_LONG).show();
                                                    obtenerYcargarListaViajes();
                                                }else{
                                                    Toast.makeText(getApplicationContext(), "Codigo de error: " + response.code(), Toast.LENGTH_LONG).show();
                                                }
                                                return;
                                            }
                                            //Si la respuesta del servidor es existosa obtenemos la lista, eso si, antes limpiamos la lista actual:
                                            Intent VentanaPublicarViaje = new Intent(getApplicationContext(), VentanaViajeReservado.class);
                                            startActivity(VentanaPublicarViaje);
                                            finish();
                                        }

                                        //En caso de que no responda el servidor mostramos mensaje de error:
                                        @Override
                                        public void onFailure(Call<Reserva> call, Throwable t) {
                                            Toast.makeText(getApplicationContext(), "El servidor esta caido.", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }else{
                                    Toast.makeText(getApplicationContext(), "Viaje ya no disponible.", Toast.LENGTH_LONG).show();
                                    obtenerYcargarListaViajes();
                                }
                            }
                        });
                    }else{
                        //Reiniciamos vision de componentes:
                        recyclerViewViajesEncontrados.setVisibility(View.GONE);
                        person_tristesito.playAnimation();
                        person_tristesito.setRepeatCount(ValueAnimator.INFINITE);
                        person_tristesito.setVisibility(View.VISIBLE);
                        textViewPersonTristesito.setVisibility(View.VISIBLE);
                    }
                }else{
                    //Reiniciamos vision de componentes:
                    recyclerViewViajesEncontrados.setVisibility(View.GONE);
                    person_tristesito.playAnimation();
                    person_tristesito.setRepeatCount(ValueAnimator.INFINITE);
                    person_tristesito.setVisibility(View.VISIBLE);
                    textViewPersonTristesito.setVisibility(View.VISIBLE);
                }
            }
            //En caso de que no responda el servidor mostramos mensaje de error:
            @Override
            public void onFailure(Call<List<Viaje>> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"El servidor esta caido.", Toast.LENGTH_LONG).show();
            }
        });
    }
}
