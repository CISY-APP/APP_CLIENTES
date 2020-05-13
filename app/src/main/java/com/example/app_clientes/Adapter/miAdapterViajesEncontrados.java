package com.example.app_clientes.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.app_clientes.Item.ItemViajesEncontrados;
import com.example.app_clientes.R;

import java.util.ArrayList;

//RECYCLERVIEW DE LA VENTANA PRODUCTOS
public class miAdapterViajesEncontrados extends RecyclerView.Adapter<miAdapterViajesEncontrados.ExampleViewHolder> {

    private ArrayList<ItemViajesEncontrados> viajesEncontradosList;//Atributo que contiene la lista de los datos a tratar (objetos de tipo ExampleItem)
    private Context c;
    private OnItemClickListener mListener;//Atributo que nos permitira asignar un listener a cada item

    //INTERFAZ dentro de la clase la cual nos obliga a implementar y sobreescribir el metodo OnItemClick
    public interface OnItemClickListener {
        void OnMensajeClick(int position);//Metodo abstracto que recibe por parametro la posicion del item que ha sido pulsado
        void OnReservaClick(int position);
    }

    //Metodo SET de la clase Adapter que nos permite asignar un listener
    public void setOnClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public miAdapterViajesEncontrados(Context c, ArrayList<ItemViajesEncontrados> viajesEncontradosList ) {
        this.c = c;
        this.viajesEncontradosList = viajesEncontradosList;
    }

    @NonNull
    @Override
    //Sobreescribimos el metodo onCreateViewHolder que se va a encargar de asignar a una vista los elementos que contiene la plantilla creada en XML
    //para posteriormente instanciar un objeto de la clase interna ExampleViewHolder, pasandole por parametro la vista anterior y un listener
    //finaliza devolviendo un objeto de tipo exampleViewHolder
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_viaje_encontrado,parent,false);    //Usamos el método inflate() para crear una vista a partir del layout XML definido en layout_listitem.
        ExampleViewHolder exampleViewHolder=new ExampleViewHolder(v,mListener);

        return exampleViewHolder;
    }

    //Sobreescribimos el metodo onBindViewHolder que recibe por parametro un objeto de tipo ExampleViewHolder y la posicion del item al que
    //debe asociar datos, asocia a una instancia de ExampleItem el objeto de tipo ExampleItem que se encuentra en la posicion que ha recibido
    //por parametro. Por ultimo utiliza el objeto de tipo ExampleViewHolder para acceder a su unico atributo (mImageView) al que establece
    //el recurso que tiene asociado el objeto de la clase ExampleItem (currentItem) del que recibe la posición por parametro
    //El método onBindViewHolder() personaliza un elemento de tipo ViewHolder según su posicion.
    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
        ItemViajesEncontrados nuevoViajeEncontrado=viajesEncontradosList.get(position); //Crea un objeto ExampleItem igual que el objeto que devuelve el metodo mExampleList.get() en su posicion
        holder.TVNombre.setText(viajesEncontradosList.get(position).getNombre());
        holder.TVApellidos.setText(viajesEncontradosList.get(position).getApellidos());
        holder.TVEdad.setText("Edad: "+viajesEncontradosList.get(position).getEdad()+" años");
        holder.TVAsientosLibres.setText("Asientos disponibles: "+viajesEncontradosList.get(position).getAsientosLibres());
        holder.TVPrecio.setText(viajesEncontradosList.get(position).getPrecio() +"€");
        holder.mImageValoracion.setImageResource(nuevoViajeEncontrado.getValoracion());
        Glide.with(c).load(viajesEncontradosList.get(position).getUriImagenUsuario()).into(holder.mImageUsuario);


    }

    //Sobreescribimos el metodo getItemCount que nos devuelve el tamaño de la lista de objetos ExampleItem
    //Este metodo internamente establece la longuitud maxima que tendra la lista
    @Override
    public int getItemCount() {
        return viajesEncontradosList.size();
    }

    //CLASE INTERNA ESTATICA
    public static class ExampleViewHolder extends RecyclerView.ViewHolder {

        private ImageView mImageUsuario;
        private ImageView mImageValoracion;
        private Button BTMensaje;
        private Button BTReservar;
        private TextView TVNombre;
        private TextView TVApellidos;
        private TextView TVEdad;
        private TextView TVAsientosLibres;
        private TextView TVPrecio;


        //METODO CONSTRUCTOR de la clase interna ExampleViewHolder que recibe como parametro una instancia de la clase View y un listener ya que
        //al ser una clase estatica de no pasarselo no podria acceder a el listener
        public ExampleViewHolder(View itemView, final OnItemClickListener listener) {

            super(itemView);

            this.TVNombre = itemView.findViewById(R.id.TVdatosViaje);
            this.TVApellidos = itemView.findViewById(R.id.TVApellidos);
            this.TVEdad = itemView.findViewById(R.id.TVEdad);
            this.TVAsientosLibres = itemView.findViewById(R.id.TVAsientosDisponibles);
            this.TVPrecio = itemView.findViewById(R.id.TVPrecio);
            mImageUsuario = itemView.findViewById(R.id.IVUsuario);//Asocia el atributo de la clase al XML (imagen para el tablero)
            mImageValoracion = itemView.findViewById(R.id.IMGValoracion);
            BTMensaje = itemView.findViewById(R.id.BTMensaje);
            BTMensaje.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {//Si el listener que recibimos en el constructor es valido
                        //El adapter es el que sabe la posición absoluta dentro de la vista,
                        int position = getAdapterPosition();//Almacenamos la posicion del elemento que ha activado el evento
                        if (position != RecyclerView.NO_POSITION) {//Si la posición recibida es una posición valida dentro del RecyclerView
                            listener.OnMensajeClick(position);//Asignamos un listener al Item de la posición que hemos comprobado
                        }
                    }
                }
            });
            BTReservar = itemView.findViewById(R.id.BTReservar);
            BTReservar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {//Si el listener que recibimos en el constructor es valido
                        //El adapter es el que sabe la posición absoluta dentro de la vista,
                        int position = getAdapterPosition();//Almacenamos la posicion del elemento que ha activado el evento
                        if (position != RecyclerView.NO_POSITION) {//Si la posición recibida es una posición valida dentro del RecyclerView
                            listener.OnReservaClick(position);//Asignamos un listener al Item de la posición que hemos comprobado
                        }
                    }
                }
            });
        }
    }

}

