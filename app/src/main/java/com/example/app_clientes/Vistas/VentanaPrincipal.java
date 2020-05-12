package com.example.app_clientes.Vistas;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.example.app_clientes.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class VentanaPrincipal extends AppCompatActivity{

    private AppBarConfiguration mAppBarConfiguration;
    private StorageReference storageReference;
    private static final int GALERY_INTENT = 1;
    private CircleImageView IVImagenUsuarioMenuLateral;
    private static final String ID_USUARIO = "1";

    public VentanaPrincipal() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ventana_principal);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        final View headView = navigationView.getHeaderView(0);
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
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_viajes, R.id.nav_cartera, R.id.nav_mensajes,  R.id.nav_incidencias, R.id.nav_datos,  R.id.nav_vehiculos,  R.id.nav_configuracion,  R.id.nav_cerrarSesion, R.id.nav_chat)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
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
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
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
}
