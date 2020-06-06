//Indicamos a que paquete pertenece esta clase:
package com.example.app_clientes.splash_screen;
//Importamos los siguientes paquetes:
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.app_clientes.Biblioteca;
import com.example.app_clientes.R;
import com.example.app_clientes.vistas.VentanaLogin;
//Clase que muestra el splashscreen de la app al iniciar ademas del nombre animado:
public class Splash_screen extends AppCompatActivity {
    //Atributos XML:
    private TextView textViewNombre;
    //Metodo onCreate encargado de crear la actividad:
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Llamamos al superconstructor y conectamos el XML del login:
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_screen);
        //Asociamos el elemento XML a el atributo:
        textViewNombre = findViewById(R.id.textViewNombreSplashScreen);
        //Duracion del splash:
        int duracionSplash = 2800;
        //Lanzamos en un hilo la animacion, y cuando termine se lleva a la ventana de login:
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Splash_screen.this, VentanaLogin.class);
                startActivity(intent);
                finish();
            }
        }, duracionSplash);
        //Animaciones tipo scale despues de que todoo se haya realizado:
        textViewNombre.post(new Runnable() {
            @Override
            public void run() {
                //Declaramos un animatorSet para poder luego ejecutar un conjunto de animaciones a la vez:
                AnimatorSet animatorSetEscale = new AnimatorSet();
                //Animacion para el txtView con el nombre de la app:
                ObjectAnimator scaleDownX_txtViewNombre = ObjectAnimator.ofFloat(textViewNombre, "scaleX", 0.0f, 1.0f);
                ObjectAnimator scaleDownY_txtViewNombre = ObjectAnimator.ofFloat(textViewNombre, "scaleY", 0.0f, 1.0f);
                //Configuramos el animatorSet, como se tienen que reproducir las animaciones, el tiempo que duran, que clase de interpolador utilizan y la lanzamos:
                animatorSetEscale.play(scaleDownX_txtViewNombre).with(scaleDownY_txtViewNombre);
                animatorSetEscale.setDuration(Biblioteca.tAnimacionesScaleInicial);
                animatorSetEscale.setInterpolator(new AccelerateDecelerateInterpolator());
                animatorSetEscale.start();
            }
        });
        //Recibidor de broadcast para cerrar sesion:
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent intent) {
                String action = intent.getAction();
                if (action.equals("cierre_de_sesion")) {
                    finish();
                }
            }
        };
        registerReceiver(broadcastReceiver, new IntentFilter("cierre_de_sesion"));
    }
}
