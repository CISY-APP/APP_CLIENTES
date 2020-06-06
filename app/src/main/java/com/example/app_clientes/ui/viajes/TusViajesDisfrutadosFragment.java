//Indicamos a que paquete pertenece esta clase:
package com.example.app_clientes.ui.viajes;
//Importamos los siguientes paquetes:
import android.animation.ValueAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.example.app_clientes.Biblioteca;
import com.example.app_clientes.R;
import com.example.app_clientes.adapter.MiAdapterTusViajesDisfrutados;
import com.example.app_clientes.jsonplaceholder.JsonPlaceHolderApi;
import com.example.app_clientes.pojos.Usuario;
import com.example.app_clientes.pojos.Viaje;
import com.example.app_clientes.vistas.VentanaChatIndividual;
import com.makeramen.roundedimageview.RoundedImageView;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
/**
 * Un simple {@link Fragment} subclase.
 */
//Clase que contiene toda la logica y conexion con la ventana de mostrar los viajes disfrutados o a disfrutar de la app:
public class TusViajesDisfrutadosFragment extends Fragment {
    //Atributos XML:
    private LottieAnimationView me_siento_vasio, elige_vehiculo_wey;
    private TextView textViewMe_siento_vasio, textViewElige_vehiculo_wey;
    private EditText editTextDescripcionUsuario,editTextOrigen,editTextDestino,editTextFecha,editTextPrecio,editTextMatricula,editTextMarcModelo;
    private TextView textViewNombreUsuario,textViewEdadUsuario;
    private TextView textViewTituloOrigen,textViewTituloDestino,textViewTituloFecha,textViewTituloPrecio,textViewTituloMatricula,textViewTituloMarcaYmodelo;
    private ImageView imageViewChat, imageViewEstado;
    private CircleImageView imageViewConductor;
    private RoundedImageView imgViewCoche;
    //Atributos de la clase:
    private List<Viaje> misViajesList = new ArrayList<>();
    private List<Usuario> misUsuariosList = new ArrayList<>();
    private RecyclerView recyclerViewTusViajes;
    private MiAdapterTusViajesDisfrutados miAdapterTusViajes;
    private String ID_USUARIO_CONVER;
    private String NOMBRE_USUARIO_CONVER;
    private String FOTO_USUARIO_CONVER;
    //Metodo que se ejecuta al crearse la vista:
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Conectamos el xml:
        View view = inflater.inflate(R.layout.fragment_tusviajesdisfrutados, container, false);
        //Vinculamos los atributos de la clase:
        imageViewEstado = view.findViewById(R.id.imageViewEstadoViajesDisfrutados);
        me_siento_vasio = view.findViewById(R.id.animation_vacio_ViajesDisfrutados);
        elige_vehiculo_wey = view.findViewById(R.id.animation_elige_algo_wey_ViajesDisfrutados);
        textViewMe_siento_vasio = view.findViewById(R.id.textViewTituloAnimacionVacioViajesDisfrutados);
        textViewElige_vehiculo_wey = view.findViewById(R.id.textViewTituloAnimacionEligeViajesDisfrutados);
        textViewTituloOrigen = view.findViewById(R.id.textViewTituloOrigenViajesDisfrutados);
        textViewTituloDestino = view.findViewById(R.id.textViewTituloDestinoViajesDisfrutados);
        textViewTituloFecha = view.findViewById(R.id.textViewTituloFechaViajesDisfrutados);
        textViewTituloPrecio = view.findViewById(R.id.textViewTituloPrecioViajesDisfrutados);
        textViewTituloMatricula = view.findViewById(R.id.textViewTituloMatriculaViajesDisfrutados);
        textViewTituloMarcaYmodelo = view.findViewById(R.id.textViewTituloMarcaYmodeloViajesDisfrutados);
        editTextDescripcionUsuario = view.findViewById(R.id.textViewDescripcionUsuarioViajesDisfrutados);editTextDescripcionUsuario.setKeyListener(null);
        editTextOrigen = view.findViewById(R.id.editTextOrigenViajesDisfrutados);editTextOrigen.setKeyListener(null);
        editTextDestino = view.findViewById(R.id.editTextDestinoViajesDisfrutados);editTextDestino.setKeyListener(null);
        editTextFecha = view.findViewById(R.id.editTextFechaViajesDisfrutados);editTextFecha.setKeyListener(null);
        editTextPrecio = view.findViewById(R.id.editTextPrecioViajesDisfrutados);editTextPrecio.setKeyListener(null);
        editTextMatricula = view.findViewById(R.id.editTextMatriculaViajesDisfrutados);editTextMatricula.setKeyListener(null);
        editTextMarcModelo = view.findViewById(R.id.editTextMarcaYmodeloViajesDisfrutados);editTextMarcModelo.setKeyListener(null);
        textViewNombreUsuario = view.findViewById(R.id.textViewNombreUsuarioViajesDisfrutados);
        textViewEdadUsuario = view.findViewById(R.id.textViewEdadUsuarioViajesDisfrutados);
        imageViewChat = view.findViewById(R.id.imageViewChatViajesDisfrutados);
        imageViewConductor = view.findViewById(R.id.IMGConductorViajesDisfrutados);
        imgViewCoche = view.findViewById(R.id.imageViewCocheViajesPublicados);
        recyclerViewTusViajes = view.findViewById(R.id.RVViajesDisfrutados);
        //Metodo onClick para chatear con el usuario:
        imageViewChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent VentanaChatIndividual = new Intent(getContext(), VentanaChatIndividual.class);
                VentanaChatIndividual.putExtra("ID_USUARIO_CONVER",ID_USUARIO_CONVER);
                VentanaChatIndividual.putExtra("NOMBRE_USUARIO_CONVER",NOMBRE_USUARIO_CONVER);
                VentanaChatIndividual.putExtra("FOTO_USUARIO_CONVER", FOTO_USUARIO_CONVER);
                startActivity(VentanaChatIndividual);
            }
        });
        //Cargamos los datos:
        cargarUsuariosViajesDisfrutados();
        //Recibidor de broadcast para cerrar sesion:
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent intent) {
                String action = intent.getAction();
                if (action.equals("cierre_de_sesion")) {
                    requireActivity().finish();
                }
            }
        };
        requireActivity().registerReceiver(broadcastReceiver, new IntentFilter("cierre_de_sesion"));
        return view;
    }
    //CARGA LOS DATOS DE LOS USUARIO CON LOS QUE EL USUARIO A CONTRATADO VIAJES
    private void cargarUsuariosViajesDisfrutados() {
        //Creamos objeto Retrofit, para lanzar peticiones y poder recibir respuestas:
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Biblioteca.ip).addConverterFactory(GsonConverterFactory.create()).build();
        //Vinculamos el cliente con la interfaz:
        final JsonPlaceHolderApi peticiones = retrofit.create(JsonPlaceHolderApi.class);
        //Creamos una peticion para obtener los viajes que ha reservado:
        Call <List<Viaje>> call = peticiones.getListViajesReservados(Biblioteca.usuarioSesion.getIdusuario());
        //Ejecutamos la petición en un hilo en segundo plano, retrofit lo hace por nosotros y esperamos a la respuesta:
        call.enqueue(new Callback <List<Viaje>>() {
            //Gestionamos la respuesta del servidor:
            @Override
            public void onResponse(Call <List<Viaje>> call, Response <List<Viaje>> response) {
                //Respuesta del servidor con un error y paramos el flujo del programa, indicando el codigo de error:
                if (!response.isSuccessful()) {
                    if(response.code()==404){
                        //Cambiamos la visibilidad
                        visibilidadVista(View.GONE);
                        elige_vehiculo_wey.setVisibility(View.GONE);
                        textViewElige_vehiculo_wey.setVisibility(View.GONE);
                        me_siento_vasio.setVisibility(View.VISIBLE);
                        me_siento_vasio.playAnimation();
                        me_siento_vasio.setRepeatCount(ValueAnimator.INFINITE);
                        textViewMe_siento_vasio.setVisibility(View.VISIBLE);
                    }else {
                        Toast.makeText(getContext(), "Code: " + response.code(), Toast.LENGTH_LONG).show();
                    }
                    return;
                }
                //Si la respuesta es correcta pasamos a rellenar el modelo que utliza el recycler view:
                misViajesList=response.body();
                if (misViajesList!=null&&!misViajesList.isEmpty()) {
                    //Tambien rellenamos la lista de usuarios, en el mismo orden que su viaje en la otra lista, para que se pueda acceder a los dos con la misma posicion:
                    for (int i = 0; i < misViajesList.size(); i++) {
                        misUsuariosList.add(misViajesList.get(i).getUsuario());
                    }
                    //INSTANCIAMOS Y ASOCIAMOS ELEMENTOS NECESARIOS PARA EL CORRECTO FUNCIONAMIENTO DEL RECYCLERVIEW
                    recyclerViewTusViajes.setHasFixedSize(true);// RecyclerView sabe de antemano que su tamaño no depende del contenido del adaptador, entonces omitirá la comprobación de si su tamaño debería cambiar cada vez que se agregue o elimine un elemento del adaptador.(mejora el rendimiento)
                    recyclerViewTusViajes.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                    miAdapterTusViajes = new MiAdapterTusViajesDisfrutados(getContext(), misUsuariosList);//Instanciamos un objeto de tipo Example_Adapter
                    recyclerViewTusViajes.setAdapter(miAdapterTusViajes);//Vinculamos el adapter al recyclerView
                    miAdapterTusViajes.setOnClickListener(new MiAdapterTusViajesDisfrutados.OnItemClickListener() {
                        @Override
                        public void OnUsuarioClick(int position) {
                            //Usuario
                            Glide.with(getContext()).load(misUsuariosList.get(position).getFotousuario()).error(R.drawable.user).into(imageViewConductor);
                            textViewNombreUsuario.setText(misUsuariosList.get(position).getNombre() + " " + misUsuariosList.get(position).getApellidos());
                            textViewEdadUsuario.setText(Biblioteca.obtieneEdad(misUsuariosList.get(position).getFechanacimiento()) + " años");
                            editTextDescripcionUsuario.setText(misUsuariosList.get(position).getDescripcion());
                            ID_USUARIO_CONVER = misUsuariosList.get(position).getIdusuario().toString();
                            NOMBRE_USUARIO_CONVER = misUsuariosList.get(position).getNombre()+" "+misUsuariosList.get(position).getApellidos();
                            FOTO_USUARIO_CONVER = misUsuariosList.get(position).getFotousuario();
                            //Viaje
                            editTextOrigen.setText(misViajesList.get(position).getLocalidadOrigen() + " - " + misViajesList.get(position).getLugarSalida());
                            editTextDestino.setText(misViajesList.get(position).getLocalidadDestino() + " - " + misViajesList.get(position).getLugarLlegada());
                            editTextFecha.setText(Biblioteca.obtieneHoraViajesEncontrados(misViajesList.get(position).getFechasalida()));
                            editTextPrecio.setText(misViajesList.get(position).getPrecio() + " €");
                            //Vehiculo
                            Glide.with(getContext()).load(misViajesList.get(position).getVehiculo().getFotovehiculo()).error(R.drawable.coche).into(imgViewCoche);
                            editTextMatricula.setText(misViajesList.get(position).getVehiculo().getMatricula());
                            editTextMarcModelo.setText(misViajesList.get(position).getVehiculo().getMarca() + " - " + misViajesList.get(position).getVehiculo().getModelo());
                            //Colocamos el estado del viaje:
                            Date fechaViaje = misViajesList.get(position).getFechasalida();
                            Date fechaActual = new Date(Calendar.getInstance().getTime().getTime());
                            if (fechaViaje.compareTo(fechaActual) < 0 || fechaViaje.equals(fechaActual)) {
                                imageViewEstado.setImageResource(R.drawable.caducado);
                            }else{
                                imageViewEstado.setImageResource(R.drawable.activo);
                            }
                            //Cambiamos la visibilidad
                            elige_vehiculo_wey.setVisibility(View.GONE);
                            textViewElige_vehiculo_wey.setVisibility(View.GONE);
                            me_siento_vasio.setVisibility(View.GONE);
                            textViewMe_siento_vasio.setVisibility(View.GONE);
                            visibilidadVista(View.VISIBLE);
                        }
                    });
                    //Cambiamos la visibilidad
                    me_siento_vasio.setVisibility(View.GONE);
                    textViewMe_siento_vasio.setVisibility(View.GONE);
                    visibilidadVista(View.GONE);
                    elige_vehiculo_wey.setVisibility(View.VISIBLE);
                    elige_vehiculo_wey.playAnimation();
                    elige_vehiculo_wey.setRepeatCount(ValueAnimator.INFINITE);
                    textViewElige_vehiculo_wey.setVisibility(View.VISIBLE);
                }else{
                    //Cambiamos la visibilidad
                    visibilidadVista(View.GONE);
                    elige_vehiculo_wey.setVisibility(View.GONE);
                    textViewElige_vehiculo_wey.setVisibility(View.GONE);
                    me_siento_vasio.setVisibility(View.VISIBLE);
                    me_siento_vasio.playAnimation();
                    me_siento_vasio.setRepeatCount(ValueAnimator.INFINITE);
                    textViewMe_siento_vasio.setVisibility(View.VISIBLE);
                }
            }
            //En caso de que no responda el servidor mostramos mensaje de error:
            @Override
            public void onFailure(Call <List<Viaje>> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    //Metodo que vuelve visible o gone los atributos en funcion de lo que se le pase:
    public void visibilidadVista(int tipo){
        imageViewEstado.setVisibility(tipo);
        editTextDescripcionUsuario.setVisibility(tipo);
        editTextOrigen.setVisibility(tipo);
        editTextDestino.setVisibility(tipo);
        editTextFecha.setVisibility(tipo);
        editTextPrecio.setVisibility(tipo);
        editTextMatricula.setVisibility(tipo);
        editTextMarcModelo.setVisibility(tipo);
        textViewNombreUsuario.setVisibility(tipo);
        textViewEdadUsuario.setVisibility(tipo);
        imageViewChat.setVisibility(tipo);
        imageViewConductor.setVisibility(tipo);
        imgViewCoche.setVisibility(tipo);
        textViewTituloOrigen.setVisibility(tipo);
        textViewTituloDestino.setVisibility(tipo);
        textViewTituloFecha.setVisibility(tipo);
        textViewTituloPrecio.setVisibility(tipo);
        textViewTituloMatricula.setVisibility(tipo);
        textViewTituloMarcaYmodelo.setVisibility(tipo);
    }
}
