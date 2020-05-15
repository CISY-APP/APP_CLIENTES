package com.example.app_clientes.item;

public class ItemViajesEncontrados {

    private String uriImagenUsuario;
    private String cod_usuario;
    private String nombre;
    private String apellidos;
    private String edad;
    private String asientosLibres;
    private String precio;
    private int valoracion;

    public ItemViajesEncontrados(String uriImagenUsuario,  String cod_usuario, String nombre, String apellidos, String edad, String asientosLibres, int valoracion, String precio) {
        this.uriImagenUsuario = uriImagenUsuario;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.edad = edad;
        this.asientosLibres = asientosLibres;
        this.valoracion = valoracion;
        this.precio = precio;
        this.cod_usuario=cod_usuario;
    }

    public String getCod_usuario() {
        return cod_usuario;
    }

    public void setCod_usuario(String cod_usuario) {
        this.cod_usuario = cod_usuario;
    }

    public String getUriImagenUsuario() {
        return uriImagenUsuario;
    }

    public void setUriImagenUsuario(String uriImagenUsuario) {
        this.uriImagenUsuario = uriImagenUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEdad() {
        return edad;
    }

    public void setEdad(String edad) {
        this.edad = edad;
    }

    public String getAsientosLibres() {
        return asientosLibres;
    }

    public void setAsientosLibres(String asientosLibres) {
        this.asientosLibres = asientosLibres;
    }

    public int getValoracion() {
        return valoracion;
    }

    public void setValoracion(int valoracion) {
        this.valoracion = valoracion;
    }
    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }
}
