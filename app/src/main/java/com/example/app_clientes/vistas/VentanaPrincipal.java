//Indicamos a que paquete pertenece esta clase:
package com.example.app_clientes.vistas;
//Importamos los siguientes paquetes:
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.bumptech.glide.Glide;
import com.example.app_clientes.Biblioteca;
import com.example.app_clientes.pojos.Conversacion;
import com.example.app_clientes.pojos.Usuario;
import com.example.app_clientes.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import de.hdodenhof.circleimageview.CircleImageView;
//Clase que contiene toda la logica y conexion con la ventana principal y menu de la app:
public class VentanaPrincipal extends AppCompatActivity {
    //Atributos de la clase:
    private AppBarConfiguration mAppBarConfiguration;
    private StorageReference storageReference;
    private static final int GALERY_INTENT = 1;
    public static CircleImageView IVImagenUsuarioMenuLateral;
    private TextView nombreUsuario;
    private TextView my_counter;
    private TextView correoUsuario;
    private String ID_USUARIO;
    private final String CHANNEL_ID = "1";
    public static Usuario usuario;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference chatReference;
    public VentanaPrincipal() {
    }
    //Metodo onCreate encargado de crear la actividad:
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Llamamos al superconstructor y conectamos el XML del login y demas...:
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ventana_principal);
        createNotificationChannel();
        ID_USUARIO = Biblioteca.usuarioSesion.getIdusuario().toString();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        final View headView = navigationView.getHeaderView(0);
        nombreUsuario = headView.findViewById(R.id.nombreUsuario);
        nombreUsuario.setText(Biblioteca.usuarioSesion.getNombre().toString() + " " + Biblioteca.usuarioSesion.getApellidos().toString());
        correoUsuario = headView.findViewById(R.id.correoUsuario);
        correoUsuario.setText(Biblioteca.usuarioSesion.getEmail().toString());
        IVImagenUsuarioMenuLateral = headView.findViewById(R.id.IVImagenUsuarioMenuLateral);

        cargarImagenUsuario(headView);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_home, R.id.nav_viajes, R.id.nav_mensajes, R.id.nav_datos, R.id.nav_vehiculos, R.id.nav_cerrarSesion, R.id.nav_chat).setDrawerLayout(drawer).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference ref = firebaseDatabase.getReference("USUARIOS").child(ID_USUARIO);

        ref.orderByChild("mensajesSinLeer").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int total = 0;
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Long mensajes = child.getValue(Conversacion.class).getMensajesSinLeer();
                    total +=(mensajes != null ? mensajes : 0L);
                }
                TextView myCounter = findViewById(R.id.my_counter);
                if (myCounter != null) {
                    myCounter.setText(Long.toString(total));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.ventana_principal, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }

    public void cargarImagenUsuario(final View headView) {
        storageReference = FirebaseStorage.getInstance().getReference();
        storageReference.child("Fotos").child(ID_USUARIO).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(headView.getContext()).load(Biblioteca.usuarioSesion.getFotousuario()).error(R.drawable.user).into(IVImagenUsuarioMenuLateral);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                IVImagenUsuarioMenuLateral.setImageResource(R.drawable.user);
            }
        });
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString();
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private CharSequence getString() {
        CharSequence charSequence = new StringBuffer("A");
        return charSequence;
    }
}
