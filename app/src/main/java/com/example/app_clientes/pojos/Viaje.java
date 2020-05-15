package com.example.app_clientes.pojos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Viaje implements Serializable {

    @SerializedName("idviaje")
    private Integer idviaje;

    @SerializedName("usuario")
    private Usuario usuario;

    @SerializedName("vehiculo")
    private Vehiculo vehiculo;

    @SerializedName("origen")
    private String origen;

    @SerializedName("destino")
    private String destino;

    @SerializedName("codigoverificacion")
    private Integer codigoverificacion;

    @SerializedName("precio")
    private BigDecimal precio;

    @SerializedName("numplazasdisponibles")
    private int numplazasdisponibles;

    @SerializedName("fechasalida")
    private Date fechasalida;

    @SerializedName("horasalida")
    private Date horasalida;

    @SerializedName("fechacreacionviaje")
    private Date fechacreacionviaje;

    @SerializedName("reservas")
    private Set<Reserva> reservas = new HashSet<Reserva>(0);

    public Viaje() {
    }

    public Viaje(Usuario usuario, Vehiculo vehiculo, String origen, String destino, int numplazasdisponibles,
                 Date fechacreacionviaje) {
        this.usuario = usuario;
        this.vehiculo = vehiculo;
        this.origen = origen;
        this.destino = destino;
        this.numplazasdisponibles = numplazasdisponibles;
        this.fechacreacionviaje = fechacreacionviaje;
    }

    public Viaje(Usuario usuario, Vehiculo vehiculo, String origen, String destino, Integer codigoverificacion,
                 BigDecimal precio, int numplazasdisponibles, Date fechasalida, Date horasalida, Date fechacreacionviaje,
                 Set<Reserva> reservas) {
        this.usuario = usuario;
        this.vehiculo = vehiculo;
        this.origen = origen;
        this.destino = destino;
        this.codigoverificacion = codigoverificacion;
        this.precio = precio;
        this.numplazasdisponibles = numplazasdisponibles;
        this.fechasalida = fechasalida;
        this.horasalida = horasalida;
        this.fechacreacionviaje = fechacreacionviaje;
        this.reservas = reservas;
    }

    public Integer getIdviaje() {
        return this.idviaje;
    }

    public void setIdviaje(Integer idviaje) {
        this.idviaje = idviaje;
    }

    public Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Vehiculo getVehiculo() {
        return this.vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    public String getOrigen() {
        return this.origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getDestino() {
        return this.destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public Integer getCodigoverificacion() {
        return this.codigoverificacion;
    }

    public void setCodigoverificacion(Integer codigoverificacion) {
        this.codigoverificacion = codigoverificacion;
    }

    public BigDecimal getPrecio() {
        return this.precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public int getNumplazasdisponibles() {
        return this.numplazasdisponibles;
    }

    public void setNumplazasdisponibles(int numplazasdisponibles) {
        this.numplazasdisponibles = numplazasdisponibles;
    }

    public Date getFechasalida() {
        return this.fechasalida;
    }

    public void setFechasalida(Date fechasalida) {
        this.fechasalida = fechasalida;
    }

    public Date getHorasalida() {
        return this.horasalida;
    }

    public void setHorasalida(Date horasalida) {
        this.horasalida = horasalida;
    }

    public Date getFechacreacionviaje() {
        return this.fechacreacionviaje;
    }

    public void setFechacreacionviaje(Date fechacreacionviaje) {
        this.fechacreacionviaje = fechacreacionviaje;
    }

    public Set<Reserva> getReservas() {
        return this.reservas;
    }

    public void setReservas(Set<Reserva> reservas) {
        this.reservas = reservas;
    }

}