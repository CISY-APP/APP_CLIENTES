package com.example.app_clientes.ui.Datos;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.app_clientes.otros.CalendarioFragment;
import com.example.app_clientes.R;
import com.example.app_clientes.vistas.VentanaAgregarVehiculo;
import com.example.app_clientes.vistas.VentanaCambiarContrasena;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class DatosFragment extends Fragment {

    private CircleImageView IMGUsuarioDatos;
    private EditText ETFechaNacimientoDatos;

    private StorageReference storageReference;
    private String uriFotoUsuario = "";

    private static final int GALERY_INTENT = 1;
    private String ID_USUARIO;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_datos, container, false);

        ID_USUARIO = cargarCredencialesIdUsuario();

        //Carga la imagen del usuario al abrir la ventana
        cargarImagenUsuario();

        EditText ETNombreDatos = view.findViewById(R.id.ETNombreDatos);
        EditText ETApellidosDatos = view.findViewById(R.id.ETApellidosDatos);
        EditText ETEmailDatos = view.findViewById(R.id.ETEmailDatos);
        EditText ETNumeroTelefonoDatos = view.findViewById(R.id.ETNumeroTelefonoDatos);
        ETFechaNacimientoDatos = view.findViewById(R.id.ETFechaNacimientoDatos);
        EditText ETDocumentoDatos = view.findViewById(R.id.ETDocumentoDatos);
        EditText ETDescripcionDatos = view.findViewById(R.id.ETDescripcionDatos);
        IMGUsuarioDatos = view.findViewById(R.id.mImageVehiculoPeque);
        IMGUsuarioDatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirGaleria();
            }
        });
        Button BTCambiarContrasena = view.findViewById(R.id.BTCambiarContrasena);
        BTCambiarContrasena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VentanaCambiarContrasena ventanaCambiarContrasena = new VentanaCambiarContrasena();
                Intent VentanaCambiarContrasena = new Intent(getContext(), ventanaCambiarContrasena.getClass());
                startActivity(VentanaCambiarContrasena);
            }
        });
        Button BTEliminarCuenta = view.findViewById(R.id.BTEliminarCuenta);
        BTEliminarCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Consulta a la base de datos eliminando la cuenta
            }
        });
        Button BTGuardarCambios = view.findViewById(R.id.BTGuardarCambios);
        BTGuardarCambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Consulta a la base de datos, realizar los controles pertinentes
            }
        });
        ETFechaNacimientoDatos = view.findViewById(R.id.ETFechaNacimientoDatos);
        ETFechaNacimientoDatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        TextView TVAgregarCoche = view.findViewById(R.id.TVAgregarCoche);
        TVAgregarCoche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VentanaAgregarVehiculo ventanaAgregarVehiculo = new VentanaAgregarVehiculo();
                Intent VentanaAgregarVehiculo = new Intent(getContext(), ventanaAgregarVehiculo.getClass());
                startActivity(VentanaAgregarVehiculo);
            }
        });
        /*ImgPais = view.findViewById(R.id.ImgPais);
        ImgPais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRadioButtonDialog();
            }
        });
        //REALIZAR CONSULTA A LA BASE DE DATOS PARA SABER SI EL USUARIO TIENE COCHES.
        //Y EN FUNCION DE ESO PONER EL BOTON A CHECK
        /*switch1Coche= view.findViewById(R.id.switch1Coche);
        switch1Coche.setChecked(false);
        if(switch1Coche.isChecked()){
            switch1Coche.setClickable(false);
        }else{
            switch1Coche.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(getActivity(), "Abrir Intent nuevo", Toast.LENGTH_SHORT).show();
                }
            });
        }*/

        return view;
    }

    //Coge la direccion del dispositivo
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALERY_INTENT && resultCode == RESULT_OK) {
            //Coge la Uri del dispositivo
            Uri uri = data.getData();
            //Cambia la imagen desde el dispositivo
            Glide.with(getContext()).load(uri).into(IMGUsuarioDatos);
            //Crea una direccion para poder subir la imagen a firebase
            StorageReference filePath = storageReference.child("Fotos").child(ID_USUARIO);
            //Utiliza la direccion para coger la imagen del dispositivo, sube la imagen a firebase y escucha si se ha realizado de manera adecuada
            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getContext(), "Imagen cambiada", Toast.LENGTH_SHORT).show();
                    //Coje la URL de la imagen de la carpeta que le indiquemos con el nombre que le indiquemos de firebase
                    storageReference.child("Fotos").child(ID_USUARIO).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            //Meter la URI en un String para posteriormente hacer el update o el insert en la base de datos
                            uriFotoUsuario = uri.toString();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(getContext(), "No se ha podido realizar el insert en la base de datos", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

        }
    }



    //Metodo para cargar la imagen del usuario
    public void cargarImagenUsuario(){
        //Inatancia el objeto de tipo storageReference
        storageReference = FirebaseStorage.getInstance().getReference();
        //Coje la URL de la imagen de la carpeta que le indiquemos con el nombre que le indiquemos
        storageReference.child("Fotos").child(ID_USUARIO).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //Si la carga es optima la coloca en IMGUsuarioDatos
                Glide.with(getActivity()).load(uri).into(IMGUsuarioDatos);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                //Si la carga no es optima es decir que no existe la direccion proporcinada carga una imagen por defecto
                IMGUsuarioDatos.setImageResource(R.drawable.user);
            }
        });
    }

    public void abrirGaleria(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,GALERY_INTENT);
    }

    //Muestra un cuadro de dialogo con un calendario al pulsar sobre el EditextFecha
    //Este nos permite copturar la fecha para posteriormente hacer un insert en la base de datos con esta
    private void showDatePickerDialog() {
        CalendarioFragment newFragment = CalendarioFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 porque el mes de enero es 0
                final String selectedDate = day + " / " + (month+1) + " / " + year;
                ETFechaNacimientoDatos.setText(selectedDate);
            }
        });
        newFragment.show(getChildFragmentManager(), "datePicker");
    }

    private String cargarCredencialesIdUsuario(){
        SharedPreferences credenciales = getContext().getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        return credenciales.getString("idUsuario","0");
    }

    //Hacer si nos queda tiempo cambiar pais de origen
    /*private void showRadioButtonDialog() {
        // custom dialog
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_paises);

        List<String> stringList=new ArrayList<>();  // here is list
        for(int i=0;i<2;i++) {
            if (i==0){
                stringList.add("Number Mode");
            }else {
                stringList.add("Character Mode");
            }
        }
        RadioGroup rg = (RadioGroup) dialog.findViewById(R.id.radio_group);
        for(int i=0;i<stringList.size();i++){
            RadioButton rb=new RadioButton(getContext()); // dynamically creating RadioButton and adding to RadioGroup.
            rb.setText(stringList.get(i));
            rg.addView(rb);
        }
        dialog.show();
    }*/
}
