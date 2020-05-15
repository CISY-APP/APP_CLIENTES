package com.example.app_clientes.Controlador;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.app_clientes.JSONPLACEHOLDER.JsonPlaceHolderApi;
import com.example.app_clientes.Pojos.Usuario;
import com.example.app_clientes.Pojos.Vehiculo;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Controlador {

    //Devuelve un objeto de tipo cliente
    private Usuario usuario;
    public Usuario getCliente(String email, String clave){
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://192.168.56.1:8080/").addConverterFactory(GsonConverterFactory.create()).build();
        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        Call<Usuario> call = jsonPlaceHolderApi.getUsuario(email, clave);
        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (!response.isSuccessful()) {
                    Log.i("Coder" , response.code()+"" );
                    return;
                }
                usuario = response.body();
            }
            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Log.i("Code: " ,  t.getMessage()+"" );
            }
        });
        return usuario;
    }

    //Devuelve un objeto de tipo vehiculo
    private Vehiculo vehiculo;
    public Vehiculo getVehiculo(String matricula){
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://192.168.56.1:8080/").addConverterFactory(GsonConverterFactory.create()).build();
        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        Call<Vehiculo> call = jsonPlaceHolderApi.getVehiculo(matricula);
        call.enqueue(new Callback<Vehiculo>() {
            @Override
            public void onResponse(Call<Vehiculo> call, Response<Vehiculo> response) {
                if (!response.isSuccessful()) {
                    Log.i("Coder" , response.code()+"" );
                    return;
                }
                vehiculo = response.body();
            }
            @Override
            public void onFailure(Call<Vehiculo> call, Throwable t) {
                Log.i("Code: " ,  t.getMessage()+"" );
            }
        });
        return vehiculo;
    }

}
