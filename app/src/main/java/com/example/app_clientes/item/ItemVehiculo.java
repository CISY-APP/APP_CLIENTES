package com.example.app_clientes.item;

public class ItemVehiculo {

    private String matricula;
    private int mImageVehiculo;

    public ItemVehiculo(String matricula, int mImageVehiculo) {
        this.matricula = matricula;
        this.mImageVehiculo = mImageVehiculo;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public int getmImageVehiculo() {
        return mImageVehiculo;
    }

    public void setmImageVehiculo(int mImageVehiculo) {
        this.mImageVehiculo = mImageVehiculo;
    }
}
