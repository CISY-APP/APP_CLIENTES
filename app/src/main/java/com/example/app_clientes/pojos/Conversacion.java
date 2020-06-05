package com.example.app_clientes.pojos;

public class Conversacion  {

    public String idConversacion;
    public String id_usuario;
    public String nombreUsuario;
    public String horaUltimoMensaje;
    public String ultimoMensaje;
    public String fotoUsuarioContrario;
    public Long mensajesSinLeer = 0L;

    public Conversacion() {
    }

    public Conversacion(String idConversacion, String id_usuario, String nombreUsuario, String horaUltimoMensaje, String ultimoMensaje, String fotoUsuarioContrario) {
        this.idConversacion = idConversacion;
        this.id_usuario = id_usuario;
        this.horaUltimoMensaje = horaUltimoMensaje;
        this.ultimoMensaje = ultimoMensaje;
        this.fotoUsuarioContrario = fotoUsuarioContrario;
        this.nombreUsuario = nombreUsuario;
    }

    public String getIdConversacion() {
        return idConversacion;
    }

    public void setIdConversacion(String idConversacion) {
        this.idConversacion = idConversacion;
    }

    public String getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(String id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getHoraUltimoMensaje() {
        return horaUltimoMensaje;
    }

    public void setHoraUltimoMensaje(String horaUltimoMensaje) {
        this.horaUltimoMensaje = horaUltimoMensaje;
    }

    public String getUltimoMensaje() {
        return ultimoMensaje;
    }

    public void setUltimoMensaje(String ultimoMensaje) {
        this.ultimoMensaje = ultimoMensaje;
    }

    public String getFotoUsuarioContrario() {
        return fotoUsuarioContrario;
    }

    public void setFotoUsuarioContrario(String fotoUsuarioContrario) {
        this.fotoUsuarioContrario = fotoUsuarioContrario;
    }

    public Long getMensajesSinLeer() {
        return mensajesSinLeer;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }
}