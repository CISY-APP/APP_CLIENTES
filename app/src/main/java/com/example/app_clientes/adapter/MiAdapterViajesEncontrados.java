//Indicamos a que paquete pertenece esta clase:
package com.example.app_clientes.adapter;
//Importamos los siguientes paquetes:
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.app_clientes.Biblioteca;
import com.example.app_clientes.item.ItemViajesEncontrados;
import com.example.app_clientes.R;
import com.makeramen.roundedimageview.RoundedImageView;
import java.util.ArrayList;
import de.hdodenhof.circleimageview.CircleImageView;
//Clase adapter de viajes encontrados:
public class MiAdapterViajesEncontrados extends RecyclerView.Adapter<MiAdapterViajesEncontrados.ExampleViewHolder> {
    //Atributos:
    private final ArrayList<ItemViajesEncontrados> viajesEncontradosList;//Atributo que contiene la lista de los datos a tratar (objetos de tipo ExampleItem)
    private final Context c;
    private OnItemClickListener mListener;//Atributo que nos permitira asignar un listener a cada item
    //INTERFAZ dentro de la clase la cual nos obliga a implementar y sobreescribir el metodo OnItemClick, haremos uno para cada boton:
    public interface OnItemClickListener {
        void OnMensajeClick(int position);//Metodo abstracto que recibe por parametro la posicion del item que ha sido pulsado
        void OnReservaClick(int position);
    }
    //Metodo SET de la clase Adapter que nos permite asignar un listener:
    public void setOnClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
    //Constructor:
    public MiAdapterViajesEncontrados(Context c, ArrayList<ItemViajesEncontrados> viajesEncontradosList ) {
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
        holder.nombreApellidos.setText(nuevoViajeEncontrado.getNombreApellidos());
        holder.edad.setText(nuevoViajeEncontrado.getEdad());
        holder.telefono.setText(nuevoViajeEncontrado.getTelefono());
        holder.origen.setText(nuevoViajeEncontrado.getOrigen());
        holder.destino.setText(nuevoViajeEncontrado.getDestino());
        holder.fecha.setText(nuevoViajeEncontrado.getFecha());
        holder.precio.setText(nuevoViajeEncontrado.getPrecio());
        holder.matricula.setText(nuevoViajeEncontrado.getMatricula());
        holder.marcaModelo.setText(nuevoViajeEncontrado.getMarcaModelo());
        holder.combustible.setText(nuevoViajeEncontrado.getCombustible());
        holder.color.setBackgroundColor(Color.parseColor(nuevoViajeEncontrado.getColor()));
        Glide.with(c).load(nuevoViajeEncontrado.getUriImagenUsuario()).error(R.drawable.user).into(holder.mImageUsuario);
        Glide.with(c).load(nuevoViajeEncontrado.getUriImagenCoche()).error(R.drawable.coche).into(holder.mImageVehiculo);
    }
    //Sobreescribimos el metodo getItemCount que nos devuelve el tamaño de la lista de objetos ExampleItem
    //Este metodo internamente establece la longuitud maxima que tendra la lista
    @Override
    public int getItemCount() {
        return viajesEncontradosList.size();
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
    //CLASE INTERNA ESTATICA
    public static class ExampleViewHolder extends RecyclerView.ViewHolder {
        //Atributos:
        private final CircleImageView mImageUsuario;
        private TextView nombreApellidos;
        private TextView edad;
        private TextView telefono;
        private TextView origen;
        private TextView destino;
        private TextView fecha;
        private TextView precio;
        private final RoundedImageView mImageVehiculo;
        private TextView matricula;
        private TextView marcaModelo;
        private final RoundedImageView color;
        private TextView combustible;
        //METODO CONSTRUCTOR de la clase interna ExampleViewHolder que recibe como parametro una instancia de la clase View y un listener ya que
        //al ser una clase estatica de no pasarselo no podria acceder a el listener
        public ExampleViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            //Enlazamos xml:
            this.mImageUsuario = itemView.findViewById(R.id.imageViewUsuarioItemViajeEncontrado);
            this.nombreApellidos = itemView.findViewById(R.id.textViewNombreApellidosItemViajeEncontrado);
            this.edad = itemView.findViewById(R.id.textViewEdadItemViajeEncontrado);
            this.telefono = itemView.findViewById(R.id.textViewTelefonoItemViajeEncontrado);
            this.origen = itemView.findViewById(R.id.textViewOrigenItemViajeEncontrado);
            this.destino = itemView.findViewById(R.id.textViewDestinoItemViajeEncontrado);
            this.fecha = itemView.findViewById(R.id.textViewFechaItemViajeEncontrado);
            this.precio = itemView.findViewById(R.id.textViewPrecioItemViajeEncontrado);
            this.mImageVehiculo = itemView.findViewById(R.id.imageViewCocheItemViajeEncontrado);
            this.matricula = itemView.findViewById(R.id.textViewMatriculaItemViajeEncontrado);
            this.marcaModelo = itemView.findViewById(R.id.textViewMarcaYmodeloItemViajeEncontrado);
            this.color = itemView.findViewById(R.id.imgColorItemViajeEncontrado);
            this.combustible = itemView.findViewById(R.id.textViewCombustibleItemViajeEncontrado);
            Button BTMensaje = itemView.findViewById(R.id.buttonMensajeItemViajeEncontrado);
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
            Button BTReservar = itemView.findViewById(R.id.buttonReservaItemViajeEncontrado);
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

