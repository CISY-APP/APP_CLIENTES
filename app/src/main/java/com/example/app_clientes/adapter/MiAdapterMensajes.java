package com.example.app_clientes.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.app_clientes.pojos.Conversacion;
import com.example.app_clientes.R;

import java.util.ArrayList;

//RECYCLERVIEW DE LA VENTANA PRODUCTOS
public class MiAdapterMensajes extends RecyclerView.Adapter<MiAdapterMensajes.ExampleViewHolder> {

    private final Context c;
    private ArrayList<Conversacion> conversacionArrayListList;//Atributo que contiene la lista de los datos a tratar (objetos de tipo ExampleItem)
    private OnItemClickListener mListener;//Atributo que nos permitira asignar un listener a cada item

    public MiAdapterMensajes(Context c, ArrayList<Conversacion> conversacionArrayListList ) {
        this.c = c;
        this.conversacionArrayListList = conversacionArrayListList;
    }
    //INTERFAZ dentro de la clase la cual nos obliga a implementar y sobreescribir el metodo OnItemClick
    public interface OnItemClickListener {
        void OnAbreChatClick(int position);//Metodo abstracto que recibe por parametro la posicion del item que ha sido pulsado
    }

    //Metodo SET de la clase Adapter que nos permite asignar un listener
    public void setOnClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    public Conversacion getConversacion(int i){
        return conversacionArrayListList.get(i);
    }

    public void setConversaciones(ArrayList<Conversacion> conversaciones) {
        conversacionArrayListList = conversaciones;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    //Sobreescribimos el metodo onCreateViewHolder que se va a encargar de asignar a una vista los elementos que contiene la plantilla creada en XML
    //para posteriormente instanciar un objeto de la clase interna ExampleViewHolder, pasandole por parametro la vista anterior y un listener
    //finaliza devolviendo un objeto de tipo exampleViewHolder
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_conversacion,parent,false);    //Usamos el método inflate() para crear una vista a partir del layout XML definido en layout_listitem.
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
        Conversacion conv = conversacionArrayListList.get(position);
        holder.TVNombreUsuario.setText(conv.getId_usuario());
        holder.TVUltimoMensaje.setText(conv.getUltimoMensaje());
        holder.TVHoraUltimoMensaje.setText(conv.getHoraUltimoMensaje());

        holder.TSinLeer.setText(conv.getMensajesSinLeer().toString());
        holder.TSinLeer.setVisibility( conv.mensajesSinLeer == 0 ? View.GONE : View.VISIBLE );

        if(conv.getFotoUsuarioContrario().equals("")){
            Glide.with(c).load(R.drawable.user).into(holder.CIUsuarioCoversacion);
        }else{
            Glide.with(c).load(conv.getFotoUsuarioContrario()).into(holder.CIUsuarioCoversacion);
        }
    }

    //Sobreescribimos el metodo getItemCount que nos devuelve el tamaño de la lista de objetos ExampleItem
    //Este metodo internamente establece la longuitud maxima que tendra la lista
    @Override
    public int getItemCount() {
        return conversacionArrayListList.size();
    }

    //CLASE INTERNA ESTATICA
    public static class ExampleViewHolder extends RecyclerView.ViewHolder {

        private final TextView TVNombreUsuario;
        private final TextView TVUltimoMensaje;
        private final TextView TVHoraUltimoMensaje;
        private final TextView TSinLeer;
        private final ImageView CIUsuarioCoversacion;

        //METODO CONSTRUCTOR de la clase interna ExampleViewHolder que recibe como parametro una instancia de la clase View y un listener ya que
        //al ser una clase estatica de no pasarselo no podria acceder a el listener
        ExampleViewHolder(View itemView, final OnItemClickListener listener) {

            super(itemView);

            TVNombreUsuario = itemView.findViewById(R.id.TVNombreUsuarioConversacion);
            TVUltimoMensaje = itemView.findViewById(R.id.TVUltimoMensajeConversacion);
            TVHoraUltimoMensaje = itemView.findViewById(R.id.TVHoraUltimoMensaje);
            TSinLeer = itemView.findViewById(R.id.TSinLeer);
            CIUsuarioCoversacion = itemView.findViewById(R.id.CIUsuarioCoversacion);
            View cardViewCoversacion = itemView.findViewById(R.id.cardViewCoversacion);
            cardViewCoversacion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {//Si el listener que recibimos en el constructor es valido
                        //El adapter es el que sabe la posición absoluta dentro de la vista,
                        int position = getAdapterPosition();//Almacenamos la posicion del elemento que ha activado el evento
                        if (position != RecyclerView.NO_POSITION) {//Si la posición recibida es una posición valida dentro del RecyclerView
                            listener.OnAbreChatClick(position);//Asignamos un listener al Item de la posición que hemos comprobado
                        }
                    }
                }
            });
            cardViewCoversacion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {//Si el listener que recibimos en el constructor es valido
                        //El adapter es el que sabe la posición absoluta dentro de la vista,
                        int position = getAdapterPosition();//Almacenamos la posicion del elemento que ha activado el evento
                        if (position != RecyclerView.NO_POSITION) {//Si la posición recibida es una posición valida dentro del RecyclerView
                            listener.OnAbreChatClick(position);//Asignamos un listener al Item de la posición que hemos comprobado
                        }
                    }
                }
            });
        }
    }

}