//Indicamos a que paquete pertenece esta clase:
package com.example.app_clientes.vistas;
//Importamos los siguientes paquetes:
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.app_clientes.Biblioteca;
import com.example.app_clientes.R;
//Clase que contiene toda la logica y conexion con la ventana de viaje publica de la app:
public class VentanaRegistroRealizado extends AppCompatActivity implements View.OnClickListener{
    //Atributos XML:
    private TextView mensaje;
    private Button btOK;
    //Metodo que se ejecuta al crearse la vista:
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Conectamos el xml:
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ventana_registro_realizado);
        //Vinculamos los atributos de la clase:
        mensaje=findViewById(R.id.textViewMensajeRegistroRealizado);
        btOK =findViewById(R.id.btListoRegistroRealizado);
        //Vinculamos los botones al listener del metodo onclick, que esta implementado en esta clase:
        btOK.setOnClickListener(this);
        //Animaciones despues de que todoo se haya realizado:
        btOK.post(new Runnable() {
            @Override
            public void run() {
                //Declaramos un animatorSet para poder luego ejecutar un conjunto de animaciones a la vez:
                AnimatorSet animatorSet = new AnimatorSet();
                //Animacion para el boton ok:
                ObjectAnimator scaleDownX_btOK = ObjectAnimator.ofFloat(btOK, "scaleX", 0.0f, 1.0f);
                ObjectAnimator scaleDownY_btOK = ObjectAnimator.ofFloat(btOK, "scaleY", 0.0f, 1.0f);
                //Configuramos el animatorSet, como se tienen que reproducir las animaciones, el tiempo que duran, que clase de interpolador utilizan y la lanzamos:
                animatorSet.play(scaleDownX_btOK).with(scaleDownY_btOK);
                animatorSet.setDuration(Biblioteca.tAnimacionesScaleInicial);
                animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
                animatorSet.start();
            }
        });
    }
    //Metodo de la interfaz View.OnClickListener:
    @Override
    public void onClick(View v) {
        if(v.equals(btOK)){
            //Si la respuesta es satisfactoria, lo indicamos llevando a la ventana inicial:
            onBackPressed();
            finish();
        }
    }
}
