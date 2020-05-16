package com.example.app_clientes.jsonplaceholder;


import androidx.annotation.NonNull;

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
    //Busca cliente por usuario y clave, el cual se le pasa por la cabecera
    @GET("loginUser/{usuario}/{clave}")
    Call<Usuario> getUsuarioLogin(@NonNull @Path("usuario") String usuario, @NonNull @Path("clave") String clave);
    //Busca cliente por id, el cual se le pasa por la cabecera
    @GET("consultarUsuarioPorId/{id}")
    Call<Usuario> getUsuarioById(@NonNull @Path("id") Integer id);

}