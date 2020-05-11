package com.example.app_clientes.Vistas;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app_clientes.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class VentanaChatIndividual extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private String cod_conducto_1;
    private String cod_conducto_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_individual);

        //Implementacion de firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(cod_conducto_1 + " - " + recibirDatosViaje()); //Sala de chat (nombre)



    }

    private String recibirDatosViaje() {
        Bundle datosIN=getIntent().getExtras();
        cod_conducto_1 = datosIN.getString("COD_CONDUCTOR_1");
        cod_conducto_2 = datosIN.getString("COD_CONDUCTOR_2");
        return nombreUsuario(cod_conducto_2);
    }

    private String nombreUsuario(String cod_conducto_2){
        String nombreUsuario = null;
        //cod_conducto_2 consulta a retrofit para conseguir el nombre de usuario,
        return nombreUsuario;

    }
}
