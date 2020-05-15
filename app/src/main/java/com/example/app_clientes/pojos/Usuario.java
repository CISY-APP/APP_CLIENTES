package com.example.app_clientes.pojos;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Usuario implements java.io.Serializable {

    private Integer idusuario;
    private String nombre;
    private String apellidos;
    private Boolean espasajero;
    private Boolean esconductor;
    private Integer telefono;
    private String email;
    private String clave;
    private Date fechanacimiento;
    private String fotousuario;
    private String fotokyc;
    private String descripcion;
    private Integer valoracion;
    private Date fecharegistro;
    private Date fechadesconexion;
    private Boolean sesioniniciada;
    private Boolean telefonoverificado;
    private Boolean emailverificado;
    private Boolean doblefactoractivado;
    private Set<Vehiculo> vehiculos = new HashSet<Vehiculo>(0);
    private Set<Reserva> reservas = new HashSet<Reserva>(0);
    private Set<Viaje> viajes = new HashSet<Viaje>(0);


    public Usuario() {
    }

    public Usuario(String nombre, String apellidos, String email, String clave, Date fecharegistro) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.clave = clave;
        this.fecharegistro = fecharegistro;
    }

    public Usuario(String nombre, String apellidos, Boolean espasajero, Boolean esconductor, Integer telefono,
                   String email, String clave, Date fechanacimiento, String fotousuario, String fotokyc, String descripcion,
                   Integer valoracion, Date fecharegistro, Date fechadesconexion, Boolean sesioniniciada,
                   Boolean telefonoverificado, Boolean emailverificado, Boolean doblefactoractivado, Set<Vehiculo> vehiculos,
                   Set<Reserva> reservas, Set<Viaje> viajes) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.espasajero = espasajero;
        this.esconductor = esconductor;
        this.telefono = telefono;
        this.email = email;
        this.clave = clave;
        this.fechanacimiento = fechanacimiento;
        this.fotousuario = fotousuario;
        this.fotokyc = fotokyc;
        this.descripcion = descripcion;
        this.valoracion = valoracion;
        this.fecharegistro = fecharegistro;
        this.fechadesconexion = fechadesconexion;
        this.sesioniniciada = sesioniniciada;
        this.telefonoverificado = telefonoverificado;
        this.emailverificado = emailverificado;
        this.doblefactoractivado = doblefactoractivado;
        this.vehiculos = vehiculos;
        this.reservas = reservas;
        this.viajes = viajes;
    }

    public Integer getIdusuario() {
        return this.idusuario;
    }

    public void setIdusuario(Integer idusuario) {
        this.idusuario = idusuario;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return this.apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public Boolean getEspasajero() {
        return this.espasajero;
    }

    public void setEspasajero(Boolean espasajero) {
        this.espasajero = espasajero;
    }

    public Boolean getEsconductor() {
        return this.esconductor;
    }

    public void setEsconductor(Boolean esconductor) {
        this.esconductor = esconductor;
    }

    public Integer getTelefono() {
        return this.telefono;
    }

    public void setTelefono(Integer telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getClave() {
        return this.clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public Date getFechanacimiento() {
        return this.fechanacimiento;
    }

    public void setFechanacimiento(Date fechanacimiento) {
        this.fechanacimiento = fechanacimiento;
    }

    public String getFotousuario() {
        return this.fotousuario;
    }

    public void setFotousuario(String fotousuario) {
        this.fotousuario = fotousuario;
    }

    public String getFotokyc() {
        return this.fotokyc;
    }

    public void setFotokyc(String fotokyc) {
        this.fotokyc = fotokyc;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getValoracion() {
        return this.valoracion;
    }

    public void setValoracion(Integer valoracion) {
        this.valoracion = valoracion;
    }

    public Date getFecharegistro() {
        return this.fecharegistro;
    }

    public void setFecharegistro(Date fecharegistro) {
        this.fecharegistro = fecharegistro;
    }

    public Date getFechadesconexion() {
        return this.fechadesconexion;
    }

    public void setFechadesconexion(Date fechadesconexion) {
        this.fechadesconexion = fechadesconexion;
    }

    public Boolean getSesioniniciada() {
        return this.sesioniniciada;
    }

    public void setSesioniniciada(Boolean sesioniniciada) {
        this.sesioniniciada = sesioniniciada;
    }

    public Boolean getTelefonoverificado() {
        return this.telefonoverificado;
    }

    public void setTelefonoverificado(Boolean telefonoverificado) {
        this.telefonoverificado = telefonoverificado;
    }

    public Boolean getEmailverificado() {
        return this.emailverificado;
    }

    public void setEmailverificado(Boolean emailverificado) {
        this.emailverificado = emailverificado;
    }

    public Boolean getDoblefactoractivado() {
        return this.doblefactoractivado;
    }

    public void setDoblefactoractivado(Boolean doblefactoractivado) {
        this.doblefactoractivado = doblefactoractivado;
    }

    public Set<Vehiculo> getVehiculos() {
        return this.vehiculos;
    }

    public void setVehiculos(Set<Vehiculo> vehiculos) {
        this.vehiculos = vehiculos;
    }

    public Set<Reserva> getReservas() {
        return this.reservas;
    }

    public void setReservas(Set<Reserva> reservas) {
        this.reservas = reservas;
    }

    public Set<Viaje> getViajes() {
        return this.viajes;
    }

    public void setViajes(Set<Viaje> viajes) {
        this.viajes = viajes;
    }

}
