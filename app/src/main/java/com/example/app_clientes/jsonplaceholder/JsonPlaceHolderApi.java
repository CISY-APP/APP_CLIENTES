package com.example.app_clientes.jsonplaceholder;


import androidx.annotation.NonNull;

import com.example.app_clientes.pojos.Usuario;
import com.example.app_clientes.pojos.Vehiculo;
import com.example.app_clientes.pojos.Viaje;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;


public interface JsonPlaceHolderApi {
    //METODOS PARA USUARIO********************************************************************************************
    //Login de usuario por nombre usuario y clave, el cual se le pasa por la cabecera
    @GET("loginUser/{usuario}/{clave}")
    Call<Usuario> getUsuarioLogin(@NonNull @Path("usuario") String usuario, @NonNull @Path("clave") String clave);
    //Busca usuario por id, el cual se le pasa por la cabecera
    @GET("consultarUsuarioPorId/{id}")
    Call<Usuario> getUsuarioById(@NonNull @Path("id") Integer id);
    //Registrar usuario por body
    @POST("registrarUsuario")
    Call<Usuario> registrarUsuario(@Body Usuario usuario);
    //Cambiar contrasena de usuario por id usuario
    @PUT("actualizarClaveUsuario")
    Call<Usuario> actualizarClaveUsuario(@Body Map<String, String> param);
    //Dar de baja al usuario por id
    @DELETE("eliminarUsuario/{id}")
    Call<Void> eliminarUsuarioById(@NonNull @Path("id") Integer id);
}