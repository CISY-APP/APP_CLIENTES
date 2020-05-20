package com.example.app_clientes.item;

public class ItemVehiculo {

    private String marcaYmodelo;
    private String matricula;
    private int mImageVehiculo;

    public ItemVehiculo(String marcaYmodelo, String matricula, int mImageVehiculo) {
        this.marcaYmodelo = marcaYmodelo;
        this.matricula = matricula;
        this.mImageVehiculo = mImageVehiculo;
    }

    public String getMarcaYmodelo() {
        return marcaYmodelo;
    }

    public void setMarcaYmodelo(String marcaYmodelo) {
        this.marcaYmodelo = marcaYmodelo;
    }

    public int getmImageVehiculo() {
        return mImageVehiculo;
    }

    public void setmImageVehiculo(int mImageVehiculo) {
        this.mImageVehiculo = mImageVehiculo;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }
}
