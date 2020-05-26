package com.example.app_clientes.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.app_clientes.R;
import com.example.app_clientes.pojos.Usuario;

import java.util.ArrayList;

//RECYCLERVIEW DE LA VENTANA PRODUCTOS
public class MiAdapterTusViajesDisfrutadosTusViajes extends RecyclerView.Adapter<MiAdapterTusViajesDisfrutadosTusViajes.ExampleViewHolder> {

    private ArrayList<Usuario> misUsuariosList;//Atributo que contiene la lista de los datos a tratar (objetos de tipo ExampleItem)
    private MiAdapterTusViajesDisfrutadosTusViajes.OnItemClickListener mListener;//Atributo que nos permitira asignar un listener a cada item
    private final Context c;

    //INTERFAZ dentro de la clase la cual nos obliga a implementar y sobreescribir el metodo OnItemClick
    public interface OnItemClickListener {
        void OnUsuarioClick(int position);//Metodo abstracto que recibe por parametro la posicion del item que ha sido pulsado
    }

    //Metodo SET de la clase Adapter que nos permite asignar un listener
    public void setOnClickListener(MiAdapterTusViajesDisfrutadosTusViajes.OnItemClickListener listener) {
        mListener = listener;
    }

    public MiAdapterTusViajesDisfrutadosTusViajes(Context c, ArrayList<Usuario> misUsuariosList) {
        this.c = c;
        this.misUsuariosList = misUsuariosList;
    }

    public void setMisUsuariosList(ArrayList<Usuario> misUsuariosList){
        this.misUsuariosList = misUsuariosList;
    }

    @NonNull
    @Override
    //Sobreescribimos el metodo onCreateViewHolder que se va a encargar de asignar a una vista los elementos que contiene la plantilla creada en XML
    //para posteriormente instanciar un objeto de la clase interna ExampleViewHolder, pasandole por parametro la vista anterior y un listener
    //finaliza devolviendo un objeto de tipo exampleViewHolder
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_usuariopeque,parent,false);    //Usamos el método inflate() para crear una vista a partir del layout XML definido en layout_listitem.
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
        Usuario nuevoUsuario= misUsuariosList.get(position); //Crea un objeto ExampleItem igual que el objeto que devuelve el metodo mExampleList.get() en su posicion
        Glide.with(c).load(misUsuariosList.get(position).getFotousuario()).into(holder.mImageUsuarioPeque);
    }

    //Sobreescribimos el metodo getItemCount que nos devuelve el tamaño de la lista de objetos ExampleItem
    //Este metodo internamente establece la longuitud maxima que tendra la lista
    @Override
    public int getItemCount() {
        return misUsuariosList.size();
    }

    //CLASE INTERNA ESTATICA
    static class ExampleViewHolder extends RecyclerView.ViewHolder {

        private final ImageView mImageUsuarioPeque;

        //METODO CONSTRUCTOR de la clase interna ExampleViewHolder que recibe como parametro una instancia de la clase View y un listener ya que
        //al ser una clase estatica de no pasarselo no podria acceder a el listener
        ExampleViewHolder(View itemView, final OnItemClickListener listener) {

            super(itemView);
            this.mImageUsuarioPeque = itemView.findViewById(R.id.mImageUsuarioPeque);//Asocia el atributo de la clase al XML (imagen para el tablero)
            mImageUsuarioPeque.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {//Si el listener que recibimos en el constructor es valido
                        //El adapter es el que sabe la posición absoluta dentro de la vista,
                        int position = getAdapterPosition();//Almacenamos la posicion del elemento que ha activado el evento
                        if (position != RecyclerView.NO_POSITION) {//Si la posición recibida es una posición valida dentro del RecyclerView
                            listener.OnUsuarioClick(position);//Asignamos un listener al Item de la posición que hemos comprobado
                        }
                    }
                }
            });
        }
    }

}

