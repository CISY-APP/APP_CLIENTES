//Indicamos a que paquete pertenece esta clase:
package com.example.app_clientes.item;
//Clase que representa un item de viaje encontrado para el recyclerview correspondiente:
public class ItemViajesEncontrados {
    //Atributos:
    private String cod_usuario;
    private String uriImagenUsuario;
    private String nombreApellidos;
    private String edad;
    private String telefono;
    private String cod_viaje;
    private String origen;
    private String destino;
    private String fecha;
    private String precio;
    private String cod_vehiculo;
    private String uriImagenCoche;
    private String matricula;
    private String marcaModelo;
    private String color;
    private String combustible;
    //Constructor:
    public ItemViajesEncontrados(String cod_usuario, String uriImagenUsuario, String nombreApellidos, String edad, String telefono, String cod_viaje, String origen, String destino, String fecha, String precio, String cod_vehiculo, String uriImagenCoche, String matricula, String marcaModelo, String color, String combustible) {
        this.cod_usuario = cod_usuario;
        this.uriImagenUsuario = uriImagenUsuario;
        this.nombreApellidos=nombreApellidos;
        this.edad = edad;
        this.telefono = telefono;
        this.cod_viaje = cod_viaje;
        this.origen = origen;
        this.destino = destino;
        this.fecha = fecha;
        this.precio = precio;
        this.cod_vehiculo = cod_vehiculo;
        this.uriImagenCoche = uriImagenCoche;
        this.matricula = matricula;
        this.marcaModelo = marcaModelo;
        this.color = color;
        this.combustible = combustible;
    }
    //Metodos get y set:
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

    public String getNombreApellidos() {
        return nombreApellidos;
    }

    public void setNombreApellidos(String nombreApellidos) {
        this.nombreApellidos = nombreApellidos;
    }

    public String getEdad() {
        return edad;
    }

    public void setEdad(String edad) {
        this.edad = edad;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCod_viaje() {
        return cod_viaje;
    }

    public void setCod_viaje(String cod_viaje) {
        this.cod_viaje = cod_viaje;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getCod_vehiculo() {
        return cod_vehiculo;
    }

    public void setCod_vehiculo(String cod_vehiculo) {
        this.cod_vehiculo = cod_vehiculo;
    }

    public String getUriImagenCoche() {
        return uriImagenCoche;
    }

    public void setUriImagenCoche(String uriImagenCoche) {
        this.uriImagenCoche = uriImagenCoche;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getMarcaModelo() {
        return marcaModelo;
    }

    public void setMarcaModelo(String marcaModelo) {
        this.marcaModelo = marcaModelo;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getCombustible() {
        return combustible;
    }

    public void setCombustible(String combustible) {
        this.combustible = combustible;
    }
}
