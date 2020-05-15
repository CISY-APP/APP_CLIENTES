package com.example.app_clientes.vistas;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app_clientes.R;

public class VentanaViajeNoEncontrado extends AppCompatActivity {

    private Button BTVolver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ventana_viaje_no_encontrado);

        BTVolver = findViewById(R.id.BTVolver);
        BTVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
}
