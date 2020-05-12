package com.example.app_clientes.ui.Vehiculos;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.app_clientes.Adapter.miAdapterMisVehiculos;
import com.example.app_clientes.Item.ItemVehiculo;
import com.example.app_clientes.R;
import com.example.app_clientes.Vistas.VentanaAgregarVehiculo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import petrov.kristiyan.colorpicker.ColorPicker;

import static android.app.Activity.RESULT_OK;

public class VehiculosFragment extends Fragment {

    private RecyclerView.LayoutManager layoutManager;
    private miAdapterMisVehiculos miAdapterMisVehiculos;
    private RecyclerView recyclerView;
    private ArrayList<ItemVehiculo> misVehiculosList = new ArrayList<>();
    private ArrayList<String> colores = new ArrayList<>();

    //Elementos del Layout
    private CircleImageView IMGVehiculoGrande;
    private EditText ETMatriculaVehiculoMisVehiculos;
    private Spinner spinner_numero_plazas_mis_vehiculos;
    private EditText spinner_tipo_combustible_mis_vehiculos;
    private EditText  BTSeleccionarColorCocheMisVehiculos;
    private ImageView IMGModificarFotoVehiculo;
    private ImageView IVModificarVehiculoMisvehiculos;
    private CircleImageView  ImgColorCocheMisCoches;

    private String colorSeleccionado;

    private StorageReference storageReference;
    private Uri uriImagenEndispositivo;

