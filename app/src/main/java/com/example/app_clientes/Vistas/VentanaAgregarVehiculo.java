package com.example.app_clientes.Vistas;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.app_clientes.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import petrov.kristiyan.colorpicker.ColorPicker;

public class VentanaAgregarVehiculo extends AppCompatActivity {

    private EditText BTColorPiker;
    private EditText ETMatriculaVehiculo;
    private CircleImageView ImgColorCoche;
    private CircleImageView IMGCocheAgregar;
    private Spinner spinner_numero_plazas;
    private Spinner spinner_tipo_combustible;
    private ArrayList<String> colores = new ArrayList<>();
    private String colorSeleccionado;
    private ImageView IVFlechaAtrasAgregarVehiculo;
    private ImageView IVAceptarAgregarVehiculo;
    private Uri uri;
    private StorageReference storageReference;

    private static final int GALERY_INTENT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_vehiculo);

        //Carga la imagen del vehiculo por defecto
        //cargarImagenVehiculo();

        ETMatriculaVehiculo = findViewById(R.id.ETMatriculaVehiculo);

        //Imagen que representa el color del coche
        ImgColorCoche = findViewById(R.id.ImgColorCoche);

        //Abre la galeria para subir la fotoa firebase
        IMGCocheAgregar = findViewById(R.id.IMGCocheAgregar);
        IMGCocheAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirGaleria();
            }
        });

        //Abre el ColorPiker para seleccionar un color
        BTColorPiker = findViewById(R.id.BTSeleccionarColorCoche);
        BTColorPiker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorPiker();
            }
        });

        //SPINNER NUMERO PLAZAS
        spinner_numero_plazas = findViewById(R.id.spinner_numero_plazas);
        inicializacionSpinnerVehículos();
        spinner_numero_plazas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                //Habrá que meterlo en una variable para poder trabajar con ella
                //en este caso el numero de plazas disponibles
                 spinner_numero_plazas.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        //SPINNER TIPO COMBUSTIBLE
        spinner_tipo_combustible = findViewById(R.id.spinner_tipo_combustible);
        inicializacionSpinnerCombustible();
        spinner_tipo_combustible.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                //Habrá que meterlo en una variable para poder trabajar con ella
                //en este caso el numero de plazas disponibles
                spinner_tipo_combustible.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        IVFlechaAtrasAgregarVehiculo = findViewById(R.id.IVFlechaAtrasAgregarVehiculo);
        IVFlechaAtrasAgregarVehiculo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        IVAceptarAgregarVehiculo = findViewById(R.id.IVAceptarAgregarVehiculo);
        IVAceptarAgregarVehiculo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Coje la URL de la imagen de la carpeta que le indiquemos con el nombre que le indiquemos de firebase
                storageReference.child("EMAILUSUARIO").child(uri.toString()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        //INSERTA A LA BASE DE DATOS
                        String uriDeLaImagenAInsertarEnLaBaseDeDatos = uri.toString();
                        //Faltan el resto de datos del vehiculo solo es obligatorio
                        // - Numero de plazas
                        // - Matrícula
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(getApplicationContext(), "No se ha podido realizar el insert en la base de datos", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    //Inicializa el spinner de vehiculos
    private void inicializacionSpinnerVehículos() {
        // Initializing a String Array
        String[] numeroPlazas = new String[]{"1", "2", "3", "4", "5", "6", "7", "8"};
        // Initializing an ArrayAdapter.
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,R.layout.color_spinner,numeroPlazas);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.color_spinner);
        spinner_numero_plazas.setAdapter(spinnerArrayAdapter);
    }

    //Inicializa el spinner de vehiculos
    private void inicializacionSpinnerCombustible() {
        // Initializing a String Array
        String[] combustibles = new String[]{"Gasolina", "Diesel", "Híbrido", "Electrico"};
        // Initializing an ArrayAdapter.
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,R.layout.color_spinner,combustibles);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.color_spinner);
        spinner_tipo_combustible.setAdapter(spinnerArrayAdapter);
    }

    //Inicializa el ColorPiker del color del vehiculo.
    public void colorPiker() {
        ColorPicker colorPicker = new ColorPicker(this);
        colorPicker.setTitle("¿De que color es?");
        colores.add("#000000");colores.add("#FFFFFF");colores.add("#616161");colores.add("#C8C8C8");colores.add("#7A0000");colores.add("#E70000");colores.add("#011474");
        colores.add("#01742E");colores.add("#00BA27");colores.add("#8E6D3D");colores.add("#F5C886");colores.add("#F17C00");colores.add("#E7E300");colores.add("#6900E7");
        colores.add("#FFA3F8");
        colorPicker.setColors(colores).setColumns(5).setRoundColorButton(true).setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
            @Override
            public void onChooseColor(int position, int color) {
                ImgColorCoche.setBackgroundColor(color);
                colorSeleccionado = colores.get(position);
            }
            @Override
            public void onCancel() {

            }
        }).show();
    }

    //Coge la direccion del dispositivo
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALERY_INTENT && resultCode == RESULT_OK) {
            //Inatancia el objeto de tipo storageReference
            storageReference = FirebaseStorage.getInstance().getReference();
            //Coge la Uri del dispositivo
            Uri uri = data.getData();
            //Carla la imagen desde el dispositivo
            Glide.with(this).load(uri).into(IMGCocheAgregar);
            //Crea una direccion para poder subir la imagen a firebase
            StorageReference filePath = storageReference.child("EMAILUSUARIO").child(Long.toString(System.currentTimeMillis()));
            //Utiliza la direccion, sube la imagen a firebase y escucha si se ha realizado de manera adecuada Uri entera de la imagen
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

    //Coge la direccion de firebase
    /*public void cargarImagenVehiculo() {
        //Inatancia el objeto de tipo storageReference
        storageReference = FirebaseStorage.getInstance().getReference();
        //Coje la URL de la imagen de la carpeta que le indiquemos con el nombre que le indiquemos
        storageReference.child("Fotos"+"EMAILUSUARIO").child(ETMatriculaVehiculo.getText().toString()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //Si la carga es optima la coloca en IMGUsuarioDatos
                Glide.with(getApplication()).load(uri).into(IMGCocheAgregar);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                //Si la carga no es optima es decir que no existe la direccion proporcinada carga una imagen por defecto
                IMGCocheAgregar.setImageResource(R.drawable.coche);
            }
        });
    }*/


}
