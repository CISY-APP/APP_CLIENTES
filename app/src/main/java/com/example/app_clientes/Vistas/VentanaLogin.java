package com.example.app_clientes.Vistas;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.app_clientes.R;
//Clase que contiene toda la logica y conexion con la ventana de login de la app:
public class VentanaLogin extends AppCompatActivity implements View.OnClickListener{
    //Atributos pertenecientes a la clase
    private EditText editTextUsuario, editTextClave;
    private Button btIniciarSesion, btRegistrar;
    private TextView txtBtClaveOlvidada;
    //Metodo onCreate encargado de crear la actividad
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ventana_login);
        //Asociamos los elementos XML a los atributos
        editTextUsuario=findViewById(R.id.editTextUsuarioInicioSesion);
        editTextClave=findViewById(R.id.editTextClaveInicioSesion);
        btIniciarSesion=findViewById(R.id.buttonLoginInicioSesion);
        btRegistrar=findViewById(R.id.buttonRegistrarInicioSesion);
        txtBtClaveOlvidada=findViewById(R.id.textViewOlvidasteTuClaveInicioSesion);
        //Vinculamos los botones al listener del metodo onclick, que esta implementado en esta clase
        btIniciarSesion.setOnClickListener(this);
        btRegistrar.setOnClickListener(this);

    }
    //Metodo onClick implementado de la interfaz View.OnClickListener.
    @Override
    public void onClick(View v) {
        //Segun el boton clickado hacemos lo siguiente:
        if(v.equals(btIniciarSesion)){
            //HACER COMPROBACIONES SEGUN DE LOS DATOS LEIDOS EN LOS EDITEXT, Y A TRAVES DE RETROFIT CONSEGUIR LA INFORMACION PARA VALIDARLO
            //-----------------------------------------------------------------------------------------------------------------------------


            //Instanciamos nuestro objeto Intent explicito, ya que en los parametros ponemos que empieza en esta
            //clase que sera el contexto y que iniciara la clase que se encarga de la otra actividad en este caso.
            Intent intentInicioSesion = new Intent(this, VentanaPrincipal.class);
            //Iniciamos la nueva actividad:
            startActivity(intentInicioSesion);
        }else if(v.equals(btRegistrar)){
            //Instanciamos nuestro objeto Intent explicito, ya que en los parametros ponemos que empieza en esta
            //clase que sera el contexto y que iniciara la clase que se encarga de la otra actividad en este caso.
            Intent intentRegistro = new Intent(this, VentanaRegistro.class);
            //Iniciamos la nueva actividad:
            startActivity(intentRegistro);
        }else if(v.equals(txtBtClaveOlvidada)){
            //Instanciamos nuestro objeto Intent explicito, ya que en los parametros ponemos que empieza en esta
            //clase que sera el contexto y que iniciara la clase que se encarga de la otra actividad en este caso.
            Intent intentClaveOlvidada = new Intent(this, VentanaClaveOlvidada.class);
            //Iniciamos la nueva actividad:
            startActivity(intentClaveOlvidada);
        }
    }
}
