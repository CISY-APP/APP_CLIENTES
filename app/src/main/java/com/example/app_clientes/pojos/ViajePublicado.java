package com.example.app_clientes.pojos;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class ViajePublicado {
    //Atributos de ViajePublicado:
    @SerializedName("idviaje")
    private Integer idviaje;
    @SerializedName("localidadorigen")
    private String localidadorigen;
    @SerializedName("lugarsalida")
    private String lugarsalida;
    @SerializedName("localidaddestino")
    private String localidaddestino;
    @SerializedName("lugarllegada")
    private String lugarllegada;
    @SerializedName("precio")
    private BigDecimal precio;
    @SerializedName("numplazasdisponibles")
    private int numplazasdisponibles;
    @SerializedName("fechasalida")
    private Date fechasalida;
    @SerializedName("fechacreacionviaje")
    private Date fechacreacionviaje;
    @SerializedName("vehiculo")
    private String vehiculo;
    //Atributos UsuarioPublicado:
    @SerializedName("listUsuariosPublicados")
    private List<UsuarioPublicado> listUsuariosPublicados;
    //Constructor:
    public ViajePublicado(Integer idviaje, String localidadorigen, String lugarsalida, String localidaddestino,
                          String lugarllegada, BigDecimal precio, int numplazasdisponibles, Date fechasalida, Date fechacreacionviaje,
                          List<UsuarioPublicado> listUsuariosPublicados, String vehiculo) {
        this.idviaje = idviaje;
        this.localidadorigen = localidadorigen;
        this.lugarsalida = lugarsalida;
        this.localidaddestino = localidaddestino;
        this.lugarllegada = lugarllegada;
        this.precio = precio;
        this.numplazasdisponibles = numplazasdisponibles;
        this.fechasalida = fechasalida;
        this.fechacreacionviaje = fechacreacionviaje;
        this.vehiculo = vehiculo;
        this.listUsuariosPublicados = listUsuariosPublicados;
    }
    //Metodos Getter y Setters:
    public Integer getIdviaje() {
        return idviaje;
    }

    public void setIdviaje(Integer idviaje) {
        this.idviaje = idviaje;
    }

    public String getLocalidadorigen() {
        return localidadorigen;
    }

    public void setLocalidadorigen(String localidadorigen) {
        this.localidadorigen = localidadorigen;
    }

    public String getLugarsalida() {
        return lugarsalida;
    }

    public void setLugarsalida(String lugarsalida) {
        this.lugarsalida = lugarsalida;
    }

    public String getLocalidaddestino() {
        return localidaddestino;
    }

    public void setLocalidaddestino(String localidaddestino) {
        this.localidaddestino = localidaddestino;
    }

    public String getLugarllegada() {
        return lugarllegada;
    }

    public void setLugarllegada(String lugarllegada) {
        this.lugarllegada = lugarllegada;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public int getNumplazasdisponibles() {
        return numplazasdisponibles;
    }

    public void setNumplazasdisponibles(int numplazasdisponibles) {
        this.numplazasdisponibles = numplazasdisponibles;
    }

    public Date getFechasalida() {
        return fechasalida;
    }

    public void setFechasalida(Date fechasalida) {
        this.fechasalida = fechasalida;
    }

    public Date getFechacreacionviaje() {
        return fechacreacionviaje;
    }

    public void setFechacreacionviaje(Date fechacreacionviaje) {
        this.fechacreacionviaje = fechacreacionviaje;
    }

    public List<UsuarioPublicado> getListUsuariosPublicados() {
        return listUsuariosPublicados;
    }

    public void setListUsuariosPublicados(List<UsuarioPublicado> listUsuariosPublicados) {
        this.listUsuariosPublicados = listUsuariosPublicados;
    }

    public String getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(String vehiculo){
        this.vehiculo=vehiculo;
    }
}
