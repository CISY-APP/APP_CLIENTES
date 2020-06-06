//Indicamos a que paquete pertenece esta clase:
package com.example.app_clientes.ui.viajes;
//Importamos los siguientes paquetes:
import android.animation.ValueAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.airbnb.lottie.LottieAnimationView;
import com.example.app_clientes.Biblioteca;
import com.example.app_clientes.R;
import com.example.app_clientes.adapter.MiAdapterTusViajesPublicados;
import com.example.app_clientes.jsonplaceholder.JsonPlaceHolderApi;
import com.example.app_clientes.pojos.ViajePublicado;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
//Clase que contiene toda la logica y conexion con la ventana de mostrar los viajes disfrutados o a disfrutar de la app:
public class TusViajesPublicadosFragment extends Fragment {
    //Atributos XML:
    private LottieAnimationView me_siento_vasio ;
    private TextView textViewMe_siento_vasio;
    private LinearLayout linearLayoutAnimacion;
    //Atributos clase:
    private RecyclerView recyclerViewTusViajes;
    private MiAdapterTusViajesPublicados miAdapterTusViajes;
    private List<ViajePublicado> misViajesList = new ArrayList<>();
    //Metodo que se ejecuta al crearse la vista:
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Conectamos el xml:
        View view = inflater.inflate(R.layout.fragment_tusviajespublicados, container, false);
        //Vinculamos los atributos de la clase:
        recyclerViewTusViajes = view.findViewById(R.id.RVViajesPublicados);
        me_siento_vasio = view.findViewById(R.id.animation_vacio_ViajesPublicados);
        textViewMe_siento_vasio = view.findViewById(R.id.textViewTituloAnimacionVacioViajesPublicados);
        linearLayoutAnimacion = view.findViewById(R.id.linearLayoutPrincipalAnimacionViajesPublicados);
        //Cargamos los viajes:
        cargarViajesViajesDisfrutados();
        //Recibidor de broadcast para cerrar sesion:
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent intent) {
                String action = intent.getAction();
                if (action.equals("cierre_de_sesion")) {
                    requireActivity().finish();
                }
            }
        };
        requireActivity().registerReceiver(broadcastReceiver, new IntentFilter("cierre_de_sesion"));
        return view;
    }

    //CARGA LOS DATOS DE LOS VIAJES QUE EL USUARIO A DISFRUTADO VIAJES
    private void cargarViajesViajesDisfrutados() {
        //Creamos objeto Retrofit, para lanzar peticiones y poder recibir respuestas:
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Biblioteca.ip).addConverterFactory(GsonConverterFactory.create()).build();
        //Vinculamos el cliente con la interfaz:
        final JsonPlaceHolderApi peticiones = retrofit.create(JsonPlaceHolderApi.class);
        //Creamos una peticion para obtener los viajes que ha publicado:
        Call<List<ViajePublicado>> call = peticiones.getListViajesPublicados(Biblioteca.usuarioSesion.getIdusuario());
        //Ejecutamos la petición en un hilo en segundo plano, retrofit lo hace por nosotros y esperamos a la respuesta:
        call.enqueue(new Callback<List<ViajePublicado>>() {
            //Gestionamos la respuesta del servidor:
            @Override
            public void onResponse(Call <List<ViajePublicado>> call, Response<List<ViajePublicado>> response) {
                //Respuesta del servidor con un error y paramos el flujo del programa, indicando el codigo de error:
                if (!response.isSuccessful()) {
                    if(response.code()==404){
                        //Cambiamos la visibilidad
                        recyclerViewTusViajes.setVisibility(View.GONE);
                        linearLayoutAnimacion.setVisibility(View.VISIBLE);
                        me_siento_vasio.playAnimation();
                        me_siento_vasio.setRepeatCount(ValueAnimator.INFINITE);
                    }else {
                        Toast.makeText(getContext(), "Code: " + response.code(), Toast.LENGTH_LONG).show();
                    }
                    return;
                }
                //Si la respuesta es correcta pasamos a rellenar el modelo que utliza el recycler view:
                misViajesList=response.body();
                if (misViajesList!=null&&!misViajesList.isEmpty()) {
                    //INSTANCIAMOS Y ASOCIAMOS ELEMENTOS NECESARIOS PARA EL CORRECTO FUNCIONAMIENTO DEL RECYCLERVIEW
                    recyclerViewTusViajes.setHasFixedSize(true);// RecyclerView sabe de antemano que su tamaño no depende del contenido del adaptador, entonces omitirá la comprobación de si su tamaño debería cambiar cada vez que se agregue o elimine un elemento del adaptador.(mejora el rendimiento)
                    recyclerViewTusViajes.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                    miAdapterTusViajes = new MiAdapterTusViajesPublicados(getContext(), misViajesList);//Instanciamos un objeto de tipo Example_Adapter
                    recyclerViewTusViajes.setAdapter(miAdapterTusViajes);//Vinculamos el adapter al recyclerView
                    miAdapterTusViajes.setOnClickListener(new MiAdapterTusViajesPublicados.OnItemClickListener() {
                        @Override
                        public void OnEliminarViajePublicadoClick(int position) {
                            //Creamos objeto Retrofit, para lanzar peticiones y poder recibir respuestas:
                            Retrofit retrofit = new Retrofit.Builder().baseUrl(Biblioteca.ip).addConverterFactory(GsonConverterFactory.create()).build();
                            //Vinculamos el cliente con la interfaz:
                            final JsonPlaceHolderApi peticiones = retrofit.create(JsonPlaceHolderApi.class);
                            //Creamos una peticion para cancelar un viaje:
                            Call<Void> call = peticiones.eliminarViajeById(misViajesList.get(position).getIdviaje());
                            //Ejecutamos la petición en un hilo en segundo plano, retrofit lo hace por nosotros y esperamos a la respuesta:
                            call.enqueue(new Callback<Void>() {
                                //Gestionamos la respuesta del servidor:
                                @Override
                                public void onResponse(Call <Void> call, Response<Void> response) {
                                    //Respuesta del servidor con un error y paramos el flujo del programa, indicando el codigo de error:
                                    if (!response.isSuccessful()) {
                                        Toast.makeText(getContext(), "Code: " + response.code(), Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                    //Si la respuesta es correcta pasamos a rellenar el modelo que utliza el recycler view:
                                    Toast.makeText(getContext(), "Viaje cancelado con exito.", Toast.LENGTH_LONG).show();
                                    cargarViajesViajesDisfrutados();
                                }
                                //En caso de que no responda el servidor mostramos mensaje de error:
                                @Override
                                public void onFailure(Call <Void> call, Throwable t) {
                                    Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    });
                    recyclerViewTusViajes.setVisibility(View.VISIBLE);
                    linearLayoutAnimacion.setVisibility(View.GONE);
                }else{
                    recyclerViewTusViajes.setVisibility(View.GONE);
                    linearLayoutAnimacion.setVisibility(View.VISIBLE);
                    me_siento_vasio.playAnimation();
                    me_siento_vasio.setRepeatCount(ValueAnimator.INFINITE);
                }
            }
            //En caso de que no responda el servidor mostramos mensaje de error:
            @Override
            public void onFailure(Call <List<ViajePublicado>> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}