package com.example.app_clientes.item;

public class ItemVehiculo {

    private String marcaYmodelo;
    private String matricula;
    private String mImageVehiculo;
    private int mImageVehiculoInt;

    public ItemVehiculo(String marcaYmodelo, String matricula, String mImageVehiculo) {
        this.marcaYmodelo = marcaYmodelo;
        this.matricula = matricula;
        this.mImageVehiculo = mImageVehiculo;
    }
    public ItemVehiculo(String marcaYmodelo, String matricula, int mImageVehiculoInt) {
        this.marcaYmodelo = marcaYmodelo;
        this.matricula = matricula;
        this.mImageVehiculoInt = mImageVehiculoInt;
    }


    public String getMarcaYmodelo() {
        return marcaYmodelo;
    }

    public void setMarcaYmodelo(String marcaYmodelo) {
        this.marcaYmodelo = marcaYmodelo;
    }

    public String getmImageVehiculo() {
        return mImageVehiculo;
    }

    public void setmImageVehiculo(String mImageVehiculo) {
        this.mImageVehiculo = mImageVehiculo;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }
}
