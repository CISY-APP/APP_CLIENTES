package com.example.app_clientes.Item;

public class ItemViajesEncontrados {

    private int mImageUsuario;
    private String nombre;
    private String apellidos;
    private String edad;
    private String asientosLibres;
    private String precio;
    private int valoracion;

    public ItemViajesEncontrados(int mImageUsuario, String nombre, String apellidos, String edad, String asientosLibres, int valoracion, String precio) {
        this.mImageUsuario=mImageUsuario;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.edad = edad;
        this.asientosLibres = asientosLibres;
        this.valoracion = valoracion;
        this.precio = precio;
    }

    public int getmImageUsuario() {
        return mImageUsuario;
    }

    public void setmImageUsuario(int mImageUsuario) {
        this.mImageUsuario = mImageUsuario;
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
