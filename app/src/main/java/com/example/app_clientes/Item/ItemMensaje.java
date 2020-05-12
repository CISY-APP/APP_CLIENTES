package com.example.app_clientes.Item;

import android.widget.ImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class ItemMensaje {


    private String mensaje;
    private String nombre;
    private String hora;
    private int ImagenUsuario;

    public ItemMensaje() {
    }

    public ItemMensaje(String mensaje, String nombre, String hora, int ImagenUsuario) {
        this.mensaje = mensaje;
        this.nombre = nombre;
        this.hora = hora;
        this.ImagenUsuario = ImagenUsuario;
    }

    public int getImagenUsuario() {
        return ImagenUsuario;
    }

    public void setImagenUsuario(int imagenUsuario) {
        ImagenUsuario = imagenUsuario;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

}
