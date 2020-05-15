package com.example.app_clientes.jsonplaceholder;


import com.example.app_clientes.pojos.Usuario;
import com.example.app_clientes.pojos.Vehiculo;
import com.example.app_clientes.pojos.Viaje;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;


public interface JsonPlaceHolderApi {

    //Hace una llamada al servidor para insertar un pedido
    @POST("registrarViaje")
    Call<Viaje> createViaje(@Body Viaje viaje);

    //Recupera un cliente por DNI
    @GET("/loginUser/{email}/{clave}")
    Call<Usuario> getUsuario(@Path("email") String email, @Path("clave") String clave);

    //Recupera un vehiculos por DNI
    @GET("/consultarVehiculoPorMatricula/{matricula}")
    Call<Vehiculo> getVehiculo(@Path("matricula") String matricula);

    //Recupera un vehiculos por DNI
    @GET("/consultarVehiculoPorIdUsuario/{id}")
    Call<List<Vehiculo>> getVehiculoIdUsuaio(@Path("idUsuario") String idUsuario);
}