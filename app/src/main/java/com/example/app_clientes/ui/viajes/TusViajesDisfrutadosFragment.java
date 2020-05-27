package com.example.app_clientes.ui.viajes;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.app_clientes.R;
import com.example.app_clientes.adapter.MiAdapterTusViajesDisfrutados;
import com.example.app_clientes.pojos.Usuario;
import com.example.app_clientes.pojos.Viaje;
import com.example.app_clientes.vistas.VentanaChatIndividual;
import com.example.app_clientes.vistas.VentanaLogin;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import de.hdodenhof.circleimageview.CircleImageView;
/**
 * A simple {@link Fragment} subclass.
 */
public class TusViajesDisfrutadosFragment extends Fragment {


    private EditText TVDescripcionUsuarioViajeDisfrutado;
    private EditText ETOrigenViajeDisfrutado;
    private EditText ETDestinoViajeDisfrutado;
    private EditText editTextFechaViajesDisfrutados;
    private EditText editTextHoraViajesDisfrutados;
    private TextView TVNombreUsuarioViajeDisfrutado;
    private TextView TVApellidosUsuarioViajeDisfrutado;
    private TextView TVEdadUsuarioViajeDisfrutado;
    private ImageView imageViewChatViajesDisfrutados;
    private CircleImageView IMGConductorViajeDisfrutado;

    private ArrayList<Viaje> misViajesList = new ArrayList<>();
    private ArrayList<Usuario> misUsuariosList = new ArrayList<>();
    private RecyclerView recyclerViewTusViajes;
    private MiAdapterTusViajesDisfrutados miAdapterTusViajes;

