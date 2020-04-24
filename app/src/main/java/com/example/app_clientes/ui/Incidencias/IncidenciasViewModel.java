package com.example.app_clientes.ui.Incidencias;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class IncidenciasViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public IncidenciasViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Ventana indicendias");
    }

    public LiveData<String> getText() {
        return mText;
    }
}