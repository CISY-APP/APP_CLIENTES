package com.example.app_clientes.pojos;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class UsuarioPublicado {
    //Atributos:
    @SerializedName("idusuario")
    private Integer idusuario;
    @SerializedName("nombre")
    private String nombre;
    @SerializedName("apellidos")
    private String apellidos;
    @SerializedName("telefono")
    private Integer telefono;
    @SerializedName("email")
    private String email;
    @SerializedName("fechanacimiento")
    private Date fechanacimiento;
    @SerializedName("fotousuario")
    private String fotousuario;
    @SerializedName("descripcion")
    private String descripcion;
    //Constructor:
    public UsuarioPublicado(Integer idusuario, String nombre, String apellidos, Integer telefono, String email,Date fechanacimiento, String fotousuario, String descripcion) {
        this.idusuario = idusuario;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.email = email;
        this.fechanacimiento = fechanacimiento;
        this.fotousuario = fotousuario;
        this.descripcion = descripcion;
    }
    //Metodos Getter y Setters:
    public Integer getIdusuario() {
        return idusuario;
    }
    public void setIdusuario(Integer idusuario) {
        this.idusuario = idusuario;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getApellidos() {
        return apellidos;
    }
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }
    public Integer getTelefono() {
        return telefono;
    }
    public void setTelefono(Integer telefono) {
        this.telefono = telefono;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public Date getFechanacimiento() {
        return fechanacimiento;
    }
    public void setFechanacimiento(Date fechanacimiento) {
        this.fechanacimiento = fechanacimiento;
    }
    public String getFotousuario() {
        return fotousuario;
    }
    public void setFotousuario(String fotousuario) {
        this.fotousuario = fotousuario;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
