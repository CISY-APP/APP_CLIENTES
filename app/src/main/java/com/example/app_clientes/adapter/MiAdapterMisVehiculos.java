package com.example.app_clientes.adapter;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.app_clientes.Biblioteca;
import com.example.app_clientes.item.ItemVehiculo;
import com.example.app_clientes.R;

import java.util.ArrayList;

//RECYCLERVIEW DE LA VENTANA PRODUCTOS
public class MiAdapterMisVehiculos extends RecyclerView.Adapter<MiAdapterMisVehiculos.ExampleViewHolder> {

    private final Context c;
    private ArrayList<ItemVehiculo> misVehiculosList;//Atributo que contiene la lista de los datos a tratar (objetos de tipo ExampleItem)
    private OnItemClickListener mListener;//Atributo que nos permitira asignar un listener a cada item

    //Metodo que pinta una animacion en el holder view:
    protected void setAnimation(MiAdapterMisVehiculos.ExampleViewHolder holder) {
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
    public void onViewDetachedFromWindow(@NonNull MiAdapterMisVehiculos.ExampleViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }
    //Metodo que pinta la animacion de ese holder si esta ya dentro de la pantalla:
    @Override
    public void onViewAttachedToWindow(@NonNull MiAdapterMisVehiculos.ExampleViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        setAnimation(holder);
    }
    //INTERFAZ dentro de la clase la cual nos obliga a implementar y sobreescribir el metodo OnItemClick
    public interface OnItemClickListener {
        void OnVehiculoClick(int position);//Metodo abstracto que recibe por parametro la posicion del item que ha sido pulsado
    }

    //Metodo SET de la clase Adapter que nos permite asignar un listener
    public void setOnClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public MiAdapterMisVehiculos(Context c,ArrayList<ItemVehiculo> misVehiculosList) {
        this.c = c;
        this.misVehiculosList = misVehiculosList;
    }

    public void setMisVehiculosList(ArrayList<ItemVehiculo> misVehiculosList){
        this.misVehiculosList = misVehiculosList;
    }

    @NonNull
    @Override
    //Sobreescribimos el metodo onCreateViewHolder que se va a encargar de asignar a una vista los elementos que contiene la plantilla creada en XML
    //para posteriormente instanciar un objeto de la clase interna ExampleViewHolder, pasandole por parametro la vista anterior y un listener
    //finaliza devolviendo un objeto de tipo exampleViewHolder
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vehiculo,parent,false);    //Usamos el método inflate() para crear una vista a partir del layout XML definido en layout_listitem.
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
        ItemVehiculo nuevoVehiculo=misVehiculosList.get(position); //Crea un objeto ExampleItem igual que el objeto que devuelve el metodo mExampleList.get() en su posicion
        if(position<misVehiculosList.size()-1) {
            Glide.with(c).load(misVehiculosList.get(position).getmImageVehiculo()).error(R.drawable.coche).into(holder.mImageVehiculo);
        }else{
            Glide.with(c).load(misVehiculosList.get(position).getmImageVehiculo()).error(R.drawable.anadir).into(holder.mImageVehiculo);
        }
        holder.TVMatriculaItem.setText(misVehiculosList.get(position).getMarcaYmodelo());
    }

    //Sobreescribimos el metodo getItemCount que nos devuelve el tamaño de la lista de objetos ExampleItem
    //Este metodo internamente establece la longuitud maxima que tendra la lista
    @Override
    public int getItemCount() {
        return misVehiculosList.size();
    }

    //CLASE INTERNA ESTATICA
    static class ExampleViewHolder extends RecyclerView.ViewHolder {

        private final ImageView mImageVehiculo;
        private final TextView TVMatriculaItem;

        //METODO CONSTRUCTOR de la clase interna ExampleViewHolder que recibe como parametro una instancia de la clase View y un listener ya que
        //al ser una clase estatica de no pasarselo no podria acceder a el listener
        ExampleViewHolder(View itemView, final OnItemClickListener listener) {

            super(itemView);
            this.TVMatriculaItem = itemView.findViewById(R.id.TVMatriculaItem);
            mImageVehiculo = itemView.findViewById(R.id.mImageVehiculoPeque);//Asocia el atributo de la clase al XML (imagen para el tablero)
            mImageVehiculo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {//Si el listener que recibimos en el constructor es valido
                        //El adapter es el que sabe la posición absoluta dentro de la vista,
                        int position = getAdapterPosition();//Almacenamos la posicion del elemento que ha activado el evento
                        if (position != RecyclerView.NO_POSITION) {//Si la posición recibida es una posición valida dentro del RecyclerView
                            listener.OnVehiculoClick(position);//Asignamos un listener al Item de la posición que hemos comprobado
                        }
                    }
                }
            });
        }
    }

}

