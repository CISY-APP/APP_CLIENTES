package com.example.app_clientes.Item;

public class ItemMensaje {

    private String mensaje;
    private String nombre;
    private String email;
    private String hora;

    public ItemMensaje() {
    }

    public ItemMensaje(String mensaje, String nombre, String email, String hora) {
        this.mensaje = mensaje;
        this.nombre = nombre;
        this.email = email;
        this.hora = hora;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

}
