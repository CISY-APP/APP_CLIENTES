package com.example.app_clientes.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_clientes.R;
import com.example.app_clientes.pojos.Usuario;
import com.example.app_clientes.pojos.Viaje;

import java.util.ArrayList;

public class MiAdapterTusViajesPublicados extends RecyclerView.Adapter<MiAdapterTusViajesPublicados.ExampleViewHolder> {

    private ArrayList<Viaje> misViajesPublicadosList;//Atributo que contiene la lista de los datos a tratar (objetos de tipo ExampleItem)
    private final Context c;
    private MiAdapterTusViajesPublicados.OnItemClickListener mListener;
    private RecyclerView RVPersonasViaje;
    private ArrayList<Usuario> misUsuariosList = new ArrayList<>();
    private MiAdapterTusViajesDisfrutadosTusViajes miAdapterTusViajesDisfrutadosTusViajes;

    public MiAdapterTusViajesPublicados(Context c, ArrayList<Viaje> misViajesPublicadosList) {
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

    public void setMisViajesPublicadosList(ArrayList<Viaje> misViajesPublicadosList){
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
        misUsuariosList.clear();
        return exampleViewHolder;
    }

    //Sobreescribimos el metodo onBindViewHolder que recibe por parametro un objeto de tipo ExampleViewHolder y la posicion del item al que
    //debe asociar datos, asocia a una instancia de ExampleItem el objeto de tipo ExampleItem que se encuentra en la posicion que ha recibido
    //por parametro. Por ultimo utiliza el objeto de tipo ExampleViewHolder para acceder a su unico atributo (mImageView) al que establece
    //el recurso que tiene asociado el objeto de la clase ExampleItem (currentItem) del que recibe la posición por parametro
    //El método onBindViewHolder() personaliza un elemento de tipo ViewHolder según su posicion.
    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
        Viaje nuevoViaje= misViajesPublicadosList.get(position); //Crea un objeto ExampleItem igual que el objeto que devuelve el metodo mExampleList.get() en su posicion

        cargarUsuariosViajesDisfrutados();

        java.util.Date utilDate = new java.util.Date(); //fecha actual
        long lnMilisegundos = utilDate.getTime();
        java.sql.Date sqlDate = new java.sql.Date(lnMilisegundos);
        java.sql.Time sqlTime = new java.sql.Time(lnMilisegundos);
        java.sql.Timestamp sqlTimestamp = new java.sql.Timestamp(lnMilisegundos);

        if (nuevoViaje.getFechasalida().compareTo(sqlDate)<0) {
            holder.imageView1.setBackgroundColor(Color.parseColor("#FF6432"));
            holder.imagenEliminarViaje.setVisibility(View.INVISIBLE);
        } else {
            holder.imageView1.setBackgroundColor(Color.parseColor("#76FF32"));
            holder.imagenEliminarViaje.setVisibility(View.VISIBLE);
        }
        holder.ETOrigenViajePublicado.setText(nuevoViaje.getLugarSalida());
        holder.ETDestinoViajePublicado.setText(nuevoViaje.getLugarLlegada());
        //holder.editTextFechaViajesPublicados.setText(nuevoViaje.getFechasalida().toString());
        holder.editTextHoraViajesPublicados.setText(nuevoViaje.getFechasalida().toString());


        //Asociamos los atributos con los objeto del layout para poder usarlos
        //INSTANCIAMOS Y ASOCIAMOS ELEMENTOS NECESARIOS PARA EL CORRECTO FUNCIONAMIENTO DEL RECYCLERVIEW
        holder.RVPersonasViaje.setHasFixedSize(true);// RecyclerView sabe de antemano que su tamaño no depende del contenido del adaptador, entonces omitirá la comprobación de si su tamaño debería cambiar cada vez que se agregue o elimine un elemento del adaptador.(mejora el rendimiento)
        holder.RVPersonasViaje.setLayoutManager(new LinearLayoutManager(c, LinearLayoutManager.HORIZONTAL, false));
        miAdapterTusViajesDisfrutadosTusViajes = new MiAdapterTusViajesDisfrutadosTusViajes(c,misUsuariosList);//Instanciamos un objeto de tipo Example_Adapter
        holder.RVPersonasViaje.setAdapter(miAdapterTusViajesDisfrutadosTusViajes);//Vinculamos el adapter al recyclerView

    }

    //Sobreescribimos el metodo getItemCount que nos devuelve el tamaño de la lista de objetos ExampleItem
    //Este metodo internamente establece la longuitud maxima que tendra la lista
    @Override
    public int getItemCount() {
        return misViajesPublicadosList.size();
    }

    //CLASE INTERNA ESTATICA
    public static class ExampleViewHolder extends RecyclerView.ViewHolder {

        private TextView ETOrigenViajePublicado;
        private TextView ETDestinoViajePublicado;
        private TextView editTextHoraViajesPublicados;
        private CardView itemviaje_publicado;
        private RecyclerView RVPersonasViaje;
        private ImageView imageView1;
        private ImageView imagenEliminarViaje;

        //METODO CONSTRUCTOR de la clase interna ExampleViewHolder que recibe como parametro una instancia de la clase View y un listener ya que
        //al ser una clase estatica de no pasarselo no podria acceder a el listener
        ExampleViewHolder(View itemView, final MiAdapterTusViajesPublicados.OnItemClickListener listener) {
            super(itemView);
            this.RVPersonasViaje = itemView.findViewById(R.id.RVPersonasViaje);
            this.itemviaje_publicado = itemView.findViewById(R.id.itemviaje_publicado);
            this.ETOrigenViajePublicado = itemView.findViewById(R.id.ETOrigenViajePublicado);
            this.ETDestinoViajePublicado = itemView.findViewById(R.id.ETDestinoViajePublicado);
            this.editTextHoraViajesPublicados = itemView.findViewById(R.id.editTextHoraViajesPublicados);
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

    //CARGA LOS DATOS DE LOS USUARIO QUE HAN VIAJADO EN TUS VIAJES
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
                null, null, utilDate, "https://www.xlsemanal.com/wp-content/uploads/sites/3/2019/12/edward-norton-no-te-puedes-fiar-de-las-personas-que-fueron-felices-cuanto-tenian-14-anso.jpg", null, "Hola me llamo Pilar",
                null, null, null, null,
                null, null, null));
        misUsuariosList.add(new Usuario("Javier", "Gómez Fernández", null, null, null,
                null, null, utilDate, "https://firebasestorage.googleapis.com/v0/b/appclientes-a0e43.appspot.com/o/Fotos%2F1?alt=media&token=f59bccd3-4c6e-4872-ab50-14b39743685d", null, "Hola me llamo Javier",
                null, null, null, null,
                null, null, null));
        misUsuariosList.add(new Usuario("Roberto", "Castaño Romero", null, null, null,
                null, null, utilDate, "https://assets.entrepreneur.com/content/3x2/2000/20181012160100-atractiva.jpeg?width=700&crop=2:1", null, "Hola me llamo Roberto",
                null, null, null, null,
                null, null, null));
        misUsuariosList.add(new Usuario("Pilar", "Garcia Romero", null, null, null,
                null, null, utilDate, "https://www.xlsemanal.com/wp-content/uploads/sites/3/2019/12/edward-norton-no-te-puedes-fiar-de-las-personas-que-fueron-felices-cuanto-tenian-14-anso.jpg", null, "Hola me llamo Pilar",
                null, null, null, null,
                null, null, null));
    }

}

