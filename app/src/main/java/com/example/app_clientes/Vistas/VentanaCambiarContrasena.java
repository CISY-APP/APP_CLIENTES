package com.example.app_clientes.Vistas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.app_clientes.R;
import com.example.app_clientes.ui.Datos.DatosFragment;

public class VentanaCambiarContrasena extends AppCompatActivity {


    //AlertDialog
    private EditText ETContrasena;
    private EditText ETcontrasenaNueva;
    private EditText ETcontrasenaNuevaRepetida;
    private ImageView IVAceptar;
    private ImageView IVFlechaAtras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambiar_contrasena);

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
}