    private String ID_USUARIO;
    private String ID_USUARIO_CONVER;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tusviajesdisfrutados, container, false);

        ID_USUARIO = VentanaLogin.usuarioSesion.getIdusuario().toString();

        recyclerViewTusViajes = view.findViewById(R.id.RVViajesDisfrutados);
        TVDescripcionUsuarioViajeDisfrutado = view.findViewById(R.id.TVDescripcionUsuarioViajeDisfrutado);
        TVNombreUsuarioViajeDisfrutado = view.findViewById(R.id.TVNombreUsuarioViajeDisfrutado);
        TVApellidosUsuarioViajeDisfrutado = view.findViewById(R.id.TVApellidosUsuarioViajeDisfrutado);
        TVEdadUsuarioViajeDisfrutado = view.findViewById(R.id.TVEdadUsuarioViajeDisfrutado);
        ETOrigenViajeDisfrutado = view.findViewById(R.id.ETOrigenViajeDisfrutado);
        ETDestinoViajeDisfrutado = view.findViewById(R.id.ETDestinoViajeDisfrutado);
        editTextFechaViajesDisfrutados = view.findViewById(R.id.editTextFechaViajesDisfrutados);
        editTextHoraViajesDisfrutados = view.findViewById(R.id.editTextHoraViajesDisfrutados);
        IMGConductorViajeDisfrutado = view.findViewById(R.id.IMGConductorViajeDisfrutado);

        imageViewChatViajesDisfrutados = view.findViewById(R.id.imageViewChatViajesDisfrutados);
        imageViewChatViajesDisfrutados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VentanaChatIndividual ventanaChatIndividual = new VentanaChatIndividual();
                Intent VentanaChatIndividual = new Intent(getContext(), ventanaChatIndividual.getClass());
                VentanaChatIndividual.putExtra("ID_USUARIO", ID_USUARIO);
                VentanaChatIndividual.putExtra("ID_USUARIO_CONVER", ID_USUARIO_CONVER);
                startActivity(VentanaChatIndividual);
            }
        });

        cargarUsuariosViajesDisfrutados();
        cargarViajesViajesDisfrutados();

        //Asociamos los atributos con los objeto del layout para poder usarlos
        //INSTANCIAMOS Y ASOCIAMOS ELEMENTOS NECESARIOS PARA EL CORRECTO FUNCIONAMIENTO DEL RECYCLERVIEW
        recyclerViewTusViajes.setHasFixedSize(true);// RecyclerView sabe de antemano que su tamaño no depende del contenido del adaptador, entonces omitirá la comprobación de si su tamaño debería cambiar cada vez que se agregue o elimine un elemento del adaptador.(mejora el rendimiento)
        recyclerViewTusViajes.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        miAdapterTusViajes = new MiAdapterTusViajesDisfrutados(getContext(),misUsuariosList);//Instanciamos un objeto de tipo Example_Adapter
        recyclerViewTusViajes.setAdapter(miAdapterTusViajes);//Vinculamos el adapter al recyclerView
        miAdapterTusViajes.setOnClickListener(new MiAdapterTusViajesDisfrutados.OnItemClickListener() {
            @Override
            public void OnUsuarioClick(int position) {

                //Usuario
                TVNombreUsuarioViajeDisfrutado.setText(misUsuariosList.get(position).getNombre());
                TVApellidosUsuarioViajeDisfrutado.setText(misUsuariosList.get(position).getApellidos());
                TVEdadUsuarioViajeDisfrutado.setText(misUsuariosList.get(position).getFechanacimiento().toString());
                TVNombreUsuarioViajeDisfrutado.setText(misUsuariosList.get(position).getNombre());
                TVDescripcionUsuarioViajeDisfrutado.setText(misUsuariosList.get(position).getDescripcion());
                ID_USUARIO_CONVER = position+""; //Meter el ID que devuelva la consulta de retrofit
                Glide.with(getContext()).load(misUsuariosList.get(position).getFotousuario()).into(IMGConductorViajeDisfrutado);

                //Viaje
                ETOrigenViajeDisfrutado.setText(misViajesList.get(position).getOrigen());
                ETDestinoViajeDisfrutado.setText(misViajesList.get(position).getDestino());
                editTextFechaViajesDisfrutados.setText(misViajesList.get(position).getFechasalida().toString());
                editTextHoraViajesDisfrutados.setText(misViajesList.get(position).getHorasalida().toString());
            }
        });

        return view;
    }

    //CARGA LOS DATOS DE LOS USUARIO CON LOS QUE EL USUARIO A CONTRATADO VIAJES
    private void cargarUsuariosViajesDisfrutados() {
        java.util.Date utilDate = new java.util.Date(); //fecha actual
        long lnMilisegundos = utilDate.getTime();
        java.sql.Date sqlDate = new java.sql.Date(lnMilisegundos);
        java.sql.Time sqlTime = new java.sql.Time(lnMilisegundos);
        java.sql.Timestamp sqlTimestamp = new java.sql.Timestamp(lnMilisegundos);
        //CARGAR EN EL ARRAYLIST LOS USUARIOS CON LOS QUE HAYAMOS CONTRATADO VIAJES DESDE RETROFIT
        misUsuariosList.add(new Usuario("Javier", "Gómez Fernández", null, null, null,
                null, null, utilDate, "https://firebasestorage.googleapis.com/v0/b/appclientes-a0e43.appspot.com/o/Fotos%2F1?alt=media&token=f59bccd3-4c6e-4872-ab50-14b39743685d", null, "Hola me llamo Javier",
                null, null, null, null,
                null, null, null));
        misUsuariosList.add(new Usuario("Roberto", "Castaño Romero", null, null, null,
                null, null, utilDate, "https://assets.entrepreneur.com/content/3x2/2000/20181012160100-atractiva.jpeg?width=700&crop=2:1", null, "Hola me llamo Roberto",
                null, null, null, null,
                null, null, null));
        misUsuariosList.add(new Usuario("Pilar", "Garcia Romero", null, null, null,
                null, null, utilDate, "https://firebasestorage.googleapis.com/v0/b/appclientes-a0e43.appspot.com/o/Fotos%2F2?alt=media&token=7f07bc9d-4892-41c9-8211-35e3bc835a48", null, "Hola me llamo Pilar",
                null, null, null, null,
                null, null, null));
        misUsuariosList.add(new Usuario("Pilar", "Garcia Romero", null, null, null,
                null, null, utilDate, "https://firebasestorage.googleapis.com/v0/b/appclientes-a0e43.appspot.com/o/Fotos%2F3?alt=media&token=bd212b33-cfb4-4db9-9a3a-879a41269379", null, "Hola me llamo Pilar",
                null, null, null, null,
                null, null, null));
    }

    //CARGA LOS DATOS DE LOS VIAJES QUE EL USUARIO A DISFRUTADO VIAJES
    private void cargarViajesViajesDisfrutados() {
        java.util.Date utilDate = new java.util.Date(2019-1900,1,21); //fecha actual
        long lnMilisegundos = utilDate.getTime();
        java.sql.Date sqlDate = new java.sql.Date(lnMilisegundos);
        java.sql.Time sqlTime = new java.sql.Time(lnMilisegundos);
        java.sql.Timestamp sqlTimestamp = new java.sql.Timestamp(lnMilisegundos);

        java.util.Date utilDate1 = new java.util.Date(2021-1900,1,21); //fecha actual
        long lnMilisegundos1 = utilDate.getTime();
        java.sql.Date sqlDate1 = new java.sql.Date(lnMilisegundos);
        java.sql.Time sqlTime1 = new java.sql.Time(lnMilisegundos);
        java.sql.Timestamp sqlTimestamp1 = new java.sql.Timestamp(lnMilisegundos);

        java.util.Date utilDate2 = new java.util.Date(2019-1900,1,21); //fecha actual
        long lnMilisegundos2 = utilDate.getTime();
        java.sql.Date sqlDate2 = new java.sql.Date(lnMilisegundos);
        java.sql.Time sqlTime2 = new java.sql.Time(lnMilisegundos);
        java.sql.Timestamp sqlTimestamp2 = new java.sql.Timestamp(lnMilisegundos);

        misViajesList.add(new Viaje(null,null, "Leganes","Getafe", null,
                null, 3, utilDate, utilDate, null,
                null));
        misViajesList.add(new Viaje(null,null, "Madrid","Villaverde", null,
                null, 3, utilDate1, utilDate1, null,
                null));
        misViajesList.add(new Viaje(null,null, "Alcorcon","Fuenlabrada", null,
                null, 3, utilDate2, utilDate2, null,
                null));
        misViajesList.add(new Viaje(null,null, "Alcorcon","Fuenlabrada", null,
                null, 3, utilDate2, utilDate2, null,
                null));
    }
}


