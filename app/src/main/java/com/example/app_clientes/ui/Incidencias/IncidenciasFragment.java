package com.example.app_clientes.ui.Incidencias;

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

public class IncidenciasFragment extends Fragment {

    private IncidenciasViewModel incidenciasViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        incidenciasViewModel =
                ViewModelProviders.of(this).get(IncidenciasViewModel.class);
        View root = inflater.inflate(R.layout.fragment_incidencias, container, false);
        final TextView textView = root.findViewById(R.id.text_incidencias);
        incidenciasViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}
