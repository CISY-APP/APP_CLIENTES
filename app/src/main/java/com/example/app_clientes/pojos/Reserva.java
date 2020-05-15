package com.example.app_clientes.pojos;

import java.util.Date;

public class Reserva implements java.io.Serializable {

    private Integer id;
    private Usuario usuario;
    private Viaje viaje;
    private Date fechareserva;
    private Date horafichaje;

    public Reserva() {
    }

    public Reserva(Integer id, Usuario usuario, Viaje viaje, Date fechareserva, Date horafichaje) {
        this.id = id;
        this.usuario = usuario;
        this.viaje = viaje;
        this.fechareserva = fechareserva;
        this.horafichaje = horafichaje;
    }


    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
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

    public Date getHorafichaje() {
        return this.horafichaje;
    }

    public void setHorafichaje(Date horafichaje) {
        this.horafichaje = horafichaje;
    }

}
