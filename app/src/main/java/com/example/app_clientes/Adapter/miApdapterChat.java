package com.example.app_clientes.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.app_clientes.Pojos.Mensaje;
import com.example.app_clientes.R;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class miApdapterChat extends RecyclerView.Adapter<miApdapterChat.ExampleViewHolder> {

    private static final int DEFAULT_MESSAGE_TYPE = 0;
    private static final int OWN_MESSAGE_TYPE = 1;

    private ArrayList<Mensaje> MensajesList = new ArrayList<>();
    private Context c;
    private String IDUsuario;
    private HashMap<String, String> coloresUsuarios = new HashMap<String, String>();

    public miApdapterChat(Context c) {
        this.c = c;
    }

    public void addMensaje(Mensaje mensaje) {
        MensajesList.add(mensaje);
        notifyItemChanged(MensajesList.size());

        if (!coloresUsuarios.containsKey(mensaje.getIDUsuario())) {
            coloresUsuarios.put(mensaje.getIDUsuario(), getRandomColor());
        }
    }

    @NonNull
    @Override
    //Sobreescribimos el metodo onCreateViewHolder que se va a encargar de asignar a una vista los elementos que contiene la plantilla creada en XML
    //para posteriormente instanciar un objeto de la clase interna ExampleViewHolder, pasandole por parametro la vista anterior y un listener
    //finaliza devolviendo un objeto de tipo exampleViewHolder
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case OWN_MESSAGE_TYPE: {
                View v = LayoutInflater.from(c).inflate(R.layout.item_mensajes_enviados, parent,false);
                return new OwnViewHolder(v);
            }

            case DEFAULT_MESSAGE_TYPE:
            default: {
                View v = LayoutInflater.from(c).inflate(R.layout.item_mensajes_recibidos, parent, false);
                return new DefaultViewHolder(v);
            }
        }
    }

    //Sobreescribimos el metodo onBindViewHolder que recibe por parametro un objeto de tipo ExampleViewHolder y la posicion del item al que
    //debe asociar datos, asocia a una instancia de ExampleItem el objeto de tipo ExampleItem que se encuentra en la posicion que ha recibido
    //por parametro. Por ultimo utiliza el objeto de tipo ExampleViewHolder para acceder a su unico atributo (mImageView) al que establece
    //el recurso que tiene asociado el objeto de la clase ExampleItem (currentItem) del que recibe la posición por parametro
    //El método onBindViewHolder() personaliza un elemento de tipo ViewHolder según su posicion.
    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
        Mensaje message = MensajesList.get(position);
        holder.bindMessage(message);
    }

    //Sobreescribimos el metodo getItemCount que nos devuelve el tamaño de la lista de objetos ExampleItem
    //Este metodo internamente establece la longuitud maxima que tendra la lista
    @Override
    public int getItemCount() {
        return MensajesList.size();
    }

    //Método utilizado para saber si un mensaje es enviado o recibido
    @Override
    public int getItemViewType(int position) {
        Mensaje message = MensajesList.get(position);
        if (message.getIDUsuario().equals(IDUsuario)) {
            return OWN_MESSAGE_TYPE;
        }
        return DEFAULT_MESSAGE_TYPE;
    }

    abstract class ExampleViewHolder extends RecyclerView.ViewHolder {

        TextView TVNombreMensaje;
        TextView TVHoraMensaje;
        TextView TVMensajeMensaje;
        CircleImageView IVImagenUsuario;

        public ExampleViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void bindMessage(Mensaje message) {

            TVNombreMensaje.setTextColor(Color.parseColor(coloresUsuarios.get(message.getIDUsuario())));
            TVNombreMensaje.setText(message.getNombre());
            TVMensajeMensaje.setText(message.getMensaje());
            TVHoraMensaje.setText(message.getHora());

        }

    }

    class OwnViewHolder extends ExampleViewHolder {

        public OwnViewHolder(View itemView) {

            super(itemView);
            this.TVNombreMensaje = itemView.findViewById(R.id.TVNombreMensaje);
            this.TVHoraMensaje = itemView.findViewById(R.id.TVHoraMensaje);
            this.TVMensajeMensaje = itemView.findViewById(R.id.TVMensajeMensaje);

        }
    }

    class DefaultViewHolder extends ExampleViewHolder {

        public DefaultViewHolder(View itemView) {
            super(itemView);
            this.TVNombreMensaje = itemView.findViewById(R.id.TVNombreMensaje);
            this.TVHoraMensaje = itemView.findViewById(R.id.TVHoraMensaje);
            this.TVMensajeMensaje = itemView.findViewById(R.id.TVMensajeMensaje);
            this.IVImagenUsuario = itemView.findViewById(R.id.IVImagenUsuario);
        }

        @Override
        public void bindMessage(Mensaje message) {
            super.bindMessage(message);
            if(message.getDireccionFotoUsuario().equals("")){
                Glide.with(c).load(R.drawable.user).into(IVImagenUsuario);
            }else{
                Glide.with(c).load(message.getDireccionFotoUsuario()).into(IVImagenUsuario);
            }
        }
    }

    public void setIDUsuario(String IDUsuario) {
        this.IDUsuario = IDUsuario;
    }

    //Metodo para generar colores aleatorios
    private String getRandomColor() {
        ArrayList<String> coloresAleatorios = new ArrayList<>();
        coloresAleatorios.add("#FC0000");
        coloresAleatorios.add("#89FC00");
        coloresAleatorios.add("#00FC89");
        coloresAleatorios.add("#0041FC");
        coloresAleatorios.add("#7E00FC");
        coloresAleatorios.add("#FC009D");
        coloresAleatorios.add("#3AC286");
        coloresAleatorios.add("#3A53C2");
        coloresAleatorios.add("#728AF6");
        coloresAleatorios.add("#664605");
        coloresAleatorios.add("#E29784");
        coloresAleatorios.add("#67007C");
        return coloresAleatorios.get((int) ((Math.random() * 1000f) % 12f));
    }
}