    private static final int GALERY_INTENT = 1;
    private String ID_USUARIO;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vehiculo, container, false);

        ID_USUARIO = cargarCredencialesIdUsuario();

        //Inatancia el objeto de tipo storageReference
        storageReference = FirebaseStorage.getInstance().getReference();

        IMGVehiculoGrande = view.findViewById(R.id.IMGVehiculoGrande);
        ETMatriculaVehiculoMisVehiculos = view.findViewById(R.id.ETMatriculaVehiculoMisVehiculos);
        spinner_numero_plazas_mis_vehiculos = view.findViewById(R.id.spinner_numero_plazas_mis_vehiculos);
        spinner_tipo_combustible_mis_vehiculos = view.findViewById(R.id.spinner_tipo_combustible_mis_vehiculos);
        BTSeleccionarColorCocheMisVehiculos = view.findViewById(R.id.BTSeleccionarColorCocheMisVehiculos);
        BTSeleccionarColorCocheMisVehiculos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorPiker();
            }
        });
        ImgColorCocheMisCoches = view.findViewById(R.id.ImgColorCocheMisCoches);

        IMGModificarFotoVehiculo = view.findViewById(R.id.IMGModificarFotoVehiculo);
        IMGModificarFotoVehiculo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirGaleria();
            }
        });

        //Tick para actualizar informacion de un vehiculo un vehiculo
        IVModificarVehiculoMisvehiculos = view.findViewById(R.id.IVModificarVehiculoMisvehiculos);
        IVModificarVehiculoMisvehiculos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //COMPROVACIONES PARA HACER UN UPDATE A LA BASE DE DATOS
            }
        });

        //SPINNER NUMERO PLAZAS
        spinner_numero_plazas_mis_vehiculos = view.findViewById(R.id.spinner_numero_plazas_mis_vehiculos);
        inicializacionSpinnerVehículos();
        spinner_numero_plazas_mis_vehiculos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                //Habrá que meterlo en una variable para poder trabajar con ella
                //en este caso el numero de plazas disponibles
                spinner_numero_plazas_mis_vehiculos.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });


        //Agrega los coches al Array de vehiculo
        agregarCoches();
        misVehiculosList.add(new ItemVehiculo("Añadir", R.drawable.anadir));

        //Asociamos los atributos con los objeto del layoud para poder usarlos
        //INSTANCIAMOS Y ASOCIAMOS ELEMENTOS NECESARIOS PARA EL CORRECTO FUNCIONAMIENTO DEL RECYCLERVIEW
        recyclerView = view.findViewById(R.id.RVVehiculosEncontrados); //Vinculamos el recyclerview del xml con el de la clase main
        recyclerView.setHasFixedSize(true);// RecyclerView sabe de antemano que su tamaño no depende del contenido del adaptador, entonces omitirá la comprobación de si su tamaño debería cambiar cada vez que se agregue o elimine un elemento del adaptador.(mejora el rendimiento)
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        miAdapterMisVehiculos = new miAdapterMisVehiculos(misVehiculosList);//Instanciamos un objeto de tipo Example_Adapter
        recyclerView.setAdapter(miAdapterMisVehiculos);//Vinculamos el adapter al recyclerView
        miAdapterMisVehiculos.setOnClickListener(new miAdapterMisVehiculos.OnItemClickListener() {
            @Override
            public void OnVehiculoClick(int position) {
                if(position == misVehiculosList.size()-1){
                    VentanaAgregarVehiculo ventanaAgregarVehiculo = new VentanaAgregarVehiculo();
                    Intent VentanaAgregarVehiculo = new Intent(getContext(), ventanaAgregarVehiculo.getClass());
                    startActivity(VentanaAgregarVehiculo);
                }else{
                    IMGVehiculoGrande.setImageResource(misVehiculosList.get(position).getmImageVehiculo());
                }
            }

        });

        return view;
    }

    //Inicializa el spinner de vehiculos
    private void inicializacionSpinnerVehículos() {
        // Initializing a String Array
        String[] numeroPlazas = new String[]{"1", "2", "3", "4", "5", "6", "7", "8"};
        // Initializing an ArrayAdapter.
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(),R.layout.color_spinner,numeroPlazas);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.color_spinner);
        spinner_numero_plazas_mis_vehiculos.setAdapter(spinnerArrayAdapter);
    }

    //Inicializa el ColorPiker del color del vehiculo.
    public void colorPiker() {
        ColorPicker colorPicker = new ColorPicker(getActivity());
        colorPicker.setTitle("¿De que color es?");
        colores.clear();
        colores.add("#000000");colores.add("#FFFFFF");colores.add("#616161");colores.add("#C8C8C8");colores.add("#7A0000");colores.add("#E70000");colores.add("#011474");
        colores.add("#01742E");colores.add("#00BA27");colores.add("#8E6D3D");colores.add("#F5C886");colores.add("#F17C00");colores.add("#E7E300");colores.add("#6900E7");
        colores.add("#FFA3F8");
        colorPicker.setColors(colores).setColumns(5).setRoundColorButton(true).setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
            @Override
            public void onChooseColor(int position, int color) {
                ImgColorCocheMisCoches.setBackgroundColor(color);
                colorSeleccionado = colores.get(position);
            }
            @Override
            public void onCancel() {

            }
        }).show();
    }

    //Rellena el arrayList de vehiculo
    private void agregarCoches() {
        //Consulta ala basde de datos para cargar los datos al array
        misVehiculosList.add(new ItemVehiculo("Marca", R.drawable.seatmio));
        misVehiculosList.add(new ItemVehiculo("Marca", R.drawable.audi));
        misVehiculosList.add(new ItemVehiculo("Marca", R.drawable.bmw));
    }

    //Abre la galaeria de imagenes
    public void abrirGaleria(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,GALERY_INTENT);
    }

    //Coge la direccion del dispositivo
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALERY_INTENT && resultCode == RESULT_OK) {
            //Coge la Uri del dispositivo
            uriImagenEndispositivo = data.getData();
            //Cambia la imagen desde el dispositivo
            Glide.with(getContext()).load(uriImagenEndispositivo).into(IMGVehiculoGrande);
        }
    }

    private String cargarCredencialesIdUsuario(){
        SharedPreferences credenciales = getContext().getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        return credenciales.getString("idUsuario","0");
    }
}
