package com.example.app_clientes.pojos;

import com.google.gson.annotations.SerializedName;

import java.util.Date;


public class Reserva implements java.io.Serializable {
    @SerializedName("id")
    private ReservaId id;
    @SerializedName("usuario")
    private Usuario usuario;
    @SerializedName("viaje")
    private Viaje viaje;
    @SerializedName("fechareserva")
    private Date fechareserva;

    public Reserva() {
    }

    public Reserva(ReservaId id, Usuario usuario, Viaje viaje, Date fechareserva) {
        this.id = id;
        this.usuario = usuario;
        this.viaje = viaje;
        this.fechareserva = fechareserva;
    }

    public ReservaId getId() {
        return this.id;
    }

    public void setId(ReservaId id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Viaje getViaje() {
        return this.viaje;
    }

    public void setViaje(Viaje viaje) {
        this.viaje = viaje;
    }

    public Date getFechareserva() {
        return this.fechareserva;
    }

    public void setFechareserva(Date fechareserva) {
        this.fechareserva = fechareserva;
    }
}