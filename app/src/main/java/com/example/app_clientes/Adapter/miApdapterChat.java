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
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class miApdapterChat extends RecyclerView.Adapter<miApdapterChat.ExampleViewHolder> {

    private ArrayList<Mensaje> MensajesList= new ArrayList<>();//Atributo que contiene la lista de los datos a tratar (objetos de tipo ExampleItem)
    private Context c;

    public miApdapterChat(Context c) {
        this.c=c;
    }

    public void addMensaje(Mensaje mensaje){
        MensajesList.add(mensaje);
        notifyItemChanged(MensajesList.size());
    }

    @NonNull
    @Override
    //Sobreescribimos el metodo onCreateViewHolder que se va a encargar de asignar a una vista los elementos que contiene la plantilla creada en XML
    //para posteriormente instanciar un objeto de la clase interna ExampleViewHolder, pasandole por parametro la vista anterior y un listener
    //finaliza devolviendo un objeto de tipo exampleViewHolder
    public miApdapterChat.ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.item_mensajes, parent, false);    //Usamos el método inflate() para crear una vista a partir del layout XML definido en layout_listitem.
        return new miApdapterChat.ExampleViewHolder(v);
    }

    //Sobreescribimos el metodo onBindViewHolder que recibe por parametro un objeto de tipo ExampleViewHolder y la posicion del item al que
    //debe asociar datos, asocia a una instancia de ExampleItem el objeto de tipo ExampleItem que se encuentra en la posicion que ha recibido
    //por parametro. Por ultimo utiliza el objeto de tipo ExampleViewHolder para acceder a su unico atributo (mImageView) al que establece
    //el recurso que tiene asociado el objeto de la clase ExampleItem (currentItem) del que recibe la posición por parametro
    //El método onBindViewHolder() personaliza un elemento de tipo ViewHolder según su posicion.
    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
        holder.TVNombreMensaje.setTextColor(getRandomColor());
        holder.TVNombreMensaje.setText(MensajesList.get(position).getNombre());
        holder.TVMensajeMensaje.setText(MensajesList.get(position).getMensaje());
        holder.TVHoraMensaje.setText(MensajesList.get(position).getHora());
    }

    //Sobreescribimos el metodo getItemCount que nos devuelve el tamaño de la lista de objetos ExampleItem
    //Este metodo internamente establece la longuitud maxima que tendra la lista
    @Override
    public int getItemCount() {
        return MensajesList.size();
    }

    //CLASE INTERNA ESTATICA
    public static class ExampleViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView fotoPerfilMensaje;
        private TextView TVNombreMensaje;
        private TextView TVHoraMensaje;
        private TextView TVMensajeMensaje;

        //METODO CONSTRUCTOR de la clase interna ExampleViewHolder que recibe como parametro una instancia de la clase View y un listener ya que
        //al ser una clase estatica de no pasarselo no podria acceder a el listener
        public ExampleViewHolder(View itemView) {
            super(itemView);
            this.TVNombreMensaje = itemView.findViewById(R.id.TVNombreMensaje);
            this.TVHoraMensaje = itemView.findViewById(R.id.TVHoraMensaje);
            this.TVMensajeMensaje = itemView.findViewById(R.id.TVMensajeMensaje);

        }
    }

    public int getRandomColor(){
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }
}