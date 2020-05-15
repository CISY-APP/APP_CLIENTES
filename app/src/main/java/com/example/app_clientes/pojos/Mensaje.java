package com.example.app_clientes.pojos;

public class Mensaje {

    private String mensaje;
    private String nombre;
    private String IDUsuario;
    private String hora;
    private String direccionFotoUsuario;

    public Mensaje() {
    }

    public Mensaje(String mensaje, String nombre, String IDUsuario, String hora, String direccionFotoUsuario) {
        this.mensaje = mensaje;
        this.nombre = nombre;
        this.IDUsuario = IDUsuario;
        this.hora = hora;
        this.direccionFotoUsuario = direccionFotoUsuario;
    }
    public Mensaje(String hora, String mensaje) {
        this.mensaje = mensaje;
        this.hora = hora;
    }

    public String getDireccionFotoUsuario() {
        return direccionFotoUsuario;
    }

    public void setDireccionFotoUsuario(String direccionFotoUsuario) {
        this.direccionFotoUsuario = direccionFotoUsuario;
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

    public String getIDUsuario() {
        return IDUsuario;
    }

    public void setIDUsuario(String IDUsuario) {
        this.IDUsuario = IDUsuario;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

}