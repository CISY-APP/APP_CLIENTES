package com.example.app_clientes.vistas;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.example.app_clientes.Biblioteca;
import com.example.app_clientes.jsonplaceholder.JsonPlaceHolderApi;
import com.example.app_clientes.pojos.Usuario;
import com.example.app_clientes.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.google.android.material.navigation.NavigationView.*;

public class VentanaPrincipal extends AppCompatActivity{

    private AppBarConfiguration mAppBarConfiguration;
    private StorageReference storageReference;
    private static final int GALERY_INTENT = 1;
    private CircleImageView IVImagenUsuarioMenuLateral;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        IVImagenUsuarioMenuLateral.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirGaleria();
            }
        });

        cargarImagenUsuario(headView);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_home, R.id.nav_viajes, R.id.nav_cartera, R.id.nav_mensajes, R.id.nav_incidencias, R.id.nav_datos, R.id.nav_vehiculos, R.id.nav_configuracion, R.id.nav_cerrarSesion, R.id.nav_chat).setDrawerLayout(drawer).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
//        firebaseDatabase = FirebaseDatabase.getInstance();
//        chatReference = firebaseDatabase.getReference("USUARIOS").child(ID_USUARIO);
//        chatReference.child("mensajesSinLeer").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                TextView myCounter = findViewById(R.id.my_counter);
//                if (myCounter != null) {
//                    myCounter.setText(dataSnapshot.getValue()+"");
//                    Toast.makeText(getApplicationContext(),myCounter.getText()+"", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference ref = firebaseDatabase.getReference("USUARIOS").child(ID_USUARIO);

        ref.orderByChild("mensajesSinLeer").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d("Juanan","tupu");
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d("Juanan","tupu");
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

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



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALERY_INTENT && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            Glide.with(this).load(uri).into(IVImagenUsuarioMenuLateral);
            StorageReference filePath = storageReference.child("Fotos").child(ID_USUARIO);
            Toast.makeText(this, "Imagen cambiada", Toast.LENGTH_SHORT).show();
            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageReference = FirebaseStorage.getInstance().getReference();
                    storageReference.child("Fotos").child(ID_USUARIO).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            //Cojemos la URI para hacer el inserta ala base de datos con la URI
                            //uri --> https://firebasestorage.googleapis.com/v0/b/appclientes-a0e43.appspot.com/o/Fotos%2F1?alt=media&token=f59bccd3-4c6e-4872-ab50-14b39743685d
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(getApplicationContext(), "No tiene imaganes para cargar", Toast.LENGTH_SHORT).show();
                            IVImagenUsuarioMenuLateral.setImageResource(R.drawable.user);
                        }
                    });
                }
            });
        }
    }

    //Abre la galaeria de imagenes
    public void abrirGaleria(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,GALERY_INTENT);
    }

    public void cargarImagenUsuario(final View headView) {
        storageReference = FirebaseStorage.getInstance().getReference();
        storageReference.child("Fotos").child(ID_USUARIO).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(headView.getContext()).load(uri).into(IVImagenUsuarioMenuLateral);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(getApplicationContext(), "No tiene imaganes para cargar", Toast.LENGTH_SHORT).show();
                IVImagenUsuarioMenuLateral.setImageResource(R.drawable.user);
            }
        });
    }
    /*
    private String cargarCredencialesIdUsuario(){
        SharedPreferences credenciales = getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        return credenciales.getString("idUsuario","0");
    }

    private void guardarCredencialesIdUsuario(){
        //Controlador controlador = new Controlador();
        //usuario = controlador.getCliente("1","1");
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://192.168.56.1:8080/").addConverterFactory(GsonConverterFactory.create()).build();
        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        Call<Usuario> call = jsonPlaceHolderApi.getUsuario("1","1");
        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (!response.isSuccessful()) {
                    Log.i("Coder" , response.code()+"" );
                    return;
                }
                //SharedPreferences credenciales = getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
                //SharedPreferences.Editor editor = credenciales.edit();
                //editor.putString("idUsuario", response.body().getIdusuario().toString());
                //editor.commit();
                usuario = response.body();
            }
            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Log.i("Code: " ,  t.getMessage()+"" );
            }
        });
    }

*/
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
