package com.example.app_clientes.ui.Viajes;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TusViajesViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public TusViajesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Ventana vehiculos");
    }

    public LiveData<String> getText() {
        return mText;
    }
}