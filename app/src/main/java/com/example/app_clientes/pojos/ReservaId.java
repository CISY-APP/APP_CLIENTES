package com.example.app_clientes.pojos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ReservaId implements Serializable {
    @SerializedName("idreserva")
    private int idreserva;
    @SerializedName("idusuariopasajero")
    private int idusuariopasajero;
    @SerializedName("idviaje")
    private int idviaje;

    public ReservaId() {
    }

    public ReservaId(int idreserva, int idusuariopasajero, int idviaje) {
        this.idreserva = idreserva;
        this.idusuariopasajero = idusuariopasajero;
        this.idviaje = idviaje;
    }

    public int getIdreserva() {
        return this.idreserva;
    }

    public void setIdreserva(int idreserva) {
        this.idreserva = idreserva;
    }

    public int getIdusuariopasajero() {
        return this.idusuariopasajero;
    }

    public void setIdusuariopasajero(int idusuariopasajero) {
        this.idusuariopasajero = idusuariopasajero;
    }

    public int getIdviaje() {
        return this.idviaje;
    }

    public void setIdviaje(int idviaje) {
        this.idviaje = idviaje;
    }

    public boolean equals(Object other) {
        if ((this == other))
            return true;
        if ((other == null))
            return false;
        if (!(other instanceof ReservaId))
            return false;
        ReservaId castOther = (ReservaId) other;

        return (this.getIdreserva() == castOther.getIdreserva())
                && (this.getIdusuariopasajero() == castOther.getIdusuariopasajero())
                && (this.getIdviaje() == castOther.getIdviaje());
    }

    public int hashCode() {
        int result = 17;

        result = 37 * result + this.getIdreserva();
        result = 37 * result + this.getIdusuariopasajero();
        result = 37 * result + this.getIdviaje();
        return result;
    }
}
