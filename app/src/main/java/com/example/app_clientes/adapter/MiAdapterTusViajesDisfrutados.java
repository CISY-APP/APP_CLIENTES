//Indicamos a que paquete pertenece esta clase:
package com.example.app_clientes.adapter;
//Importamos los siguientes paquetes:
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.example.app_clientes.Biblioteca;
import com.example.app_clientes.R;
import com.example.app_clientes.pojos.Usuario;
import com.example.app_clientes.pojos.Viaje;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
//Clase adapter de viajes disfrutados:
public class MiAdapterTusViajesDisfrutados extends RecyclerView.Adapter<MiAdapterTusViajesDisfrutados.ExampleViewHolder> {
    //Atributos:
    private List<Usuario> misUsuariosList;  //Atributo que contiene la lista de los usuarios.
    private static List<Viaje> viajesClaseList = new ArrayList<>();     //Atributo que contiene la lista de los viajes asociada a los creadores usuarios.(Estatica porque su uso es para clase interna)
    private MiAdapterTusViajesDisfrutados.OnItemClickListener mListener;//Atributo que nos permitira asignar un listener a cada item.
    private final Context c;
    //INTERFAZ dentro de la clase la cual nos obliga a implementar y sobreescribir el metodo OnItemClick:
    public interface OnItemClickListener {
        void OnUsuarioClick(int position);      //Metodo abstracto para cuando el usuario clickee la foto normal se carguen sus datos.
        void OnUsuarioLongClick(int position);  //Metodo abstracto para cuando el usuario haga click largo se desbloquee el boton borrar siempre y cuando el viaje no este caducado.
        void OnBorrarClick(int position);       //Metodo abstracto para cancelar una reserva valida.
    }
    //Metodo SET de la clase Adapter que nos permite asignar un listener:
    public void setOnClickListener(MiAdapterTusViajesDisfrutados.OnItemClickListener listener) {
        mListener = listener;
    }
    //Constructor:
    public MiAdapterTusViajesDisfrutados(Context c, List<Usuario> misUsuariosList) {
        this.c = c;
        this.misUsuariosList = misUsuariosList;
        animarBoton.clear();
        for (Usuario u: misUsuariosList) {
            animarBoton.add(false);
        }

    }
    //Metodo set para establecer la lista usuarios:
    public void setMisUsuariosList(List<Usuario> misUsuariosList){
        this.misUsuariosList = misUsuariosList;
        animarBoton.clear();
        for (Usuario u: misUsuariosList) {
            animarBoton.add(false);
        }
    }
    //Metodo set para establecer la lista estatica viajes, que se usa principalmente en la clase interna:
    public void setMisViajesList(List<Viaje> misViajesList){
        viajesClaseList=misViajesList;
    }
    @NonNull
    @Override
    //Sobreescribimos el metodo onCreateViewHolder que se va a encargar de asignar a una vista los elementos que contiene la plantilla creada en XML
    //para posteriormente instanciar un objeto de la clase interna ExampleViewHolder, pasandole por parametro la vista anterior y un listener
    //finaliza devolviendo un objeto de tipo exampleViewHolder
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_usuario,parent,false);    //Usamos el método inflate() para crear una vista a partir del layout XML definido en layout_listitem.
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
        Glide.with(c).load(misUsuariosList.get(position).getFotousuario()).error(R.drawable.user).into(holder.mImageUsuarioPeque);
        holder.TVNombreItem.setText(misUsuariosList.get(position).getNombre());
    }
    //Sobreescribimos el metodo getItemCount que nos devuelve el tamaño de la lista de objetos ExampleItem
    //Este metodo internamente establece la longuitud maxima que tendra la lista
    @Override
    public int getItemCount() {
        return misUsuariosList.size();
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
        if(animarBoton.get(holder.getAdapterPosition())){   //Si se tiene que animar el boton de borrado se hace tambien:
            Animation shake = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.agitar);
            holder.itemView.setAnimation(shake);
        }
    }
    //Variable holder para guardar el ultimo holder y descolorearlo cuando llegue otro nuevo (Usado para la seleccion):
    private static LinearLayout ultimo;
    private static ArrayList<Boolean> animarBoton = new ArrayList<>();  //Lista booleana que tiene las pruebas sobre si el holder se tiene que volver a animar, porque se estaba borrando.
    //CLASE INTERNA ESTATICA:
    static class ExampleViewHolder extends RecyclerView.ViewHolder {
        //Atributos:
        private final ImageView mImageUsuarioPeque;
        private final TextView TVNombreItem;
        private final LottieAnimationView animationView;
        private final ImageView imgBorrar;
        private final LinearLayout contenedor;
        //METODO CONSTRUCTOR de la clase interna ExampleViewHolder que recibe como parametro una instancia de la clase View y un listener ya que
        //al ser una clase estatica de no pasarselo no podria acceder a el listener
        ExampleViewHolder(final View itemView, final OnItemClickListener listener) {
            super(itemView);
            this.TVNombreItem = itemView.findViewById(R.id.TVNombreItemViajesDisfrutados);
            this.mImageUsuarioPeque = itemView.findViewById(R.id.mImageUsuarioPequeViajesDisfrutados);//Asocia el atributo de la clase al XML (imagen para el tablero)
            this.animationView = itemView.findViewById(R.id.animation_example_lottie_viewItemViajesDisfrutados);
            this.contenedor = itemView.findViewById(R.id.linearLayoutPrincipalItemViajesDisfrutados);
            this.imgBorrar = itemView.findViewById(R.id.mImageBorrarViajesDisfrutados);
            //Añadimos los listeners que necesitemos:
            mImageUsuarioPeque.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onClick(View v) {
                    if (listener != null) {//Si el listener que recibimos en el constructor es valido
                        //El adapter es el que sabe la posición absoluta dentro de la vista,
                        int position = getAdapterPosition();//Almacenamos la posicion del elemento que ha activado el evento
                        if (position != RecyclerView.NO_POSITION) {//Si la posición recibida es una posición valida dentro del RecyclerView
                            listener.OnUsuarioClick(position);//Asignamos un listener al Item de la posición que hemos comprobado
                            //Quitamos la animacion de instagram ya que se ha visualizado:
                            animationView.setVisibility(View.INVISIBLE);
                            //Marcamos como seleccionado ese holder y limpiamos el anterior siempre y cuando no sea el mismo:
                            contenedor.setBackground(itemView.getContext().getDrawable(R.drawable.cell_background_2));
                            if(ultimo!=null&&ultimo!=contenedor){
                                ultimo.setBackground(itemView.getContext().getDrawable(R.color.colorBlanco));
                            }
                            ultimo = contenedor;    //Guardamos el valor para el siguiente.
                        }
                    }
                }
            });
            mImageUsuarioPeque.setOnLongClickListener(new View.OnLongClickListener(){
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public boolean onLongClick(View v) {
                    if (listener != null) {//Si el listener que recibimos en el constructor es valido
                        //El adapter es el que sabe la posición absoluta dentro de la vista,
                        int position = getAdapterPosition();//Almacenamos la posicion del elemento que ha activado el evento
                        if (position != RecyclerView.NO_POSITION) {//Si la posición recibida es una posición valida dentro del RecyclerView
                            listener.OnUsuarioLongClick(position);//Asignamos un listener al Item de la posición que hemos comprobado
                            //Comprobamos que el viaje no este caducado para meterlo en la logica de poder borrar o no:
                            Date fechaViaje = viajesClaseList.get(position).getFechasalida();
                            Date fechaActual = new Date(Calendar.getInstance().getTime().getTime());
                            if (fechaViaje.compareTo(fechaActual) > 0) {
                                //Activamos la visibilidad
                                if(imgBorrar.getVisibility()==View.GONE){
                                    imgBorrar.setVisibility(View.VISIBLE);
                                    Animation shake = AnimationUtils.loadAnimation(itemView.getContext(), R.anim.agitar);
                                    itemView.setAnimation(shake);
                                    animarBoton.set(position,true);
                                }else{
                                    imgBorrar.setVisibility(View.GONE);
                                    itemView.clearAnimation();
                                    animarBoton.set(position,false);
                                }
                            }
                        }
                    }
                    return true;    //Devolvemos true para que no se lleve tambien a cabo el evento onclick.
                }
            });
            imgBorrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {//Si el listener que recibimos en el constructor es valido
                        //El adapter es el que sabe la posición absoluta dentro de la vista,
                        int position = getAdapterPosition();//Almacenamos la posicion del elemento que ha activado el evento
                        if (position != RecyclerView.NO_POSITION) {//Si la posición recibida es una posición valida dentro del RecyclerView
                            listener.OnBorrarClick(position);//Asignamos un listener al Item de la posición que hemos comprobado
                        }
                    }
                }
            });
        }
    }

}

