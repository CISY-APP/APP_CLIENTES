package com.example.app_clientes.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

public class HomeFragment extends Fragment {

    private Button BTPublicar;
    private Button BTBuscar;
    private EditText ETFecha;
    private EditText ETHora;
    private EditText ETOrigen;
    private EditText ETDestino;
    private Button BTBuscarDialog;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    public HomeFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_home, container, false);

        //Asociamos los atributos de la clase con los item del fragment.
        BTPublicar = vista.findViewById(R.id.BTPublicar);
        BTPublicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llamadaVentanaPublicarViaje();
            }
        });

        BTBuscar = vista.findViewById(R.id.BTBuscar);
        BTBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llamadaVentanaBuscarViaje();
            }
        });
        return vista;
    }

    private void llamadaVentanaBuscarViaje() {
        VentanaBuscarViaje ventanaBuscarViaje = new VentanaBuscarViaje();
        Intent VentanaBuscarViaje = new Intent(getContext(), ventanaBuscarViaje.getClass());
        startActivity(VentanaBuscarViaje);
        //showDialogBuscarViaje();
    }

    //Llamamos al  ventanaPublicarViaje
    private void llamadaVentanaPublicarViaje() {
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
                    Toast.makeText(getContext(), "Code: " + response.code(), Toast.LENGTH_LONG).show();
                    return;
                }
                List<Vehiculo> lista = response.body();
                //Si la lista de vehiculos del usuario no esta vacia:
                if(!lista.isEmpty()){
                    //LLevamos a la ventana de publicar viaje con la creacion del siguiente intent implicito:
                    Intent ventanaPublicarViaje = new Intent(getContext(), VentanaPublicarViaje.class);
                    startActivity(ventanaPublicarViaje);
                }else{
                    Toast.makeText(getContext(), "Antes de publicar un viaje agregue un vehiculo por favor.", Toast.LENGTH_LONG).show();
                    //LLevamos a la ventana de registrar vehiculo:
                    Intent ventanaRegistrarVehiculo = new Intent(getContext(), VentanaAgregarVehiculo.class);
                    startActivity(ventanaRegistrarVehiculo);
                }
            }
            //En caso de que no responda el servidor mostramos mensaje de error:
            @Override
            public void onFailure(Call<List<Vehiculo>> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}
