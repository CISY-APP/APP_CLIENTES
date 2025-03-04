package com.example.app_clientes.pojos;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Usuario implements java.io.Serializable {
    //OJO solo se usa este atributo para cierto caso. no optimo, no confundir con idviaje, solo es para en el adapter hijo de viajesdisfrutados, tener la posicion del adapter padre
    private Integer idViaje;
    public Integer getIdViaje() {
        return idViaje;
    }
    public void setIdViaje(Integer idViaje) {
        this.idViaje = idViaje;
    }
    //Atributos REALES
    @SerializedName("idusuario")
    private Integer idusuario;
    @SerializedName("nombre")
    private String nombre;
    @SerializedName("apellidos")
    private String apellidos;
    @SerializedName("espasajero")
    private Boolean espasajero;
    @SerializedName("esconductor")
    private Boolean esconductor;
    @SerializedName("telefono")
    private Integer telefono;
    @SerializedName("email")
    private String email;
    @SerializedName("clave")
    private String clave;
    @SerializedName("fechanacimiento")
    private Date fechanacimiento;
    @SerializedName("fotousuario")
    private String fotousuario;
    @SerializedName("fotokyc")
    private String fotokyc;
    @SerializedName("descripcion")
    private String descripcion;
    @SerializedName("valoracion")
    private Integer valoracion;
    @SerializedName("fecharegistro")
    private Date fecharegistro;
    @SerializedName("fechadesconexion")
    private Date fechadesconexion;
    @SerializedName("sesioniniciada")
    private Boolean sesioniniciada;
    @SerializedName("telefonoverificado")
    private Boolean telefonoverificado;
    @SerializedName("emailverificado")
    private Boolean emailverificado;
    @SerializedName("doblefactoractivado")
    private Boolean doblefactoractivado;


    public Usuario() {
    }

    public Usuario(String nombre, String apellidos, String email, String clave) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.clave = clave;
    }

    public Usuario(String nombre, String apellidos, Boolean espasajero, Boolean esconductor, Integer telefono,
                   String email, String clave, Date fechanacimiento, String fotousuario, String fotokyc, String descripcion,
                   Integer valoracion, Date fecharegistro, Date fechadesconexion, Boolean sesioniniciada,
                   Boolean telefonoverificado, Boolean emailverificado, Boolean doblefactoractivado) {
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


}
