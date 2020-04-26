package com.example.app_clientes.ui.Configuracion;

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

public class ConfiguracionFragment extends Fragment {

    private ConfiguracionViewModel carteraViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        carteraViewModel =
                ViewModelProviders.of(this).get(ConfiguracionViewModel.class);
        View root = inflater.inflate(R.layout.fragment_configuracion, container, false);
        final TextView textView = root.findViewById(R.id.text_configuracion);
        carteraViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}
