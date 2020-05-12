package com.example.app_clientes.Vistas;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app_clientes.R;

public class VentanaCambiarContrasena extends AppCompatActivity {


    //AlertDialog
    private EditText ETContrasena;
    private EditText ETcontrasenaNueva;
    private EditText ETcontrasenaNuevaRepetida;
    private ImageView IVAceptar;
    private ImageView IVFlechaAtras;

    private String ID_USUARIO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambiar_contrasena);

        ID_USUARIO = cargarCredencialesIdUsuario();

        ETContrasena = findViewById(R.id.ETContrasena);
        ETcontrasenaNueva = findViewById(R.id.ETcontrasenaNueva);
        ETContrasena = findViewById(R.id.ETContrasena);
        IVAceptar = findViewById(R.id.IVAceptar);
        IVAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        IVFlechaAtras = findViewById(R.id.IVFlechaAtras);
        IVFlechaAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onBackPressed();
            }
        });

    }

    private String cargarCredencialesIdUsuario(){
        SharedPreferences credenciales = getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        return credenciales.getString("idUsuario","0");
    }
}
