package com.example.app_clientes.vistas;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.app_clientes.R;

//Clase que contiene toda la logica y conexion con la ventana de login de la app:
public class VentanaLogin extends AppCompatActivity implements View.OnClickListener{
    //Atributos pertenecientes a la clase
    private EditText editTextUsuario, editTextClave;
    private Button btIniciarSesion, btRegistrar;
    private TextView txtBtClaveOlvidada, txtInformativoRegistro;
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
        txtInformativoRegistro=findViewById(R.id.textViewNoCuentaInicioSesion);

        //Vinculamos los botones al listener del metodo onclick, que esta implementado en esta clase
        btIniciarSesion.setOnClickListener(this);
        btRegistrar.setOnClickListener(this);

        //Animaciones
        txtInformativoRegistro.post(new Runnable() {
            @Override
            public void run() {
                //Conjuntos de animators
                AnimatorSet animatorSetEscale = new AnimatorSet();
                //Animacion para el boton incio sesion
                ObjectAnimator scaleDownX_inicioSesion = ObjectAnimator.ofFloat(btIniciarSesion, "scaleX", 0.0f, 1.0f);
                ObjectAnimator scaleDownY_inicioSesion = ObjectAnimator.ofFloat(btIniciarSesion, "scaleY", 0.0f, 1.0f);
                //Animacion para el boton registrar
                ObjectAnimator scaleDownX_registro = ObjectAnimator.ofFloat(btRegistrar, "scaleX", 0.0f, 1.0f);
                ObjectAnimator scaleDownY_registro = ObjectAnimator.ofFloat(btRegistrar, "scaleY", 0.0f, 1.0f);
                //Animacion para el edittext Usuario
                ObjectAnimator scaleDownX_TxtUsuario = ObjectAnimator.ofFloat(editTextUsuario, "scaleX", 0.0f, 1.0f);
                ObjectAnimator scaleDownY_TxtUsuario = ObjectAnimator.ofFloat(editTextUsuario, "scaleY", 0.0f, 1.0f);
                //Animacion para el edittext clave
                ObjectAnimator scaleDownX_TxtClave = ObjectAnimator.ofFloat(editTextClave, "scaleX", 0.0f, 1.0f);
                ObjectAnimator scaleDownY_TxtClave = ObjectAnimator.ofFloat(editTextClave, "scaleY", 0.0f, 1.0f);
                //Animacion para el textview clave olvidada
                ObjectAnimator scaleDownX_TxtClaveOlvidada = ObjectAnimator.ofFloat(txtBtClaveOlvidada, "scaleX", 0.0f, 1.0f);
                ObjectAnimator scaleDownY_TxtClaveOlvidada = ObjectAnimator.ofFloat(txtBtClaveOlvidada, "scaleY", 0.0f, 1.0f);
                //Animacion para el boton registrarse
                ObjectAnimator scaleDownX_TxtRegistro = ObjectAnimator.ofFloat(txtInformativoRegistro, "scaleX", 0.0f, 1.0f);
                ObjectAnimator scaleDownY_TxtRegistro = ObjectAnimator.ofFloat(txtInformativoRegistro, "scaleY", 0.0f, 1.0f);
                //Configuramos los animatorsets
                animatorSetEscale.play(scaleDownX_inicioSesion).with(scaleDownY_inicioSesion)
                        .with(scaleDownX_registro).with(scaleDownY_registro)
                        .with(scaleDownX_TxtUsuario).with(scaleDownY_TxtUsuario)
                        .with(scaleDownX_TxtClave).with(scaleDownY_TxtClave)
                        .with(scaleDownX_TxtRegistro).with(scaleDownY_TxtRegistro)
                        .with(scaleDownX_TxtClaveOlvidada).with(scaleDownY_TxtClaveOlvidada);
                animatorSetEscale.setDuration(550);
                animatorSetEscale.setInterpolator(new AccelerateDecelerateInterpolator());
                animatorSetEscale.start();
            }
        });

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
            finish();
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
