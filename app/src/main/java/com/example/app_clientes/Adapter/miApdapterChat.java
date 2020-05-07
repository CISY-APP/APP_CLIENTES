package com.example.app_clientes.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_clientes.Item.ItemViajesEncontrados;
import com.example.app_clientes.Pojos.Mensaje;
import com.example.app_clientes.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class miApdapterChat extends RecyclerView.Adapter<miApdapterChat.ExampleViewHolder> {

    private static final int DEFAULT_MESSAGE_TYPE = 0;
    private static final int OWN_MESSAGE_TYPE = 1;

    private ArrayList<Mensaje> MensajesList = new ArrayList<>();
    //Atributo que contiene la lista de los datos a tratar (objetos de tipo ExampleItem)
    private Context c;
    private String emailUsuario;

    private HashMap<String, String> userColors = new HashMap<String, String>();

    public miApdapterChat(Context c) {
        this.c = c;
    }

    public void addMensaje(Mensaje mensaje) {
        MensajesList.add(mensaje);
        notifyItemChanged(MensajesList.size());

        if (!userColors.containsKey(mensaje.getEmail())) {
            userColors.put(mensaje.getEmail(), getRandomColor());
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
                View v = LayoutInflater.from(c).inflate(R.layout.item_mensajes_own, parent,
                        false);
                return new OwnViewHolder(v);
            }

            case DEFAULT_MESSAGE_TYPE:
            default: {
                View v = LayoutInflater.from(c).inflate(R.layout.item_mensajes, parent,
                        false);
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

    @Override
    public int getItemViewType(int position) {
        Mensaje message = MensajesList.get(position);

        if (message.getEmail().equals(emailUsuario)) {
            return OWN_MESSAGE_TYPE;
        }
        return DEFAULT_MESSAGE_TYPE;
    }

    //CLASE INTERNA ESTATICA
    abstract class ExampleViewHolder extends RecyclerView.ViewHolder {

        TextView TVNombreMensaje;
        TextView TVHoraMensaje;
        TextView TVMensajeMensaje;

        public ExampleViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void bindMessage(Mensaje message) {
            TVNombreMensaje.setTextColor(Color.parseColor(userColors.get(message.getEmail())));
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
        }
    }

    public void setEmailUsuario(String emailUsuario) {
        this.emailUsuario = emailUsuario;
    }

    private String getEmailUsuario() {
        return emailUsuario;
    }

    //Metodo para generar colores aleatorios
    private String getRandomColor() {
        ArrayList<String> coloresAleatorios = new ArrayList<>();
        coloresAleatorios.add("#07a0c3");
        coloresAleatorios.add("#f0c808");
        coloresAleatorios.add("#dd1c1a");
        coloresAleatorios.add("#ffffff");
        coloresAleatorios.add("#FFAE00");
        coloresAleatorios.add("#00FF9E");
        coloresAleatorios.add("#00FF9E");
        return coloresAleatorios.get((int) ((Math.random() * 1000f) % 7f));
    }

}