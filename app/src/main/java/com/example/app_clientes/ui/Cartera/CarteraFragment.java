package com.example.app_clientes.ui.Cartera;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.app_clientes.R;

public class CarteraFragment extends Fragment {

    private CarteraViewModel carteraViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        carteraViewModel =
                ViewModelProviders.of(this).get(CarteraViewModel.class);
        View root = inflater.inflate(R.layout.fragment_cartera, container, false);
        final TextView textView = root.findViewById(R.id.text_cerrarSesion);
        carteraViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}
