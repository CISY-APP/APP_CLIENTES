package com.example.app_clientes.ui.Vehiculos;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class VehiculosViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public VehiculosViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Ventana mensajes");
    }

    public LiveData<String> getText() {
        return mText;
    }
}