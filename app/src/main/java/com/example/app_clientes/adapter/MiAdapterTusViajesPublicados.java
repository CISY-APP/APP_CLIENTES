//Indicamos a que paquete pertenece esta clase:
package com.example.app_clientes.adapter;
//Importamos los siguientes paquetes:
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.app_clientes.Biblioteca;
import com.example.app_clientes.R;
import com.example.app_clientes.jsonplaceholder.JsonPlaceHolderApi;
import com.example.app_clientes.pojos.Usuario;
import com.example.app_clientes.pojos.UsuarioPublicado;
import com.example.app_clientes.pojos.ViajePublicado;
import com.example.app_clientes.ui.viajes.TusViajesDisfrutadosFragment;
import com.example.app_clientes.ui.viajes.TusViajesFragment;
import com.example.app_clientes.ui.viajes.TusViajesPublicadosFragment;
import com.example.app_clientes.vistas.VentanaChatIndividual;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
//Clase adapter de viajes publicados:
public class MiAdapterTusViajesPublicados extends RecyclerView.Adapter<MiAdapterTusViajesPublicados.ExampleViewHolder> {
    //Atributos:
    private List<ViajePublicado> misViajesPublicadosList;   //Atributo que contiene la lista de los viajes publicados.
    private final Context c;
    private MiAdapterTusViajesPublicados.OnItemClickListener mListener;
    //Constructor
    public MiAdapterTusViajesPublicados(Context c, List<ViajePublicado> misViajesPublicadosList) {
        this.c = c;
        this.misViajesPublicadosList = misViajesPublicadosList;
    }
    //INTERFAZ dentro de la clase la cual nos obliga a implementar y sobreescribir el metodo OnItemClick
    public interface OnItemClickListener {
        void OnEliminarViajePublicadoClick(int position);//Metodo abstracto que recibe por parametro la posicion del item que ha sido pulsado
    }
    //Metodo SET de la clase Adapter que nos permite asignar un listener
    public void setOnClickListener(MiAdapterTusViajesPublicados.OnItemClickListener listener) {
        mListener = listener;
    }
    //Metodo set para establecer la lista viajes publicados:
    public void setMisViajesPublicadosList(List<ViajePublicado> misViajesPublicadosList){
        this.misViajesPublicadosList = misViajesPublicadosList;
    }
    @NonNull
    @Override
    //Sobreescribimos el metodo onCreateViewHolder que se va a encargar de asignar a una vista los elementos que contiene la plantilla creada en XML
    //para posteriormente instanciar un objeto de la clase interna ExampleViewHolder, pasandole por parametro la vista anterior y un listener
    //finaliza devolviendo un objeto de tipo exampleViewHolder
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_viajepublicado,parent,false);    //Usamos el método inflate() para crear una vista a partir del layout XML definido en layout_listitem.
        ExampleViewHolder exampleViewHolder=new ExampleViewHolder(v, mListener);
        return exampleViewHolder;
    }
    //Sobreescribimos el metodo onBindViewHolder que recibe por parametro un objeto de tipo ExampleViewHolder y la posicion del item al que
    //debe asociar datos, asocia a una instancia de ExampleItem el objeto de tipo ExampleItem que se encuentra en la posicion que ha recibido
    //por parametro. Por ultimo utiliza el objeto de tipo ExampleViewHolder para acceder a su unico atributo (mImageView) al que establece
    //el recurso que tiene asociado el objeto de la clase ExampleItem (currentItem) del que recibe la posición por parametro
    //El método onBindViewHolder() personaliza un elemento de tipo ViewHolder según su posicion.
    @Override
    public void onBindViewHolder(@NonNull final ExampleViewHolder holder, int position) {
        ViajePublicado nuevoViaje= misViajesPublicadosList.get(position); //Crea un objeto ExampleItem igual que el objeto que devuelve el metodo mExampleList.get() en su posicion

        java.util.Date utilDate = new java.util.Date(); //fecha actual
        long lnMilisegundos = utilDate.getTime();
        java.sql.Date sqlDate = new java.sql.Date(lnMilisegundos);
        java.sql.Time sqlTime = new java.sql.Time(lnMilisegundos);
        java.sql.Timestamp sqlTimestamp = new java.sql.Timestamp(lnMilisegundos);
        //Si la fecha del viaje publicado no es anterior a la actual, le pintamos un logo de papelera para que pueda cancelar el viaje:
        if (nuevoViaje.getFechasalida().compareTo(sqlDate)<0) {
            holder.imageView1.setBackgroundColor(Color.parseColor("#ec1c24"));
            holder.imagenEliminarViaje.setVisibility(View.GONE);
        } else {
            holder.imageView1.setBackgroundColor(Color.parseColor("#48cd72"));
            holder.imagenEliminarViaje.setVisibility(View.VISIBLE);
        }
        // Cargamos los datos del modelo en el holder:
        holder.ETOrigenViajePublicado.setText(nuevoViaje.getLocalidadorigen()+" - "+nuevoViaje.getLugarsalida());
        holder.ETDestinoViajePublicado.setText(nuevoViaje.getLocalidaddestino()+" - "+nuevoViaje.getLugarllegada());
        holder.editTextHoraViajesPublicados.setText(Biblioteca.obtieneHoraViajesEncontrados(nuevoViaje.getFechasalida()));
        holder.editTextPrecio.setText(nuevoViaje.getPrecio()+"€");
        holder.editTextVehiculo.setText(nuevoViaje.getVehiculo());
        //RecyclerView interior y su configuracion:
        RecyclerView RVPersonasViaje;
        final ArrayList<Usuario> misUsuariosList = new ArrayList<>();
        MiAdapterTusViajesDisfrutadosTusViajes miAdapterTusViajesDisfrutadosTusViajes;
        misUsuariosList.clear();
        for (UsuarioPublicado u: misViajesPublicadosList.get(position).getListUsuariosPublicados()) {
            Usuario aux = new Usuario(u.getNombre(), u.getApellidos(), null, null, u.getTelefono(),
                    u.getEmail(), null, u.getFechanacimiento(), u.getFotousuario(), null, u.getDescripcion(),
                    null, null, null, null,
                    null, null, null);
            aux.setIdusuario(u.getIdusuario());
            aux.setIdViaje(position);
            misUsuariosList.add(aux);
        }
        if(misUsuariosList.size()>0){
            holder.lineaSeparador.setVisibility(View.VISIBLE);
        }else{
            holder.lineaSeparador.setVisibility(View.GONE);
        }
        //Asociamos los atributos con los objeto del layout para poder usarlos
        //INSTANCIAMOS Y ASOCIAMOS ELEMENTOS NECESARIOS PARA EL CORRECTO FUNCIONAMIENTO DEL RECYCLERVIEW
        holder.RVPersonasViaje.setHasFixedSize(true);// RecyclerView sabe de antemano que su tamaño no depende del contenido del adaptador, entonces omitirá la comprobación de si su tamaño debería cambiar cada vez que se agregue o elimine un elemento del adaptador.(mejora el rendimiento)
        holder.RVPersonasViaje.setLayoutManager(new LinearLayoutManager(c, LinearLayoutManager.HORIZONTAL, false));
        miAdapterTusViajesDisfrutadosTusViajes = new MiAdapterTusViajesDisfrutadosTusViajes(c,misUsuariosList);//Instanciamos un objeto de tipo Example_Adapter
        holder.RVPersonasViaje.setAdapter(miAdapterTusViajesDisfrutadosTusViajes);//Vinculamos el adapter al recyclerView
        //Añadir listener al rv de usuarios:
       miAdapterTusViajesDisfrutadosTusViajes.setOnClickListener(new MiAdapterTusViajesDisfrutadosTusViajes.OnItemClickListener() {
            @Override
            public void OnUsuarioClick(int position) {
                //Si el usuario hace click normal se le lleva a chatear con el usuario que ha clickeado:
                Intent VentanaChatIndividual = new Intent(c, VentanaChatIndividual.class);
                VentanaChatIndividual.putExtra("ID_USUARIO_CONVER",misUsuariosList.get(position).getIdusuario().toString());
                VentanaChatIndividual.putExtra("NOMBRE_USUARIO_CONVER",misUsuariosList.get(position).getNombre()+" "+misUsuariosList.get(position).getApellidos());
                VentanaChatIndividual.putExtra("FOTO_USUARIO_CONVER", misUsuariosList.get(position).getFotousuario());
                c.startActivity(VentanaChatIndividual);
            }
           @Override
           public void OnUsuarioLongClick(final int position) {
               //Comprobamos que el viaje no este caducado para poder borrar al usuario de su reserva:
               Date fechaViaje = misViajesPublicadosList.get(misUsuariosList.get(position).getIdViaje()).getFechasalida();
               Date fechaActual = new Date(Calendar.getInstance().getTime().getTime());
               if (fechaViaje.compareTo(fechaActual) > 0) {
                   //Cancelamos el viaje para el usuario longclickado siempre y cuando ya no se haya caducado:
                   //Creamos objeto Retrofit, para lanzar peticiones y poder recibir respuestas:
                   Retrofit retrofit = new Retrofit.Builder().baseUrl(Biblioteca.ip).addConverterFactory(GsonConverterFactory.create()).build();
                   //Vinculamos el cliente con la interfaz:
                   final JsonPlaceHolderApi peticiones = retrofit.create(JsonPlaceHolderApi.class);
                   //Creamos una peticion para obtener los viajes que ha reservado:
                   Call <Void> call = peticiones.cancelarReservaViajeReservado(misViajesPublicadosList.get(misUsuariosList.get(position).getIdViaje()).getIdviaje(),misUsuariosList.get(position).getIdusuario());
                   //Ejecutamos la petición en un hilo en segundo plano, retrofit lo hace por nosotros y esperamos a la respuesta:
                   call.enqueue(new Callback <Void>() {
                       //Gestionamos la respuesta del servidor:
                       @Override
                       public void onResponse(Call <Void> call, Response <Void> response) {
                           //Respuesta del servidor con un error y paramos el flujo del programa, indicando el codigo de error:
                           if (!response.isSuccessful()) {
                               if(response.code()==404){
                                   Toast.makeText(c, "Viaje caducado, no se pudo eliminar cliente.", Toast.LENGTH_LONG).show();
                                   //Lanzamos un broadcast a la actividad 'a' para que finalice y luego finalizamos esta, y asi solo queda el home:
                                   Intent intent = new Intent("reiniciate_fragment_viajes_publicados");
                                   c.sendBroadcast(intent);
                               }else {
                                   Toast.makeText(c, "Codigo de error: ", Toast.LENGTH_LONG).show();
                               }
                               return;
                           }
                           //Si la respuesta es correcta pasamos a rellenar el modelo que utliza el recycler view:
                           Toast.makeText(c, "Cliente eliminado correctamente.", Toast.LENGTH_LONG).show();
                           //Lanzamos un broadcast a la actividad 'a' para que finalice y luego finalizamos esta, y asi solo queda el home:
                           Intent intent = new Intent("reiniciate_fragment_viajes_publicados");
                           c.sendBroadcast(intent);
                       }
                       //En caso de que no responda el servidor mostramos mensaje de error:
                       @Override
                       public void onFailure(Call <Void> call, Throwable t) {
                           Toast.makeText(c, "El servidor esta caido.", Toast.LENGTH_LONG).show();
                       }
                   });
               }else{
                   Toast.makeText(c, "Viaje caducado, no se pudo eliminar cliente.", Toast.LENGTH_LONG).show();
                   //Lanzamos un broadcast a la actividad 'a' para que finalice y luego finalizamos esta, y asi solo queda el home:
                   Intent intent = new Intent("reiniciate_fragment_viajes_publicados");
                   c.sendBroadcast(intent);
               }
           }
        });
    }
    //Sobreescribimos el metodo getItemCount que nos devuelve el tamaño de la lista de objetos ExampleItem
    //Este metodo internamente establece la longuitud maxima que tendra la lista
    @Override
    public int getItemCount() {
        return misViajesPublicadosList.size();
    }
    //CLASE INTERNA ESTATICA
    public static class ExampleViewHolder extends RecyclerView.ViewHolder {
        //Atributos:
        private TextView ETOrigenViajePublicado;
        private TextView ETDestinoViajePublicado;
        private TextView editTextHoraViajesPublicados;
        private TextView editTextVehiculo;
        private TextView editTextPrecio;
        private CardView itemviaje_publicado;
        private View lineaSeparador;
        private RecyclerView RVPersonasViaje;
        private ImageView imageView1;
        private ImageButton imagenEliminarViaje;
        //METODO CONSTRUCTOR de la clase interna ExampleViewHolder que recibe como parametro una instancia de la clase View y un listener ya que
        //al ser una clase estatica de no pasarselo no podria acceder a el listener
        ExampleViewHolder(View itemView, final MiAdapterTusViajesPublicados.OnItemClickListener listener) {
            super(itemView);
            this.lineaSeparador = itemView.findViewById(R.id.separadorViajePublicado);
            this.RVPersonasViaje = itemView.findViewById(R.id.RVPersonasViaje);
            this.itemviaje_publicado = itemView.findViewById(R.id.itemviaje_publicado);
            this.ETOrigenViajePublicado = itemView.findViewById(R.id.ETOrigenViajePublicado);
            this.ETDestinoViajePublicado = itemView.findViewById(R.id.ETDestinoViajePublicado);
            this.editTextHoraViajesPublicados = itemView.findViewById(R.id.editTextHoraViajesPublicados);
            this.editTextVehiculo = itemView.findViewById(R.id.ETVehiculoViajePublicado);
            this.editTextPrecio = itemView.findViewById(R.id.editTextPrecioViajesPublicados);
            this.imageView1 = itemView.findViewById(R.id.imageView1);
            this.imagenEliminarViaje = itemView.findViewById(R.id.imagenEliminarViaje);
            imagenEliminarViaje.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {//Si el listener que recibimos en el constructor es valido
                        //El adapter es el que sabe la posición absoluta dentro de la vista,
                        int position = getAdapterPosition();//Almacenamos la posicion del elemento que ha activado el evento
                        if (position != RecyclerView.NO_POSITION) {//Si la posición recibida es una posición valida dentro del RecyclerView
                            listener.OnEliminarViajePublicadoClick(position);//Asignamos un listener al Item de la posición que hemos comprobado
                        }
                    }
                }
            });
        }
    }
    //Metodo que pinta una animacion en el holder view:
    protected void setAnimation(ExampleViewHolder holder) {
        //Declaramos un animatorSet para poder luego ejecutar un conjunto de animaciones a la vez:
        AnimatorSet animatorSetEscale = new AnimatorSet();
        //Instanciamos el conjunto de animaciones:
        //Animacion para el edittext Localidad Origen:
        ObjectAnimator scaleDownX_holderViajes = ObjectAnimator.ofFloat(holder.itemView, "scaleX", 0.0f, 1.0f);
        ObjectAnimator scaleDownY_holderViajes = ObjectAnimator.ofFloat(holder.itemView, "scaleY", 0.0f, 1.0f);
        //Configuramos el animatorSet, como se tienen que reproducir las animaciones, el tiempo que duran, que clase de interpolador utilizan y la lanzamos:
        animatorSetEscale.play(scaleDownX_holderViajes).with(scaleDownY_holderViajes);
        animatorSetEscale.setDuration(Biblioteca.tAnimacionesScaleInicial);
        animatorSetEscale.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSetEscale.start();
    }
    //Metodo que cancela la animacion de ese holder si ya esta fuera de la pantalla:
    @Override
    public void onViewDetachedFromWindow(@NonNull ExampleViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }
    //Metodo que pinta la animacion de ese holder si esta ya dentro de la pantalla:
    @Override
    public void onViewAttachedToWindow(@NonNull ExampleViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        setAnimation(holder);
    }
}

