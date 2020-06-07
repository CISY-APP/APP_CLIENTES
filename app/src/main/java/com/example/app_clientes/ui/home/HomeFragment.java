//Indicamos a que paquete pertenece esta clase:
package com.example.app_clientes.ui.home;
//Importamos los siguientes paquetes:
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.app_clientes.Biblioteca;
import com.example.app_clientes.R;
import com.example.app_clientes.jsonplaceholder.JsonPlaceHolderApi;
import com.example.app_clientes.pojos.Vehiculo;
import com.example.app_clientes.vistas.VentanaAgregarVehiculo;
import com.example.app_clientes.vistas.VentanaBuscarViaje;
import com.example.app_clientes.vistas.VentanaPublicarViaje;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
//Clase que contiene toda la logica y conexion con la ventana home de la app:
public class HomeFragment extends Fragment implements View.OnClickListener{
    //Atributos XML:
    private Button btPublicar, btBuscar;
    //Atributos de la clase:
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    //Constructor:
    public HomeFragment() {
    }
    //Metodo que se ejecuta al crearse la vista:
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        //Conectamos el xml:
        View vista = inflater.inflate(R.layout.fragment_home, container, false);
        //Vinculamos los atributos de la clase:
        btPublicar = vista.findViewById(R.id.buttonPublicarViajeFragmentHome);
        btBuscar = vista.findViewById(R.id.buttonBuscarViajeFragmentHome);
        //Vinculamos los botones al listener del metodo onclick, que esta implementado en esta clase:
        btPublicar.setOnClickListener(this);
        btBuscar.setOnClickListener(this);
        //Animaciones tipo scale despues de que todoo se haya realizado:
        btBuscar.post(new Runnable() {
            @Override
            public void run() {
                //Declaramos un animatorSet para poder luego ejecutar un conjunto de animaciones a la vez:
                AnimatorSet animatorSetEscale = new AnimatorSet();
                //Instanciamos el conjunto de animaciones:
                //Animacion para el bt publicar:
                ObjectAnimator scaleDownX_btPublicar = ObjectAnimator.ofFloat(btPublicar, "scaleX", 0.0f, 1.0f);
                ObjectAnimator scaleDownY_btPublicar = ObjectAnimator.ofFloat(btPublicar, "scaleY", 0.0f, 1.0f);
                //Animacion para el bt buscar:
                ObjectAnimator scaleDownX_btBuscar = ObjectAnimator.ofFloat(btBuscar, "scaleX", 0.0f, 1.0f);
                ObjectAnimator scaleDownY_btBuscar = ObjectAnimator.ofFloat(btBuscar, "scaleY", 0.0f, 1.0f);
                //Configuramos el animatorSet, como se tienen que reproducir las animaciones, el tiempo que duran, que clase de interpolador utilizan y la lanzamos:
                animatorSetEscale.play(scaleDownX_btPublicar).with(scaleDownY_btPublicar)
                        .with(scaleDownX_btBuscar).with(scaleDownY_btBuscar);
                animatorSetEscale.setDuration(Biblioteca.tAnimacionesScaleInicial);
                animatorSetEscale.setInterpolator(new AccelerateDecelerateInterpolator());
                animatorSetEscale.start();
            }
        });
        return vista;
    }
    //Metodo de la interfaz View.OnClickListener:
    @Override
    public void onClick(View v){
        //Depende del boton realizamos una cosa u otra:
        if(v.equals(btBuscar)){
            Intent ventanaBuscarViaje = new Intent(getContext(), VentanaBuscarViaje.class);
            startActivity(ventanaBuscarViaje);
        }
        else if(v.equals(btPublicar)){
            //Comprobamos que existan vehiculos antes de que el usuario pueda publicar un viaje:
            //Creamos objeto Retrofit, para lanzar peticiones y poder recibir respuestas:
            Retrofit retrofit = new Retrofit.Builder().baseUrl(Biblioteca.ip).addConverterFactory(GsonConverterFactory.create()).build();
            //Vinculamos el cliente con la interfaz:
            final JsonPlaceHolderApi peticiones = retrofit.create(JsonPlaceHolderApi.class);
            //Creamos una peticion para obtener la lista de vehiculos asociada al usuario:
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
                    List<Vehiculo> lista = response.body();
                    //Si la lista de vehiculos del usuario no esta vacia:
                    if(!lista.isEmpty()){
                        //LLevamos a la ventana de publicar viaje con la creacion del siguiente intent implicito:
                        Intent ventanaPublicarViaje = new Intent(getContext(), VentanaPublicarViaje.class);
                        startActivity(ventanaPublicarViaje);
                    }else{
                        Toast.makeText(getContext(), getText(R.string.txt_mensajeNoVehiculo_Toast_ventanaHomeFragment), Toast.LENGTH_LONG).show();
                        //LLevamos a la ventana de registrar vehiculo:
                        Intent ventanaRegistrarVehiculo = new Intent(getContext(), VentanaAgregarVehiculo.class);
                        startActivity(ventanaRegistrarVehiculo);
                    }
                }
                //En caso de que no responda el servidor mostramos mensaje de error:
                @Override
                public void onFailure(Call<List<Vehiculo>> call, Throwable t) {
                    Toast.makeText(getContext(), "El servidor esta caido.", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
