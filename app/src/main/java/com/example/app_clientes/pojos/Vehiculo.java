package com.example.app_clientes.pojos;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Vehiculo implements java.io.Serializable {
    @SerializedName("idvehiculo")
    private Integer idvehiculo;
    @SerializedName("usuario")
    private Usuario usuario;
    @SerializedName("matricula")
    private String matricula;
    @SerializedName("modelo")
    private String modelo;
    @SerializedName("color")
    private String color;
    @SerializedName("combustible")
    private String combustible;
    @SerializedName("plazas")
    private Integer plazas;
    @SerializedName("marca")
    private String marca;
    @SerializedName("fotovehiculo")
    private String fotovehiculo;
    @SerializedName("fechadealta")
    private Date fechadealta;
    @SerializedName("viajes")
    private Set<Viaje> viajes = new HashSet<Viaje>(0);

    public Vehiculo() {
    }

    public Vehiculo(Usuario usuario, String matricula, Date fechadealta) {
        this.usuario = usuario;
        this.matricula = matricula;
        this.fechadealta = fechadealta;
    }

    public Vehiculo(Usuario usuario, String matricula, String modelo, String color, String combustible, Integer plazas,
                    String marca, String fotovehiculo, Date fechadealta, Set<Viaje> viajes) {
        this.usuario = usuario;
        this.matricula = matricula;
        this.modelo = modelo;
        this.color = color;
        this.combustible = combustible;
        this.plazas = plazas;
        this.marca = marca;
        this.fotovehiculo = fotovehiculo;
        this.fechadealta = fechadealta;
        this.viajes = viajes;
    }

    public Integer getIdvehiculo() {
        return this.idvehiculo;
    }

    public void setIdvehiculo(Integer idvehiculo) {
        this.idvehiculo = idvehiculo;
    }

    public Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getMatricula() {
        return this.matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getModelo() {
        return this.modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getColor() {
        return this.color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getCombustible() {
        return this.combustible;
    }

    public void setCombustible(String combustible) {
        this.combustible = combustible;
    }

    public Integer getPlazas() {
        return this.plazas;
    }

    public void setPlazas(Integer plazas) {
        this.plazas = plazas;
    }

    public String getMarca() {
        return this.marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getFotovehiculo() {
        return this.fotovehiculo;
    }

    public void setFotovehiculo(String fotovehiculo) {
        this.fotovehiculo = fotovehiculo;
    }

    public Date getFechadealta() {
        return this.fechadealta;
    }

    public void setFechadealta(Date fechadealta) {
        this.fechadealta = fechadealta;
    }

    public Set<Viaje> getViajes() {
        return this.viajes;
    }

    public void setViajes(Set<Viaje> viajes) {
        this.viajes = viajes;
    }

}
