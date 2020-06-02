//Indicamos a que paquete pertenece esta clase:
package com.example.app_clientes.vistas;
//Importamos los siguientes paquetes:
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.app_clientes.Biblioteca;
import com.example.app_clientes.adapter.MiAdapterViajesEncontrados;
import com.example.app_clientes.item.ItemViajesEncontrados;
import com.example.app_clientes.R;
import com.example.app_clientes.jsonplaceholder.JsonPlaceHolderApi;
import com.example.app_clientes.pojos.Viaje;
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
    private final ArrayList<ItemViajesEncontrados> viajesEncontradosList = new ArrayList<>();
    private String ID_USUARIO;
    private String localidadOrigen, lugarOrigen, localidadDestino, lugarDestino, precio, fechaElegida;
    private List<Viaje> listViajes = new ArrayList<Viaje>();
    //Atributos XML:
    private ImageView btVolver;
    //Metodo que se ejecuta al crearse la vista:
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Conectamos el xml:
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ventana_viajes_encontrados);
        //Asociamos el id del usuario en sesion a la siguiente variable:
        ID_USUARIO =  Biblioteca.usuarioSesion.getIdusuario().toString();
        //Vinculamos los atributos de la clase:
        btVolver = findViewById(R.id.btFlechaAtrasViajesEncontrados);
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
                    Toast.makeText(getApplicationContext(), "Code: " + response.code(), Toast.LENGTH_LONG).show();
                    return;
                }
                //Si la respuesta del servidor es existosa obtenemos la lista, eso si, antes limpiamos la lista actual:
                listViajes.clear();
                //Recogemos la lista y la guardamos para el modelo del recycler view
                listViajes = response.body();
                if (listViajes!=null) {
                    for (Viaje vl : listViajes) {
                        if(vl.getUsuario().getFotousuario()==null){vl.getUsuario().setFotousuario("");}
                        String fechaNacRv="";
                        String aniosRv="0";
                        if(vl.getUsuario().getFechanacimiento()==null){fechaNacRv="";aniosRv="";}
                        else{
                            Date fechaActual = new Date(Calendar.getInstance().getTime().getTime());
                            SimpleDateFormat form = new SimpleDateFormat("yyyy");
                            fechaNacRv=form.format(vl.getUsuario().getFechanacimiento());
                            String fechaActRv=form.format(fechaActual);
                            int a = Integer.parseInt(fechaNacRv);
                            int b = Integer.parseInt(fechaActRv);
                            b=b-a;
                            aniosRv=b+"";
                        }
                        String telefonoRv="";
                        if(vl.getUsuario().getTelefono()==null){telefonoRv="";}
                        else{telefonoRv=vl.getUsuario().getTelefono()+"";}
                        if(vl.getVehiculo().getFotovehiculo()==null){vl.getVehiculo().setFotovehiculo("");}
                        SimpleDateFormat form = new SimpleDateFormat("dd / MM / yyyy HH:mm");
                        String fechaSalidaRv=form.format(vl.getFechasalida());
                        fechaSalidaRv=fechaSalidaRv.substring(0,13)+ "a las "+fechaSalidaRv.substring(15);

                        viajesEncontradosList.add(
                                new ItemViajesEncontrados(vl.getUsuario().getIdusuario().toString(), vl.getUsuario().getFotousuario(),
                                        vl.getUsuario().getNombre() + " " + vl.getUsuario().getApellidos(), aniosRv,
                                        telefonoRv, vl.getIdviaje().toString(), vl.getLocalidadOrigen() + " - " + vl.getLugarSalida(),
                                        vl.getLocalidadDestino() + " - " + vl.getLugarLlegada(), fechaSalidaRv, vl.getPrecio() + "€",
                                        vl.getVehiculo().getIdvehiculo().toString(), vl.getVehiculo().getFotovehiculo(), vl.getVehiculo().getMatricula(),
                                        vl.getVehiculo().getMarca() + " - " + vl.getVehiculo().getModelo(), vl.getVehiculo().getColor(), vl.getVehiculo().getCombustible()));
                    }
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
                            startActivity(VentanaChatIndividual);
                        }

                        @Override
                        public void OnReservaClick(int position) {
                            //AQUI DENTRO SE DEBE DE HACER LA LLAMADA CON RETROFIT
                            Intent VentanaPublicarViaje = new Intent(getApplicationContext(), VentanaViajeReservado.class);
                            //VentanaPublicarViaje.putExtra("usuario",ETUsuario.getText().toString());
                            //VentanaPublicarViaje.putExtra("control",ETControl.getText().toString());
                            startActivity(VentanaPublicarViaje);
                        }
                    });
                }else{

                }
            }
            //En caso de que no responda el servidor mostramos mensaje de error:
            @Override
            public void onFailure(Call<List<Viaje>> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
