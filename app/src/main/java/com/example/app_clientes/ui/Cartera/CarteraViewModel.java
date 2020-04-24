package com.example.app_clientes.ui.Cartera;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CarteraViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public CarteraViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Ventana cartera");
    }

    public LiveData<String> getText() {
        return mText;
    }
}