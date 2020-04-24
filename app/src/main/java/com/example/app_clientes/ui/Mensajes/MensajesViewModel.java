package com.example.app_clientes.ui.Mensajes;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MensajesViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MensajesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Venatana mensajes");
    }

    public LiveData<String> getText() {
        return mText;
    }
}