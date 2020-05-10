package com.example.app_clientes.Pojos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class Viaje implements java.io.Serializable {

    private String origen;
    private String destino;
    private String fechaSalida;
    private String horaSalida;
    private Double precio;
    private int numplazasDisponibles;

    public Viaje(){

    }

    public Viaje(String origen, String destino, String fechaSalida, String horaSalida) {
        this.origen = origen;
        this.destino = destino;
        this.fechaSalida = fechaSalida;
        this.horaSalida = horaSalida;
    }

    public Viaje(String origen, String destino, String fechaSalida, String horaSalida, Double precio, int numplazasDisponibles) {
        this.origen = origen;
        this.destino = destino;
        this.fechaSalida = fechaSalida;
        this.horaSalida = horaSalida;
        this.precio = precio;
        this.numplazasDisponibles = numplazasDisponibles;
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

    public String getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(String fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public String getHoraSalida() {
        return horaSalida;
    }

    public void setHoraSalida(String horaSalida) {
        this.horaSalida = horaSalida;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public int getNumplazasDisponibles() {
        return numplazasDisponibles;
    }

    public void setNumplazasDisponibles(int numplazasDisponibles) { this.numplazasDisponibles = numplazasDisponibles; }
}
